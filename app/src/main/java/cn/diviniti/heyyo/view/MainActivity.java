package cn.diviniti.heyyo.view;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.diviniti.heyyo.R;
import cn.diviniti.heyyo.model.Contact;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<Contact> test = initContactList();
    }

    private List<Contact> initContactList() {
        List<Contact> contactList = new ArrayList<Contact>();

        // 查询联系人数据
        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            // 获取联系人的Id
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的姓名
            String contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setContactName(contactName);

            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);

            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor
                        .getString(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contact.setPhoneNumber(phoneNumber);
            }

            if (null != phoneCursor && !phoneCursor.isClosed()) {
                phoneCursor.close();
            }
            Log.i("VANGO_DEBUG", contact.getContactName());

            contactList.add(contact);
        }

        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }

        Log.i("", contactList.toString());

        return contactList;
    }
}
