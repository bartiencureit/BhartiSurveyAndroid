package com.encureit.samtadoot.database;

import android.content.Context;

import com.encureit.samtadoot.R;
import com.encureit.samtadoot.database.dao.AssignDetailsDao;
import com.encureit.samtadoot.database.dao.CandidateDetailsDao;
import com.encureit.samtadoot.database.dao.CandidateSurveyStatusDetailsDao;
import com.encureit.samtadoot.database.dao.OtherValuesDao;
import com.encureit.samtadoot.database.dao.QuestionOptionDao;
import com.encureit.samtadoot.database.dao.QuestionTypeDao;
import com.encureit.samtadoot.database.dao.QuestionValidationDao;
import com.encureit.samtadoot.database.dao.SurveyQuestionDao;
import com.encureit.samtadoot.database.dao.SurveySectionDao;
import com.encureit.samtadoot.database.dao.SurveyTypeDao;
import com.encureit.samtadoot.database.dao.UserDeviceDetailsDao;
import com.encureit.samtadoot.models.AssignDetails;
import com.encureit.samtadoot.models.CandidateDetails;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.OtherValues;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.QuestionType;
import com.encureit.samtadoot.models.QuestionValidation;
import com.encureit.samtadoot.models.SurveyQuestion;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.UserDeviceDetails;

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
        AssignDetails.class,
        CandidateDetails.class,
        CandidateSurveyStatusDetails.class,
        OtherValues.class
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
    public abstract CandidateDetailsDao candidateDetailsDao();
    public abstract CandidateSurveyStatusDetailsDao candidateSurveyStatusDetailsDao();
    public abstract OtherValuesDao otherValuesDao();
}
