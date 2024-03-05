package com.coloful.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coloful.constant.Constant;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "DB_QUIZ";
    private static final int version = 2;
    public Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlAccount = "CREATE TABLE IF NOT EXISTS Account (id integer primary key autoincrement, username TEXT not null, password TEXT not null, email TEXT not null, dob TEXT not null)";
        sqLiteDatabase.execSQL(sqlAccount);

        String sqlQuiz = "CREATE TABLE IF NOT EXISTS " + Constant.Quiz.TABLE_NAME.getValue()
                + " (id integer primary key autoincrement, title TEXT not null, author TEXT not null)";
        sqLiteDatabase.execSQL(sqlQuiz);

        String sqlQuestion = "CREATE TABLE IF NOT EXISTS " + Constant.Question.TABLE_NAME.getValue()
                + " (id integer primary key autoincrement, question_content TEXT not null," +
                " quiz_id TEXT not null)";
        sqLiteDatabase.execSQL(sqlQuestion);

        String sqlAnswer = "CREATE TABLE IF NOT EXISTS " + Constant.Answer.TABLE_NAME.getValue()
                + " (id integer primary key autoincrement, answer_content TEXT not null," +
                " question_id TEXT not null)";
        sqLiteDatabase.execSQL(sqlAnswer);

        String sqlQuizAccount = "CREATE TABLE IF NOT EXISTS " + Constant.QuizAccount.TABLE_NAME.getValue()
                + " (quiz_id TEXT not null, account_id TEXT not null, last_time_join TEXT not null)";
        sqLiteDatabase.execSQL(sqlQuizAccount);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sqlAccount = "Drop table if exists " + Constant.Account.TABLE_NAME.getValue();
        sqLiteDatabase.execSQL(sqlAccount);

        String sqlQuiz = "Drop table if exists " + Constant.Quiz.TABLE_NAME.getValue();
        sqLiteDatabase.execSQL(sqlQuiz);

        String sqlQues = "Drop table if exists " + Constant.Question.TABLE_NAME.getValue();
        sqLiteDatabase.execSQL(sqlQues);

        String sqlAnswer = "Drop table if exists " + Constant.Answer.TABLE_NAME.getValue();
        sqLiteDatabase.execSQL(sqlAnswer);

        String sqlQuizAccount = "Drop table if exists " + Constant.QuizAccount.TABLE_NAME.getValue();
        sqLiteDatabase.execSQL(sqlQuizAccount);

        onCreate(sqLiteDatabase);
    }
}
