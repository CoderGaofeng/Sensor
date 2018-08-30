package com.and.middleware;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.and.middleware.dialog.TipDialog;
public class AndActivity extends AppCompatActivity {

    private TipDialog dialog;


    public TipDialog obtainDialog() {
        if (dialog == null) {
            dialog = new TipDialog(this);
        }
        return dialog;
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();

    }


    public void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm == null) {
            return;
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

    }

    public void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm == null) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
