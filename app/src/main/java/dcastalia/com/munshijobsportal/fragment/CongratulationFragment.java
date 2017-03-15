package dcastalia.com.munshijobsportal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dcastalia.com.munshijobsportal.activity.EditProfileActivity;
import dcastalia.com.munshijobsportal.activity.MainActivity;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

/**
 * Created by PT on 3/5/2017.
 */
public class CongratulationFragment extends Fragment {
    Button btn_profile;
    Button btn_finish;
    SessionManager sessionManager ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_congratulation, container, false);

        sessionManager = new SessionManager(getContext());
        btn_finish=(Button)view.findViewById(R.id.btn_finish);

        btn_profile=(Button)view.findViewById(R.id.btn_profile);

        //btn finish listener-----------------------------------
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                 startActivity(intent);
                sessionManager.setLoginSession(true);
                getActivity().finish();


            }
        });

        //profile edit listener---
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("name",sessionManager.getUserDetails().get(sessionManager.KEY_NAME));
                intent.putExtra("phone",sessionManager.getUserDetails().get(sessionManager.KEY_PHONE));
                intent.putExtra("passport",sessionManager.getUserDetails().get(sessionManager.KEY_PASSPORT));
                sessionManager.setLoginSession(true);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
