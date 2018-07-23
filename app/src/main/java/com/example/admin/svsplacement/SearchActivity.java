package com.example.admin.svsplacement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private AutoCompleteTextView searchAuto;
    private TextView searchSeacrh;
    private RecyclerView searchRecyclerView;
    private EditText searchEditText;
    private LinearLayoutManager mLayoutManager;
    private String spinnerName,autoCompleteText;
    private String s_name,s_tenthmark,s_cgpa,s_sno,s_count,s_twelthmark;
    private Spinner spinner;
    private String percentage;
    private  ProgressDialog pd;
    private ProgressBar pb;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Database").child("usersData");
        databaseReference.keepSynced(true);
        searchAuto = (AutoCompleteTextView) findViewById(R.id.search_auto);
        searchSeacrh = (TextView) findViewById(R.id.search_search);
        searchRecyclerView = (RecyclerView) findViewById(R.id.search_recyclerView);
        searchRecyclerView.setHasFixedSize(false);
        spinner = (Spinner) findViewById(R.id.spinner);
        searchRecyclerView.setNestedScrollingEnabled(false);
        searchRecyclerView.setHasFixedSize(false);
        pd = new ProgressDialog(SearchActivity.this);
        pb = (ProgressBar) findViewById(R.id.search_pb);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_name,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerName = (String) parent.getItemAtPosition(position);
                searchAuto.setText("");
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.autocomplete,
                android.R.layout.simple_list_item_1);
        searchAuto.setAdapter(adapter1);
        searchSeacrh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                try {
                    if (searchAuto.getText().toString() != null) {
                        FirebaseRecyclerAdapter<SearchClass, Holder> firebaseRecyclerAdapter =
                                new FirebaseRecyclerAdapter<SearchClass, Holder>(
                                        SearchClass.class, R.layout.searchlayout, Holder.class, databaseReference
                                ) {
                                    @Override
                                    protected void populateViewHolder(final Holder holder, final SearchClass searchClass, int i) {
                                        final String s_key = getRef(i).getKey();
                                        autoCompleteText = searchAuto.getText().toString().trim();
                                        holder.searchLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                                intent.putExtra("key", s_key);
                                                startActivity(intent);
                                            }
                                        });
                                        switch (spinnerName) {
                                            case "Register No":
                                                if (searchClass.getREGNO().equals(autoCompleteText)) {
                                                    count++;
                                                    holder.setText(searchClass.getNAME(), "");
                                                    pb.setVisibility(View.GONE);
                                                    break;
                                                } else {
                                                    holder.searchName.setVisibility(View.GONE);
                                                    holder.searchLayout.setVisibility(View.GONE);
                                                    holder.view.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                                }
                                                break;
                                            case "Department":
                                            if (searchClass.getDEPT().equalsIgnoreCase(autoCompleteText)) {
                                                count++;
                                                holder.setText(searchClass.getNAME(), searchClass.getDEPT());
                                                pb.setVisibility(View.GONE);
                                                break;
                                            } else {
                                                holder.searchName.setVisibility(View.GONE);
                                                holder.searchLayout.setVisibility(View.GONE);
                                                holder.view.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                            }
                                            break;
                                            case "HSC":
                                                try {
                                                    if (Float.parseFloat(searchClass.getTWM()) >= Float.parseFloat(autoCompleteText)) {
                                                        count++;
                                                        holder.setText(searchClass.getNAME(), searchClass.getTWM());
                                                        pb.setVisibility(View.GONE);
                                                    } else {
                                                        holder.searchName.setVisibility(View.GONE);
                                                        holder.searchLayout.setVisibility(View.GONE);
                                                        holder.view.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                                    }
                                                }
                                                catch (NumberFormatException e){
                                                    System.out.println("numberStr is not a number");
                                                }
                                                catch (NullPointerException e){
                                                    System.out.println("Null");
                                                }

                                                break;
                                            case "SSLC":
                                                try {
                                                    if (Float.parseFloat(searchClass.getTM()) >= Float.parseFloat(autoCompleteText)) {
                                                        count++;
                                                        holder.setText(searchClass.getNAME(), searchClass.getTM());
                                                        pb.setVisibility(View.GONE);
                                                    }
                                                    else {
                                                        holder.searchName.setVisibility(View.GONE);
                                                        holder.searchLayout.setVisibility(View.GONE);
                                                        holder.view.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                                    }
                                                }
                                                catch (NumberFormatException e) {
                                                    System.out.println("numberStr is not a number");
                                                }
                                                catch (NullPointerException e){
                                                    System.out.println("Null");
                                                }
                                                break;
                                            case "CGPA":
                                                try {
                                                    if (Float.parseFloat(searchClass.getCGPA()) >= Float.parseFloat(autoCompleteText)) {
                                                        count++;
                                                        holder.setText(searchClass.getNAME(), searchClass.getCGPA());
                                                        pb.setVisibility(View.GONE);
                                                    }
                                                    else {
                                                        holder.searchName.setVisibility(View.GONE);
                                                        holder.searchLayout.setVisibility(View.GONE);
                                                        holder.view.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                                    }
                                                }
                                                catch (NumberFormatException e){
                                                    System.out.println("numberStr is not a number");
                                                }
                                                catch (NullPointerException e){
                                                    System.out.println("Null");
                                                }
                                                break;
                                        }
                                    }

                                };

                        pd.dismiss();
                        searchRecyclerView.setLayoutManager(mLayoutManager);
                        searchRecyclerView.setAdapter(firebaseRecyclerAdapter);
                    }
                    else {
                        Toast.makeText(SearchActivity.this, "Fill the fields", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(SearchActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static class Holder extends RecyclerView.ViewHolder {
        View view;
        private TextView searchName;
        private LinearLayout searchLayout;
        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            searchName = (TextView) view.findViewById(R.id.search_name);
            searchLayout  = (LinearLayout) view.findViewById(R.id.search_layout);
        }
        public void setText(String Name,String Value){
            searchName.setText(Name+"\n"+Value);
        }
    }
    public class search extends AsyncTask<Void,Void,Void>{
        private ProgressDialog pd;
        public search(Context activity) {
            pd = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            pd.setTitle("Please Wait");
            pd.setMessage("Searching Data...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }
}