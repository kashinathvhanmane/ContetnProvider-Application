package com.example.contentproviderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    EditText edtuser;
    TextView resulttxt;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtuser=findViewById(R.id.username);
        resulttxt=findViewById(R.id.result);
    }



    public void viewDetails(View view)
    {

        Cursor cursor=getContentResolver().query(MyContentProvider.CONTENT_URI,null,null,null,null);
        StringBuilder stringBuilder=new StringBuilder();

        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                stringBuilder.append("\n"+cursor.getString(cursor.getColumnIndex("name")));
                cursor.moveToNext();
            }

            resulttxt.setText(stringBuilder);
        }
        else
        {
            Toast.makeText(this,"Record not Found......",Toast.LENGTH_LONG).show();
        }



    }

    public void addDetails(View view)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyContentProvider.name,edtuser.getText().toString());

        getContentResolver().insert(MyContentProvider.CONTENT_URI,contentValues);

        Toast.makeText(this,"new Record inserted",Toast.LENGTH_LONG).show();
    }
}