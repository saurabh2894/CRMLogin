package Adapters;

/**
 * Created by hp on 10/11/2017.
 */


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.crm.pharmbooks.PharmCRM.PrescriptionRefillActivity;
import com.crm.pharmbooks.PharmCRM.R;

import java.util.ArrayList;

import Model.RefillEditModel;

public class RefillEditAdapter extends ArrayAdapter<RefillEditModel> {


    RefillEditModel word;
    public RefillEditAdapter(Activity context, ArrayList<RefillEditModel> word) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, word);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.refill_list_edit_row, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        final RefillEditModel currentWord = getItem(position);
        final TextView dose,name;

        // Find the TextView in the list_item.xml layout with the ID version_name
        Button inc,dec;
        dose=(TextView)listItemView.findViewById(R.id.med_dose);
        name=(TextView)listItemView.findViewById(R.id.med_name);
        name.setText(currentWord.getMedName());
        dose.setText(currentWord.getDosage());
        inc = (Button)listItemView.findViewById(R.id.increment);
        dec = (Button)listItemView.findViewById(R.id.decrement);
        if(PrescriptionRefillActivity.clicked_pos==position) {

            dose.setText(currentWord.getDosage());

            inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int day = Integer.parseInt(dose.getText().toString());
                    day++;
                    dose.setText(day + "");
                    Log.d("Position of item",position+"");
                    int add = currentWord.getIncrement();
                    currentWord.setIncrement(++add);
                    Log.d("Mytag009",currentWord.getIncrement()+"");
                }
            });
            dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int day = Integer.parseInt(dose.getText().toString());
                    day--;
                    dose.setText(day + "");
                    int add = currentWord.getDecrement();
                    Log.d("Position of item",position+"");
                    currentWord.setDecrement(++add);
                    Log.d("Mytag009",currentWord.getDecrement()+"");
                }
            });
        }
        return listItemView;
    }

}

