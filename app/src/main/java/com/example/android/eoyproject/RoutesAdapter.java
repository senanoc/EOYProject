package com.example.android.eoyproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RoutesViewHolder> {

    private List<DocumentSnapshot> mRoutesSnapshots = new ArrayList<>();

    public RoutesAdapter () {

        CollectionReference routesRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH);

        routesRef.orderBy(Constants.KEY_RID, Query.Direction.DESCENDING).limit(50).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listening Failed");
                    return;
                }
                mRoutesSnapshots = documentSnapshots.getDocuments();
                notifyDataSetChanged();
            }
        });

    }

    @NonNull
    @Override
    public RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_itemview, parent,false);
        return new RoutesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesViewHolder routesViewHolder, int i) {
        DocumentSnapshot ds = mRoutesSnapshots.get(i);
        String name = (String) ds.get(Constants.KEY_NAME);
        String addr = (String) ds.get(Constants.KEY_ADDR);
        String rid = (String) ds.get(Constants.KEY_RID);
        String order = (String) ds.get(Constants.KEY_ORDER);
        routesViewHolder.mNameTextView.setText(name);
        routesViewHolder.mAddrTextView.setText(addr);
        routesViewHolder.mRIDTextView.setText(rid);
        routesViewHolder.mOrderTextView.setText(order);

    }

    @Override
    public int getItemCount() {
        return mRoutesSnapshots.size();
    }


    class RoutesViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mAddrTextView;
        private TextView mRIDTextView;
        private TextView mOrderTextView;

        public RoutesViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.itemview_name);
            mAddrTextView = itemView.findViewById(R.id.itemview_addr);
            mRIDTextView = itemView.findViewById(R.id.itemview_RID);
            mOrderTextView = itemView.findViewById(R.id.itemview_order);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot ds = mRoutesSnapshots.get(getAdapterPosition());
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RoutesDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_DOC_ID,ds.getId());
                    context.startActivity(intent);
                }
            });

        }


    }

}
