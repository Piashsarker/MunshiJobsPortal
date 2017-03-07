package dcastalia.com.munshijobsportal.SqliteDatabase;

/**
 * Created by shahimtiyaj-pc on 10/19/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import dcastalia.com.munshijobsportal.Model.UserInfo;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "SampleDB";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public  static final String KEY_PHONE = "phone";
   // public  static final String KEY_PASSWORD = "password";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) throws SQLException {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT UNIQUE "
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public long addUser(String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PHONE, phone); // Email
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);

        return id;
    }

    /**
     * Getting user data from database
     */
    public List<UserInfo> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("phone", cursor.getString(2));
            user.put("uid", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return getUserDetails();
    }

//    public boolean checkEmailPassword(String phone, String password) {
//        boolean check = false;
//
//        String query = "SELECT * FROM " + TABLE_USER;
//
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (phone.equals(cursor.getString(cursor.getColumnIndex(KEY_PHONE)))
//                        && password.equals(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)))) {
//                    check = true;
//                    break;
//                }
//            } while (cursor.moveToNext());
//        }
//
//        return check;
//    }


//      public void showData(String name,String email)
//
//    {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        db = new SQLiteHandler(getApplicationContext());
//        HashMap<String, String> dataset=db.getUserDetails();
//        if(dataset.size()>0){
//
//            for(ArrayList<String> temp: dataset){
//                Toast.makeText(getApplicationContext(),
//                        "name:: "+temp.get(0)+"\n"
//                                +"email:: "+temp.get(1)
//                        ,
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
