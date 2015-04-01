package ca.ubc.ubyssey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

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
                convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, parent, false);
            }
        }

        TextView menuTextView = (TextView) convertView.findViewById(R.id.menu_item_text);
        menuTextView.setText(menuItem.getTitle());

        return convertView;
    }
}
