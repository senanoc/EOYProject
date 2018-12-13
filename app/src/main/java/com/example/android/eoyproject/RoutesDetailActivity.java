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
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class RoutesDetailActivity extends AppCompatActivity {

    private DocumentSnapshot mDocSnapshot;
    private DocumentReference mDocRef;
    private TextView mNameTextView;
    private TextView mAddrTextView;
    private TextView mLat;
    private TextView mLon;
    private TextView mComplete;
    private TextView mDriverNote;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNameTextView = findViewById(R.id.detail_name);
        mAddrTextView = findViewById(R.id.detail_addr);
        mComplete = findViewById(R.id.detail_complete);
        mDriverNote = findViewById(R.id.detail_drivernote);
        mLat = findViewById(R.id.detail_lat);
        mLon = findViewById(R.id.detail_lon);

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
                    mComplete.setText((String)documentSnapshot.get(Constants.KEY_COMPLETE));
                    mDriverNote.setText((String)documentSnapshot.get(Constants.KEY_NOTE));
                    mLat.setText((String)documentSnapshot.get(Constants.KEY_LAT));
                    mLon.setText((String)documentSnapshot.get(Constants.KEY_LON));
                }
            }
        });
    }

    public void button_complete(View view) {
        Map<String, Object> rt = new HashMap<>();
        rt.put(Constants.KEY_COMPLETE, "Complete".toString());
        mDocRef.update(rt);
    }

    public void button_missed(View view) {
        Map<String, Object> rt = new HashMap<>();
        rt.put(Constants.KEY_COMPLETE, "Incomplete".toString());
        mDocRef.update(rt);
    }

    public void button_save(View view) {
        Map<String, Object> rt = new HashMap<>();
        rt.put(Constants.KEY_NOTE, mDriverNote.getText().toString());
        mDocRef.update(rt);
        finish();
    }

    public void button_nav(View view) {
        String location = mLat.getText().toString() + "," + mLon.getText().toString();
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+location);
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
