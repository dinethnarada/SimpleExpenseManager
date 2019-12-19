package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

    public class PersistentTransactionDAO implements TransactionDAO{
        private DatabaseHelper dbHelper;
        private List<Transaction> transactions;

        public  PersistentTransactionDAO(DatabaseHelper dbHelper){
            this.dbHelper = dbHelper;
        }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        dbHelper.insertToTransaction(accountNo, expenseType.toString(),amount , (java.sql.Date) date);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        Cursor res = dbHelper.getAllTransactions();
        ArrayList<Transaction> transactions_list = new ArrayList<>();
        res.moveToNext();
        while(res.isAfterLast()){
            String account_no = res.getString(0);
            Double amount = res.getDouble(2);

            Date dateAdjust = new SimpleDateFormat("dd-mm-yy").parse(res.getString(3));

            String expense_type_db = res.getString(1);
            ExpenseType expenseType = ExpenseType.EXPENSE;
            if(expense_type_db.equals("INCOME")){
                expenseType = ExpenseType.INCOME;
            }
            Transaction transaction = new Transaction(dateAdjust, account_no, expenseType, amount);
            transactions_list.add(transaction);
            res.moveToNext();
        }
        return transactions_list;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        Cursor res = dbHelper.getPaginatedTransaction(limit);
        ArrayList<Transaction> transaction_list = new ArrayList<>();
        while(res.isAfterLast()){
            String account_no = res.getString(0);
            Double amount = res.getDouble(2);

            Date dateAdjust = new SimpleDateFormat("dd-mm-yy").parse(res.getString(3));

            String expense_type_db = res.getString(1);
            ExpenseType expenseType = ExpenseType.EXPENSE;
            if(expense_type_db.equals("INCOME")){
                expenseType = ExpenseType.INCOME;
            }
            Transaction transaction = new Transaction(dateAdjust, account_no, expenseType, amount);
            transaction_list.add(transaction);
            res.moveToNext();
        }
        return transaction_list;
    }
}
