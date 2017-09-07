package com.crm.pharmbooks.PharmCRM;

import android.view.View;

/**
 * Created by Dell on 24-Aug-17.
 */

public interface ClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
