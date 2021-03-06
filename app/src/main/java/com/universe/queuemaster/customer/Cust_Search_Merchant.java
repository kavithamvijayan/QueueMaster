package com.universe.queuemaster.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.universe.queuemaster.R;
import com.universe.queuemaster.database.MerchantInformation_forSearch;
import com.universe.queuemaster.database.QueueInformation;

public class Cust_Search_Merchant extends AppCompatActivity {


    private EditText mSearchField;
    private ImageView mSearchBtn;
    private Context mContext;
    private RecyclerView mResultList;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_merchant_searchresult);
        mSearchField = findViewById(R.id.testcme_editTxt_SearchBar);
        mSearchBtn = findViewById(R.id.imageView2);

        mResultList = (RecyclerView) findViewById(R.id.testmerchant_search_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(Cust_Search_Merchant.this, "Started Search", Toast.LENGTH_LONG).show();
        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference("Queue");
        //Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");


        FirebaseRecyclerOptions<MerchantInformation_forSearch> options =
                new FirebaseRecyclerOptions.Builder<MerchantInformation_forSearch>()
                        .setQuery(firebaseSearchQuery, MerchantInformation_forSearch.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MerchantInformation_forSearch, UsersViewHolder>(options) {

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate( R.layout.cust_list_item, parent, false);
                return new UsersViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position, @NonNull MerchantInformation_forSearch model) {
                int total_wait_time = model.getAvewaiting() * model.getNumPeople();

                usersViewHolder.setDetails(getApplicationContext(), model.getName(), Integer.toString(total_wait_time), model.getImageUrl(),
                        Integer.toString(model.getNumPeople()),
                        model.getDesc(), model.getLocation());
            }
        };
        firebaseRecyclerAdapter.startListening();
        mResultList.setAdapter(firebaseRecyclerAdapter);

    }



    // View Holder Class

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser user;
        private FirebaseUser merchant;
        private final String TAG = "ImageAdapterRecycler";
        private String priority = "false";
        private DatabaseReference queueDatabaseRef;
        private DatabaseReference customerDatabaseRef;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            customerDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
            queueDatabaseRef= FirebaseDatabase.getInstance().getReference("Queue");
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
        }

        public void setDetails(Context ctx, String userName, String avewaiting, String userImage, String numPeople, String desc, String location){
            TextView user_name = mView.findViewById(R.id.queueName);
            TextView user_waitingTime =  mView.findViewById(R.id.queueWaitTime);
            ImageView user_image = mView.findViewById(R.id.queueImage);
            final TextView qNumPeople= mView.findViewById(R.id.queueNumPeople);
            Button joinQButton = mView.findViewById(R.id.joinQ_recycler);
            user_name.setText(userName);
            qNumPeople.setText(numPeople);
            user_waitingTime.setText(avewaiting);
            Glide.with(ctx).load(userImage).into(user_image);

            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Card's image Button Clicked.");
                    Intent i = new Intent(v.getContext(),Cust_Gallery.class);
                    i.putExtra("image_url", userImage);
                    i.putExtra("location", location);
                    i.putExtra("queue_name", userName);
                    i.putExtra("queue_waiting_time", avewaiting);
                    i.putExtra("queue_num_people", numPeople);
                    i.putExtra("description", desc);
                    v.getContext().startActivity(i);
                }
            });

            customerDatabaseRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("priority").getValue() != null) {
                        priority = dataSnapshot.child("priority").getValue().toString();
                    }
                    if (dataSnapshot.hasChild("merchantID")) {
                        joinQButton.setClickable(false);
                        joinQButton.setBackgroundResource(R.drawable.already_join_button);
                    }
                    else {
                        joinQButton.setClickable(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            joinQButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d(TAG,"joinQ Button clicked");

                    queueDatabaseRef.orderByChild("name").equalTo(user_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        //merchantDatabaseRef.orderByChild("name").equalTo(textViewName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                                QueueInformation queueInformation = childSnapshot.getValue(QueueInformation.class);
                                String merchantID = childSnapshot.getKey();

                                if(queueInformation.queue.contains(user.getUid())){
                                    Log.d("Join Queue", "Already in queue.");

                                }
                                else {
                                    if(priority.equals("true")){
                                        queueInformation.queue.add(0,user.getUid());
                                    }else{
                                        queueInformation.queue.add(user.getUid());
                                    }
                                    queueDatabaseRef.child(merchantID).setValue(queueInformation);
                                    customerDatabaseRef.child(user.getUid()).child("merchantID").setValue(merchantID);
                                    qNumPeople.setText(String.valueOf(queueInformation.getNumPeople()));
                                    Log.d(TAG, "adding customer to queue.");

                                    Toast toast = Toast.makeText(v.getContext(),"Joined Queue!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                    Intent intent = new Intent (v.getContext(), Cust_MainActivity.class);
                                    v.getContext().startActivity(intent);

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Database", "Error");

                        }
                    });


                }


            });

        }




    }

}

