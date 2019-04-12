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

public class CustomListView1 extends ArrayAdapter<String> {

    private String[] month,pois,count;
    private Activity context;

    public CustomListView1(Activity context, String [] month, String [] pois,String[] count) {
        super(context,R.layout.listview_layout1,month);
        this.context=context;
        this.month=month;
        this.pois=pois;
        this.count=count;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        CustomListView1.ViewHolder viewHolder=null;
        if (r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout1,null,true);
            viewHolder=new CustomListView1.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder=(CustomListView1.ViewHolder) r.getTag();
        }
        viewHolder.txt3.setText(pois[position]);
        viewHolder.txt2.setText(month[position]);
        viewHolder.txt1.setText(count[position]);
        return r;
    }

    class ViewHolder
    {   TextView txt1,txt2,txt3;

        ViewHolder(View v)
        {
            txt1=v.findViewById(R.id.textView);
            txt2=v.findViewById(R.id.textView1);
            txt3= v.findViewById(R.id.textView3);


        }



    }
}
