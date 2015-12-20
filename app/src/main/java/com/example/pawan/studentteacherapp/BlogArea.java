package com.example.pawan.studentteacherapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class BlogArea extends ListActivity {

    private List<Note> posts;
    private Note note;
    TextView titleEditText;
    protected EditText commentbox;
    protected Button AddComment;
    //String n="aa";
    String classId;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_blog_area);
        Intent intent = this.getIntent();

        titleEditText = (TextView) findViewById(R.id.className);
        commentbox = (EditText) findViewById(R.id.commentbox);
        AddComment = (Button) findViewById(R.id.AddComment);

        titleEditText.getText();
        if (intent.getExtras() != null) {

            note = new Note(intent.getStringExtra("noteId"), intent.getStringExtra("noteTitle"));
            // note = new Note(intent.getStringExtra("noteId"), intent.getStringExtra(""));
            titleEditText.setText(note.getTitle());


        }
        classId = titleEditText.getText().toString();
        id = note.getId();


        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String classname = titleEditText.getText().toString().trim();
                String box = commentbox.getText().toString();
                box = box.trim();
                if (box.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BlogArea.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {


                    /*
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("gender", "female");
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                // The query was successful.
                            } else {
                                // Something went wrong.
                            }
                        }
                    });
*/





                    setProgressBarIndeterminateVisibility(true);
                    final ParseObject comm = new ParseObject("Thoughts");
                    //change student to name and get name and role form class table...
                    comm.put("studentID", ParseUser.getCurrentUser());
                    comm.put("thoughts", box);
                    comm.put("idClass", ParseObject.createWithoutData("Class", id));
                    //role should be changed to username..............
                    // final ParseObject comm1 = new ParseObject("User");
                    //String rolename;
                    //ParseUser.getCurrentUser().getString("role");
                    comm.put("role", ParseUser.getCurrentUser().getUsername());
                    //if(ParseUser.getCurrentUser().getUsername()==comm1.get)
                    //rolename= comm1.getString("role");
                    //comm.put("role",rolename);

                    comm.put("classname", classId);
                    // comm.put("title", classname);
                    setProgressBarIndeterminateVisibility(true);
                    comm.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                // Saved successfully.
                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                // The save failed.
                                Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "User update error: " + e);
                            }
                        }
                    });
                }
            }
        });
        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, R.layout.datalist, posts);
        setListAdapter(adapter);
        refreshPostList();
    }



    private void refreshPostList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Thoughts");
        query.whereEqualTo("classname", classId);
        setProgressBarIndeterminateVisibility(true);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                //  ListView listview = (ListView) findViewById(R.id.list);
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    posts.clear();
                    for (ParseObject post : postList) {
                        {
                            Note note = new Note(post.getObjectId(), post.getString("role")+":-"+post.getString("thoughts"));
                            posts.add(note);
                        }

                    }
                    ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();


                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_refresh: {
                refreshPostList();
                break;
            }

            case R.id.action_new: {
                Intent intent = new Intent(this, TeacherAddClass.class);
                startActivity(intent);
                break;
            }
            case R.id.action_settings: {
                // Do something when user selects Settings from Action Bar overlay
                break;
            }
            case R.id.action_logout: {
                ParseUser.logOut();
                loadLoginView();
                break;
            }
        }


        return super.onOptionsItemSelected(item);
    }



    private void loadLoginView(){
        Intent intent = new Intent(this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
