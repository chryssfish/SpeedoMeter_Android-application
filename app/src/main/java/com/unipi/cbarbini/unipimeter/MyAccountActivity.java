package com.unipi.cbarbini.unipimeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyAccountActivity extends AppCompatActivity {
    Button btn_locations,btn_locstatistics,btn_speedstatistics,btn_settings,btn_update;
    EditText edt_name,edt_username,edt_pass,edt_region;
    DatabaseHandler db_object ;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        btn_locations=findViewById(R.id.button9);
        btn_locstatistics=findViewById(R.id.button7);
        btn_speedstatistics=findViewById(R.id.button11);
        btn_settings =findViewById(R.id.button10);
        btn_update   =findViewById(R.id.update);

        edt_name=findViewById(R.id.editText);
        edt_username=findViewById(R.id.editText1);
        edt_pass=findViewById(R.id.editText2);
        edt_region=findViewById(R.id.editText3);

        edt_username.setEnabled(false);



        db_object =new DatabaseHandler(this);
        String[] user=db_object.viewUser(preferences.getString("username", "examples@example.com"));
        edt_name.setText(user[0]);
        edt_username.setText(user[1]);
        edt_pass.setText(user[2]);
        edt_region.setText(user[3]);


        btn_locations.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),LocationsActivity.class));

            }
        }));
        btn_locstatistics.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),LocStatisticsActivity.class));

            }
        }));
        btn_speedstatistics.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),SpeedStatisticsActivity.class));

            }
        }));
        btn_settings.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));

            }
        }));
        btn_update.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stored_name = edt_name.getText().toString();
                String stored_email =edt_username.getText().toString();
                String stored_password = edt_pass.getText().toString();
                String stored_region = edt_region.getText().toString();

                // Check if username and password is empty
                if (stored_password.trim().length() > 0)
                {
                        //update user
                        db_object.update(stored_name,stored_email,stored_password,stored_region);
                        Toast.makeText(getApplicationContext(), "Your data have been updated", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please fill the empty fields ", Toast.LENGTH_SHORT).show();


                //update user
               db_object.update(edt_name.getText().toString(),edt_username.getText().toString(),edt_pass.getText().toString(),edt_region.getText().toString());

            }
        }));



    }


}
