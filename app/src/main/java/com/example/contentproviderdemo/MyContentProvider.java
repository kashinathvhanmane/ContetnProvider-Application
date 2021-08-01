package com.example.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.net.URI;
import java.util.Map;

import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider
{

    public static  final String CONTENT_AUTHORITY="com.example.contentproviderdemo.user";
    public static final String uri="content://"+CONTENT_AUTHORITY+"/users";

    public static Uri CONTENT_URI=Uri.parse(uri);

    static String id="id";
    static String name="name";

    static final int uriCode=1;

    Map<String,String> values;

   static UriMatcher uriMatcher=null;

    static
    {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CONTENT_AUTHORITY,"/users",uriCode);

        uriMatcher.addURI(CONTENT_AUTHORITY,"/*",uriCode);
    }







    public MyContentProvider()
    {

    }

    @Override
    public String getType(Uri uri)
    {

        switch (uriMatcher.match(uri))
        {
            case uriCode:
                return "vnd.android.cursor.dir/users";
            default:
                throw new UnsupportedOperationException("unknown URI:"+uri);

        }


    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {

        long rowId=db.insert(TABLE_NAME,"",values);
        if(rowId>0)
        {
            Uri updatedUri= ContentUris.withAppendedId(CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(updatedUri,null);
            return updatedUri;
        }

        throw new UnsupportedOperationException("Field to insert new Record:"+uri);
    }



    @Override
    public boolean onCreate()
    {
        SqliteDataBaseHelper dataBaseHelper=new SqliteDataBaseHelper(getContext());

        db=dataBaseHelper.getWritableDatabase();
        if (db!=null)
        {
            return true;
        }

        return false;
    }





    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri))
        {
            case uriCode:
                qb.setProjectionMap(values);
                break;
            default:
                throw new UnsupportedOperationException("unknown URI:"+uri);

        }

        if (sortOrder==null||sortOrder==" ")
        {
            sortOrder=id;
        }

        Cursor cursor=qb.query(db,projection,selection,selectionArgs,null,null,sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }






    static String DATA_BASENAME="student";
    static String TABLE_NAME="stud";
    static final int DATABASE_VERSION=1;

    SQLiteDatabase db;


    class SqliteDataBaseHelper extends SQLiteOpenHelper
    {


        public SqliteDataBaseHelper(@Nullable Context context) {
            super(context, DATA_BASENAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase)
        {
            String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(100))";
            sqLiteDatabase.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(sqLiteDatabase);

        }



    }











    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}