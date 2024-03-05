package com.coloful.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.coloful.constant.Constant;
import com.coloful.model.Account;
import com.coloful.model.Question;
import com.coloful.model.Quiz;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QuizDao {

    private final AccountDao accountDao;
    private DBHelper db;
    private SQLiteDatabase sqLiteDatabase;

    public QuizDao() {
        accountDao = new AccountDao();
    }

    public List<Quiz> allQuiz(Context context, Integer accountID) {
        List<Quiz> quizList = new ArrayList<>();
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM quiz WHERE author = ?;", new String[]{accountID.toString()});
        cursor.moveToFirst();
        QuestionDao questionDao = new QuestionDao();
        while (!cursor.isAfterLast()) {
            Quiz q = new Quiz();
            q.setId(cursor.getInt(0));
            q.setTitle(cursor.getString(1));
            Account account = accountDao.getAccountForQuiz(context, cursor.getInt(2));
            q.setAuthor(account);
            q.setQuestionList(questionDao.getQuestionForQuiz(context, q.getId()));
            quizList.add(q);
            cursor.moveToNext();
        }
        cursor.close();
        return quizList;
    }

    public Quiz getQuizById(Context context, int id) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM quiz WHERE id = ?;", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        QuestionDao questionDao = new QuestionDao();
        if (cursor.getCount() > 0) {
            Quiz q = new Quiz();
            q.setId(cursor.getInt(0));
            q.setTitle(cursor.getString(1));
            q.setQuestionList(questionDao.getQuestionForQuiz(context, q.getId()));
            Account account = accountDao.getAccountForQuiz(context, cursor.getInt(2));
            q.setAuthor(account);
            return q;
        }
        return null;
    }

    public boolean isQuizExistByTitle(Context context, String title) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM quiz WHERE title = ?;", new String[]{title});
        cursor.moveToFirst();
        Quiz q = new Quiz();
        if (cursor.getCount() > 0) {
            q.setId(cursor.getInt(0));
            q.setTitle(cursor.getString(1));
        }
        return !StringUtils.isEmpty(q.getTitle());
    }

    public Long addQuiz(Context context, String author, String title) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constant.Quiz.TITLE.getValue(), title);
        cv.put(Constant.Quiz.AUTHOR.getValue(), author);
        long result = sqLiteDatabase.insert(Constant.Quiz.TABLE_NAME.getValue(), null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added success", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void addQuestion(Context context, String question, Long quiz_id, String answer) {
        ContentValues values = new ContentValues();
        values.put(Constant.Question.CONTENT.getValue(), question);
        values.put(Constant.Question.QUIZ_ID.getValue(), quiz_id);
        long q_id = sqLiteDatabase.insert(Constant.Question.TABLE_NAME.getValue(), null, values);
        if (q_id == -1) {
            Toast.makeText(context, "Add question failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added success", Toast.LENGTH_SHORT).show();
            addAnswer(context, answer, q_id);
        }
    }

    public void addAnswer(Context context, String answer, Long ques_id) {
        ContentValues values = new ContentValues();
        values.put(Constant.Answer.CONTENT.getValue(), answer);
        values.put(Constant.Answer.QUES_ID.getValue(), ques_id);
        long q_id = sqLiteDatabase.insert(Constant.Answer.TABLE_NAME.getValue(), null, values);
        if (q_id == -1) {
            Toast.makeText(context, "Add answer failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added answer success", Toast.LENGTH_SHORT).show();
        }
    }

    public void joinQuiz(Context context, Integer quizId, Integer accountID) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();
        String lastTimeJoin = Calendar.getInstance().getTime().toString();
        ContentValues values = new ContentValues();
        values.put(Constant.QuizAccount.LAST_TIME_JOIN.getValue(), lastTimeJoin);
        if (checkJoinQuiz(context, quizId, accountID)) {
            sqLiteDatabase.update(Constant.QuizAccount.TABLE_NAME.getValue(), values,
                    "quiz_id=? and account_id=?", new String[]{quizId.toString(), accountID.toString()});
            return;
        }

        values.put(Constant.QuizAccount.QUIZ_ID.getValue(), quizId);
        values.put(Constant.QuizAccount.ACCOUNT_ID.getValue(), accountID);
        sqLiteDatabase.insert(Constant.QuizAccount.TABLE_NAME.getValue(), null, values);
    }

    private boolean checkJoinQuiz(Context context, int quizId, int accountId) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from quiz_account where quiz_id =? and account_id =?",
                new String[]{String.valueOf(quizId), String.valueOf(accountId)});

        return cursor.getCount() > 0;
    }

    public List<Quiz> getQuizRecently(Context context, Account account) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from quiz inner join quiz_account " +
                        "on quiz.id = quiz_account.quiz_id " +
                        "where quiz.author = ? " +
                        "order by quiz_account.last_time_join desc " +
                        "limit 3 ;"
                , new String[]{account.getId().toString()});
        cursor.moveToFirst();
        List<Quiz> quizList = new ArrayList<>();
        QuestionDao questionDao = new QuestionDao();
        while (!cursor.isAfterLast()) {
            Quiz q = new Quiz();
            q.setId(cursor.getInt(0));
            q.setTitle(cursor.getString(1));
            q.setAuthor(account);
            q.setQuestionList(questionDao.getQuestionForQuiz(context, q.getId()));
            quizList.add(q);
            cursor.moveToNext();
        }
        return quizList;
    }

    public List<Quiz> getYourQuiz(Context context, Account account) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from quiz where author =? order by id desc "
                , new String[]{account.getId().toString()});
        cursor.moveToFirst();
        List<Quiz> quizList = new ArrayList<>();
        QuestionDao questionDao = new QuestionDao();
        while (!cursor.isAfterLast()) {
            Quiz q = new Quiz();
            q.setId(cursor.getInt(0));
            q.setTitle(cursor.getString(1));
            q.setAuthor(account);
            q.setQuestionList(questionDao.getQuestionForQuiz(context, q.getId()));
            quizList.add(q);
            cursor.moveToNext();
        }
        return quizList;
    }

    private boolean insertQuiz(Context context, Quiz quiz) {
        db = new DBHelper(context);
        sqLiteDatabase = db.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.Quiz.TITLE.getValue(), quiz.getTitle());
        contentValues.put(Constant.Quiz.AUTHOR.getValue(), quiz.getAuthor().getId());

        long quizId = sqLiteDatabase.insert(Constant.Quiz.TABLE_NAME.getValue(), null, contentValues);
        if (quizId <= 0) {
            return false;
        } else {
            for (Question question : quiz.getQuestionList()) {
                ContentValues values = new ContentValues();
                values.put(Constant.Question.CONTENT.getValue(), question.getContent());
                values.put(Constant.Question.QUIZ_ID.getValue(), quizId);
                long questionId = sqLiteDatabase.insert(Constant.Question.TABLE_NAME.getValue(), null, values);

                if (questionId > 0) {
                    ContentValues values1 = new ContentValues();
                    values1.put(Constant.Answer.CONTENT.getValue(), question.getAnswer());
                    values1.put(Constant.Answer.QUES_ID.getValue(), questionId);
                    long answerId = sqLiteDatabase.insert(Constant.Answer.TABLE_NAME.getValue(), null, values1);

                    if (answerId <= 0) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
    }
}
