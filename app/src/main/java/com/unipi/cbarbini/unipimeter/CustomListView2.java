package com.unipi.cbarbini.unipimeter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;

public class CustomListView2 extends ArrayAdapter<String> {

    private String[] speed,speedlimit,t;
    private Activity context;
    public CustomListView2(Activity context, String []speed, String [] speedlimit,String[] t) {
        super(context,R.layout.listview_layout2,speed);
        this.context=context;
        this.t=t;
        this.speed=speed;
        this.speedlimit=speedlimit;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        CustomListView2.ViewHolder viewHolder=null;
        if (r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout2,null,true);
            viewHolder=new CustomListView2.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder=(CustomListView2.ViewHolder) r.getTag();
        }
        viewHolder.txt1.setText(t[position]);
        viewHolder.txt2.setText(speed[position]);
        viewHolder.txt3.setText(speedlimit[position]);
        return r;
    }

    class ViewHolder
    {   TextView txt1,txt2,txt3;

        ViewHolder(View v) {
            txt1 = v.findViewById(R.id.textView);
            txt2 = v.findViewById(R.id.textView1);
            txt3 = v.findViewById(R.id.textView2);
        }
}
}
