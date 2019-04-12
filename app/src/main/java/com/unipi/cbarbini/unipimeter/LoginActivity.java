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

public class LoginActivity extends AppCompatActivity {
    DatabaseHandler db_object;
    EditText email,password;
    Button lgn_button;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        lgn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean success;
                String stored_email = email.getText().toString();
                String stored_password = password.getText().toString();
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                SharedPreferences.Editor editor = preferences.edit();

                // Check if username and password is empty
                if (stored_email.trim().length() > 0 && stored_password.trim().length() > 0)
                {
                    success=db_object.login(stored_email,stored_password);
                    email= findViewById(R.id.editText);
                    password = findViewById(R.id.editText2);
                    lgn_button =findViewById(R.id.button);




                    db_object=new DatabaseHandler(getApplicationContext());
                    if(success==true){
                        //db_object.ins();
                        editor.putString("username",stored_email);
                        //Save data
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(),SpeedOmeterActivity.class));


                    }
                    else Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please fill the empty fields ", Toast.LENGTH_SHORT).show();

            }
        }) ;
    }
    public void redirect_reg(View view)
    {
        startActivity(new Intent(this,RegistrationActivity.class));
    }

}
