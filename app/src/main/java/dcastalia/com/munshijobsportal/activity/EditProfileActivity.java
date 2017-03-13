package dcastalia.com.munshijobsportal.activity;

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
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

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
    private String name, profession, dateOfBirth, passport, nationalId, email, phone, address, gender;

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
            passport = intent.getStringExtra("passport");
            inputPassport_Number.setText(passport);
            phone = intent.getStringExtra("phone");
            inputPhone_Number.setText(phone);
            if(user!=null){
                profession = user.getProfession();
                dateOfBirth = user.getDateOfBirth();
                nationalId = user.getNationalId();
                email = user.getEmail();
                address = user.getAddress();
                gender = user.getGender();
                picturePath = user.getPicturePath();
            }

            if (profession != null && email != null && nationalId != null && passport != null && dateOfBirth != null && address != null && picturePath != null) {
                inputAddress.setText(address);
                etBirthDate.setText(dateOfBirth);
                inputEmail.setText(email);
                inputProfession.setText(profession);
                inputNID_Number.setText(nationalId);
                loadImageFromStorage(picturePath);
            }

            btn_pic_upload = (Button) findViewById(R.id.btn_pic_upload);
            btn_profile_update = (Button) findViewById(R.id.btn_profile_update);


            btn_profile_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.showProgress();
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


                    if (name.length() != 0 && passportNo.length() != 0 && profession.length() != 0 && email.length() != 0 && nationalId.length() != 0 && passport.length() != 0 && dateOfBirth.length() != 0 && address.length() != 0) {

                        if (picturePath!=null) {
                            boolean isInserted = sqLiteHelper.insertUserInfo(new User(name, dateOfBirth, profession, passportNo, nationalId, email, phone, address, gender, picturePath));
                            sessionManager.setUserName(name);


                            if (isInserted) {
                                Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();
                                sentProfileinformationToServer(sqLiteHelper.getUserDetails());
                                Intent intent1 = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                startActivity(intent1);
                            } else {
                                errorDialog.showDialog("Error!","Data Not Saved");
                            }
                        } else {
                            Toast.makeText(context, "Select Image", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "All Field Required", Toast.LENGTH_SHORT).show();
                    }
                progressDialog.hideProgress();

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
    }

    private void sentProfileinformationToServer(User userDetails) {
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

                    PackageManager pm = getApplicationContext().getPackageManager();

                    if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    } else {

                        errorDialog.showDialog("No Camera","Sorry Your Device Has No Camera Permission");

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
        etBirthDate.setEnabled(false);
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
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
}
