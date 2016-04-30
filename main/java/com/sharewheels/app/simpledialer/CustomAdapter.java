package com.sharewheels.app.simpledialer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adminpc on 4/30/2016.
 */
public class CustomAdapter extends BaseAdapter {

    public ArrayList<ContactItem> contactItems;

    public Activity activity;

    private static LayoutInflater inflater=null;

    public CustomAdapter(Activity activity, ArrayList<ContactItem> contactItems){

        this.contactItems = contactItems;

        this.activity = activity;

        this.inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return contactItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.contact_item_layout, null);

        ((TextView)v.findViewById(R.id.contactName)).setText(contactItems.get(position).contactName);
        ((TextView)v.findViewById(R.id.contactNumber)).setText(contactItems.get(position).contactNumber);

        final String fContactNubmer = contactItems.get(position).contactNumber;
        final String fContactName = contactItems.get(position).contactName;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.findViewById(R.id.listView).setVisibility(View.GONE);
                activity.findViewById(R.id.btnNumber).setVisibility(View.GONE);

                activity.findViewById(R.id.tvConfirmName).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.btnOK).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.btnCancel).setVisibility(View.VISIBLE);

                ((TextView)activity.findViewById(R.id.tvConfirmName)).setText(fContactName);

                activity.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + fContactNubmer));
                        try {
                            activity.startActivity(callIntent);
                        } catch (Exception p) {

                        }

                    }
                });

                activity.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        activity.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                        activity.findViewById(R.id.btnNumber).setVisibility(View.VISIBLE);

                        activity.findViewById(R.id.tvConfirmName).setVisibility(View.GONE);
                        activity.findViewById(R.id.btnOK).setVisibility(View.GONE);
                        activity.findViewById(R.id.btnCancel).setVisibility(View.GONE);

                    }
                });

            }
        });

        return v;

    }
}
