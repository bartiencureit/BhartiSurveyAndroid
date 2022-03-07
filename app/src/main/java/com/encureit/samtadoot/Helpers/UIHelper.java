/*
 * Copyright (c) - 2020 & Created By AbhishekR - EncureIT :)
 */

package com.encureit.samtadoot.Helpers;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UIHelper extends GlobalHelper {

    public UIHelper(Context context) {
        super(context);
    }

//    public void ShowAlertDialogNotification(String title, String message, String positiveButton) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this.context, R.style.AppCompatAlertDialogStyle);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.setPositiveButton(positiveButton, null);
//        builder.show();
//    }
//
//    public AlertDialog.Builder ShowConfirmDialog(String title, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this.context, R.style.AppCompatAlertDialogStyle);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        return builder;
//    }

    /*public SpotsDialog getSpotsDialog(Context context) {
        final SpotsDialog dialog = new SpotsDialog(context, R.style.SpotsDialogCustom);
        return dialog;
    }*/

//    public android.app.AlertDialog getSpotsDialog(Context context) {
//        final android.app.AlertDialog dialog = new SpotsDialog.Builder()
//                .setContext(context).setTheme(R.style.SpotsDialogCustom).build();
//        return dialog;
//    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void setHintOnEditText(final EditText editText, final String hintText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setHint("");
                } else {
                    editText.setHint(hintText);
                }
            }
        });
    }

}
