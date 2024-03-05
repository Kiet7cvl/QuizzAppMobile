package com.coloful.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coloful.constant.Constant;
import com.coloful.model.Answer;

import java.util.ArrayList;
import java.util.List;

public class AnswerDao {

    private DBHelper db;

    private SQLiteDatabase sqLiteDatabase;

    public List<Answer> getAllAnswer(Context context) {
        List<Answer> answers = new ArrayList<>();
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        String selectQuery = "SELECT Distinct * FROM " + Constant.Answer.TABLE_NAME.getValue();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Answer ans = new Answer();
                ans.setContent(cursor.getString(1));
                answers.add(ans);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        db.close();
        // return quest list
        return answers;
    }

    public void removeAllAnswerByQuizId(Context context, int quizId) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        String sql = "DELETE FROM " + Constant.Answer.TABLE_NAME.getValue() +
                " WHERE question_id IN" +
                " (SELECT qs.id FROM Question qs INNER JOIN Quiz qz" +
                " ON qs.quiz_id = qz.id " +
                " WHERE qs.quiz_id = ?)";
        String[] selectionArgs = new String[]{Integer.toString(quizId)};
        sqLiteDatabase.execSQL(sql, selectionArgs);
        sqLiteDatabase.close();
    }
}
