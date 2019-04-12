package com.unipi.cbarbini.unipimeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;

public class LocationsActivity extends AppCompatActivity {
    ListView listView;
    String[] title,desc,images;
    Button btn_add,btn_sync;
    //Object
    CustomListView customliview_object;
    DatabaseHandler db_object ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        db_object=new DatabaseHandler(this);


        listView = (ListView)findViewById(R.id.listview);
        btn_add=findViewById(R.id.button4);
        btn_sync=findViewById(R.id.button);

        adapter();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddLocation.class));
            }
        });
         btn_sync.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               if(title.length==0)
               {db_object.init();
                adapter();}
               else Toast.makeText(getApplicationContext(), "Locations are already synchronized", Toast.LENGTH_SHORT).show();
             }
         });
    }
    public void adapter()
    {
        title=db_object.locations("loc_title");
        desc=db_object.locations("loc_description");
        images=db_object.locations("loc_image");
        customliview_object =new CustomListView(this,title,desc,images);

        //set adapter based on each layout.xml using customlistview_object
        listView.setAdapter(customliview_object);
    }

}
