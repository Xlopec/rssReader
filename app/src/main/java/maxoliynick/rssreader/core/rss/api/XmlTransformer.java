package maxoliynick.rssreader.core.rss.api;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.roboguice.shaded.goole.common.base.Preconditions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import maxoliynick.rssreader.core.rss.bo.RssItem;
import maxoliynick.rssreader.core.rss.vo.Image;
import maxoliynick.rssreader.core.rss.vo.RssID;

/**
 * Created by Максим on 11/25/2016.
 */

final class XmlTransformer {

    private static final String TAG = XmlTransformer.class.getSimpleName();

    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String DESCRIPTION_TAG = "description";
    private static final String LINK_TAG = "link";
    private static final String DATE_TAG = "pubDate";

    private static final String IMAGE_TAG = "media:thumbnail";
    private static final String IMAGE_WIDTH_ATTR = "width";
    private static final String IMAGE_HEIGHT_ATTR = "height";
    private static final String IMAGE_URL_ATTR = "url";

    private static final DateTimeFormatter formatter;

    static {
        formatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z").withLocale(Locale.ENGLISH);
    }

    private XmlTransformer() {
        // prevent instantiating via reflection
        throw new RuntimeException("shouldn't be instantiated");
    }

    static RssItem parse(@NotNull Element entry) {

        Preconditions.checkNotNull(entry);

        final Element eTitle = (Element) entry.getElementsByTagName(TITLE_TAG).item(0);
        final Element eDescription = (Element) entry.getElementsByTagName(DESCRIPTION_TAG).item(0);
        final Element eLink = (Element) entry.getElementsByTagName(LINK_TAG).item(0);
        final Element eDate = (Element) entry.getElementsByTagName(DATE_TAG).item(0);

        final URL url;
        try {
            url = new URL(eLink.getFirstChild().getNodeValue());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        final DateTime pubDate = formatter.parseDateTime(eDate.getFirstChild().getNodeValue());
        final String title = eTitle.getFirstChild().getNodeValue();
        final String description = eDescription.getFirstChild().getNodeValue();
        final Collection<Image> images = XmlTransformer.parseImages(entry.getElementsByTagName(IMAGE_TAG));

        return new RssItem.Builder(title, description, new RssID(UUID.randomUUID()), url, pubDate).setImages(images).build();
    }

    static Collection<RssItem> parse(@NotNull Document document) {

        final Element element = document.getDocumentElement();
        final NodeList items = element.getElementsByTagName(ITEM_TAG);

        final ArrayList<RssItem> rssItems = new ArrayList<>(items.getLength());

        for (int i = 0; i < items.getLength(); ++i) {

            final RssItem item = XmlTransformer.parse((Element) items.item(i));

            if (item != null) {
                rssItems.add(item);
            }
        }

        rssItems.trimToSize();
        return rssItems;
    }

    private static Collection<Image> parseImages(NodeList nodes) {

        final ArrayList<Image> images = new ArrayList<>(nodes.getLength());

        for (int j = 0; j < nodes.getLength(); ++j) {

            final Element eImage = (Element) nodes.item(j);
            final String imgUrl = eImage.getAttribute(IMAGE_URL_ATTR);

            try {

                final URL url = new URL(imgUrl);
                int width = -1;
                int height = -1;

                String imgStr = eImage.getAttribute(IMAGE_HEIGHT_ATTR);

                if (!TextUtils.isEmpty(imgStr)) height = tryParseInt(imgStr);

                imgStr = eImage.getAttribute(IMAGE_WIDTH_ATTR);

                if (!TextUtils.isEmpty(imgStr)) width = tryParseInt(imgStr);

                images.add(new Image(url, width, height));
            } catch (final MalformedURLException e) {
                Log.w(TAG, String.format("Failed to parse url: %s", imgUrl), e);
            }
        }
        images.trimToSize();
        return images;
    }

    private static int tryParseInt(String input) {

        int result = -1;

        try {
            result = Integer.parseInt(input);
        } catch (final Exception e) {
            Log.w(TAG, String.format("Failed to parse int for input: %s", input), e);
        }

        return result;
    }

}
