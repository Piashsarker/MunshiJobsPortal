package dcastalia.com.munshijobsportal.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.ErrorDialog;
import dcastalia.com.munshijobsportal.ProgressDialog;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHandler;
import dcastalia.com.munshijobsportal.Util.VolleyCustomRequest;
import dcastalia.com.munshijobsportal.fragment.SetPasswordAgentFragment;
import dcastalia.com.munshijobsportal.fragment.SetPasswordFragmentUser;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

import static com.android.volley.VolleyLog.d;

public class RegisterActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    protected static final String TAG = "RegFragment_1";
    private Context context = RegisterActivity.this;

    Button btn_reg1;
    String category;
    int radioSelectedId;
    EditText input_userName;
    EditText input_passport_nubmer;
    EditText input_phone;
    SessionManager sessionManager;
    private RadioButton radioButton;
    private SQLiteHandler db;
    private String name, passport, phone;
    RadioGroup radioGroup;
    ErrorDialog errorDialog ;
    ProgressDialog progressDialog ;
    TextInputLayout inputLayoutUserName , inputLayoutPassportNo , inputLayoutPhone;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeView();

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        if(sessionManager.isLoggedIn()){
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);

        }
        errorDialog = new ErrorDialog(RegisterActivity.this);
        btn_reg1 = (Button) findViewById(R.id.btn_reg1);

        db = new SQLiteHandler(context);

        sessionManager = new SessionManager(getApplicationContext());






        btn_reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitRegistration();






            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initializeView() {
        input_userName = (EditText) findViewById(R.id.input_userName);
        input_passport_nubmer = (EditText) findViewById(R.id.input_passport_nubmer);
        input_phone = (EditText) findViewById(R.id.input_phone);
        radioGroup = (RadioGroup) findViewById(R.id.radio_select);
        inputLayoutUserName = (TextInputLayout) findViewById(R.id.input_layout_user_name);
        inputLayoutPassportNo = (TextInputLayout) findViewById(R.id.input_layout_passport);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        input_userName.addTextChangedListener(new MyTextWatcher(input_userName));
        input_passport_nubmer.addTextChangedListener(new MyTextWatcher(input_passport_nubmer));
        input_phone.addTextChangedListener(new MyTextWatcher(input_phone));


    }

    private void submitRegistration() {
        if (!validateName()) {
            return;
        }

        if (!validatePassport()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        sentRegistrationInformation();

    }

    private void sentRegistrationInformation() {

        name = input_userName.getText().toString();
        passport = input_passport_nubmer.getText().toString();
        phone = input_phone.getText().toString();
        radioSelectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioSelectedId);
        category = radioButton.getText().toString();
        dataSendToServer(name, passport, phone, category);


    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_userName:
                    validateName();
                    break;
                case R.id.input_passport_nubmer:
                    validatePassport();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePhone() {
        if (input_phone.getText().toString().trim().isEmpty() || input_phone.getText().length()!=11) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(input_phone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;

    }

    private boolean validatePassport() {
        if (input_passport_nubmer.getText().toString().trim().isEmpty() || input_passport_nubmer.getText().length()!=9) {
            input_passport_nubmer.setError(getString(R.string.err_msg_passport));
            requestFocus(input_passport_nubmer);
            return false;
        } else {
            inputLayoutPassportNo.setErrorEnabled(false);
        }

        return true;

    }

    private boolean validateName() {
        if (input_userName.getText().toString().trim().isEmpty()) {
            input_userName.setError(getString(R.string.err_msg_name));
            requestFocus(input_userName);
            return false;
        } else {
            inputLayoutUserName.setErrorEnabled(false);
        }

        return true;
    }

    private void fragmentTransactionAgent() {
        Fragment fragment = new SetPasswordAgentFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void fragmentTransaction() {
        Fragment fragment = new SetPasswordFragmentUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }

    public void dataSendToServer(final String name, final String passport, final String phone, String catagory) {

        progressDialog.showProgress();
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
                                String id = response.getString("id");
                                String code = response.getString("verification_code").toString();
                                sessionManager.createLoginSession(id, name, phone, passport, code);
                                Toast.makeText(context,
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, response.getString("verification_code").toString(), Toast.LENGTH_LONG).show();

                                if(category.equals("Individual")){
                                    progressDialog.hideProgress();
                                    fragmentTransaction();
                                }
                                else{
                                    progressDialog.hideProgress();
                                    fragmentTransactionAgent();
                                }

                            }
                            else{
                                progressDialog.hideProgress();
                               errorDialog.showDialog("Sorry!","Phone Number Not Unique");
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
