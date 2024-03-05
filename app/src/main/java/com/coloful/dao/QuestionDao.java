package com.coloful.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coloful.constant.Constant;
import com.coloful.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDao {

    private DBHelper db;
    private SQLiteDatabase sqLiteDatabase;

    public List<Question> getQuestionForQuiz(Context context, Integer quizId) {
        List<Question> questionList = new ArrayList<>();
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from question where quiz_id =?", new String[]{quizId.toString()});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question q = new Question();
            q.setId(cursor.getInt(0));
            q.setContent(cursor.getString(1));
            Cursor cs = sqLiteDatabase.rawQuery("select * from answer where question_id = ?", new String[]{q.getId().toString()});
            cs.moveToFirst();
            q.setAnswer(cs.getString(1));

            questionList.add(q);
            cursor.moveToNext();
        }
        db.close();
        return questionList;
    }

    public List<Question> getAllQuestions(Context context, int quizId) {
        List<Question> questionList = new ArrayList<>();
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Constant.Question.TABLE_NAME.getValue() + " where " + Constant.Question.QUIZ_ID.getValue() + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Question quest = new Question();
                quest.setId(cursor.getInt(0));
                quest.setContent(cursor.getString(1));
                quest.setAnswer(cursor.getString(2));

                questionList.add(quest);
            } while (cursor.moveToNext());
        }
        db.close();
        // return quest list
        return questionList;
    }

    public Integer removeAllQuestionByQuizId(Context context, int quizId) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        String whereClause = " quiz_id = ?";
        String[] whereArgs = new String[]{Integer.toString(quizId)};

        // Delete the row from the database
        int result = sqLiteDatabase.delete(Constant.Question.TABLE_NAME.getValue(), whereClause, whereArgs);
        sqLiteDatabase.close();
        return result;
    }
}
