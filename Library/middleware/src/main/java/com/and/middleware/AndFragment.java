package com.and.middleware;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.and.middleware.dialog.TipDialog;


public class AndFragment extends Fragment {
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }


    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }


    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {

    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {

    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {

    }


    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {

    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
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

    private TipDialog dialog;


    public TipDialog obtainDialog() {
        if (dialog == null) {
            dialog = new TipDialog(getContext());
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroyView();
    }


}
