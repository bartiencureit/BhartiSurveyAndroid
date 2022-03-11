package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.models.OtherValues;
import com.encureit.samtadoot.network.responsemodel.OtherValuesResponseModel;
import com.encureit.samtadoot.network.responsemodel.OtpCheckResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveySectionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.UserAssignedDetailsResponseModel;

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
        void getOtherValuesResponse(OtherValuesResponseModel otherValuesResponseModel);
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
        void getOtherValues();
    }
}
