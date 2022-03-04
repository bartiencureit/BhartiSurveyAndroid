package com.encureit.bhartisurveyandroid.database;

import android.content.Context;

import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.database.dao.AssignDetailsDao;
import com.encureit.bhartisurveyandroid.database.dao.QuestionOptionDao;
import com.encureit.bhartisurveyandroid.database.dao.QuestionTypeDao;
import com.encureit.bhartisurveyandroid.database.dao.QuestionValidationDao;
import com.encureit.bhartisurveyandroid.database.dao.SurveyQuestionDao;
import com.encureit.bhartisurveyandroid.database.dao.SurveySectionDao;
import com.encureit.bhartisurveyandroid.database.dao.SurveyTypeDao;
import com.encureit.bhartisurveyandroid.database.dao.UserDeviceDetailsDao;
import com.encureit.bhartisurveyandroid.models.AssignDetails;
import com.encureit.bhartisurveyandroid.models.QuestionOption;
import com.encureit.bhartisurveyandroid.models.QuestionType;
import com.encureit.bhartisurveyandroid.models.QuestionValidation;
import com.encureit.bhartisurveyandroid.models.SurveyQuestion;
import com.encureit.bhartisurveyandroid.models.SurveySection;
import com.encureit.bhartisurveyandroid.models.SurveyType;
import com.encureit.bhartisurveyandroid.models.UserDeviceDetails;

import androidx.room.Database;

@Database(entities =
 {
        SurveyType.class,
        UserDeviceDetails.class,
        QuestionOption.class,
        QuestionType.class,
        QuestionValidation.class,
        SurveyQuestion.class,
        SurveySection.class,
        AssignDetails.class
},
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

    public abstract SurveyTypeDao surveyTypeDao();
    public abstract UserDeviceDetailsDao userDeviceDetailsDao();
    public abstract QuestionOptionDao questionOptionDao();
    public abstract QuestionTypeDao questionTypeDao();
    public abstract QuestionValidationDao questionValidationDao();
    public abstract SurveyQuestionDao surveyQuestionDao();
    public abstract SurveySectionDao surveySectionDao();
    public abstract AssignDetailsDao assignDetailsDao();
}
