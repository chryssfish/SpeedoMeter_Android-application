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

public class RegistrationActivity extends AppCompatActivity {
    DatabaseHandler db_object;
    EditText name,email,password,region;
    Button reg_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name = findViewById(R.id.editText3);
        email = findViewById(R.id.editText4);
        password = findViewById(R.id.editText5);
        region = findViewById(R.id.editText6);
        reg_button =findViewById(R.id.button2);


        db_object=new DatabaseHandler(this);

        reg_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean success ;
                String stored_name = name.getText().toString();
                String stored_email = email.getText().toString();
                String stored_password = password.getText().toString();
                String stored_region = region.getText().toString();

                // Check if username and password is empty
                if (stored_email.trim().length() > 0 && stored_password.trim().length() > 0)
                {
                    success=db_object.register(stored_name,stored_email,stored_password,stored_region);
                    if (success==true)startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    else Toast.makeText(getApplicationContext(), "It seems that this account already exists .Please enter another email!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please fill the empty fields ", Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

}
