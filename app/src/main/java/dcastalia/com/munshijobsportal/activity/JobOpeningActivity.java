package dcastalia.com.munshijobsportal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.adapter.JobOpeningAdapter;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class JobOpeningActivity extends AppCompatActivity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Job opening  json url
    private static final String URL = " http://bestinbd.com/projects/web/munshi/restAPI/site/joblist";
    private static final String url = " http://bestinbd.com/projects/web/munshi/restAPI/site/availablejobs";
    Button btn_search;
    RecyclerView recyclerView;
    JobOpeningAdapter adapter;
    TextView txt;
    private ProgressDialog pDialog;
    private Context context = JobOpeningActivity.this;
    private ArrayList<Jobs> jobList;
    private SessionManager sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestFullScreenWindow();


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_job_opening);
        sessionManager = new SessionManager(JobOpeningActivity.this);
        sessionManager.checkLogin();
        setupToolbar();
        txt = (TextView) findViewById(R.id.available_job);
        makeJsonObjectRequest();

        btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobOpeningActivity.this, JobSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        jobList = new ArrayList<Jobs>();
        //Listview adapter----------------------

        // Creating volley request obj
        JsonArrayRequest jobReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Json response: " + response.toString());


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

                                jobList.add(jobs);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            loadJobList(jobList);
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data


                        hidePDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
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
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jobReq);


    }

    private void setupToolbar() {
        // Show the Actionbar in the activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Job Opening");
    }

    private void requestFullScreenWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void loadJobList(ArrayList<Jobs> jobList) {
        adapter = new JobOpeningAdapter(context, jobList);
        recyclerView = (RecyclerView) findViewById(R.id.list_job_open);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    /**
     * Method to make json object request where json response starts wtih showing availlble job {
     */
    private void makeJsonObjectRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Json response text: " + response.toString());
                try {
                    // Parsing json object response
                    // response will be a json object
                    String avilable_job = response.getString("available_job");
                    txt.setText(avilable_job);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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
        Intent intent = new Intent(JobOpeningActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        startActivity(intent);
    }
}