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

/**
 * Created by PT on 3/5/2017.
 */
public class CongratulationFragment extends Fragment {
    Button btn_profile;
    Button btn_finish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_congratulation, container, false);


        btn_finish=(Button)view.findViewById(R.id.btn_finish);

        btn_profile=(Button)view.findViewById(R.id.btn_profile);

        //btn finish listener-----------------------------------
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                 startActivity(intent);
                getActivity().finish();


            }
        });

        //profile edit listener---
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
