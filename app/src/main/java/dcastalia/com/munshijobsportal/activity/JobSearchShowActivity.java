package dcastalia.com.munshijobsportal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHelper;
import dcastalia.com.munshijobsportal.adapter.JobOpeningAdapter;

public class JobSearchShowActivity extends AppCompatActivity {

    public static final String TAG_JOB_SEARCH_SHOW = "JobSearchShowActivity";
    RecyclerView recyclerView;
    JobOpeningAdapter adapter;
    SQLiteHelper sqLiteHelper ;
    private Context context = JobSearchShowActivity.this;
    private ArrayList<Jobs> jobList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search_show);
        recyclerView = (RecyclerView) findViewById(R.id.job_search_list);
        sqLiteHelper = new SQLiteHelper(JobSearchShowActivity.this);
        Intent intent = getIntent() ;
        if(intent!=null){
            jobList = (ArrayList<Jobs>) intent.getSerializableExtra("job_list");
        }
        if(jobList!=null){
            sqLiteHelper.insertIntoSearchJob(jobList);
        }
        loadSearchList(sqLiteHelper.getSearchJobList());
    }

    private void loadSearchList(ArrayList<Jobs> jobList) {
        adapter = new JobOpeningAdapter(context, jobList,TAG_JOB_SEARCH_SHOW);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home :
                sqLiteHelper.deleteSearchJobs();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sqLiteHelper.deleteSearchJobs();
        Intent intent = new Intent(JobSearchShowActivity.this,JobSearchActivity.class);
        startActivity(intent);

    }
}
