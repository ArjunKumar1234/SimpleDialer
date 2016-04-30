package com.sharewheels.app.simpledialer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onResume(){

        super.onResume();
        populateList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateList();

        Button btnNumber = (Button)findViewById(R.id.btnNumber);
        btnNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.llKeyPad).setVisibility(View.VISIBLE);

                findViewById(R.id.tvConfirmName).setVisibility(View.GONE);
                findViewById(R.id.btnOK).setVisibility(View.GONE);
                findViewById(R.id.btnCancel).setVisibility(View.GONE);

                findViewById(R.id.listView).setVisibility(View.GONE);
                findViewById(R.id.btnNumber).setVisibility(View.GONE);

                final TextView tvInputNumber =  (TextView)findViewById(R.id.tvInputNumber);
                tvInputNumber.setText("");

                handleKeyPadButtons(tvInputNumber, findViewById(R.id.llKeyPad));

                findViewById(R.id.btnCall).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tvInputNumber.getText().toString().length() > 0) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + tvInputNumber.getText().toString()));
                            try {
                                startActivity(callIntent);
                            } catch (Exception p) {

                            }

                        }

                    }
                });

                findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String inputNumber = tvInputNumber.getText().toString();

                        if(inputNumber.length() == 1){
                            tvInputNumber.setText("");
                        }
                        else if(inputNumber.length() > 1) {

                            tvInputNumber.setText(inputNumber.substring(0,inputNumber.length() - 1 ));

                        }

                    }
                });

            }
        });
    }

    public void handleKeyPadButtons(final TextView tvInputNumber,  View view)
    {
        int text = 0;
        text++;
        if(view instanceof Button
                && ((Button)view).getText().toString().toLowerCase() != "call"
                && ((Button)view).getText().toString().toLowerCase() != "delete")
        {

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tvInputNumber.setText(tvInputNumber.getText().toString() + ((Button)v).getText());

                }
            });

        }
        else if(view instanceof LinearLayout){

            LinearLayout ll = (LinearLayout)view;

            for(int i=0; i<ll.getChildCount(); i++){

                handleKeyPadButtons(tvInputNumber,ll.getChildAt(i));

            }

        }

    }

    public void populateList(){

        findViewById(R.id.llKeyPad).setVisibility(View.GONE);

        findViewById(R.id.tvConfirmName).setVisibility(View.GONE);
        findViewById(R.id.btnOK).setVisibility(View.GONE);
        findViewById(R.id.btnCancel).setVisibility(View.GONE);

        findViewById(R.id.listView).setVisibility(View.VISIBLE);
        findViewById(R.id.btnNumber).setVisibility(View.VISIBLE);

        ListView listView = (ListView)findViewById(R.id.listView);

        ArrayList<ContactItem> contactItems = new ArrayList<ContactItem>();

        contactItems = getContactItems();

        CustomAdapter customAdapter = new CustomAdapter(this, contactItems);
        listView.setAdapter(customAdapter);

    }

    public ArrayList<ContactItem> getContactItems(){

        ArrayList<ContactItem> contactItems = new ArrayList<ContactItem>();

        ContentResolver cr = this.getContentResolver();

        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        // use the cursor to access the contacts
        while (phones.moveToNext())
        {
            String contactName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            // get display name
            String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            // get phone number
            ContactItem contactItem = new ContactItem(contactName, contactNumber);
            contactItems.add(contactItem);
        }

        return contactItems;

    }
}
