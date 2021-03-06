package dcastalia.com.munshijobsportal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import dcastalia.com.munshijobsportal.ErrorDialog;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.Util.VolleyCustomRequest;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

import static com.android.volley.VolleyLog.d;

/**
 * Created by PT on 3/5/2017.
 */

public class VerifyPhoneFragment extends Fragment {
    protected static final String TAG = "VarifyPhoneFragment";
    SessionManager sessionManager ;
    Button btn_phone_varify;
    EditText input_varify_code;
    String varify_code;
    ErrorDialog errorDialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_phone, container, false);
        sessionManager = new SessionManager(getContext());

        btn_phone_varify = (Button) view.findViewById(R.id.btn_phone_varify);

        input_varify_code = (EditText) view.findViewById(R.id.input_varify_code);
        input_varify_code.setText(sessionManager.getUserDetails().get(SessionManager.KEY_VERIFICATION_CODE));
        errorDialog = new ErrorDialog(getContext());


        btn_phone_varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                varify_code = input_varify_code.getText().toString();
                String id = sessionManager.getUserId();
                dataSendToServer(id, varify_code);


            }
        });

        return view;
    }

    public void fragmentTransaction() {
         Fragment fragment = new CongratulationFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void dataSendToServer(String id, String varify_code) {

        String hitURL = "http://bestinbd.com/projects/web/munshi/restAPI/site/verify_phone";
        HashMap<String, String> params = new HashMap<>();
        params.put("key_name", id); //Item varify code
        params.put("varify", varify_code); //Item varify code


        VolleyCustomRequest postRequest = new VolleyCustomRequest(Request.Method.POST, hitURL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Register Response: " + response.toString());

                        try {
                            int status = response.getInt("status");
                            if (status == 1) {

                                Toast.makeText(getContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                                fragmentTransaction();
                            }
                            if (status == 0) {
                                Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorDialog.showDialog("Error!","Try Again Later!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        d(TAG, "Error: " + volleyError.getMessage());


                        if (volleyError instanceof NetworkError) {
                            errorDialog.showDialog("No Internet!","Enable Moblie Data or WIFI");
                        } else if (volleyError instanceof ServerError) {
                            errorDialog.showDialog("Server Error!","Server Not Found,Try Again Later!");

                        } else if (volleyError instanceof AuthFailureError) {
                            errorDialog.showDialog("No Internet!","Enable Moblie Data or WIFI");

                        } else if (volleyError instanceof ParseError) {

                            errorDialog.showDialog("Parsing Error!","Parsing Error, Try Again Later.");
                        } else if (volleyError instanceof NoConnectionError) {
                            errorDialog.showDialog("No Internet!","Enable Moblie Data or WIFI");
                        } else if (volleyError instanceof TimeoutError) {
                            errorDialog.showDialog("Request Timeout!","Please Check Your Internet Connection");
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