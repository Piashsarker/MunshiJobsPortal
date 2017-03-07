package dcastalia.com.munshijobsportal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class FavouriteActivity extends AppCompatActivity {

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
    }
}
