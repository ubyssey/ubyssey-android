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


/**
 * Adapter for the navigator drawer list view
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {


    private Context mContext;
    private List<AbstractMap.SimpleEntry<String, Integer>> mMenuItems;
    private LayoutInflater mLayoutInflater = null;

    public NavigationDrawerAdapter(Context context, List<AbstractMap.SimpleEntry<String, Integer>> menuItems) {
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

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        Map.Entry<String, Integer> menuItem = mMenuItems.get(position);

        TextView menuTextView = (TextView) convertView.findViewById(R.id.menu_item_text);
        menuTextView.setText(menuItem.getKey());

        return convertView;
    }
}
