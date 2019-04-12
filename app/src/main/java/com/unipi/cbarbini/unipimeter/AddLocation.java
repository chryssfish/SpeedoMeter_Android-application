package com.unipi.cbarbini.unipimeter;

import android.content.Intent;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddLocation extends AppCompatActivity {
   EditText title,desc,x,y;
   Button btn_addlocation;
   RadioGroup radioGroup;
   RadioButton radioButton;
   DatabaseHandler db_object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        title=findViewById(R.id.editText9);
        desc=findViewById(R.id.editText10);
        x=findViewById(R.id.editText11);
        y=findViewById(R.id.editText12);
        btn_addlocation=findViewById(R.id.button);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        db_object = new DatabaseHandler(this);

        btn_addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean success ;
                String stored_title =title.getText().toString();
                String stored_desc = desc.getText().toString();
                String stored_category;
                String stored_x = x.getText().toString();
                String stored_y = y.getText().toString();

                 //check for empty values
                if (stored_title.trim().length() > 0 && stored_desc.trim().length() > 0 && stored_x.trim().length() > 0  && stored_y.trim().length() > 0)
                {   // get selected radio button from radioGroup
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);

                    stored_category= (String) radioButton.getText();

                    success=db_object.addlocation(stored_title,stored_desc,stored_category,stored_x,stored_y);

                    if (success==true)startActivity(new Intent(getApplicationContext(),LocationsActivity.class));
                    else Toast.makeText(getApplicationContext(), "It seems that this location already exists .Please enter another location(coordinates)!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please fill the empty fields ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
