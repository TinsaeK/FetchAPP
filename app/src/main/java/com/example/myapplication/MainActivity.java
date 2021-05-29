package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

/**********************************
 * main class
 *******************************/
public class MainActivity extends AppCompatActivity {
    public static TextView data;
    public static ExpandableListView listView;

    @Override
    /*****************************************
     * present JSON data in expanding list
     * view format grouped by list id.
     **************************************/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        listView = (ExpandableListView) findViewById(R.id.listview);


        FetchData process = new FetchData();
        process.execute();
    }
}
