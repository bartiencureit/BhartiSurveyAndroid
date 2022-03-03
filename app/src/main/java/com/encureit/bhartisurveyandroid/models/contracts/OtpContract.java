package com.encureit.bhartisurveyandroid.models.contracts;

import com.encureit.bhartisurveyandroid.models.SurveySection;
import com.encureit.bhartisurveyandroid.network.responsemodel.OtpCheckResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveySectionResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.UserAssignedDetailsResponseModel;

/**
 * Created by Swapna Thakur on 3/1/2022.
 * Contract for otp
 */
public interface OtpContract {

    interface ViewModel {
        void getOtp(OtpCheckResponseModel otpCheckResponseModel);
        void getSurveySectionFieldsResponse(SurveySectionResponseModel surveySectionResponseModel);
        void getSurveyQuestionFieldResponse(SurveyQuestionResponseModel surveyQuestionResponseModel);
        void getQuestionOptionFieldResponse(QuestionOptionResponseModel questionOptionResponseModel);
        void getQuestionTypesFieldResponse(QuestionTypeResponseModel questionTypeResponseModel);
        void getQuestionValidationFieldsResponse(QuestionValidationResponseModel questionValidationResponseModel);
        void getUserAssignedDetails(UserAssignedDetailsResponseModel userAssignedDetailsResponseModel);
        void getSurveyMasterResponse(SurveyTypeResponseModel surveyTypeResponseModel);
        void showOtpFailed(String error);
    }
    interface Presenter {
        void sendOtp();
        void getSurveySectionFields();
        void getSurveyQuestionField();
        void getQuestionOptionField();
        void getQuestionTypesField();
        void getQuestionValidationFields();
        void getUserAssignedDetails(String user_id);
        void getSurveyMaster();
    }
}
