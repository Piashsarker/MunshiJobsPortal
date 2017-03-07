package dcastalia.com.munshijobsportal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class SplashActivity extends AppCompatActivity {
    Button btn_sp;
    private SessionManager sessionManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreenWindow();
        setContentView(R.layout.splash_layout);
        btn_sp=(Button)findViewById(R.id.btn_sp);
        sessionManager = new SessionManager(SplashActivity.this);

        //  Reg button Click Event
        btn_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    finish();
                
            }
        });

    }
    private void requestFullScreenWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
