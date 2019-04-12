package com.unipi.cbarbini.unipimeter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class SpeedStatisticsActivity extends AppCompatActivity {
    ListView listView;
    String[] speed,speedlimit,t;
    String username;
    SharedPreferences preferences;

    //Object
    CustomListView2 customliview_object;
    DatabaseHandler db_object ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_statistics);
        listView = (ListView)findViewById(R.id.listview);
        db_object=new DatabaseHandler(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        username=preferences.getString("username", "examples@example.com");

        speed=db_object.viewSpeedStat("speed",username);
        speedlimit=db_object.viewSpeedStat("speedlimit",username);
        t=db_object.viewSpeedStat("t",username);
        customliview_object =new CustomListView2(this,speed,speedlimit,t);

        //set adapter based on each layout.xml using customlistview_object
        listView.setAdapter(customliview_object);
    }
}
