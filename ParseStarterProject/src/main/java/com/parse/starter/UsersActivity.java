package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ListView usersListView = (ListView) findViewById(R.id.usersListView);

        final ArrayList<String> users = new ArrayList<>();
        users.add("Test user");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        usersListView.setAdapter(adapter);

        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("User");
//        userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        userQuery.addAscendingOrder("username");
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (ParseUser user : list) {
                            users.add(user.getUsername());
                        }
                    }
                } else {
                    Log.i("Error", e.getLocalizedMessage());
                }
            }
        });
    }
}
