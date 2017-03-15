package dcastalia.com.munshijobsportal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.ErrorDialog;
import dcastalia.com.munshijobsportal.Model.Agent;
import dcastalia.com.munshijobsportal.PasswordValidator;
import dcastalia.com.munshijobsportal.ProgressDialog;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.Util.VolleyCustomRequest;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

import static com.android.volley.VolleyLog.d;

/**
 * Created by PT on 3/8/2017.
 */

public class SetPasswordAgentFragment extends  Fragment{
    protected static final String TAG = "SetPasswordAgentFragment";
    Button btn_reg2;
    EditText input_password;
    EditText input_confirm_password;
    SessionManager sessionManager ;
    Spinner spinnerAgent ;
    String spinnerAgentType ;
    ArrayList<Agent> agentArrayList;

    private static  final String AGENT_URL = "http://bestinbd.com/projects/web/munshi/restAPI/site/agentlist";
    ErrorDialog errorDialog ;
    PasswordValidator passwordValidator ;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.set_password_agent_fragment, container, false);

        progressDialog = new ProgressDialog(getContext());
        passwordValidator = new PasswordValidator();
        sessionManager = new SessionManager(getContext());
        errorDialog = new ErrorDialog(getContext());
        input_password = (EditText) view.findViewById(R.id.input_password);
        input_confirm_password = (EditText) view.findViewById(R.id.input_confirm_password);
        spinnerAgent = (Spinner) view.findViewById(R.id.spinner_agent);
        btn_reg2 = (Button) view.findViewById(R.id.btn_reg2);

        getAgentArrayListFromServer();


        spinnerAgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                if (spinner.getId()==R.id.spinner_agent){
                    spinnerAgentType = agentArrayList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        btn_reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = input_password.getText().toString();
                String repassword = input_confirm_password.getText().toString();



                if (!passwordValidator.validate(password)) {
                    input_password.setError("6 Character & One Uppercase Letter Required");
                    input_password.requestFocus();
                } else if (!passwordValidator.validate(repassword)) {
                    input_confirm_password.setError("6 Character & One Uppercase Letter Required");
                    input_confirm_password.requestFocus();
                } else if (!checkPassWordAndConfirmPassword(password, repassword)) {

                    Toast.makeText(getContext(), "Password don't match", Toast.LENGTH_LONG).show();
                } else {

                    dataSendToServer(sessionManager.getUserId(), password,spinnerAgentType);


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

    private void setAgentSpinnerData(ArrayList<Agent> agentArrayList) {
        ArrayList<String> agentType = new ArrayList<>();
        for(int i= 0 ; i<agentArrayList.size(); i++){
            agentType.add(agentArrayList.get(i).getName());
        }

        ArrayAdapter<String> salaryRangeAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,agentType);
        salaryRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgent.setAdapter(salaryRangeAdapter);

    }


    public void dataSendToServer(String id, final String password,String spinnerAgentType) {

        progressDialog.showProgress();

        String hitURL = "http://bestinbd.com/projects/web/munshi/restAPI/site/save_password";

        HashMap<String, String> params = new HashMap<>();
        params.put("key_name", id); //Items - Item 1 - name
        params.put("password", password);
        params.put("agent_id",spinnerAgentType);//Items - Item 3 - pass


        VolleyCustomRequest postRequest = new VolleyCustomRequest(Request.Method.POST, hitURL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {

                                progressDialog.hideProgress();
                                Toast.makeText(getContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                                sessionManager.setPassword(password);
                                Fragment fragment = new VerifyPhoneFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                            else{
                                progressDialog.hideProgress();
                                errorDialog.showDialog("Error!","Try Again Later");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideProgress();
                            errorDialog.showDialog("Error!","Try Again Later.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.hideProgress();
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


    public void getAgentArrayListFromServer() {

        agentArrayList = new ArrayList<>();
        JsonArrayRequest jobReq = new JsonArrayRequest(AGENT_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {



                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);

                                Agent agent = new Agent();
                                agent.setId(obj.getString("id"));
                                agent.setName(obj.getString("name"));
                                agent.setCode(obj.getString("code"));
                                agentArrayList.add(agent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        setAgentSpinnerData(agentArrayList);

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                volleyError.printStackTrace();


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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jobReq);

    }
}
