package com.encureit.samtadoot.presenter;

import android.view.View;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.features.subforms.QuesSectionListActivity;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.contracts.SurveySectionContract;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/4/2022.
 */
public class SurveySectionPresenter implements SurveySectionContract.Presenter {
    private final QuesSectionListActivity mActivity;
    private final SurveySectionContract.ViewModel mViewModel;
    public View rootView;

    public SurveySectionPresenter(QuesSectionListActivity mActivity, SurveySectionContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
    }

    @Override
    public void startSurveySection(String form_id) {
        List<SurveySection> surveySectionList = DatabaseUtil.on().getSurveySectionDao().getAllSections(form_id);
        mViewModel.setupFields(surveySectionList);
    }

}
