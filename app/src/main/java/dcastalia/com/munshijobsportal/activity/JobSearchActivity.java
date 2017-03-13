package dcastalia.com.munshijobsportal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.ErrorDialog;
import dcastalia.com.munshijobsportal.Model.Country;
import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.Model.Profession;
import dcastalia.com.munshijobsportal.Model.Salary;
import dcastalia.com.munshijobsportal.ProgressDialog;
import dcastalia.com.munshijobsportal.R;

public class JobSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etJobName ;
    private Spinner locationSpinner, professionSpinner, experinceSpinner,expireSpinner,salaryRangeSpinner;
    private ArrayList<Country> countryArrayList ;
    private ArrayList<Profession> professionArrayList;
    private ArrayList<Salary> salaryArrayList;
    ArrayList<String> experinceYearList = new ArrayList<>();
    ArrayList<String> expireDateList = new ArrayList<>();
    private String jobName , countryId , professionId , experince , expireDate , salaryRange ;
    private static final String SEARCH_URL = "http://bestinbd.com/projects/web/munshi/restAPI/site/search";
    private static final String SPINNER_URL = "http://bestinbd.com/projects/web/munshi/restAPI/site/search_prefil";

    private String selectLocation, selectProfession , selectExperince, selectExpireDate , selectSalaryRange;
    ProgressDialog progressDialog;
    Button btnSearch , btnSearchNow;
    ErrorDialog errorDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreenWindow();
        setContentView(R.layout.activity_job_search);
        toolbarSetup();
        initializeViews();
        progressDialog = new ProgressDialog(JobSearchActivity.this);
        errorDialog = new ErrorDialog(JobSearchActivity.this);
        loadServerData();
        setExpireSpinnerData();
        setExperinceSpinnerData();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getJobSearchListFromServer();
            }
        });

        btnSearchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJobSearchListFromServer();
            }
        });



        




    }

    private void getJobSearchListFromServer() {
        progressDialog.showProgress();
        final ArrayList<Jobs> searchJobList = new ArrayList<Jobs>() ;
        jobName = etJobName.getText().toString();
        jobName = jobName.trim().toLowerCase();
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.length()==0){
                    errorDialog.showDialog("Empty","No Data Found..");
                }
                else{
                    Log.d("JobSearchActivity", "Search Response " + response.toString());
                    try {
                        JSONArray jsonArray  = new JSONArray(response);
                        for (int i=0 ; i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
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

                            searchJobList.add(jobs);
                        }
                        goToSearchResultActivity(searchJobList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            errorDialog.showDialog("Error!","Server Error!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                 if(!jobName.equals("")){
                     params.put("job_name", jobName);
                 }

                if(!countryId.equals("")){
                    params.put("country_id",countryId);
                }
                if(!professionId.equals("")){
                    params.put("trade_id",professionId);
                }
                if(!experince.equals("")){
                    params.put("experience",experince);
                }
                if(!salaryRange.equals("")){
                    params.put("salary_range",salaryRange);
                }
                if(!expireDate.equals("")){
                    params.put("deadline",expireDate);
                }

               //


                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);

        progressDialog.hideProgress();

    }

    private void goToSearchResultActivity(ArrayList<Jobs> searchJobList) {

        Intent intent = new Intent(JobSearchActivity.this,JobSearchShowActivity.class);
        intent.putExtra("job_list", searchJobList);
        startActivity(intent);

    }

    private void loadServerData() {

        progressDialog.showProgress();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,SPINNER_URL,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    countryArrayList= new ArrayList<>();
                    JSONObject  countyObject = response.getJSONObject("country_list");

                    Iterator<String> countryIterator = countyObject.keys();
                    while(countryIterator.hasNext()){
                        String key = countryIterator.next();
                        Object value = countyObject.get(key);
                        Country country = new Country();
                        country.setCountryId(key);
                        country.setCountryName(value.toString());
                        countryArrayList.add(country);
                        Log.d("Iterator Value",value.toString());
                    }
                    if(!countryArrayList.isEmpty()){
                        setLocationSpinnerData(countryArrayList);
                    }


                    professionArrayList = new ArrayList<>();
                    JSONObject professionObject = response.getJSONObject("profession_list");
                    Iterator<String> professionIterator = professionObject.keys();
                    while (professionIterator.hasNext()){
                        String key = professionIterator.next();
                        Object value = professionObject.get(key);
                        Profession profession = new Profession();
                        profession.setProfessionId(key);
                        profession.setProfessionName(value.toString());
                        professionArrayList.add(profession);
                    }
                    if(!professionArrayList.isEmpty()){
                        setProfessionSpinnerData(professionArrayList);
                    }

                    salaryArrayList = new ArrayList<>();
                    JSONObject salaryObject = response.getJSONObject("salary_range_list");
                    Iterator<String> salaryIterator = salaryObject.keys();
                    while (salaryIterator.hasNext()){
                        String key = salaryIterator.next();
                        Object value = salaryObject.get(key);
                        Salary salary = new Salary();
                        salary.setSalaryId(key);
                        salary.setSalaryRange(value.toString());
                        salaryArrayList.add(salary);
                    }
                    if(!salaryArrayList.isEmpty()){
                        setSalaryRangeSpinnerData(salaryArrayList);
                    }




                }catch (JSONException ex){
                    Toast.makeText(JobSearchActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }

            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JobSearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        progressDialog.hideProgress();

    }

    private void setExperinceSpinnerData() {
        experinceYearList.add(getResources().getString(R.string.select_experience));
        experinceYearList.add("1");
        experinceYearList.add("2");
        experinceYearList.add("3");
        experinceYearList.add("4");
        experinceYearList.add("5");
        experinceYearList.add("6");
        experinceYearList.add("7");
        experinceYearList.add("8");
        experinceYearList.add("9");
        experinceYearList.add("10");
        ArrayAdapter<String> salaryRangeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,experinceYearList);
        salaryRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experinceSpinner.setAdapter(salaryRangeAdapter);

    }

    private void setProfessionSpinnerData(ArrayList<Profession> professionArrayList) {
        ArrayList<String> professionName = new ArrayList<>();
        professionName.add(getResources().getString(R.string.select_profession));
        for(int i =0 ; i<professionArrayList.size();i++){

            professionName.add(professionArrayList.get(i).getProfessionName());
        }

        ArrayAdapter<String> professionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,professionName);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(professionAdapter);

    }


    private void setLocationSpinnerData(ArrayList<Country> countryArrayList) {
        ArrayList<String> countryName = new ArrayList<>();
        countryName.add(getResources().getString(R.string.select_location));
        for(int i =0 ; i<countryArrayList.size();i++){
            countryName.add(countryArrayList.get(i).getCountryName());
        }

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countryName);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

    }

    private void setExpireSpinnerData() {
        expireDateList.add(getResources().getString(R.string.select_expire_date));
        expireDateList.add("10");
        expireDateList.add("20");
        expireDateList.add("30");
        expireDateList.add("40");
        expireDateList.add("50");
        expireDateList.add("60");
        expireDateList.add("70");
        expireDateList.add("80");
        expireDateList.add("100");
        expireDateList.add("120");
        ArrayAdapter<String> salaryRangeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,expireDateList);
        salaryRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expireSpinner.setAdapter(salaryRangeAdapter);
    }

    private void setSalaryRangeSpinnerData(ArrayList<Salary> salaryList) {
        ArrayList<String> salaryRange = new ArrayList<>();
        salaryRange.add(getResources().getString(R.string.select_salary_range));
        for(int i =0 ; i<salaryList.size();i++){
            salaryRange.add(salaryList.get(i).getSalaryRange());
        }
        ArrayAdapter<String> salaryRangeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,salaryRange);
        salaryRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salaryRangeSpinner.setAdapter(salaryRangeAdapter);
    }

   

    private void initializeViews() {
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearchNow = (Button) findViewById(R.id.btn_search_now);
        etJobName = (EditText) findViewById(R.id.et_job_name);
        locationSpinner = (Spinner) findViewById(R.id.spinner_location);
        professionSpinner = (Spinner) findViewById(R.id.spinner_profession);
        experinceSpinner = (Spinner) findViewById(R.id.spinner_experince);
        expireSpinner = (Spinner) findViewById(R.id.spinner_expire_date);
        salaryRangeSpinner = (Spinner) findViewById(R.id.spinner_salary_range);

        locationSpinner.setOnItemSelectedListener(this);
        professionSpinner.setOnItemSelectedListener(this);
        experinceSpinner.setOnItemSelectedListener(this);
        expireSpinner.setOnItemSelectedListener(this);
        salaryRangeSpinner.setOnItemSelectedListener(this);

    }

    private void toolbarSetup() {
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner)parent;
        if (spinner.getId()==R.id.spinner_location){
            selectLocation = locationSpinner.getSelectedItem().toString();
            if(selectLocation.equals(getResources().getString(R.string.select_location))){
                countryId = "";
            }
            else{
                countryId = countryArrayList.get(position-1).getCountryId();
            }
        }
        if (spinner.getId()==R.id.spinner_profession){
            selectProfession = professionSpinner.getSelectedItem().toString();
            if(selectProfession.equals(getResources().getString(R.string.select_profession))){
                professionId= "";
            }
            else {
                professionId = professionArrayList.get(position-1).getProfessionId();
            }

        }
        if (spinner.getId()==R.id.spinner_experince){
            experince = experinceSpinner.getSelectedItem().toString();
            if(experince.equals(getResources().getString(R.string.select_experience))){
               experince = "";
            }
        }
        if (spinner.getId()==R.id.spinner_expire_date){
            expireDate = expireSpinner.getSelectedItem().toString();
            if(expireDate.equals(getResources().getString(R.string.select_expire_date))){
                expireDate= "";
            }
        }
        if (spinner.getId()==R.id.spinner_salary_range){
            selectSalaryRange = salaryRangeSpinner.getSelectedItem().toString();
            if(selectSalaryRange.equals(getResources().getString(R.string.select_salary_range))){
                salaryRange = "";
            }
            else{
                salaryRange = salaryArrayList.get(position-1).getSalaryRange();
            }

        }




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(JobSearchActivity.this,MainActivity.class);
        startActivity(intent);
    }
}