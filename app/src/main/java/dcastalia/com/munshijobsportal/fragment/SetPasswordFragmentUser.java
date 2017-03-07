package dcastalia.com.munshijobsportal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.Util.VolleyCustomRequest;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

import static com.android.volley.VolleyLog.d;

/**
 * Created by PT on 3/5/2017.
 */

public class SetPasswordFragmentUser extends Fragment {
    protected static final String TAG = "Set_pass_individual_frag";
    Button btn_reg2;
    EditText input_password;
    EditText input_confirm_password;
    SessionManager sessionManager ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_password_user, container, false);

        sessionManager = new SessionManager(getContext());
        input_password = (EditText) view.findViewById(R.id.input_password);
        input_confirm_password = (EditText) view.findViewById(R.id.input_confirm_password);


        btn_reg2 = (Button) view.findViewById(R.id.btn_reg2);

        btn_reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = input_password.getText().toString();
                String repassword = input_confirm_password.getText().toString();


                if (input_password.getText().toString().length() == 0) {
                    input_password.setError("Please enter password");
                    input_password.requestFocus();
                } else if (input_confirm_password.getText().toString().length() == 0) {
                    input_confirm_password.setError("Please confirm your password");
                    input_confirm_password.requestFocus();
                } else if (!checkPassWordAndConfirmPassword(password, repassword)) {

                    Toast.makeText(getContext(), "Password don't match", Toast.LENGTH_LONG).show();
                } else {

                    dataSendToServer(sessionManager.getUserId(), password);

                    Fragment fragment = new VerifyPhoneFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }

        });

        return view;
    }

    //Check password and repassword is write or wrong
    public boolean checkPassWordAndConfirmPassword(String password, String repassword) {
        boolean pstatus = false;
        if (repassword != null && password != null) {
            if (password.equals(repassword)) {
                pstatus = true;
            }
        }
        return pstatus;
    }


    public void dataSendToServer(String id, final String password) {



        String hitURL = "http://bestinbd.com/projects/web/munshi/restAPI/site/save_password";

        HashMap<String, String> params = new HashMap<>();
        params.put("key_name", id); //Items - Item 1 - name
        params.put("password", password); //Items - Item 3 - pass


        VolleyCustomRequest postRequest = new VolleyCustomRequest(Request.Method.POST, hitURL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            int status = response.getInt("status");
                            if (status == 1) {


                                Toast.makeText(getContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();

                                sessionManager.setPassword(password);
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
                            Toast.makeText(getContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(getContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(getContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();


                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(getContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof TimeoutError) {
                            Toast.makeText(getContext(), "Connection TimeOut! Please check your internet connection", Toast.LENGTH_SHORT).show();

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