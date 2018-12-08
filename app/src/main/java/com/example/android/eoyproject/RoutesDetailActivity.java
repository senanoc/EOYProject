package com.example.android.eoyproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class RoutesDetailActivity extends AppCompatActivity {

    private DocumentSnapshot mDocSnapshot;
    private DocumentReference mDocRef;
    private TextView mNameTextView;
    private TextView mAddrTextView;
    private double mLat;
    private double mLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNameTextView = findViewById(R.id.detail_name);
        mAddrTextView = findViewById(R.id.detail_addr);


        String docId = getIntent().getStringExtra(Constants.EXTRA_DOC_ID);


        mDocRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH).document(docId);
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e != null){
                    Log.w(Constants.TAG, "Listen Failed!");
                    return;
                }
                if(documentSnapshot.exists()){
                    mDocSnapshot = documentSnapshot;
                    mNameTextView.setText((String)documentSnapshot.get(Constants.KEY_NAME));
                    mAddrTextView.setText((String)documentSnapshot.get(Constants.KEY_ADDR));



                }
            }
        });
    }

    public void button_complete(View view) {
    }

    public void button_missed(View view) {
    }

    public void button_nav(View view) {

        //TODO get coords from firebase

        Uri gmmIntentUri = Uri.parse("google.navigation:q=52.54854,-8.7297");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);




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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
