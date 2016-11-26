package maxoliynick.rssreader.core.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import maxoliynick.rssreader.R;
import maxoliynick.rssreader.core.mvp.core.BaseActivity;
import maxoliynick.rssreader.core.mvp.model.RssItemModel;
import maxoliynick.rssreader.core.mvp.presenter.IMainPresenter;
import maxoliynick.rssreader.core.mvp.util.IFutureBitmap;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import rx.android.schedulers.AndroidSchedulers;

@ContentView(R.layout.activity_main)
public final class MainActivity extends BaseActivity implements IMainView {

    private static final class RightSideView {

        private final TextView title;
        private final TextView description;
        private final TextView timestamp;

        RightSideView(View itemView) {
            title = (TextView) itemView.findViewById(R.id.item_title);
            description = (TextView) itemView.findViewById(R.id.item_body);
            timestamp = (TextView) itemView.findViewById(R.id.item_timestamp);
        }

        void setTitle(String title) {
            this.title.setText(title);
        }

        void setTimestamp(String timestamp) {
            this.timestamp.setText(timestamp);
        }

        void setDescription(String description) {
            this.description.setText(description);
        }

    }

    private static final class ItemHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView title;
        private final TextView description;
        private final TextView timestamp;
        private IFutureBitmap bitmap;

        ItemHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            title = (TextView) itemView.findViewById(R.id.item_title);
            description = (TextView) itemView.findViewById(R.id.item_body);
            timestamp = (TextView) itemView.findViewById(R.id.item_timestamp);
        }

        String getTitle() {
            return title.getText().toString();
        }

        String getTimestamp() {
            return timestamp.getText().toString();
        }

        String getDescription() {
            return description.getText().toString();
        }

        void setBitmap(IFutureBitmap bitmap) {
            this.bitmap = bitmap;
        }

        void setTitle(String title) {
            this.title.setText(title);
        }

        void setTimestamp(String timestamp) {
            this.timestamp.setText(timestamp);
        }

        void setDescription(String description) {
            this.description.setText(description);
        }

        void tryLoadImage(Context context) {

            final ViewGroup.LayoutParams params = image.getLayoutParams();

            bitmap.fetch(params.width, params.height, context)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(image::setImageBitmap,
                            th -> {});
        }

    }

    private class Adapter extends RecyclerView.Adapter<ItemHolder> {

        private final ArrayList<RssItemModel> content;

        Adapter() {
            content = new ArrayList<>();
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.rss_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            final RssItemModel model = content.get(position);

            holder.setBitmap(model.getBitmap());
            holder.setTitle(model.getTitle());
            holder.setDescription(model.getDescription());
            holder.setTimestamp(model.getTimestamp());
            holder.itemView.setOnClickListener(v -> presenter.onItemClick(model));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        @Override
        public void onViewAttachedToWindow(ItemHolder holder) {

            if(rightSideView != null) {
                rightSideView.setTitle(holder.getTitle());
                rightSideView.setTimestamp(holder.getTimestamp());
                rightSideView.setDescription(holder.getDescription());
            }

            holder.tryLoadImage(MainActivity.this);
        }

        void addItem(RssItemModel model) {
            content.add(model);
        }

        void clearItems() {
            content.clear();
        }

        void addItem(@NotNull Collection<RssItemModel> models) {
            content.addAll(models);
        }

    }

    private final Adapter adapter;

    @Inject
    private IMainPresenter presenter;

    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    @InjectView(R.id.feed_list)
    private RecyclerView categoriesRecycleView;

    @InjectView(R.id.swipe_refresh)
    private SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(R.id.item_root)
    @maxoliynick.rssreader.core.di.Nullable
    private View currentItemView;

    private RightSideView rightSideView;

    private final LinearLayoutManager layoutManager;

    public MainActivity() {
        adapter = new Adapter();
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());

        categoriesRecycleView.setLayoutManager(layoutManager);
        categoriesRecycleView.setAdapter(adapter);

        if(currentItemView != null) {
            rightSideView = new RightSideView(currentItemView);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.attachView(this, getIntent().getExtras(), savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.action_refresh) {
            presenter.onRefresh();
        }

        return true;
    }


    @Override
    public void addModel(@NotNull Collection<RssItemModel> itemModels) {
        adapter.addItem(itemModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setModel(@NotNull Collection<RssItemModel> itemModels) {
        adapter.clearItems();
        adapter.addItem(itemModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideRefreshProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void clear() {
        adapter.clearItems();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showRefreshProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
