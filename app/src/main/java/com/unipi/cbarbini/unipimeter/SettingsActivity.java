package com.unipi.cbarbini.unipimeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    TextView stored_speedlimit, stored_r, seekbar_speed;
    SeekBar sb;
    EditText editext_r;

    Button updt_button;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        stored_speedlimit = findViewById(R.id.textView16);
        stored_r = findViewById(R.id.textView18);
        sb = findViewById(R.id.seekBar2);
        seekbar_speed = findViewById(R.id.textView13);
        updt_button = findViewById(R.id.button5);
        editext_r = findViewById(R.id.editText8);



        //Current Data
        stored_speedlimit.setText(preferences.getString("speedlimit", "40"));
        stored_r.setText(preferences.getString("R", "4"));
        seekbar_speed.setText(preferences.getString("speedlimit", "40"));
        editext_r.setText(preferences.getString("R", "4"));
        sb.setProgress(Integer.valueOf(preferences.getString("speedlimit", "40")));

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar2, int progress, boolean fromUser) {
                seekbar_speed.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        updt_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storedd_speedlimit = seekbar_speed.getText().toString();
                String value = editext_r.getText().toString();

                SharedPreferences.Editor editor = preferences.edit();

                //First check R value
                if (value.trim().length() > 0) {
                    int storedd_r = Integer.parseInt(value);
                        if (storedd_r >= 4 && storedd_r <= 10) {

                            editor.putString("speedlimit", storedd_speedlimit);
                            editor.putString("R", String.valueOf(storedd_r));

                            //Save data
                            editor.commit();
                            //Current Data
                            stored_speedlimit.setText(storedd_speedlimit);
                            stored_r.setText(value);

                            Toast.makeText(getApplicationContext(), "Data have been updated ", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Warning!Please give an integer between 4 and 10 ! ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Please fill the R value!", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}