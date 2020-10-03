package mx.feliperomero.guardias;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] mDataset;
    private DatabaseReference mDatabase;
    private ArrayList<String> mGuardias = new ArrayList<>();
    private ArrayList<String> mGuardiasIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFirebase();

        mRecyclerView = findViewById(R.id.my_recycler_view);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new CustomAdapter(mGuardias);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("guardias");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: " + snapshot.toString());
                Guardia guardia = snapshot.getValue(Guardia.class);
                if (guardia != null) {
                    guardia.setId(snapshot.getKey());
                    mGuardiasIds.add(snapshot.getKey());
                    mGuardias.add(guardia.toString());
                    mAdapter.notifyItemInserted(mGuardias.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildChanged: " + snapshot.getKey());

                Guardia changedGuardia = snapshot.getValue(Guardia.class);
                int guardiaIndex = mGuardiasIds.indexOf(snapshot.getKey());
                if (guardiaIndex > -1) {
                    if (changedGuardia != null) {
                        mGuardias.set(guardiaIndex, changedGuardia.toString());
                        mAdapter.notifyItemChanged(guardiaIndex);
                    }
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child: " + snapshot.getKey());
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onChildRemoved: " + snapshot.getKey());

                int guardiaIndex = mGuardiasIds.indexOf(snapshot.getKey());
                if (guardiaIndex > -1) {
                    mGuardiasIds.remove(guardiaIndex);
                    mGuardias.remove(guardiaIndex);
                    mAdapter.notifyItemRemoved(guardiaIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child: " + snapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildMoved: " + snapshot.getKey());

                Guardia movedGuardia = snapshot.getValue(Guardia.class);
                int guardiaIndex = mGuardiasIds.indexOf(snapshot.getKey());
                if (guardiaIndex > -1) {
                    if (movedGuardia != null) {
                        mGuardias.set(guardiaIndex, movedGuardia.toString());
                        mAdapter.notifyItemChanged(guardiaIndex);
                    }
                } else {
                    Log.w(TAG, "onChildMoved:unknown_child: " + snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.toException());
                Toast.makeText(MainActivity.this, "Failed to load guardias",
                        Toast.LENGTH_LONG).show();
            }
        });
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
