package com.chris_guzman.taasty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.f2prateek.dart.HensonNavigable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

@HensonNavigable
public class SearchTacosActivity extends AppCompatActivity {
    private static final String TAG = SearchTacosActivity.class.getSimpleName();
    @BindView(R.id.taco_search) EditText tacoSearch;
    @BindView(R.id.taco_recycler) RecyclerView tacoRecycler;
    private Realm realmInstance;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RealmResults<Taco> myDataset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tacos);
        Realm.init(this);
        ButterKnife.bind(this);
        realmInstance = Realm.getDefaultInstance();

        myDataset = realmInstance.where(Taco.class).findAll();

        Log.d(TAG, String.valueOf(myDataset.size()));

        tacoRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        tacoRecycler.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        tacoRecycler.setAdapter(mAdapter);

        tacoSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchForTaco(tacoSearch.getText().toString());
                return true;
            }
        });
    }

    private void searchForTaco(String term) {
        myDataset = realmInstance.where(Taco.class).contains("name", term, Case.INSENSITIVE).findAll();
        mAdapter = new MyAdapter(myDataset);
        tacoRecycler.swapAdapter(mAdapter, true);
        Log.d(TAG, String.valueOf(myDataset.size()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmInstance.close();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private RealmResults<Taco> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(RealmResults<Taco> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final Taco taco = mDataset.get(position);
            Log.d(TAG, "onBindViewHolder: dataset size" + mDataset.size());
            holder.mTextView.setText(taco.getName());
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        startActivity(Henson.with(SearchTacosActivity.this).gotoTacoDetailActivity().name(taco.getName()).url(taco.getUrl()).imageUrl(taco.getImageUrl()).build());
                }
            });
            Log.d(TAG, "onBindViewHolder: " + taco.getName());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
