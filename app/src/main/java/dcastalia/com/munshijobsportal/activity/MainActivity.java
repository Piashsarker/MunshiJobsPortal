package dcastalia.com.munshijobsportal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.ErrorDialog;
import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHelper;
import dcastalia.com.munshijobsportal.adapter.JobOpeningAdapter;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getSimpleName();

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Button btn_search;
    RecyclerView recyclerView;
    JobOpeningAdapter adapter;
    TextView txt;
    SessionManager sessionManager;
    SQLiteHelper sqLiteHelper;
    private ProgressDialog pDialog;
    private ErrorDialog errorDialog;
    private Context context = MainActivity.this;
    private ArrayList<Jobs> jobList;
    private static final String URL = " http://bestinbd.com/projects/web/munshi/restAPI/site/joblist";
    private static final String url = " http://bestinbd.com/projects/web/munshi/restAPI/site/availablejobs";
    private dcastalia.com.munshijobsportal.ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreenWindow();

        sessionManager = new SessionManager(MainActivity.this);
        sessionManager.checkLogin();
        setContentView(R.layout.activity_main);
        sqLiteHelper = new SQLiteHelper(MainActivity.this);
        errorDialog = new ErrorDialog(MainActivity.this);
        progressDialog = new dcastalia.com.munshijobsportal.ProgressDialog(MainActivity.this);

        getSupportActionBar().hide();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        txt = (TextView) findViewById(R.id.available_job);
        makeJsonObjectRequest();

        btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JobSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */


        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_profile) {

                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);

                }

                if (menuItem.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.nav_myjobs) {
                    Intent intent = new Intent(MainActivity.this, MyJobActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }

                if (menuItem.getItemId() == R.id.nav_fav_job) {
                    Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }

                if (menuItem.getItemId() == R.id.nav_help) {
                    Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }

                if (menuItem.getItemId() == R.id.nav_logOut) {
                    sessionManager.logoutUser();
                }


                return false;
            }

        });


        /**
         * Setup Drawer Toggle of the Toolbar
         */

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);


        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        loadMyJobsListFromServer();
        makeJsonObjectRequest();


    }

    private void loadMyJobsListFromServer() {
        jobList = new ArrayList<Jobs>();
        //Listview adapter----------------------

        progressDialog.showProgress();
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
                            saveIntoDatabase(jobList);
                            progressDialog.hideProgress();

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                volleyError.printStackTrace();

                if (volleyError instanceof NetworkError) {
                    errorDialog.showDialog("No Internet!","Enable WIFI or Mobile Data");
                } else if (volleyError instanceof ServerError) {
                    errorDialog.showDialog("Server Error!","Server Not Found. Try Again Later");
                } else if (volleyError instanceof AuthFailureError) {
                    errorDialog.showDialog("No Internet!","Enable WIFI or Mobile Data");

                } else if (volleyError instanceof ParseError) {
                    errorDialog.showDialog("Parsing Error!","Please Try Again Some Time");
                } else if (volleyError instanceof NoConnectionError) {
                    errorDialog.showDialog("No Internet!","Enable WIFI or Mobile Data");
                } else if (volleyError instanceof TimeoutError) {
                    errorDialog.showDialog("Timeout Error","Connection Timeout Check Internet Connection");
                }

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jobReq);
    }

    private void saveIntoDatabase(ArrayList<Jobs> jobList) {

      for(int i =0 ;i<jobList.size() ; i++){
          boolean found = sqLiteHelper.jobExists(jobList.get(i).getJobId());
          String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

          if(found){
              sqLiteHelper.updateJob(jobList.get(i).getJobId(),new Jobs(jobList.get(i).getCompany(),jobList.get(i).getVacancy(),jobList.get(i).getAvailable_job(),jobList.get(i).getJob_title(),jobList.get(i).getJob_position(),jobList.get(i).getCountry(),jobList.get(i).getDate(),
                      jobList.get(i).getSalary(),jobList.get(i).getExperince(),jobList.get(i).getAge(),jobList.get(i).getJobNature(),
                      jobList.get(i).getGender(),jobList.get(i).getJobDescription(),jobList.get(i).getJobRequirement()));
          }
          else{
              sqLiteHelper.insertJobIndividual(new Jobs(jobList.get(i).getJobId(), jobList.get(i).getCompany(),jobList.get(i).getVacancy(),jobList.get(i).getAvailable_job(),jobList.get(i).getJob_title(),jobList.get(i).getJob_position(),jobList.get(i).getCountry(),jobList.get(i).getDate(),
                      jobList.get(i).getSalary(),jobList.get(i).getExperince(),jobList.get(i).getAge(),jobList.get(i).getJobNature(),
                      jobList.get(i).getGender(),jobList.get(i).getJobDescription(),jobList.get(i).getJobRequirement(),currentDateTimeString,Jobs.FLAT_FOR_EMPTY));
          }
      }
    }

    private void loadJobList(ArrayList<Jobs> jobList) {
        adapter = new JobOpeningAdapter(context, jobList,TAG);
        recyclerView = (RecyclerView) findViewById(R.id.list_job_open);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
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


    private void requestFullScreenWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close application")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}


