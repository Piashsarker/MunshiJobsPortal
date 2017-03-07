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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.Model.Country;
import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.Model.Profession;
import dcastalia.com.munshijobsportal.Model.Salary;
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
    private static final String URL = "http://bestinbd.com/projects/web/munshi/restAPI/site/search_prefil";

    Button btnSearch , btnSearchNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreenWindow();
        setContentView(R.layout.activity_job_search);
        toolbarSetup();
        initializeViews();
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
        if(!etJobName.getText().toString().isEmpty() && !countryId.isEmpty() && !professionId.isEmpty() && !experince.isEmpty()&& !expireDate.isEmpty() &&!salaryRange.isEmpty()){
            ArrayList<Jobs> searchJobList ;
            searchJobList= sentToserverForResult();
            if(!searchJobList.isEmpty()){
                Intent intent = new Intent(JobSearchActivity.this,JobSearchShowActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "No Matching Data", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Fields Are Empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadServerData() {

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,new Response.Listener<JSONObject>() {
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
    }

    private void setExperinceSpinnerData() {
        experinceYearList.add("1 Yrs");
        experinceYearList.add("2 Yrs");
        experinceYearList.add("3 Yrs");
        experinceYearList.add("4 Yrs");
        experinceYearList.add("5 Yrs");
        experinceYearList.add("6 Yrs");
        experinceYearList.add("7 Yrs");
        experinceYearList.add("8 Yrs");
        experinceYearList.add("9 Yrs");
        experinceYearList.add("10 Yrs");
        ArrayAdapter<String> salaryRangeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,experinceYearList);
        salaryRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experinceSpinner.setAdapter(salaryRangeAdapter);

    }

    private void setProfessionSpinnerData(ArrayList<Profession> professionArrayList) {
        ArrayList<String> professionName = new ArrayList<>();
        for(int i =0 ; i<professionArrayList.size();i++){
            professionName.add(professionArrayList.get(i).getProfessionName());
        }
        ArrayAdapter<String> professionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,professionName);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(professionAdapter);

    }


    private void setLocationSpinnerData(ArrayList<Country> countryArrayList) {
        ArrayList<String> countryName = new ArrayList<>();
        for(int i =0 ; i<countryArrayList.size();i++){
            countryName.add(countryArrayList.get(i).getCountryName());
        }
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countryName);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

    }

    private void setExpireSpinnerData() {

        expireDateList.add("10 days");
        expireDateList.add("20 days");
        expireDateList.add("30 days");
        expireDateList.add("40 days");
        expireDateList.add("50 days");
        expireDateList.add("60 days");
        expireDateList.add("70 days");
        expireDateList.add("80 days");
        ArrayAdapter<String> salaryRangeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,expireDateList);
        salaryRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expireSpinner.setAdapter(salaryRangeAdapter);
    }

    private void setSalaryRangeSpinnerData(ArrayList<Salary> salaryList) {
        ArrayList<String> salaryRange = new ArrayList<>();
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
            countryId = countryArrayList.get(position).getCountryId();
        }

        if (spinner.getId()==R.id.spinner_profession){
            professionId = professionArrayList.get(position).getProfessionId();

        }

        if (spinner.getId()==R.id.spinner_experince){
            experince = experinceSpinner.getSelectedItem().toString();
        }
        if (spinner.getId()==R.id.spinner_expire_date){
            expireDate = expireSpinner.getSelectedItem().toString();
        }
        if (spinner.getId()==R.id.spinner_salary_range){
            salaryRange = salaryArrayList.get(position).getSalaryRange();
        }




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    private ArrayList<Jobs> sentToserverForResult() {
        return null;
    }
}