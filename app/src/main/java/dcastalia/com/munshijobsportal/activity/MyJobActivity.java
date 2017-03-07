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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.adapter.MyJobAdapter;

public class MyJobActivity extends AppCompatActivity {
    public static boolean isToastShown = false;
    Context context;
    private RecyclerView recyclerView ;
    private ArrayList<Jobs> jobsArrayList ;
    MyJobAdapter adapter ;
    private static final String URL = " http://bestinbd.com/projects/web/munshi/restAPI/site/applied_job_list";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //For Full Screen view----------------------
        requestFullScreenWindow();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        toolbarSetup();
        loadMyJobList();



    }

    private void loadMyJobList() {
        jobsArrayList = new ArrayList<Jobs>();
        //Listview adapter----------------------

        // Creating volley request obj
        JsonArrayRequest jobReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Jobs jobs = new Jobs();
                                jobs.setJobId(obj.getString("job_id"));
                                jobs.setJob_title(obj.getString("title"));
                                jobs.setJob_position(obj.getString("position_company"));
                                jobs.setCountry(obj.getString("country"));
                                jobs.setCompany(obj.getString("company"));
                                jobs.setDate(obj.getString("expire_date"));
                                jobs.setVacancy(obj.getString("no_of_vacancy"));
                                jobs.setExperince(obj.getString("experience"));
                                jobs.setSalary(obj.getString("salary"));
                                //jobs.setAge(obj.getString(""));
                                //jobs.setGender(obj.getString(""));
                                jobs.setJobNature(obj.getString("nature"));
                                //jobs.setJobDescription(obj.getString(""));
                                jobs.setJobRequirement(obj.getString("requirements"));


                                // adding jobs to Jobs array

                                jobsArrayList.add(jobs);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            loadJobList(jobsArrayList);
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();

                if (volleyError instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                } else if (volleyError instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();

                } else if (volleyError instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();


                } else if (volleyError instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();

                } else if (volleyError instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();

                } else if (volleyError instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Connection TimeOut! Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jobReq);
    }

    private void loadJobList(ArrayList<Jobs> jobsArrayList) {
        adapter = new MyJobAdapter(context, jobsArrayList);
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
