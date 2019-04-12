package com.unipi.cbarbini.unipimeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler  extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) { super(context,"unipiDatabase.db",null,3);
    }
    String timestamp= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table USERS (user_name VARCHAR,user_email VARCHAR PRIMARY KEY , user_pass VARCHAR,user_region VARCHAR)");
        db.execSQL("Create table LOCATIONS(loc_title VARCHAR,loc_description VARCHAR,loc_category VARCHAR,loc_location VARCHAR PRIMARY KEY,loc_image VARCHAR)");
        db.execSQL("Create table SpeedStatistics(username VARCHAR,t VARCHAR,speed VARCHAR,speedlimit VARCHAR,currentlocation VARCHAR)");
        db.execSQL("Create table LocStatistics(username VARCHAR ,pois VARCHAR,currentlocation VARCHAR, t DATETIME DEFAULT (datetime('now','localtime')))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists USERS");
        db.execSQL("drop table if exists LOCATIONS");
        db.execSQL("drop table if exists SpeedStatistics");
        db.execSQL("drop table if exists LocStatistics");
        onCreate(db);
    }
    //user registration
    public boolean register(String name,String email,String password, String region)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name",name);
        contentValues.put("user_email",email);
        contentValues.put("user_pass",password);
        contentValues.put("user_region",region);

        long success = db.insert("USERS", null, contentValues);
        if (success== -1) return false;
        else return true;

    }

    //user login
    public boolean login(String email, String password)
    {
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE user_email=? AND user_pass=?",new String [] {email , password});
        if(cursor.getCount()>0)return true;
        else return false;
    }

    //user update
    public boolean update (String name, String email, String password, String region) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name",name);
        contentValues.put("user_pass",password);
        contentValues.put("user_region",region);
        db.update("USERS", contentValues,"user_email=?",new String [] {email});
        return true;
    }

    //view user's details
    public String[] viewUser(String username)
    {
        ArrayList<String> user = new ArrayList<String>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM USERS WHERE user_email=?",new String [] {username});
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            user.add(cursor.getString(cursor.getColumnIndex("user_name")));
            user.add(cursor.getString(cursor.getColumnIndex("user_email")));
            user.add(cursor.getString(cursor.getColumnIndex("user_pass")));
            user.add(cursor.getString(cursor.getColumnIndex("user_region")));

            cursor.moveToNext();
        }
        return user.toArray((new String[user.size()]));
    }
    //speedometer records
    public Boolean insertSpeedStat(String username,String speed,String speedlimit,String location)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("t",timestamp);
        contentValues.put("speed",speed);
        contentValues.put("speedlimit",speedlimit);
        contentValues.put("currentlocation",location);

        long success = db.insert("SpeedStatistics", null, contentValues);
        if (success== -1) return false;
        else return true;


    }
    //location records
    public boolean insertLocStat(String username,String pois,String location)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("pois",pois);
        contentValues.put("currentlocation",location);

        long success = db.insert("LocStatistics", null, contentValues);
        if (success== -1) return false;
        else return true;

    }
    //view list with speed records
    public String [] viewSpeedStat(String column,String username)
    {
        ArrayList<String> statistics = new ArrayList<String>();
        String query="SELECT "+column+" FROM SpeedStatistics WHERE username=?";
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,new String [] {username});
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            statistics.add(cursor.getString(cursor.getColumnIndex(column)));
            cursor.moveToNext();
        }
        return statistics.toArray((new String[statistics.size()]));
    }

    //view list with location records
    public String[] viewLocStat(String column,String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) as count,strftime('%m', t) as month, pois  FROM LocStatistics WHERE username=? GROUP BY pois";
        Cursor cursor = null;
        ArrayList<Integer> count = new ArrayList<Integer>();
        ArrayList<Integer> month = new ArrayList<Integer>();
        ArrayList<String>  pois = new ArrayList<String>();
        ArrayList<String> list=new ArrayList<String>();
        cursor=db.rawQuery(query,new String [] {username});
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            count.add(cursor.getInt(cursor.getColumnIndex(column)));
            month.add(cursor.getInt(cursor.getColumnIndex(column)));
            pois.add(cursor.getString(cursor.getColumnIndex(column)));
            cursor.moveToNext();
        }
        if     (column=="count")
        {
            for(int i :count )list.add(String.valueOf(i));
            return list.toArray((new String[list.size()]));

        }
        else if(column=="month")
        {
            for(int i :month )list.add(String.valueOf(getMonthForInt(i)));
            return list.toArray((new String[list.size()]));

        }
        else {return pois.toArray((new String[pois.size()]));}

    }
    //view all locations
    public String[][] array_of_locations()
    {

        SQLiteDatabase db =this.getReadableDatabase();
        int count = 0;

        Cursor cursor=db.rawQuery("SELECT * FROM LOCATIONS",null);
        int counter = cursor.getCount();
        String locations [][]=new String[counter][2];

        while(cursor.moveToNext())
        {
            String[] array1 = new String[2];
            array1[0] =cursor.getString((cursor.getColumnIndex("loc_title")));
            array1[1] =cursor.getString((cursor.getColumnIndex("loc_location")));

            locations[count]  = array1;
            count++;
        }
        return locations;
    }

    //view a single column of locations
    public String[] locations(String column)
    {
        ArrayList<String> locations = new ArrayList<String>();
        String query="SELECT "+column+" FROM LOCATIONS";
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            locations.add(cursor.getString(cursor.getColumnIndex(column)));
            cursor.moveToNext();
        }
        return locations.toArray((new String[locations.size()]));
    }
    //add a location manually , photo is by default photodef.png
    public Boolean addlocation(String title,String desc,String category,String x,String y) {

        String imageformat="photodef";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("loc_title",title);
        contentValues.put("loc_description",desc);
        contentValues.put("loc_category",category);
        contentValues.put("loc_location",x+","+ y);
        contentValues.put("loc_image",imageformat);
        long success = db.insert("LOCATIONS", null, contentValues);
        if (success== -1) return false;
        else return true;

    }
    //store locations
    public void init()
    {   SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("loc_title","University Of Piraeus");
        contentValues.put("loc_description","A remarkable university");
        contentValues.put("loc_category","university");
        contentValues.put("loc_location", "37.941529,23.652834");
        contentValues.put("loc_image", "unipi");

        db.insert("LOCATIONS", null, contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("loc_title","Skurou 1");
        contentValues1.put("loc_description","Square on Galatsi");
        contentValues1.put("loc_category","square");
        contentValues1.put("loc_location", "38.018848,23.756782");
        contentValues1.put("loc_image", "skurou");
        db.insert("LOCATIONS", null, contentValues1);

        ContentValues contentValues2 = new ContentValues();

        contentValues2.put("loc_title","Alsos Veikou");
        contentValues2.put("loc_description","Green Spaces");
        contentValues2.put("loc_category","park");
        contentValues2.put("loc_location", "38.027419,23.765461");
        contentValues2.put("loc_image", "alsos");

        db.insert("LOCATIONS", null, contentValues2);

        ContentValues contentValues3 = new ContentValues();

        contentValues3.put("loc_title","Monastiraki metro Station");
        contentValues3.put("loc_description","The most populated station");
        contentValues3.put("loc_category","station");
        contentValues3.put("loc_location", "37.976085,23.725626");
        contentValues3.put("loc_image", "monastiraki");

        db.insert("LOCATIONS", null, contentValues3);

        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("loc_title","Viktwria Station");
        contentValues4.put("loc_description","Station near the city center");
        contentValues4.put("loc_category","station");
        contentValues4.put("loc_location", "37.993077,23.730286");
        contentValues4.put("loc_image", "viktoria");
        db.insert("LOCATIONS", null, contentValues4);

        ContentValues contentValues5 = new ContentValues();
        contentValues5.put("loc_title", "TO BALKONI MOY");
        contentValues5.put("loc_description", "YEAAAAAH");
        contentValues5.put("loc_category", "Other");
        contentValues5.put("loc_location", "38.01798659439092,23.7577755691316");
        contentValues5.put("loc_image", "balkoni");
        db.insert("LOCATIONS", null, contentValues5);
    }
    public void del(){
        SQLiteDatabase db = this.getWritableDatabase();
       // db.delete("SpeedStatistics", null, null);
        db.delete("LocStatistics", null, null);
    }

     //convert int to month string
    public String getMonthForInt(int m){
        String month="invalid";
        DateFormatSymbols dfs=new DateFormatSymbols();
        String[] months =dfs.getMonths();
          if( m>=0&& m<=12){month=months[m-1];}

          return  month;
    }
    public void ins(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM USERS");
        db.execSQL("DELETE  FROM LocStatistics");
       // db.execSQL("Create table LocStatistics(username VARCHAR ,pois VARCHAR,currentlocation VARCHAR, t DATETIME DEFAULT (datetime('now','localtime')))");
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", "Chrysa@gmail.com");
        contentValues.put("pois", "University Of Piraeus");
        contentValues.put("currentlocation", "37.941139054485426,23.653119447165068");
        db.insert("LocStatistics", null, contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("username", "Chrysa@gmail.com");
        contentValues1.put("pois", "University Of Piraeus");
        contentValues1.put("currentlocation", "37.941173745476355,23.652980911044892");
        db.insert("LocStatistics", null, contentValues1);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("username", "Chrysa@gmail.com");
        contentValues2.put("pois", "Monastiraki metro Station");
        contentValues2.put("currentlocation", "37.976047871190076,23.725708547301565");
        db.insert("LocStatistics", null, contentValues2);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("username", "Chrysa@gmail.com");
        contentValues3.put("pois", "Alsos Veikou");
        contentValues3.put("currentlocation", "38.026815025595404,23.767031713231404");
        db.insert("LocStatistics", null, contentValues3);

        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("username", "Chrysa@gmail.com");
        contentValues4.put("pois", "Viktwria Station");
        contentValues4.put("currentlocation", "37.99310078304915,23.730190909255498");
        db.insert("LocStatistics", null, contentValues4);}
}
