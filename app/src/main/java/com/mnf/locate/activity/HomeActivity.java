package com.mnf.locate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mnf.locate.MainActivity;
import com.mnf.locate.R;
import com.mnf.locate.adapter.MainMenuAdapter;
import com.mnf.locate.model.MenuModel;
import com.mnf.locate.tools.PreferensHandler;
import com.mnf.locate.tools.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class HomeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    MainMenuAdapter adapter;
    List<MenuModel> model = new ArrayList<>();
    Context c;
    private DatabaseReference mDatabase;
    TinyDB tinydb;
    PreferensHandler pref;
    Set<String> menuItem;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;
    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        c = getApplicationContext();


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    //user logged in
                    //setAccountVisibility(true);
                    Log.e("HomeActivity","user is = "+user.getUid());
                    Intent mainAct = new Intent(HomeActivity.this,MainActivity.class);
                    startActivity(mainAct);
                }else{
                    Log.e("HomeActivity","user is not logged in  ");

                    Toast.makeText(getApplicationContext(),"Not cool bro, not cool.  Login first",Toast.LENGTH_LONG).show();
                    //user logged out
                    //  dettachView();

                   /* startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().createSignInIntentBuilder().build(),
                            RC_SIGN_IN);*/
                    openauthenticationView();
                }
            }
        };
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthStateListener);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


       /* tinydb = new TinyDB(c);
        pref = new PreferensHandler(c);
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // pref = new PreferensHandler(c);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_main_menu);

        mLayoutManager
                = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(c, 1));
        //   mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),));
       /* model.add(new MenuModel("Bus 1 - place","  ",R.mipmap.bus,"1"));
        model.add(new MenuModel("Bus 2 - place"," ",R.mipmap.bus,"2"));
        model.add(new MenuModel("Bus 3 - place"," ",R.mipmap.bus,"3"));
        model.add(new MenuModel("Bus 4 - place"," ",R.mipmap.bus,"4"));
        model.add(new MenuModel("Bus 5 - place"," ",R.mipmap.bus,"5"));

        model.add(new MenuModel("Bus 6 - place"," ",R.mipmap.bus,"6"));
        model.add(new MenuModel("Bus 7 - place"," ",R.mipmap.bus,"7"));
        model.add(new MenuModel("Bus 8 - place"," ",R.mipmap.bus,"8"));
        model.add(new MenuModel("Bus 9 - place"," ",R.mipmap.bus,"9"));
        model.add(new MenuModel("Bus 10 - place"," ",R.mipmap.bus,"10"));
        adapter = new MainMenuAdapter(c,model);
        mRecyclerView.setAdapter(adapter);*/

      /* menuItem =  pref.getMenuItems();
        for(String item:menuItem){
            model.add(new MenuModel("Bus 2 - place"," ",R.mipmap.bus,"2"));

        }
*/
    /*    mDatabase.child("buses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("TAG","onDataChange  data = "+dataSnapshot.getChildrenCount());
                int i=0;
                List<MenuModel> modelSynced = new ArrayList<MenuModel>();
              for(DataSnapshot data:dataSnapshot.getChildren()){
                  Log.e("TAG","key = "+data.child("key").getValue()+" location = "+data.child("location").toString());

                  modelSynced.add(new MenuModel("Bus "+data.child("key").getValue()+" - "+data.child("location").getValue(),"",R.mipmap.bus,data.child("key").getValue()+""));



              }
              //  tinydb.putListObject("",model);
                Log.e("TAg",modelSynced.toString());
                adapter = new MainMenuAdapter(c,modelSynced);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG","DatabaseError  data = "+databaseError.getMessage());
            }
        });








        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }
    public void openauthenticationView(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                       // .setTheme(R.style.LoginTheme)

                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())  //new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())
                        )

                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        openauthenticationView();
    }
}
