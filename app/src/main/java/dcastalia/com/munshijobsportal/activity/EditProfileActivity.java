package dcastalia.com.munshijobsportal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import dcastalia.com.munshijobsportal.Controller.AppController;
import dcastalia.com.munshijobsportal.ErrorDialog;
import dcastalia.com.munshijobsportal.Model.User;
import dcastalia.com.munshijobsportal.ProgressDialog;
import dcastalia.com.munshijobsportal.R;
import dcastalia.com.munshijobsportal.SqliteDatabase.SQLiteHelper;
import dcastalia.com.munshijobsportal.sessionmanager.SessionManager;

public class EditProfileActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG_FROM_GALLERY = 2;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final String TAG = "EditProfileActivity";
    private static final int MY_REQUEST_CODE = 3 ;
    private final String Tag = getClass().getName();
    private final int CAMERA_RESULT = 1;
    Button btn_pic_upload;
    Button btn_profile_update;
    Context context = EditProfileActivity.this;
    private ImageView imageView;
    private EditText inputProfession;
    private Button inputBirthDate;
    private ErrorDialog errorDialog;


    //DatePickerDialog listener
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            etBirthDate.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(year));
        }
    };
    private String picturePath;
    private EditText inputPassport_Number;
    private EditText inputNID_Number;
    private EditText inputEmail;
    private EditText inputPhone_Number;
    private EditText inputAddress;
    private EditText etProfileName;
    private EditText etBirthDate;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    private SessionManager sessionManager;
    private SQLiteHelper sqLiteHelper;
    private ProgressDialog progressDialog ;
    private String name, profession, dateOfBirth, passport, nationalId, email, phone, address, gender ="";
    private TextInputLayout inputLayoutName , inputLayoutEmail , inputLayoutPhone , inputLayoutPassport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initializeViews();
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        sqLiteHelper = new SQLiteHelper(EditProfileActivity.this);
        User user = sqLiteHelper.getUserDetails();
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        errorDialog  = new ErrorDialog(context);
        Intent intent = getIntent();



        if (intent != null) {
            name = intent.getStringExtra("name");
            etProfileName.setText(name);
            phone = intent.getStringExtra("phone");
            inputPhone_Number.setText(phone);
            passport = intent.getStringExtra("passport");
            inputPassport_Number.setText(passport);
        }
        else{
            name = sessionManager.getUserDetails().get(SessionManager.KEY_NAME);
            phone = sessionManager.getUserDetails().get(SessionManager.KEY_PHONE);
            passport = sessionManager.getUserDetails().get(SessionManager.KEY_PASSPORT);
            etProfileName.setText(name);
            inputPhone_Number.setText(phone);
            inputPassport_Number.setText(passport);
        }
            if(user!=null){
                profession = user.getProfession();
                dateOfBirth = user.getDateOfBirth();
                nationalId = user.getNationalId();
                email = user.getEmail();
                address = user.getAddress();
                gender = user.getGender();
                picturePath = user.getPicturePath();
                inputAddress.setText(address);
                etBirthDate.setText(dateOfBirth);
                inputEmail.setText(email);
                inputProfession.setText(profession);
                inputNID_Number.setText(nationalId);
                loadImageFromStorage(picturePath);
            }


            btn_pic_upload = (Button) findViewById(R.id.btn_pic_upload);
            btn_profile_update = (Button) findViewById(R.id.btn_profile_update);
           etProfileName.setFocusable(true);

            btn_profile_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                     updateProfile();

                }
            });


            btn_pic_upload.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    selectImage();
                }

            });

            inputBirthDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker();
                }
            });




    }

    private void updateProfile() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePhoneNo()) {
            return;
        }
        if(!validatePassport()){
            return ;
        }

        updateProflieInformationToDatabaseAndServer();
    }

    private void updateProflieInformationToDatabaseAndServer() {
        int genderSelectedId = genderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(genderSelectedId);
        String profession = inputProfession.getText().toString();
        String dateOfBirth = etBirthDate.getText().toString();
        String passportNo = inputPassport_Number.getText().toString();
        String nationalId = inputNID_Number.getText().toString();
        String email = inputEmail.getText().toString();
        String phone = inputPhone_Number.getText().toString();
        String address = inputAddress.getText().toString();
        String gender = genderRadioButton.getText().toString();
        String name = etProfileName.getText().toString();
        if (picturePath!=null) {
            boolean isInserted = sqLiteHelper.insertUserInfo(new User(name, dateOfBirth, profession, passportNo, nationalId, email, phone, address, gender, picturePath));
            sessionManager.setUserName(name);
            sessionManager.setPhone(phone);



            if (isInserted) {
                Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();
                sentProfileinformationToServer(sqLiteHelper.getUserDetails());

            } else {
                errorDialog.showDialog("Error!","Data Not Saved");
            }
        } else {
            Toast.makeText(context, "Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void sentProfileinformationToServer(final User userDetails) {
        final String personId = sessionManager.getUserId();
        String tag_json_obj = "json_obj_req";

        String url = "http://bestinbd.com/projects/web/munshi/restAPI/site/updatepersoninfo?id="+personId+"&email="+userDetails.getEmail()+"&picture="+userDetails.getPicturePath()+
                "&passport_number="+userDetails.getPassportNo()+"&gender="+userDetails.getGender()+"&nid_number="+userDetails.getNationalId()+"&phone_number="+userDetails.getPhone();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());
                        Toast.makeText(EditProfileActivity.this, "Data Saved..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                errorDialog.showDialog("Error!","Error Data Not Saved");
            }
        }) {

            /**
             * Passing some request headers
             * */


        };


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);



    }

    private void loadImageFromStorage(String path) {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                        if (checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_REQUEST_CODE);
                        }
                        else{
                            startCameraIntent();
                        }
                    }
                    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
                        startCameraIntent();
                    }



                } else if (options[item].equals("Choose from Gallery")) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG_FROM_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void startCameraIntent() {
        PackageManager pm = getApplicationContext().getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        } else {

            errorDialog.showDialog("No Camera","Sorry Your Device Has No Camera Permission");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraIntent();
            }
            else {
              errorDialog.showDialog("Error","Permission Denied");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

      progressDialog.showProgress();
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
           try{
               Bitmap photo = (Bitmap) data.getExtras().get("data");


               imageView.setImageBitmap(photo);
               picturePath = saveToInternalStorage(photo);
           } catch (Exception ex){
               errorDialog.showDialog("Error!","Try Again");
               Log.d("EditProfileActivity",ex.toString());
           }

        }


        else if (resultCode == RESULT_OK  && requestCode==RESULT_LOAD_IMG_FROM_GALLERY) {
            try {
                final Uri imageUri = data.getData();
                Bitmap selectedImage = decodeUri(EditProfileActivity.this,imageUri,500);
                imageView.setImageBitmap(selectedImage);
              picturePath=  saveToInternalStorage(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                errorDialog.showDialog("Error!","Try Another Image");
            }

        }else {
            Toast.makeText(EditProfileActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
        progressDialog.hideProgress();
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    private void initializeViews() {
        etProfileName = (EditText) findViewById(R.id.et_profile_name);
        inputProfession = (EditText) findViewById(R.id.profession);
        inputPassport_Number = (EditText) findViewById(R.id.passport_no);
        inputNID_Number = (EditText) findViewById(R.id.nid_no);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPhone_Number = (EditText) findViewById(R.id.phone_no);
        inputAddress = (EditText) findViewById(R.id.input_address);
        imageView = (ImageView) findViewById(R.id.profile_pic_id);
        //find the birth date id
        inputBirthDate = (Button) findViewById(R.id.btn_date_picker);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        etBirthDate = (EditText) findViewById(R.id.birth_date);
        etBirthDate.setText("1-1-1980");
        etBirthDate.setEnabled(false);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassport = (TextInputLayout) findViewById(R.id.input_layout_passport);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        etProfileName.addTextChangedListener(new MyTextWatcher(etProfileName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassport_Number.addTextChangedListener(new MyTextWatcher(inputPassport_Number));
        inputPhone_Number.addTextChangedListener(new MyTextWatcher(inputPhone_Number));


    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", 1980);
        args.putInt("month", 00);
        args.putInt("day", 01);
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    public static class DatePickerFragment extends DialogFragment {
        DatePickerDialog.OnDateSetListener ondateSet;
        private int year, month, day;

        public DatePickerFragment() {


        }

        public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
            ondateSet = ondate;
        }

        @SuppressLint("NewApi")
        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
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
                case R.id.et_profile_name:
                    validateName();
                    break;
                case R.id.passport_no:
                    validatePassport();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.phone_no:
                    validatePhoneNo();
                    break;
            }
        }
    }

    private boolean validatePhoneNo() {
        if (inputPhone_Number.getText().toString().trim().isEmpty() || inputPhone_Number.length()!=11) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(inputPhone_Number);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;

    }

    private boolean validatePassport() {
        if (inputPassport_Number.getText().toString().trim().isEmpty() || inputPassport_Number.length()!=9) {
            inputLayoutPassport.setError(getString(R.string.err_msg_passport));
            requestFocus(inputPassport_Number);
            return false;
        } else {
            inputLayoutPassport.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;

    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validateName() {
        if (etProfileName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(etProfileName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
