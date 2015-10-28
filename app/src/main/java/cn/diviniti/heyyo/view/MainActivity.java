package cn.diviniti.heyyo.view;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Timer;
import java.util.TimerTask;

import cn.diviniti.heyyo.R;
import cn.diviniti.heyyo.model.ContactsList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        ContactsList contactsList = (ContactsList) intent.getSerializableExtra("contactsList");
        ListAdapter listAdapter = new SimpleAdapter(this, contactsList.getContactsData(getApplicationContext()), R.layout.layout_list_item,
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


    protected void sendSMS(String phoneNumber) {
        SmsManager sms = SmsManager.getDefault();
        final MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .title("发送中")
                .progress(true, 0)
                .show();

        sms.sendTextMessage(phoneNumber, null, "HeyYO!", null, null);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        };
        timer.schedule(task, 4000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_setting) {

        }
        return true;
    }
}
