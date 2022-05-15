package com.example.caller991.adapters;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;

import com.example.caller991.ContactsActivity;
import com.example.caller991.R;
import com.example.caller991.SMSActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {
    private Context context;
    private LayoutInflater inflater;
    private Cursor cursor;

    public ContactsAdapter(Context context) {
        this.context = context;

        this.inflater = LayoutInflater.from(context);
        this.cursor = getContacts();
        cursorOut(cursor);
    }

    private Cursor getContacts() {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER
                }, null, null);
        return cursor;
    }

    private Cursor getContactData(String contactId){
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.DATA1
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }

    private Cursor getContactData(String contactId, String... keys) {
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                keys,
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }

    private Cursor getPhones(String contactId){
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.LABEL
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }


    private Cursor getEmails(String contactId){
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Email.ADDRESS
                },
                ContactsContract.CommonDataKinds.Email.CONTACT_ID+"=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }


    @SuppressLint("Range")
    private void cursorOut(Cursor cursor) {
        Log.e("FF", "=======================");
        while (cursor.moveToNext()) {
            Log.e("FF", "-------------------------");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
//                Log.e("FF", String.valueOf(cursor.getString(
//                        cursor.getColumnIndex("data" + i)
//                )));
                Log.e("FF", String.valueOf(cursor.getString(i)));
            }
        }
        Log.e("FF", "=======================");
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(view);
    }


//    @SuppressLint("Range")
//    @Override
//    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
//        if(cursor.moveToPosition(position)){
//            holder.nameField.setText(String.valueOf(
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
//            );
//            Cursor phones = getPhones(
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
//            );
//
//            if(phones.moveToFirst()){
//                holder.phoneField.setText(String.valueOf(
//                        phones.getString(phones.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.DATA
//                        ))
//                ));
//            }
//// my code
//            if(phones.moveToNext()){
//                String columnName = ContactsContract.CommonDataKinds.Phone.DATA; //  columnName = "data1"
//                int columnNumber = phones.getColumnIndex(columnName);
//                String phoneNumber = phones.getString(columnNumber); //"12345"
//                holder.phone2Field.setText(phoneNumber);
//
////                holder.phone2Field.setText(
////                        phones.getString(phones.getColumnIndex(
////                                ContactsContract.CommonDataKinds.Phone.DATA
////                        ))
////                );
//            }
//
//            Cursor emails = getEmails(
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
//            );
//
//            if(emails.moveToFirst()){
//                holder.emailField.setText(
//                        emails.getString(emails.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Email.ADDRESS
//                        ))
//                );
//            }
//// my code
//
//
//        }
//    }

    // ===========================================================================================

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String[] keys = {
                    ContactsContract.Data.CONTACT_ID,
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.Data.DATA1,
                    ContactsContract.Data.DATA2,
                    ContactsContract.Data.DATA3,
            };
            Cursor cursorData = getContactData(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)), keys
            );
//            cursorOut(cursorData, keys);
            holder.nameField.setText(String.valueOf(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            ));
            int phoneCount = 0;
            String val;


            String mimeType = "email";
            switch (mimeType){
                case "email":
                    System.out.println("save email");
                    break;
                case "phone":
                    System.out.println("save phone");
                    break;
            }

            while (cursorData.moveToNext()) {
                switch (cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.MIMETYPE))) {
                    case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                        val = String.valueOf(
                                cursorData.getString(cursorData.getColumnIndex(
                                        ContactsContract.Data.DATA1
                                ))
                        );
                        switch (phoneCount++) {
                            case 0:
                                holder.phoneField.setText(val);
                                break;
                            case 1:
                                holder.phone2Field.setText(val);
                                break;
                        }
                        break;
                    case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                        val = String.valueOf(
                                cursorData.getString(cursorData.getColumnIndex(
                                        ContactsContract.Data.DATA1
                                ))
                        );
                        holder.emailField.setText(val);
                        Log.e("FF", val);
                        break;
                }
            }
        }
        holder.phoneField.setOnLongClickListener(view -> {
            // sms(view);
            // call(view);
            dial(view);
            return true;
        });
        holder.phone2Field.setOnLongClickListener(view -> {
            // sms(view);
            // call(view);
            dial(view);
            return true;
        });
    }




    // ===========================================================================================

    private void dial(View view) {
        TextView textView = (TextView) view;
        if (textView.getText() == null || textView.getText().length() == 0)
            return;
        try {

            ((ContactsActivity) context).callPermission.launch(Manifest.permission.CALL_PHONE);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + textView.getText()));
            context.startActivity(intent);
        } catch (Exception ignored) {

        }
    }

    private void call(View view) {
        TextView textView = (TextView) view;
        if (textView.getText() == null || textView.getText().length() == 0)
            return;
        try {

            ((ContactsActivity) context).callPermission.launch(Manifest.permission.CALL_PHONE);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + textView.getText()));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("FF", e.toString());
        }
    }

    private void sms(View view){
        TextView textView = (TextView) view;

//        CharSequence charSequence = textView.getText();
//        int length = charSequence.length();
        if(textView.getText() == null || textView.getText().length() == 0) return;
        Intent intent = new Intent(context, SMSActivity.class);
        intent.putExtra(SMSActivity.PHONE_KEY, textView.getText().toString());
        context.startActivity(intent);
    }





    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ContactHolder extends RecyclerView.ViewHolder {
        TextView nameField;
        TextView phoneField;
        TextView phone2Field;
        TextView emailField;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.nameField);
            phoneField = itemView.findViewById(R.id.phoneField);
            phone2Field = itemView.findViewById(R.id.phone2Field);
            emailField = itemView.findViewById(R.id.emailField);
        }
    }
}
