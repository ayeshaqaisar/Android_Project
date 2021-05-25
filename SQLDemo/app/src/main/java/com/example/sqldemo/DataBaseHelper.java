package com.example.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
//        super(context, name, factory, version);
        super(context,"customer.db",null,1);
    }

    //This is called the first time a database is accessed. There should be code in here to create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
        db.execSQL(createTableStatement);
    }
   //onUpgrade is called if the database version is change.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(CustomerModel customerModel){

        SQLiteDatabase db = this.getWritableDatabase();
        //Similar to setting params
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());


        long insert = db.insert(CUSTOMER_TABLE, null,cv);

        if(insert == -1){
            return false;
        }
        else{
            return true;
        }


    }

    public boolean deleteOne(CustomerModel customerModel){
        //find customerModel in database and if found delete it

        SQLiteDatabase db =this.getWritableDatabase();
        String query = "DELETE FROM "+ CUSTOMER_TABLE+" WHERE "+COLUMN_ID+ " = "+customerModel.getId();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }

    }

    public List<CustomerModel> getALlCustomers(){

        List<CustomerModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM "+ CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor= db.rawQuery(queryString,null);
        //cursor is return type of raw query
        //Cursor is return set from a Set from  a SQL statement

            if (cursor.moveToFirst()) {
                //loop through the cursor(result set) and create new customer objects. Put them into return list.

                do {
                    int customerID = cursor.getInt(0);
                    String customerName = cursor.getString(1);
                    int customerAge = cursor.getInt(2);
                    // In SQLite there is no Boolean it just saves the value 0 or 1.
                    //So while retrieving data we convert integer value to boolean.
                    boolean customerActive = cursor.getInt(3) == 1 ? true : false;

                    CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
                     returnList.add(newCustomer);

                } while (cursor.moveToNext());

            } else {
                //Failure, don't add anything to the list
            }
            //close both cursor and db when done
            cursor.close();
            db.close();
           return returnList;

        }





}
