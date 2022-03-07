package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.Helpers.GlobalHelper;

/**
 * Created by Swapna Thakur on 3/1/2022.
 * Contract for splash
 */
public interface SplashContract {

    interface ViewModel {
        void startAnotherActivity(Class<?> cls, boolean finishCurrent);
    }

    interface Presenter {
        void checkLastLoginDate(GlobalHelper helper);
    }
}
