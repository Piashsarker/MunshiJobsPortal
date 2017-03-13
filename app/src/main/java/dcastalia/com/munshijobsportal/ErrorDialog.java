package dcastalia.com.munshijobsportal;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by PT on 3/12/2017.
 */

public class ErrorDialog {
    private Context context ;

    public ErrorDialog(Context context ){
        this.context = context;
    }

    public void showDialog( String title , String message ){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = builder.create();


        alertDialog.show();

    }
}
