package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "expenseManager.db";
    public static final String TABLE_ACCOUNT = "account_table";
    public static final String TABLE_TRANSACTION = "transaction_table";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yy", Locale.getDefault());

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
        onCreate(sqLiteDatabase);
    }

    public boolean insertToAccount(String accNo, String bank, String accHolder, double balance){
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

    public boolean insertToTransaction(String accNo, String type, double amount, Date date){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AccountNo", accNo);
        contentValues.put("Type", type);
        contentValues.put("Amount", amount);
        contentValues.put("Date", dateFormat.format(date));
        long hasError = database.insert(TABLE_TRANSACTION, null, contentValues);
        if (hasError == -1){ //check whether insertion to the account table has any error.
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAccountNumbers(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor accountNumbersList = database.rawQuery("SELECT AccountNo FROM " + TABLE_ACCOUNT, null );
        return accountNumbersList;
    }

    public Cursor getAllAccounts(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor allAccount = database.rawQuery("SELECT * FROM " + TABLE_ACCOUNT, null);
        return  allAccount;
    }

    public Cursor getAnAccount(String accountNo){
        SQLiteDatabase database = this.getReadableDatabase();
        String[] account_No = {accountNo};
        Cursor anAccount = database.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE AccountNo" + " = ?",account_No);
        return  anAccount;
    }

    public void updateBalance(Account account){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Balance", account.getBalance());
        database.update(TABLE_ACCOUNT, contentValues, "AccountNo " + " = ? ",new String[] {account.getAccountNo()} );
    }

    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] account_no = {accountNo};
        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE AccountNo" + " = ?", account_no);

        if(res.moveToFirst()){
            database.delete(TABLE_ACCOUNT, "AccountNo = ?", account_no);
            return;
        }else{
            String msg = "Account" + accountNo + "is Invalid";
            throw new InvalidAccountException(msg);
        }
    }

    public Cursor getAllTransactions(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor transactions = database.rawQuery("SELECT * FROM " + TABLE_TRANSACTION, null);
        return  transactions;
    }

    public Cursor getPaginatedTransaction(int limit) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor limits = database.rawQuery("SELECT * FROM " + TABLE_TRANSACTION + " order by Date desc limit ?",new String[]{Integer.toString(limit)});
        return  limits;
    }
}