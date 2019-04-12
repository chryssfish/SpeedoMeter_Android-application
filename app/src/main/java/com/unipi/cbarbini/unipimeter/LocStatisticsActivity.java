package com.unipi.cbarbini.unipimeter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class LocStatisticsActivity extends AppCompatActivity {
    ListView listView;
    String[] month,pois,count;
    //Object
    CustomListView1 customliview_object;
    DatabaseHandler db_object ;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_statistics);

        db_object=new DatabaseHandler(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        listView = (ListView)findViewById(R.id.listview);
        month=db_object.viewLocStat("month",preferences.getString("username", "examples@example.com"));
        pois =db_object.viewLocStat("pois",preferences.getString("username", "examples@example.com"));
        count=db_object.viewLocStat("count",preferences.getString("username", "examples@example.com"));
        customliview_object =new CustomListView1(this,month,pois,count);

        //set adapter based on each layout.xml using customlistview_object
        listView.setAdapter(customliview_object);

    }
}
