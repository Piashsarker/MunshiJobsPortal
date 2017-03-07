package dcastalia.com.munshijobsportal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class LoginActivity extends AppCompatActivity {

    protected static final String TAG = "SignInFragment_1";

    Button btn_forgot_pass;
    Button btn_signIn;
    EditText input_phone;
    EditText input_password;
    private SessionManager sessionManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(LoginActivity.this);
        input_phone = (EditText)findViewById(R.id.input_phone);
        input_password = (EditText)findViewById(R.id.input_password);

        btn_forgot_pass=(Button)findViewById(R.id.btn_forgot_pass);
        btn_signIn=(Button)findViewById(R.id.btn_signIn);



        btn_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = input_phone.getText().toString();
                String password = input_password.getText().toString();
                checkInLoginCredential(phone, password);


            }
        });
    }

    private void checkInLoginCredential(String phone, String password) {

        HashMap<String , String> userDetails = sessionManager.getUserDetails();

        if(userDetails.get(sessionManager.KEY_PHONE).equals(phone) && userDetails.get(sessionManager.KEY_PASSWORD).equals(password)){
            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Wrong Phone or Password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close application")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
