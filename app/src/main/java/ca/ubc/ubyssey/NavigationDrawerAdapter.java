package ca.ubc.ubyssey;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.ubc.ubyssey.models.DrawerItem;


/**
 * Adapter for the navigator drawer list view
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private static final int SECTION_TYPE = 0;
    private static final int ITEM_TYPE = 1;
    private static final int TOPIC_TYPE = 2;

    private Context mContext;
    private List<DrawerItem> mMenuItems;
    private LayoutInflater mLayoutInflater = null;

    public NavigationDrawerAdapter(Context context, List<DrawerItem> menuItems) {
        mContext = context;
        mMenuItems = menuItems;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {

        DrawerItem item = mMenuItems.get(position);

        if (item.isSection()) {
            return SECTION_TYPE;
        } else if (item.isTopic()) {
            return TOPIC_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItem menuItem = mMenuItems.get(position);

            switch (getItemViewType(position)) {

                case SECTION_TYPE:
                    SectionHolder sectionHolder;
                    if (convertView == null) {
                        convertView = mLayoutInflater.inflate(R.layout.drawer_list_section_item, parent, false);
                        sectionHolder = new SectionHolder();
                        convertView.setOnClickListener(null);
                        convertView.setOnLongClickListener(null);
                        convertView.setLongClickable(false);
                        sectionHolder.sectionTextView = (TextView) convertView.findViewById(R.id.menu_item_text);
                        convertView.setTag(sectionHolder);
                    } else {
                        sectionHolder = (SectionHolder) convertView.getTag();
                    }
                    sectionHolder.sectionTextView.setText(menuItem.getTitle());
                    return convertView;

                case TOPIC_TYPE:
                    TopicHolder topicHolder;
                    if (convertView == null) {
                        convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, parent, false);
                        topicHolder = new TopicHolder();
                        topicHolder.topicTextView = (TextView) convertView.findViewById(R.id.menu_item_text);
                        convertView.setTag(topicHolder);
                    } else {
                        topicHolder = (TopicHolder) convertView.getTag();
                    }
                    topicHolder.topicTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    topicHolder.topicTextView.setText(menuItem.getTitle());
                    return convertView;

                case ITEM_TYPE:
                    ItemHolder itemHolder;
                    if (convertView == null) {
                        convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, parent, false);
                        itemHolder = new ItemHolder();
                        itemHolder.itemTextView = (TextView) convertView.findViewById(R.id.menu_item_text);
                        itemHolder.imageView = (ImageView) convertView.findViewById(R.id.menu_item_icon);
                        if (menuItem.getTitle().equals("Trending")) {
                            convertView.setBackground(mContext.getResources().getDrawable(R.drawable.drawer_trending_item_background));
                        }
                        convertView.setTag(itemHolder);
                    } else {
                        itemHolder = (ItemHolder) convertView.getTag();
                    }

                    itemHolder.itemTextView.setText(menuItem.getTitle());
                    if (menuItem.getTitle().equals("Galleries")) {
                        itemHolder.imageView.setImageResource(R.drawable.ic_image_photo_library);
                        itemHolder.imageView.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.imageView.setVisibility(View.GONE);
                    }
                    return convertView;
            }

        return null;
    }

    public void addTopicItems(List<DrawerItem> topicItems) {
        mMenuItems.addAll(topicItems);
        notifyDataSetChanged();
    }

    public static class SectionHolder {
        TextView sectionTextView;
    }

    public static class ItemHolder {
        TextView itemTextView;
        ImageView imageView;
    }

    public static class TopicHolder {
        TextView topicTextView;
    }

}
