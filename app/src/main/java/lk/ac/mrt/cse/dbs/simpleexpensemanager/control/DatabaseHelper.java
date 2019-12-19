package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "expenseManager.db";
    public static  final  String TABLE_ACCOUNT = "account_table";
    public static  final  String TABLE_TRANSACTION = "transaction_table";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE table " + TABLE_ACCOUNT + "(AccountNo INTEGER primary key, Bank TEXT, AccountHolder TEXT, Balance REAL)");
        sqLiteDatabase.execSQL("CREATE table " + TABLE_TRANSACTION + "(AccountNo INTEGER primary key, Type TEXT, Amount REAL, Date TEXT, FOREIGN KEY (AccountNo) REFERENCES TABLE_ACCOUNT(AccountNo) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_TRANSACTION);
    }
}
