package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crm.pharmbooks.PharmCRM.PrescriptionListActivity;
import com.crm.pharmbooks.PharmCRM.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by saurabh on 18/09/17.
 */

public class ExpandableListAdapterDashboard extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    LinearLayout base,child;

    public ExpandableListAdapterDashboard(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.dashboard_childlist_row, null);
        }

        child = (LinearLayout)convertView.findViewById(R.id.dashboard_childlist_layout);

//        if(PrescriptionListActivity.CHILDLONG_CLICK_FLAG==1)
//        {
//            if(childPosition==PrescriptionListActivity.pos) {
//                child.setBackgroundColor(Color.parseColor("#9e9e9e"));
//            }
//        }
//        else{
//            child.setBackgroundColor(Color.TRANSPARENT);
//        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.dashboard_parentlist_row, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.TitleHead);
        base = (LinearLayout)convertView.findViewById(R.id.dashboard_parentlist_layout);

//        if(PrescriptionListActivity.LONG_CLICK_FLAG==1){
//            if(groupPosition==PrescriptionListActivity.pos){
//                base.setBackgroundColor(Color.parseColor("#9e9e9e"));
//            }
//        }
//
//
//        else if(PrescriptionListActivity.LONG_CLICK_FLAG==1){
//            base.setBackgroundColor(Color.TRANSPARENT);
//        }
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}