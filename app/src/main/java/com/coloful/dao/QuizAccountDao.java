package com.coloful.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coloful.constant.Constant;
import com.coloful.model.QuizAccount;

import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizAccountDao {

    private DBHelper db;
    private SQLiteDatabase sqLiteDatabase;

    public void addQuizAccount(Context context, int accountId, int quizId) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constant.QuizAccount.ACCOUNT_ID.getValue(), accountId);
        cv.put(Constant.QuizAccount.QUIZ_ID.getValue(), quizId);
        cv.put(Constant.QuizAccount.LAST_TIME_JOIN.getValue(), LocalDateTime.now().toString());
        sqLiteDatabase.insert(Constant.QuizAccount.TABLE_NAME.getValue(), null, cv);
        sqLiteDatabase.close();
    }

    public void updateQuizAccount(Context context, int accountId, int quizId) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constant.QuizAccount.ACCOUNT_ID.getValue(), accountId);
        cv.put(Constant.QuizAccount.QUIZ_ID.getValue(), quizId);
        cv.put(Constant.QuizAccount.LAST_TIME_JOIN.getValue(), LocalDateTime.now().toString());
        sqLiteDatabase.update(Constant.QuizAccount.TABLE_NAME.getValue(), cv, "quiz_id=? and account_id=?", new String[]{Integer.toString(quizId), Integer.toString(accountId)});
        sqLiteDatabase.close();
    }

    public boolean getQuizAccountByIds(Context context, int accountId, int quizId) {
        List<QuizAccount> quizAccountDaos = new ArrayList<>();
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from quiz_account where quiz_id =? and account_id=?", new String[]{Integer.toString(quizId), Integer.toString(accountId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QuizAccount quizAccount = new QuizAccount();
            quizAccount.setQuizId(cursor.getInt(0));
            quizAccount.setAccountId(cursor.getInt(1));
            quizAccountDaos.add(quizAccount);
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return ObjectUtils.isNotEmpty(quizAccountDaos);
    }
}
