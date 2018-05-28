package com.example.dell.firebaseintro.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dell.firebaseintro.Adapter.RecyclerViewAdapter;
import com.example.dell.firebaseintro.Model.Book;
import com.example.dell.firebaseintro.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.security.cert.CertificateNotYetValidException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddedBooksActivity extends PostListActivity {

    private List<Book> myBookList;
    private ValueEventListener mvalueEventListener;
    private DatabaseReference mDatabaseReference;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!

        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.content_added_books, null, false);
        drawerLayout.addView(contentView, 0);

        myBookList = new ArrayList<>();


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Book");
        mDatabaseReference.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        navigationView.setCheckedItem(R.id.added_books);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                Book book = ds.getValue(Book.class);

                if (book.getId().equals(mUser.getUid()))
                    myBookList.add(book);

            }

            if(!myBookList.isEmpty()) {
                recyclerViewAdapter = new RecyclerViewAdapter(AddedBooksActivity.this, myBookList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            else {

                Toast.makeText(AddedBooksActivity.this, "No items!", Toast.LENGTH_LONG).show();

                startActivity(new Intent(AddedBooksActivity.this,PostListActivity.class));
                finish();

            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        mDatabaseReference.addValueEventListener(valueEventListener);
        mvalueEventListener = valueEventListener;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.home_book :

                if (mUser != null && mAuth != null) {

                    item.setChecked(true);
                    startActivity(new Intent(AddedBooksActivity.this, PostListActivity.class));
                    finish();
                    break;
                }


            case R.id.signOut:

                if (mUser != null && mAuth != null) {
                    mAuth.signOut();
                    startActivity(new Intent(AddedBooksActivity.this, MainActivity.class));
                    finish();
                }
                break;

            case R.id.add_book:

                if (mUser != null && mAuth != null) {
                    startActivity(new Intent(AddedBooksActivity.this, AddBookActivity.class));

                }
                break;

            case R.id.added_books :

                if((mUser != null) && mAuth != null)
                {
                    item.setChecked(true);
                }
                break;



            case R.id.search:

                item.setChecked(true);
                startActivity(new Intent(AddedBooksActivity.this,SearchActivityClass.class));
               finish();
                break;


            case R.id.register_bookstore:

                //Check if the user has already registered bookstore then if not start new activity

                Toast.makeText(getApplicationContext(),"REGISTER BOOKSTORE",Toast.LENGTH_SHORT).show();
                break;

            case R.id.my_bookstore:

                //Check if the user has bookstore

                Toast.makeText(getApplicationContext(),"MY BOOKSTORE",Toast.LENGTH_SHORT).show();
                break;

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_1);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();


        if(mvalueEventListener!=null)
            mDatabaseReference.removeEventListener(mvalueEventListener);

        if(!myBookList.isEmpty())
        recyclerViewAdapter.clearData();

        myBookList.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
