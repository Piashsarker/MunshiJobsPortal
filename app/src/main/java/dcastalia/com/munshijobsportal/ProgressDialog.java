package dcastalia.com.munshijobsportal;


import android.content.Context;

/**
 * Created by PT on 3/12/2017.
 */

public class ProgressDialog {

    private android.app.ProgressDialog progressDialog;
    private Context context;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void showProgress(){
        progressDialog = new android.app.ProgressDialog(context);
        progressDialog.setTitle("Wait");
        progressDialog.setMessage("Data Loading......");
        progressDialog.show();
    }
    public void hideProgress(){
        progressDialog.hide();
    }

}
