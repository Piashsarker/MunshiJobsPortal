package dcastalia.com.munshijobsportal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.Util.VolleyCustomRequest;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;
import it.sephiroth.android.library.tooltip.Tooltip;

public class JobDetailsActivity extends AppCompatActivity {

    Button btn_apply;
    Button btn_like;
    String jobId, title , position , coutry,company ,vacancy, experince, salary , expireDate , age , gender , jobNature,jobDescription,jobRequirement ;
    TextView tvJobTitle , tvJobPosition , tvJobLocation, tvCompany , tvVacancy, tvExperince, tvSalary , tvExpireDate , tvAge , tvGender , tvJobNature, tvJobDescription,tvJobRequirement;
    private SessionManager sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestFullScreenWindow();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        sessionManager = new SessionManager(JobDetailsActivity.this);
        sessionManager.checkLogin();
        btn_apply=(Button)findViewById(R.id.btn_apply);
        tvJobTitle = (TextView) findViewById(R.id.txt_title);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postApplyToServer();

            }
        });

        btn_like=(Button)findViewById(R.id.btn_like);

        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Added to the favourite list!",Toast.LENGTH_LONG).show();

            }
        });

        tvJobTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(JobDetailsActivity.this, new Tooltip.Builder(101)
                                .anchor(tvJobTitle, Tooltip.Gravity.BOTTOM)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 4000)
                                .activateDelay(900)
                                .showDelay(400)
                                .text(tvJobTitle.getText().toString())
                                .maxWidth(600)
                                .withArrow(true)
                                .withOverlay(true).build()
                ).show();
            }
        });


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                jobId = null;
                title= null;
                position = null;
                coutry = null;
                company = null;
                vacancy = null ;
                experince = null ;
                salary = null;
                expireDate = null ;
                age = null ;
                gender = null ;
                jobNature = null;
                jobDescription = null;
                jobRequirement = null;
            } else {
                jobId = extras.getString("job_id");
                title= extras.getString("job_title");
                position = extras.getString("job_position");
                coutry = extras.getString("country");
                vacancy = extras.getString("vacancy");
                company = extras.getString("company");
                experince = extras.getString("experience");
                salary = extras.getString("salary");
                expireDate = extras.getString("expire_date");
                age = extras.getString("age");
                gender = extras.getString("gender");
                jobNature = extras.getString("job_nature");
                jobDescription = extras.getString("job_description");
                jobRequirement = extras.getString("job_requirement");

                setView(company, title, position,coutry,vacancy,experince,salary,expireDate,age,gender,jobNature,jobDescription,jobRequirement);
            }
        } else {
            title= (String) savedInstanceState.getSerializable("job_title");
            position= (String) savedInstanceState.getSerializable("job_position");
            coutry= (String) savedInstanceState.getSerializable("country");

        }




    }

    private void postApplyToServer() {

        String personId = sessionManager.getUserId();
        String hitURL = "http://bestinbd.com/projects/web/munshi/restAPI/site/apply_on_job";
        HashMap<String, String> params = new HashMap<>();
        params.put("job_id", jobId ); //Items - Item 1 - name
        params.put("person_id", personId); //Items - passport



        VolleyCustomRequest postRequest = new VolleyCustomRequest(Request.Method.POST, hitURL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            int status = response.getInt("status");
                            if (status == 1) {

                                Toast.makeText(JobDetailsActivity.this, "Apply Successful", Toast.LENGTH_SHORT).show();
                                btn_apply.setText("Applied");
                                btn_apply.setClickable(false);
                                btn_apply.setBackgroundColor(Color.GRAY);

                            }
                            if (status==0){
                                Toast.makeText(JobDetailsActivity.this, "Can Not Apply Right Now", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (volleyError instanceof NetworkError) {
                            Toast.makeText(JobDetailsActivity.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(JobDetailsActivity.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(JobDetailsActivity.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();


                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(JobDetailsActivity.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof NoConnectionError) {
                            Toast.makeText(JobDetailsActivity.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof TimeoutError) {
                            Toast.makeText(JobDetailsActivity.this, "Connection TimeOut! Please check your internet connection", Toast.LENGTH_SHORT).show();

                        }


                    }
                });


        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest);

    }

    private void setView(String company, String title, String position, String coutry, String vacancy, String experince, String salary, String expireDate, String age, String gender, String jobNature, String jobDescription, String jobRequirement) {
        tvJobTitle = (TextView) findViewById(R.id.txt_title);
        tvJobPosition = (TextView) findViewById(R.id.txt_job_position);
        tvJobLocation = (TextView) findViewById(R.id.txt_job_location);
        tvCompany = (TextView) findViewById(R.id.txt_company_name);
        tvVacancy = (TextView) findViewById(R.id.txt_job_vacancy);
        tvExperince = (TextView) findViewById(R.id.txt_experience);
        tvSalary = (TextView) findViewById(R.id.txt_salary);
        tvExpireDate = (TextView) findViewById(R.id.txt_expire_date);
        tvAge = (TextView) findViewById(R.id.txt_age);
        tvGender = (TextView) findViewById(R.id.txt_gender);
        tvJobNature = (TextView) findViewById(R.id.txt_job_nature);
        tvJobDescription = (TextView) findViewById(R.id.txt_job_description);
        tvJobRequirement = (TextView) findViewById(R.id.txt_job_requirement);

        tvJobTitle.setText(title);
        tvJobPosition.setText(position);
        tvJobLocation.setText(coutry);
        tvCompany.setText(company);
        tvVacancy.setText(vacancy);
        tvExperince.setText(experince);
        tvSalary.setText(salary);
        tvAge.setText(age);
        tvGender.setText(gender);
        tvJobNature.setText(jobNature);
        tvJobDescription.setText(jobDescription);
        tvJobRequirement.setText(jobRequirement);
    }



    private void requestFullScreenWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        Intent intent = new Intent(JobDetailsActivity.this, JobOpeningActivity.class);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        startActivity(intent);


    }


}
