package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper dbHelper;

    public  PersistentAccountDAO(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }
    @Override
    public List<String> getAccountNumbersList() {
        Cursor res = dbHelper.getAccountNumbers();
        ArrayList<String> acc_numbers = new ArrayList<>();
        while(res.moveToNext()){
            acc_numbers.add(res.getString(0));
        }
        return acc_numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor res = dbHelper.getAllAccounts();
        ArrayList<Account> accounts = new ArrayList<>();
        while(res.moveToNext()){
            String acc_no = res.getString(0);
            String bank_name = res.getString(1);
            String holder = res.getString(2);
            Double balance = res.getDouble(3);

            Account account = new Account(acc_no, bank_name, holder, balance);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor res = dbHelper.getAnAccount(accountNo);
        while(res.moveToNext()){
            String acc_no = res.getString(0);
            String bank_name = res.getString(1);
            String holder = res.getString(2);
            Double balance = res.getDouble(3);

            Account account = new Account(acc_no, bank_name, holder, balance);
            return account;
        }
        String msg = "Account " +accountNo+" is Invalid";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        dbHelper.insertToAccount(account.getAccountNo(), account.getBankName(),account.getAccountHolderName(),account.getBalance());
        return;
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbHelper.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);

        switch (expenseType){
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
        }

        dbHelper.updateBalance(account);
    }
}
