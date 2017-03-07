package dcastalia.com.munshijobsportal.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHandler;
import dcastalia.com.munshijobsportal.Util.VolleyCustomRequest;
import dcastalia.com.munshijobsportal.fragment.SetPasswordFragmentUser;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

import static com.android.volley.VolleyLog.d;

public class RegisterActivity extends AppCompatActivity  {

    protected static final String TAG = "RegFragment_1";
    private Context context = RegisterActivity.this;

    Button btn_reg1;
    String category;
    int radioSelectedId ;
    EditText input_userName;
    EditText input_passport_nubmer;
    EditText input_phone;
    SharedPreferences sharedpreferences;
    SessionManager sessionManager ;
    private RadioButton radioButton;
    private SQLiteHandler db;
    private String name , passport, phone;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_reg1 = (Button)findViewById(R.id.btn_reg1);

        db = new SQLiteHandler(context);

        sessionManager = new SessionManager(getApplicationContext());
        input_userName = (EditText)findViewById(R.id.input_userName);
        input_passport_nubmer = (EditText)findViewById(R.id.input_passport_nubmer);
        input_phone = (EditText)findViewById(R.id.input_phone);
        radioGroup = (RadioGroup) findViewById(R.id.radio_select);



        btn_reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = input_userName.getText().toString();
                passport = input_passport_nubmer.getText().toString();
                phone = input_phone.getText().toString();
                radioSelectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(radioSelectedId);
                category =  radioButton.getText().toString();

                if (input_userName.getText().toString().length() == 0) {
                    input_userName.setError("Please enter your name");
                    input_userName.requestFocus();
                }
                else if (input_passport_nubmer.getText().toString().length() == 0) {
                    input_passport_nubmer.setError("Please enter your passport number");
                    input_passport_nubmer.requestFocus();
                }

                else   if (input_passport_nubmer.length()!=9)
                {
                    Toast.makeText(context, "Invalid passport number!", Toast.LENGTH_LONG).show();

                }
                else  if (input_phone.getText().toString().length()!=11) {
                    input_phone.setError("Please enter your phone number");
                    input_phone.requestFocus();
                }

                else if (!isValidPhone(phone)) {
                    Toast.makeText(context, "Phone number not valid!", Toast.LENGTH_LONG).show();

                }
                else if (category==null) {
                    Toast.makeText(context, "Select Catagory!", Toast.LENGTH_LONG).show();

                }


                if(name !=null && passport!=null && category != null){
                    dataSendToServer(name,passport,phone, category);


                }







            }
        });






    }

    public void fragmentTransaction(){
        Fragment fragment = new SetPasswordFragmentUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    public static boolean isValidPhone(String phone)
    {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }

    public void dataSendToServer(final String name, final String passport, final String phone, String catagory) {

        String hitURL = "http://bestinbd.com/projects/web/munshi/restAPI/site/register";

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name); //Items - Item 1 - name
        params.put("passport", passport); //Items - passport
        params.put("phone", phone); //Items - Item 3 - phone
        params.put("catagory", catagory); //Items - Item 3 - catagory


        VolleyCustomRequest postRequest = new VolleyCustomRequest(Request.Method.POST, hitURL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Register Response: " + response.toString());

                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                Toast.makeText(context, "Registration success!", Toast.LENGTH_LONG).show();
                                String id=response.getString("id");
                                String code = response.getString("verification_code").toString();
                                sessionManager.createLoginSession(id,name,phone,passport,code);
                                Toast.makeText(context,
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context,response.getString("verification_code").toString(), Toast.LENGTH_LONG).show();

                                fragmentTransaction();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        d(TAG, "Error: " + volleyError.getMessage());

                        if (volleyError instanceof NetworkError) {
                            Toast.makeText(context, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(context, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(context, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();


                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(context, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof NoConnectionError) {
                            Toast.makeText(context, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof TimeoutError) {
                            Toast.makeText(context, "Connection TimeOut! Please check your internet connection", Toast.LENGTH_SHORT).show();

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
}
