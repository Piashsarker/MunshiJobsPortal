package dcastalia.com.munshijobsportal.SqliteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.Model.User;

/**
 * Created by PT on 3/5/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MunshiJobDatabase";
    private static final String TABLE_USER = "user";
    private static final String TABLE_JOBS = "jobs";
    private static final String TABLE_SEARCH_JOBS="search_jobs";

    private static final String CREATE_TABLE_JOBS = "CREATE TABLE IF NOT EXISTS " +TABLE_JOBS +
            " (job_id varchar(30) primary key,  company varchar(30) , available_job varchar(30) , job_title varchar(50) , " +
            "job_position varchar(30) , country varchar(30) , date varchar(30), vacancy varchar(30) , experience varchar(30) , salary varchar(30) , age varchar(30) , " +
            "gender varchar(30) , job_nature varchar(30) , job_description varchar(30) , job_requirement varchar(30) , time varchar(30),flag int )";
    private static final String CREATE_TABLE_SEARCH_JOBS = "CREATE TABLE IF NOT EXISTS " +TABLE_SEARCH_JOBS +
            " (job_id varchar(30) primary key,  company varchar(30) , available_job varchar(30) , job_title varchar(50) , " +
            "job_position varchar(30) , country varchar(30) , date varchar(30), vacancy varchar(30) , experience varchar(30) , salary varchar(30) , age varchar(30) , " +
            "gender varchar(30) , job_nature varchar(30) , job_description varchar(30) , job_requirement varchar(30) , time varchar(30),flag int )";

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "( user_id integer primary key autoincrement," +
            " name varchar(30),profession varchar(30) , date_of_birth varchar(30), passport_no varchar(30) " +
            ",national_id varchar(30) , email varchar(30) , phone varchar(30) , address varchar(30) , gender varchar(30),picture_path varchar(30))";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_JOBS);
        db.execSQL(CREATE_TABLE_SEARCH_JOBS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_JOBS);
        onCreate(db);

    }



    public boolean jobExists(String jobId){
        boolean found = false;
        String selectQuery = "SELECT * FROM "+TABLE_JOBS+" WHERE job_id="+jobId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        found = !(!cursor.moveToFirst() || cursor.getCount() == 0);

        return found;

    }
    public boolean favJobExist(String jobId){
        boolean found;
        String selectQuery = "SELECT * FROM "+TABLE_JOBS+" WHERE job_id="+jobId +" and (flag="+Jobs.FLAG_FOR_FAVOURITE+" or flag="+Jobs.FLAG_FOR_APPLIED_AND_UPDATE+")" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        found = !(!cursor.moveToFirst() || cursor.getCount() == 0);

        return found;
    }
    public boolean appliedJob(String jobId){
        boolean found;
        String selectQuery = "SELECT * FROM "+TABLE_JOBS+" WHERE job_id="+jobId +" and (flag="+Jobs.FLAG_FOR_APPLIED+" or flag="+Jobs.FLAG_FOR_APPLIED_AND_UPDATE+")" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        found = !(!cursor.moveToFirst() || cursor.getCount() == 0);

        return found;
    }

    public boolean applyAndFavJob(String  jobId){
        boolean found;
        String selectQuery = "SELECT * FROM "+TABLE_JOBS+" WHERE flag="+Jobs.FLAG_FOR_APPLIED_AND_UPDATE+" and job_id= "+jobId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        found = !(!cursor.moveToFirst() || cursor.getCount() == 0);

        return found;
    }

    public boolean insertUserInfo(User user) {
        boolean isInserted = false;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        try{
            if(isInserted==false){
                values.put("name", user.getName());
                values.put("profession", user.getProfession());
                values.put("date_of_birth", user.getDateOfBirth());
                values.put("passport_no", user.getPassportNo());
                values.put("national_id", user.getNationalId());
                values.put("email", user.getEmail());
                values.put("phone", user.getPhone());
                values.put("address", user.getAddress());
                values.put("gender", user.getGender());
                values.put("picture_path",user.getPicturePath());
                db.insert(TABLE_USER, null, values);
                isInserted = true;
            }
        }catch (SQLiteException ex){
            Log.d(LOG,ex.toString());
        }


        db.close();
        return  isInserted;
    }



    public void insertJobIndividual(Jobs jobs){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("job_id", jobs.getJobId());
            values.put("company", jobs.getCompany());
            values.put("available_job", jobs.getAvailable_job());
            values.put("job_title", jobs.getJob_title());
            values.put("job_position", jobs.getJob_position());
            values.put("country", jobs.getCountry());
            values.put("date", jobs.getDate());
            values.put("vacancy", jobs.getVacancy());
            values.put("experience", jobs.getExperince());
            values.put("salary", jobs.getSalary());
            values.put("gender", jobs.getGender());
            values.put("job_nature", jobs.getJobNature());
            values.put("job_description", jobs.getJobDescription());
            values.put("job_requirement", jobs.getJobRequirement());
            values.put("time",jobs.getTime());
            values.put("flag",jobs.getFlag());
            db.insert(TABLE_JOBS, null, values);
        }
        catch (SQLiteException ex){
           Log.d(LOG , ex.toString());
        }

            }

        public void updateJob(String id, Jobs jobs){
            try{
                SQLiteDatabase db = this.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("company", jobs.getCompany());
                values.put("available_job", jobs.getAvailable_job());
                values.put("job_title", jobs.getJob_title());
                values.put("job_position", jobs.getJob_position());
                values.put("country", jobs.getCountry());
                values.put("date", jobs.getDate());
                values.put("vacancy", jobs.getVacancy());
                values.put("experience", jobs.getExperince());
                values.put("salary", jobs.getSalary());
                values.put("gender", jobs.getGender());
                values.put("job_nature", jobs.getJobNature());
                values.put("job_description", jobs.getJobDescription());
                values.put("job_requirement", jobs.getJobRequirement());
                db.update(TABLE_JOBS,values,"job_id="+id,null);
            }
            catch (SQLiteException ex){
                 Log.d(LOG,ex.toString());
            }

        }



    public void insertIntoSearchJob(ArrayList<Jobs> jobsArrayList) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        if(!jobsArrayList.isEmpty()){
            for(int i=0; i<jobsArrayList.size();i++){
                values.put("job_id", jobsArrayList.get(i).getJobId());
                values.put("company", jobsArrayList.get(i).getCompany());
                values.put("available_job", jobsArrayList.get(i).getAvailable_job());
                values.put("job_title", jobsArrayList.get(i).getJob_title());
                values.put("job_position", jobsArrayList.get(i).getJob_position());
                values.put("country", jobsArrayList.get(i).getCountry());
                values.put("date", jobsArrayList.get(i).getDate());
                values.put("vacancy", jobsArrayList.get(i).getVacancy());
                values.put("experience", jobsArrayList.get(i).getExperince());
                values.put("salary", jobsArrayList.get(i).getSalary());
                values.put("gender", jobsArrayList.get(i).getGender());
                values.put("job_nature", jobsArrayList.get(i).getJobNature());
                values.put("job_description", jobsArrayList.get(i).getJobDescription());
                values.put("job_requirement", jobsArrayList.get(i).getJobRequirement());
                values.put("time",jobsArrayList.get(i).getTime());
                values.put("flag",jobsArrayList.get(i).getFlag());
                db.insert(TABLE_SEARCH_JOBS, null, values);
            }

        }

        db.close();
    }

    public void deleteSearchJobs(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_SEARCH_JOBS);
        db.close();
    }

    public ArrayList<Jobs> getSearchJobList() {
        ArrayList<Jobs> jobsArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SEARCH_JOBS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Jobs jobs = new Jobs();
                jobs.setJobId(cursor.getString(0));
                jobs.setCompany(cursor.getString(1));
                jobs.setAvailable_job(cursor.getString(2));
                jobs.setJob_title(cursor.getString(3));
                jobs.setJob_position(cursor.getString(4));
                jobs.setCountry(cursor.getString(5));
                jobs.setDate(cursor.getString(6));
                jobs.setVacancy(cursor.getString(7));
                jobs.setExperince(cursor.getString(8));
                jobs.setSalary(cursor.getString(9));
                jobs.setAge(cursor.getString(10));
                jobs.setGender(cursor.getString(11));
                jobs.setJobNature(cursor.getString(12));
                jobs.setJobDescription(cursor.getString(13));
                jobs.setJobRequirement(cursor.getString(14));

                // Adding contact to list
                jobsArrayList.add(jobs);
            } while (cursor.moveToNext());
        }

        // return contact list
        return jobsArrayList;

    }


    public ArrayList<Jobs> getFavouriteJobList() {
        ArrayList<Jobs> jobsArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_JOBS + " WHERE flag= "+Jobs.FLAG_FOR_FAVOURITE+" or flag="+Jobs.FLAG_FOR_APPLIED_AND_UPDATE ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Jobs jobs = new Jobs();
                jobs.setJobId(cursor.getString(0));
                jobs.setCompany(cursor.getString(1));
                jobs.setAvailable_job(cursor.getString(2));
                jobs.setJob_title(cursor.getString(3));
                jobs.setJob_position(cursor.getString(4));
                jobs.setCountry(cursor.getString(5));
                jobs.setDate(cursor.getString(6));
                jobs.setVacancy(cursor.getString(7));
                jobs.setExperince(cursor.getString(8));
                jobs.setSalary(cursor.getString(9));
                jobs.setAge(cursor.getString(10));
                jobs.setGender(cursor.getString(11));
                jobs.setJobNature(cursor.getString(12));
                jobs.setJobDescription(cursor.getString(13));
                jobs.setJobRequirement(cursor.getString(14));

                // Adding contact to list
                jobsArrayList.add(jobs);
            } while (cursor.moveToNext());
        }

        // return contact list
        return jobsArrayList;

    }


    public void insertFavouriteJobs(String jobId , int flag , String time){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("time",time);
        values.put("flag",flag);
        db.update(TABLE_JOBS,values,"job_id="+jobId,null);
    }
    public void insertFavAndAppliedJobs(String jobId , int flag , String time){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("time",time);
        values.put("flag",flag);
        db.update(TABLE_JOBS,values,"job_id="+jobId,null);
    }
    public void insertAppliedJobs(String jobId , int flag , String time){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("time",time);
        values.put("flag",flag);
        db.update(TABLE_JOBS,values,"job_id="+jobId,null);
    }

    public ArrayList<Jobs> getAppliedJobList() {
        ArrayList<Jobs> jobsArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_JOBS  + " WHERE jobs.flag="+Jobs.FLAG_FOR_APPLIED+" or jobs.flag="+Jobs.FLAG_FOR_APPLIED_AND_UPDATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Jobs jobs = new Jobs();
                jobs.setJobId(cursor.getString(0));
                jobs.setCompany(cursor.getString(1));
                jobs.setAvailable_job(cursor.getString(2));
                jobs.setJob_title(cursor.getString(3));
                jobs.setJob_position(cursor.getString(4));
                jobs.setCountry(cursor.getString(5));
                jobs.setDate(cursor.getString(6));
                jobs.setVacancy(cursor.getString(7));
                jobs.setExperince(cursor.getString(8));
                jobs.setSalary(cursor.getString(9));
                jobs.setAge(cursor.getString(10));
                jobs.setGender(cursor.getString(11));
                jobs.setJobNature(cursor.getString(12));
                jobs.setJobDescription(cursor.getString(13));
                jobs.setJobRequirement(cursor.getString(14));

                // Adding contact to list
                jobsArrayList.add(jobs);
            } while (cursor.moveToNext());
        }

        // return contact list
        return jobsArrayList;

    }

    public User getUserDetails() {
        String selectQuery = "SELECT * \n" +
                "    FROM    user\n" +
                "    WHERE   user_id = (SELECT MAX(user_id)  FROM user);";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        User user = new User();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                user.setName(cursor.getString(1));
                user.setProfession(cursor.getString(2));
                user.setDateOfBirth(cursor.getString(3));
                user.setPassportNo(cursor.getString(4));
                user.setNationalId(cursor.getString(5));
                user.setEmail(cursor.getString(6));
                user.setPhone(cursor.getString(7));
                user.setAddress(cursor.getString(8));
                user.setGender(cursor.getString(9));
                user.setPicturePath(cursor.getString(10));



            } while (cursor.moveToNext());
        }

        return  user;

    }

}
