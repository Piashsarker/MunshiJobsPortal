package dcastalia.com.munshijobsportal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.android.volley.VolleyLog.d;

public class ResetPasswordActivity extends AppCompatActivity {

    protected static final String TAG = "ResetPassActivity";

    Button btn_resend_reset_code;
    Button btn_change_pass_confirm;

    EditText input_reset_code;
    EditText input_new_password;
    EditText input_confirm_password;
    EditText input_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        input_reset_code = (EditText) findViewById(R.id.input_reset_code);
        input_new_password = (EditText)findViewById(R.id.input_new_password);
        input_confirm_password = (EditText)findViewById(R.id.input_confirm_password);
        input_phone = (EditText)findViewById(R.id.phone_no);

        btn_resend_reset_code=(Button)findViewById(R.id.btn_resend_reset_code);
        btn_change_pass_confirm=(Button)findViewById(R.id.btn_change_pass_confirm);


        btn_resend_reset_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Send Request for reset code",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ResetPasswordActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btn_change_pass_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = input_reset_code.getText().toString();
                String password = input_new_password.getText().toString();
                String phone = input_phone.getText().toString();

                dataSendToServer(code, password,phone);


            }
        });
    }

    public void dataSendToServer(String code, String password,String phone) {


        String hitURL = "http://bestinbd.com/projects/web/munshi/restAPI/site/reset_password";

        HashMap<String, String> params = new HashMap<>();
        params.put("code", code); //Items - Item 2  new password
        params.put("password", password); //Items - Item 2  new password
        params.put("phone", phone); //Items - Item 1 - resetcode




        VolleyCustomRequest postRequest = new VolleyCustomRequest(Request.Method.POST, hitURL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Register Response password change: " + response.toString());

                        try {
                            int status = response.getInt("status");
                            if (status == 1) {


                                Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),"Your password has been changed",Toast.LENGTH_LONG).show();
                                jumpToLoginActivity();
                            }
                            if(status==0){
                                Toast.makeText(getApplicationContext(), "Error ! Password Not Changed", Toast.LENGTH_SHORT).show();
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


        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest);


    }

    private void jumpToLoginActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
