package com.coloful.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coloful.constant.Constant;
import com.coloful.dao.DBHelper;

public class QuizProvider extends ContentProvider {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private static final String AUTHORITY = "com.coloful.provider";
    private static final String BASE_PATH = Constant.Quiz.TABLE_NAME.getValue();
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final String MIME_TYPE_ROWS = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + BASE_PATH;
    private static final String MIME_TYPE_SINGLE_ROW = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + BASE_PATH;
    private static final int ROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, ROWS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        database = dbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case ROWS:
                cursor = database.query(Constant.Quiz.TABLE_NAME.getValue(), Constant.Quiz.ALL_VALUES.getAllValues(),
                        s, null, null, null, Constant.Quiz.ID.getValue() + " ASC");
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ROWS:
                return MIME_TYPE_ROWS;
            case SINGLE_ROW:
                return MIME_TYPE_SINGLE_ROW;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(Constant.Quiz.TABLE_NAME.getValue(), null, contentValues);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case ROWS:
                delCount = database.delete(Constant.Quiz.TABLE_NAME.getValue(), s, strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case ROWS:
                updCount = database.update(Constant.Quiz.TABLE_NAME.getValue(), contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
}
