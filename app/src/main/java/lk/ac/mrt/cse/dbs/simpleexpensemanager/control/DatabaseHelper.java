package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "expenseManager.db";
    public static final String TABLE_ACCOUNT = "account_table";
    public static final String TABLE_TRANSACTION = "transaction_table";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE table " + TABLE_ACCOUNT + "(AccountNo TEXT primary key, Bank TEXT, AccountHolder TEXT, Balance REAL)");
        sqLiteDatabase.execSQL("CREATE table " + TABLE_TRANSACTION + "(AccountNo TEXT primary key, Type TEXT, Amount REAL, Date TEXT, FOREIGN KEY (AccountNo) REFERENCES TABLE_ACCOUNT(AccountNo) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_TRANSACTION);
    }

    public boolean insertToAccount(String accNo, String bank, String accHolder, Float balance){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AccountNo", accNo);
        contentValues.put("Bank", bank);
        contentValues.put("AccountHolder", accHolder);
        contentValues.put("Balance", balance);
        long hasError = database.insert(TABLE_ACCOUNT, null, contentValues);
        if (hasError == -1){ //check whether insertion to the account table has any error.
            return false;
        }else {
            return true;
        }
    }

    public boolean insertToTransaction(String accNo, String type, double amount, String date){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AccountNo", accNo);
        contentValues.put("Type", type);
        contentValues.put("Amount", amount);
        contentValues.put("Date", date);
        long hasError = database.insert(TABLE_TRANSACTION, null, contentValues);
        if (hasError == -1){ //check whether insertion to the account table has any error.
            return false;
        }else {
            return true;
        }
    }
}
