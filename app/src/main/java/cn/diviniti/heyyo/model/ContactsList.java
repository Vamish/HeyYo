package cn.diviniti.heyyo.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.github.promeg.pinyinhelper.Pinyin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vango on 15/10/27.
 */
public class ContactsList implements Serializable {

    public List<Map<String, Object>> getContactsData(Context context) {
        List<Map<String, Object>> contactsList = new ArrayList<Map<String, Object>>();

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);

        while (cursor.moveToNext()) {
            Map<String, Object> contact = new HashMap<String, Object>();

            //联系人ID
            String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            //联系人姓名
            String display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            //联系人号码
            Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID;
            Cursor phoneCursor = contentResolver.query(phoneUri, null, selection, null, null);

            String phones = "";
            while (phoneCursor.moveToNext()) {
                phones += phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                phones += ";";
            }
            if (!"".equals(phones.trim())) {
                contact.put("name", display_name);
                contact.put("pinyin", Pinyin.toPinyin(display_name.toCharArray()[0]));
                contact.put("phones", phones);
                contact.put("ID", contactID);
                contactsList.add(contact);
            }
            Collections.sort(contactsList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
                    return ((String) lhs.get("pinyin")).compareTo((String) rhs.get("pinyin"));
                }
            });
        }

        return contactsList;
    }

}
