package ca.ubc.ubyssey;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.ubc.ubyssey.models.DrawerItem;


/**
 * Adapter for the navigator drawer list view
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {


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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItem menuItem = mMenuItems.get(position);
        if (convertView == null) {
            if (menuItem.isSection()) {
                convertView = mLayoutInflater.inflate(R.layout.drawer_list_section_item, parent, false);
                convertView.setOnClickListener(null);
                convertView.setOnLongClickListener(null);
                convertView.setLongClickable(false);

            } else {

                if (menuItem.getTitle().equals("Trending")) {
                    convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, parent, false);
                    convertView.setBackground(mContext.getResources().getDrawable(R.drawable.drawer_trending_item_background));
                } else {
                    convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, parent, false);
                }
            }
        }

        TextView menuTextView = (TextView) convertView.findViewById(R.id.menu_item_text);
        menuTextView.setText(menuItem.getTitle());

        return convertView;
    }
}
