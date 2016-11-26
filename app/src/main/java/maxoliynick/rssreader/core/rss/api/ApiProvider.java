package maxoliynick.rssreader.core.rss.api;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.google.inject.Singleton;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import maxoliynick.rssreader.core.rss.bo.RssItem;
import maxoliynick.rssreader.core.rss.exc.ApiException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 11/25/2016.
 */
@Singleton
public final class ApiProvider implements IApiProvider {

    private static final String TAG = ApiProvider.class.getSimpleName();

    private static final int READ_TIMEOUT = 10_000;
    private static final int CONNECT_TIMEOUT = 10_000;

    @Override
    public Observable<Collection<RssItem>> fetchRss(@NotNull final URL url) {

        return Observable.defer(() -> Observable.create((Observable.OnSubscribe<Collection<RssItem>>) subscriber -> {

            subscriber.onStart();
            HttpURLConnection connection = null;
            InputStream is = null;

            try {

                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    subscriber.onError(new ApiException(
                            String.format(Locale.getDefault(), "unexpected response status, was %d",
                                    connection.getResponseCode())));
                    return;
                }

                try {
                    is = connection.getInputStream();

                    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    final DocumentBuilder db = factory.newDocumentBuilder();
                    final Document document = db.parse(is);

                    subscriber.onNext(XmlTransformer.parse(document));
                    subscriber.onCompleted();
                } finally {
                    if (is != null) is.close();
                }
            } catch (final ParserConfigurationException | SAXException e) {
                Log.e(TAG, "Failed to parse document", e);
                subscriber.onError(new ApiException("Malformed rss feed", e));

            } catch (final ProtocolException e) {
                Log.e(TAG, "Invalid net protocol", e);
                subscriber.onError(new NetworkErrorException("Network exception", e));

            } catch (final Exception e) {
                Log.e(TAG, "internal exception", e);
                subscriber.onError(e);

            } finally {
                if (connection != null) connection.disconnect();
                subscriber.onCompleted();
            }
        })).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}