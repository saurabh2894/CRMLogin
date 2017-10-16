package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crm.pharmbooks.PharmCRM.PrescriptionListActivity;
import com.crm.pharmbooks.PharmCRM.R;
import com.crm.pharmbooks.PharmCRM.RefillListActivity;

import java.util.HashMap;
import java.util.List;


/**
 * Created by hp on 14/10/17.
 */

public class ExpandableRefillListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    ImageView marker,markerHead;

    LinearLayout base,child;

    public ExpandableRefillListAdapter(Context context, List<String> listDataHeader,
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
            convertView = infalInflater.inflate(R.layout.refill_list_row_notify, null);
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        child = (LinearLayout) convertView.findViewById(R.id.pres_list_layout);
        marker = (ImageView) convertView.findViewById(R.id.marker);


        if (PrescriptionListActivity.ACTIVITY_FLAG.equals("Refill")){

         if (RefillListActivity.REFILL_FLAG == 1) {
             if (RefillListActivity.child_pos.containsKey(groupPosition)) {
                 if (RefillListActivity.child_pos.get(groupPosition).contains(childPosition)) {
//                     child.setBackgroundColor(Color.parseColor("#FFFF00"));
                     marker.setVisibility(View.VISIBLE);
                 }else {
//                     child.setBackgroundColor(Color.TRANSPARENT);
                     marker.setVisibility(View.GONE);
                 }
             }else {
//                 child.setBackgroundColor(Color.TRANSPARENT);
                 marker.setVisibility(View.GONE);
             }
         }
            else {
//                child.setBackgroundColor(Color.TRANSPARENT);
             marker.setVisibility(View.GONE);
            }
        }


        //txtListChild.setTypeface(null, Typeface.BOLD);
        txtListChild.setText(childText);
        txtListChild.setTextSize(15);

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
            convertView = infalInflater.inflate(R.layout.refill_customer_head_row, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.CName);
        markerHead = (ImageView)convertView.findViewById(R.id.markerHead);
        markerHead.setVisibility(View.GONE);
        base = (LinearLayout) convertView.findViewById(R.id.customer_list_layout);

         if(PrescriptionListActivity.ACTIVITY_FLAG.equals("Refill")){
            if (RefillListActivity.REFILL_FLAG == 1) {
                if (RefillListActivity.child_pos.containsKey(groupPosition)) {
//                    base.setBackgroundColor(Color.parseColor("#FFFF00"));
                    markerHead.setVisibility(View.VISIBLE);
                }
                else {
                    markerHead.setVisibility(View.GONE);
                }
            }
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            lblListHeader.setTextSize(24);

        }


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