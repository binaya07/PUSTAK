package com.example.dell.firebaseintro.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.firebaseintro.Adapter.SellerRecyclerAdapter;
import com.example.dell.firebaseintro.Model.Book;
import com.example.dell.firebaseintro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchActivityClass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private android.support.v7.widget.Toolbar mToolbar;
    private TextView userEmail;
    private TextView userName;
    private TextView userPhone;
    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private SellerRecyclerAdapter recyclerViewAdapter;
    private LinearLayoutManager mManager;
    private List<Book> resultBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_2);
        mToggle = new ActionBarDrawerToggle(SearchActivityClass.this, drawerLayout, R.string.open, R.string.close);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.nav_bar);
        navigationView = (NavigationView) findViewById(R.id.nav_view_2);


        View header = navigationView.getHeaderView(0);
        userEmail = (TextView) header.findViewById(R.id.user_email_id);
        userName = (TextView) header.findViewById(R.id.user_full_name);
        userPhone = (TextView) header.findViewById(R.id.phone_number);

        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchField = (EditText) findViewById(R.id.search_field);

        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        // FOR USERs INFO

        userEmail.setText(mUser.getEmail());
        String key = mUser.getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(key);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String fname = dataSnapshot.child("firstName").getValue(String.class);
                String lname = dataSnapshot.child("lastName").getValue(String.class);
                String phone = dataSnapshot.child("phoneNumber").getValue(String.class);

                userName.setText(fname + " " + lname);
                userPhone.setText(phone);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(SearchActivityClass.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        resultBookList = new ArrayList<>();

        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerSearchResult);

        mManager = new LinearLayoutManager(SearchActivityClass.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Book");

        navigationView.setCheckedItem(R.id.search);

    }


            private void searchFirebase(String searchedText) {

        //TODO:MAKE SEARCH FLEXIBLE AND capital, small both searching

                mDatabaseReference.orderByChild("name").startAt(searchedText)
                        .endAt(searchedText + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Book book = ds.getValue(Book.class);

                            if(!book.getId().equals(mUser.getUid()))
                            {
                                resultBookList.add(book);


                            }



                        }

                         if (!resultBookList.isEmpty()) {
                            recyclerViewAdapter = new SellerRecyclerAdapter(SearchActivityClass.this, resultBookList);
                            recyclerView.setAdapter(recyclerViewAdapter);
                            recyclerViewAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(SearchActivityClass.this, "No items!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(SearchActivityClass.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

    @Override
    protected void onStart() {
        super.onStart();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!resultBookList.isEmpty())
                recyclerViewAdapter.clearData();


                resultBookList.clear();

                String searchedText = searchField.getText().toString().trim();

                if (!searchedText.isEmpty())
                    searchFirebase(searchedText);

                else

                    Snackbar.make(v,"Search text required !",Snackbar.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();

       // resultBookList.clear();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()) {
            case android.R.id.home:

                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.home_book :

                if((mUser != null) && mAuth != null)
                {
                    item.setChecked(true);
                    startActivity(new Intent(SearchActivityClass.this,PostListActivity.class));
                    finish();
                }
                break;

            case R.id.signOut:

                if (mUser != null && mAuth != null) {
                    mAuth.signOut();
                    startActivity(new Intent(SearchActivityClass.this, MainActivity.class));
                    finish();
                }
                break;

            case R.id.add_book:

                if (mUser != null && mAuth != null) {
                    startActivity(new Intent(SearchActivityClass.this, AddBookActivity.class));

                }
                break;

            case R.id.added_books:

                if (mUser != null && mAuth != null) {

                    item.setChecked(true);
                    startActivity(new Intent(SearchActivityClass.this,AddedBooksActivity.class));
                    finish();
                    break;
                }

            case R.id.search:

                item.setChecked(true);
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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_2);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}
