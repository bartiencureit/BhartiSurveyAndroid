package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.network.responsemodel.OtherValuesResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveySectionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.UserAssignedDetailsResponseModel;

/**
 * Created by Swapna Thakur on 3/2/2022.
 * Contract for dashboard
 */
public interface SettingsContract {

    interface ViewModel {
        void syncFinished();
        void syncFormsFinished();
        void syncFormsPartiallyFinished(String message);
        void logoutFinished();
        void getSurveySectionFieldsResponse(SurveySectionResponseModel surveySectionResponseModel);
        void getSurveyQuestionFieldResponse(SurveyQuestionResponseModel surveyQuestionResponseModel);
        void getQuestionOptionFieldResponse(QuestionOptionResponseModel questionOptionResponseModel);
        void getQuestionTypesFieldResponse(QuestionTypeResponseModel questionTypeResponseModel);
        void getQuestionValidationFieldsResponse(QuestionValidationResponseModel questionValidationResponseModel);
        void getUserAssignedDetails(UserAssignedDetailsResponseModel userAssignedDetailsResponseModel);
        void getSurveyMasterResponse(SurveyTypeResponseModel surveyTypeResponseModel);
        void getOtherValuesResponse(OtherValuesResponseModel otherValuesResponseModel);
        void showResponseFailed(String error);
        void showResponseNoData(String message);
    }
    interface Presenter {
        void setUpData(GlobalHelper helper);
        void syncAll();
        void syncAllForms();
        void logout();
    }
}
