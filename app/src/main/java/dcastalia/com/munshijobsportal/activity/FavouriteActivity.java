package dcastalia.com.munshijobsportal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHelper;
import dcastalia.com.munshijobsportal.adapter.JobOpeningAdapter;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class FavouriteActivity extends AppCompatActivity {

    SessionManager sessionManager;
    Context context;
    private RecyclerView recyclerView ;
    private ArrayList<Jobs> jobsArrayList ;
    JobOpeningAdapter adapter ;
    private SQLiteHelper sqLiteHelper ;
    public static final String TAG_FAVOURITE = "Favourite";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        context = FavouriteActivity.this;
        sqLiteHelper = new SQLiteHelper(FavouriteActivity.this);
        jobsArrayList = new ArrayList<>();
        jobsArrayList = sqLiteHelper.getFavouriteJobList();
        loadJobList(jobsArrayList);

    }
    private void loadJobList(ArrayList<Jobs> jobsArrayList) {
        adapter = new JobOpeningAdapter(context, jobsArrayList,TAG_FAVOURITE);
        recyclerView = (RecyclerView) findViewById(R.id.favourite_job_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
