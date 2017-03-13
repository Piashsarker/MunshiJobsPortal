package dcastalia.com.munshijobsportal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import dcastalia.com.munshijobsportal.Model.User;
import dcastalia.com.munshijobsportal.Model.UserInfo;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHelper;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private TextView inputBirthDate , inputProfession;
    private TextView txtProfileName;
    private List<UserInfo> userinformation;
    private TextView inputPassport_Number;
    private TextView inputNID_Number;
    private TextView inputEmail,etGender;
    private TextView inputPhone_Number;
    private TextView inputAddress;
    private SessionManager sessionManager ;
    private String profileName , phone , passport,email , profession, dateOfBirth,nationalId , address,gender;
    private ImageView imageView;
    private SQLiteHelper sqLiteHelper ;
    private FloatingActionButton favEdit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        sessionManager = new SessionManager(getApplicationContext());
        sqLiteHelper = new SQLiteHelper(ProfileActivity.this);
        initializeView();


        profileName = sessionManager.getUserDetails().get(sessionManager.KEY_NAME);
        phone = sessionManager.getUserDetails().get(sessionManager.KEY_PASSPORT);
        passport = sessionManager.getUserDetails().get(sessionManager.KEY_PHONE);
        if(profileName!= null && phone != null&& passport != null){
            txtProfileName.setText(profileName);
            inputPassport_Number.setText(phone);
            inputPhone_Number.setText(passport);
        }
        setdata();
        initializeToolbar();

        favEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfileActivity();
            }
        });


    }

    private void goToEditProfileActivity() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("name",profileName);
        intent.putExtra("phone",phone);
        intent.putExtra("passport",passport);
        profession = inputProfession.getText().toString();
        email =inputEmail.getText().toString();
        nationalId = inputNID_Number.getText().toString();
        passport = inputPassport_Number.getText().toString();
        dateOfBirth =  inputBirthDate.getText().toString() ;
        address = inputAddress.getText().toString();
        gender = etGender.getText().toString();

        if(!profession.isEmpty()&&!email.isEmpty() && nationalId.isEmpty() && !passport.isEmpty()
                &&!dateOfBirth.isEmpty() &&!address.isEmpty() && !gender.isEmpty()){
            intent.putExtra("profession",profession);
            intent.putExtra("email",email);
            intent.putExtra("national_id",nationalId);
            intent.putExtra("passport",passport);
            intent.putExtra("date_of_birth",dateOfBirth);
            intent.putExtra("address",address);
            intent.putExtra("gender",gender);
        }
        startActivity(intent);
    }

    private void initializeToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Profile");
    }

    public void setdata(){
        User user = sqLiteHelper.getUserDetails();
        if(user.getName()!=null){
            txtProfileName.setText(user.getName());
        }
        if(user.getProfession()!=null){
             inputProfession.setText(user.getProfession());
        }
        if(user.getAddress()!=null){
            inputAddress.setText(user.getAddress());
        }
        if(user.getDateOfBirth()!=null){
            inputBirthDate.setText(user.getDateOfBirth());
        }
        if(user.getEmail()!=null){
            inputEmail.setText(user.getEmail());
        }
        if(user.getGender()!=null){
            etGender.setText(user.getGender());
        }
        if(user.getNationalId()!=null){
            inputNID_Number.setText(user.getNationalId());
        }
        if(user.getPassportNo()!=null){
            inputPassport_Number.setText(user.getPassportNo());
        }
        if(user.getPicturePath()!=null){
            loadImageFromStorage(user.getPicturePath());
        }

    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private void initializeView() {
         txtProfileName = (TextView) findViewById(R.id.txt_profile_name);
        inputProfession = (TextView) findViewById(R.id.profession);
        inputPassport_Number = (TextView) findViewById(R.id.passport_no);
        inputNID_Number = (TextView) findViewById(R.id.nid_no);
        inputEmail = (TextView) findViewById(R.id.input_email);
        inputPhone_Number = (TextView) findViewById(R.id.phone_no);
        inputAddress = (TextView) findViewById(R.id.input_address);
        imageView = (ImageView) findViewById(R.id.profile_pic_id);
        inputBirthDate = (TextView) findViewById(R.id.birth_date);
        etGender  = (TextView) findViewById(R.id.et_gender);
        favEdit = (FloatingActionButton) findViewById(R.id.fav_edit);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
}