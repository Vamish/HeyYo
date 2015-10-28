package cn.diviniti.heyyo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.diviniti.heyyo.R;
import cn.diviniti.heyyo.model.Contact;
import cn.diviniti.heyyo.model.ContactsList;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Bundle bundle = new Bundle();
        final ContactsList list = new ContactsList();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(
                        new Intent(getApplicationContext(), MainActivity.class)
                                .putExtra("contactsList",list)
                );
                finish();
            }
        };

        timer.schedule(task, 2000);
    }
}
