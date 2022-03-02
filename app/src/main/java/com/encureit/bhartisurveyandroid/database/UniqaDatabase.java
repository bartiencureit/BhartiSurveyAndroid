package com.encureit.bhartisurveyandroid.database;

import android.content.Context;

import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.models.SurveyType;

import androidx.room.Database;

@Database(entities = {SurveyType.class},
        version = 1, exportSchema = false)
public abstract class UniqaDatabase extends AppDatabase {

    private static volatile UniqaDatabase sInstance;

    // Get a database instance
    public static synchronized UniqaDatabase on() {
        return sInstance;
    }

    public static synchronized void init(Context context) {

        if (sInstance == null) {
            synchronized (UniqaDatabase.class) {
                sInstance = createDb(context, context.getString(R.string.app_name), UniqaDatabase.class);
            }
        }
    }

    public abstract UniqaCustomDao codeDao();
}
