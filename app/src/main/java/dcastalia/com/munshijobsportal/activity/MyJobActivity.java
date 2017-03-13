package dcastalia.com.munshijobsportal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHelper;
import dcastalia.com.munshijobsportal.adapter.JobOpeningAdapter;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class MyJobActivity extends AppCompatActivity {
    Context context;
    private RecyclerView recyclerView ;
    private ArrayList<Jobs> jobsArrayList ;
    JobOpeningAdapter adapter ;
    private SessionManager sessionManager ;
    private SQLiteHelper sqLiteHelper ;
    public static final String TAG_MYJOBACTIVITY = "MyJobActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //For Full Screen view----------------------
        requestFullScreenWindow();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        context = MyJobActivity.this;
        sessionManager = new SessionManager(MyJobActivity.this);
        sessionManager.checkLogin();
        sqLiteHelper = new SQLiteHelper(MyJobActivity.this);
        toolbarSetup();
        loadMyJobList();

    }

    private void loadMyJobList() {
        jobsArrayList = new ArrayList<>();
        jobsArrayList = sqLiteHelper.getAppliedJobList();
        loadJobListView(jobsArrayList);
    }

    private void loadJobListView(ArrayList<Jobs> jobsArrayList) {
        adapter = new JobOpeningAdapter(context, jobsArrayList,TAG_MYJOBACTIVITY);
        recyclerView = (RecyclerView) findViewById(R.id.list_my_job);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void requestFullScreenWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void toolbarSetup() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("My Job");
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.finish();
        Intent intent = new Intent(MyJobActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        startActivity(intent);


    }
}
