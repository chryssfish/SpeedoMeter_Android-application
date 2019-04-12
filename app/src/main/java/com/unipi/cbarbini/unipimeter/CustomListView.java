package com.unipi.cbarbini.unipimeter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListView extends ArrayAdapter<String> {
    private String[] title,desc,images;
    int image;
    private Activity context;

    public CustomListView(Activity context, String [] title , String [] desc,String[] images) {
        super(context,R.layout.listview_layout,title);
        this.context=context;
        this.title=title;
        this.desc=desc;
        this.images=images;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent) {
        View r=convertView;
        ViewHolder viewHolder=null;
        if (r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) r.getTag();
        }
        viewHolder.txt1.setText(title[position]);
        viewHolder.txt2.setText(desc[position]);
        image= context.getResources().getIdentifier(images[position], "drawable", context.getPackageName());
        viewHolder.img.setImageResource(image);
        return r;
    }

    class ViewHolder
    {   TextView txt1, txt2;
        ImageView img;

        ViewHolder(View v)
        {
            txt1=v.findViewById(R.id.textView);
            txt2=v.findViewById(R.id.textView2);
            img= v.findViewById(R.id.imageView);


        }



    }
}
