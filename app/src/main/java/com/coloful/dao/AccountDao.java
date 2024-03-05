package com.coloful.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coloful.constant.Constant;
import com.coloful.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    DBHelper db;
    SQLiteDatabase sqLiteDatabase;

    public static void initDataAccount(Context context) {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        List<Account> accountList = new ArrayList<>();
        accountList.add(new Account(null, "user_user2", "123456", "abcd@gmail.com", "01/09/1996"));
        accountList.add(new Account(null, "user_user3", "123456", "abcd1@gmail.com", "02/09/1996"));
        accountList.add(new Account(null, "user_user4", "123456", "abc2d@gmail.com", "03/09/1996"));
        accountList.add(new Account(null, "user_user5", "123456", "abcd3@gmail.com", "04/09/1996"));
        accountList.forEach(account -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constant.Account.USERNAME.getValue(), account.getUsername());
            contentValues.put(Constant.Account.PASSWORD.getValue(), account.getPassword());
            contentValues.put(Constant.Account.EMAIL.getValue(), account.getEmail());
            contentValues.put(Constant.Account.DOB.getValue(), account.getDob());
            sqLiteDatabase.insert(Constant.Account.TABLE_NAME.getValue(), null, contentValues);
        });
        sqLiteDatabase.close();
    }

    public Account checkAccount(Context context, Account account) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from account where username = ? and password = ?",
                new String[]{account.getUsername(), account.getPassword()});
        Account acc = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            acc = new Account();
            acc.setId(cursor.getInt(0));
            acc.setUsername(cursor.getString(1));
            acc.setEmail(cursor.getString(3));
            acc.setDob(cursor.getString(4));
        }
        cursor.close();
        sqLiteDatabase.close();
        return acc;
    }

    public boolean checkUsernameAndEmail(Context context, Account account) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from account where username = ? or email = ?",
                new String[]{account.getUsername(), account.getEmail()});
        if (cursor.getCount() > 0) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        } else {
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }
    }

    public boolean checkEmailExist(Context context, String email) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from account where email = ?",
                new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        } else {
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }
    }

    public Account checkUsernameExisted(Context context, String username) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from account where username = ?",
                new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Account acc = new Account();
            acc.setId(cursor.getInt(0));
            acc.setUsername(cursor.getString(1));
            acc.setEmail(cursor.getString(3));
            acc.setDob(cursor.getString(4));
            cursor.close();
            sqLiteDatabase.close();
            return acc;
        } else {
            cursor.close();
            sqLiteDatabase.close();
            return null;
        }
    }

    // Action with table account
    public boolean insertAccount(Context context, Account account) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.Account.USERNAME.getValue(), account.getUsername());
        contentValues.put(Constant.Account.PASSWORD.getValue(), account.getPassword());
        contentValues.put(Constant.Account.EMAIL.getValue(), account.getEmail());
        contentValues.put(Constant.Account.DOB.getValue(), account.getDob());
        long result = sqLiteDatabase.insert(Constant.Account.TABLE_NAME.getValue(), null, contentValues);
        sqLiteDatabase.close();
        return result > 0;
    }

    public boolean updateEmail(Context context, String newEmail, Integer id) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.Account.EMAIL.getValue(), newEmail);
        int row = sqLiteDatabase.update(Constant.Account.TABLE_NAME.getValue(), values, "id=?",
                new String[]{id.toString()});
        sqLiteDatabase.close();
        return (row > 0);
    }

    public boolean updateUsername(Context context, String newUsername, Integer id) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.Account.USERNAME.getValue(), newUsername);
        int row = sqLiteDatabase.update(Constant.Account.TABLE_NAME.getValue(), values, "id=?",
                new String[]{id.toString()});
        sqLiteDatabase.close();
        return (row > 0);
    }

    public boolean updatePassword(Context context, String newPass, Integer id) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.Account.PASSWORD.getValue(), newPass);
        int row = sqLiteDatabase.update(Constant.Account.TABLE_NAME.getValue(), values, "id=?",
                new String[]{id.toString()});
        sqLiteDatabase.close();
        return (row > 0);
    }

    public Account getAccountForQuiz(Context context, int accountId) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM account WHERE id=?",
                new String[]{String.valueOf(accountId)});
        Account acc = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            acc = new Account();
            acc.setId(cursor.getInt(0));
            acc.setUsername(cursor.getString(1));
            acc.setEmail(cursor.getString(3));
            acc.setDob(cursor.getString(4));
        }
        cursor.close();
        sqLiteDatabase.close();
        return acc;
    }

    public String getUsernameByEmail(Context context, String email) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select username from account where email =? ", new String[]{email});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return cursor.getString(0);
        }
        sqLiteDatabase.close();
        return null;
    }
}
