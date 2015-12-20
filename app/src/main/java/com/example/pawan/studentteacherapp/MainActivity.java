package com.example.pawan.studentteacherapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;
import java.util.Objects;


public class MainActivity extends ListActivity {
    private List<Note> posts;
    public String statusTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser==null)
        {
            statusTxt="fails";
        }
        else
        {
            statusTxt= currentUser.getString("role");
        }

        //statusTxt =ParseUser.getCurrentUser().getString("Status");
        // statusTxt =currentUser.getString("Role");

        TextView textView = (TextView)findViewById(R.id.statusText);
        textView.setText(statusTxt);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(statusTxt, "student")) {
                    Intent intent = new Intent(MainActivity.this, StudentPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, TeacherPage.class);
                    startActivity(intent);
                }
            }
        });



        if (currentUser == null) {
            loadLoginView();
        }
    }
    private void loadLoginView(){
        Intent intent = new Intent(this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // intent.putExtra("teachername", note.getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }
    }


