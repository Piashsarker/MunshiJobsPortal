package dcastalia.com.munshijobsportal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import dcastalia.com.munshijobsportal.ErrorDialog;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.Util.VolleyCustomRequest;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

import static com.android.volley.VolleyLog.d;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button btn_recover_pass;
    SessionManager sessionManager ;
    ErrorDialog errorDialog;
    private static  final String TAG = "ForgotPasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        sessionManager = new SessionManager(ForgotPasswordActivity.this );
        errorDialog = new ErrorDialog(ForgotPasswordActivity.this);

        btn_recover_pass=(Button)findViewById(R.id.btn_recover_pass);

        btn_recover_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String  id = sessionManager.getUserId();

                dataSendToServer(id);

                Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                startActivity(intent);

            }
        });


    }

    public void dataSendToServer(String id) {



        String hitURL = "http://bestinbd.com/projects/web/munshi/restAPI/site/send_verification_code";

        HashMap<String, String> params = new HashMap<>();

        params.put("id", id); //Item varify code
        // params.put("phone", phone); //Items - Item 1 - phone

        VolleyCustomRequest postRequest = new VolleyCustomRequest(Request.Method.POST, hitURL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Register Response: " + response.toString());

                        try {
                            int status = response.getInt("status");
                            if (status == 1) {

                                Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();

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


        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest);


    }
}
