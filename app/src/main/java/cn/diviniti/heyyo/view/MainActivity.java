package cn.diviniti.heyyo.view;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.diviniti.heyyo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListAdapter listAdapter = new SimpleAdapter(this, getContactsData(), R.layout.layout_list_item,
                new String[]{"name", "phones", "ID"},
                new int[]{R.id.user_name, R.id.user_phone_number, R.id.user_id});
        ListView listView = (ListView) findViewById(R.id.contacts);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nameTv = (TextView) view.findViewById(R.id.user_name);
                final TextView phoneTv = (TextView) view.findViewById(R.id.user_phone_number);
                new MaterialDialog.Builder(MainActivity.this)
                        .title("跟" + nameTv.getText() + "打个招呼")
//                        .content("测试用手机号：" + phoneTv.getText().toString().split(";")[0])
                        .positiveText("确认发送")
                        .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                        .negativeColor(getResources().getColor(R.color.colorPrimaryDark))
                        .negativeText("还是算了")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                sendSMS(phoneTv.getText().toString().split(";")[0]);
//                                Toast.makeText(getApplicationContext(), "发送", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                Toast.makeText(getApplicationContext(), "算了", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

    }

    protected List<Map<String, Object>> getContactsData() {
        List<Map<String, Object>> contactsList = new ArrayList<Map<String, Object>>();

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
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

    protected void sendSMS(String phoneNumber) {
        SmsManager sms = SmsManager.getDefault();
        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .title("发送中")
                .progress(true, 0)
                .show();

        sms.sendTextMessage(phoneNumber, null, "HeyYO!", null, null);
    }

}
