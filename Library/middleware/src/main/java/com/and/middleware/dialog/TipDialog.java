package com.and.middleware.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.and.middleware.R;
import com.and.middleware.event.Tip;
import com.and.middleware.widget.LoadingView;


public class TipDialog extends android.app.Dialog {

    private Runnable mDismissDaemon = new Runnable() {
        @Override
        public void run() {
            if (isShowing()) {
                dismiss();
            }
        }
    };

    private AppCompatImageView iconView;
    private TextView contentView;
    private LoadingView loadingView;

    /**
     * 显示 Loading 图标
     */


    public void dispatchTipEvent(int type, String message) {
        if (type == Tip.TYPE_LOADING) {
            loading(message);
        } else if (type == Tip.TYPE_SUCCESS) {
            success(message);
        } else if (type == Tip.TYPE_FAIL) {
            error(message);
        } else if (type == Tip.TYPE_INFO) {
            message(message);
        } else if (type == Tip.TYPE_DISMISS) {
            dismiss();
        }
    }


    public TipDialog(@NonNull Context context) {
        this(context, 0);
    }

    public TipDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.common_tip);
        iconView = findViewById(R.id.icon);
        contentView = findViewById(R.id.content);
        loadingView = findViewById(R.id.loading);
        setCancelable(false);
    }


    public void showHint(String content) {
        showHint(content, 1500);
    }

    public void showHint(String content, long delayMillis) {
        showWithDaemon(R.drawable.ic_common_notify_info, content, delayMillis);
    }


    public void showSuccess(String content) {
        showSuccess(content, 700);
    }

    public void showSuccess(String content, long delayMillis) {
        showWithDaemon(R.drawable.ic_common_notify_done, content, delayMillis);
    }

    public void showError(String content) {
        showError(content, 1500);
    }

    public void showError(String content, long delayMillis) {
        showWithDaemon(R.drawable.ic_common_notify_error, content, delayMillis);
    }


    public void showWithDaemon(int drawableId, String content, long delayMillis) {
        iconView.setImageResource(drawableId);
        iconView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        if (content == null) {
            this.contentView.setVisibility(View.GONE);
        } else {
            this.contentView.setVisibility(View.VISIBLE);
            this.contentView.setText(content);
        }
        if (!isShowing()) {
            show();
        }
        dismissWithDaemon(delayMillis);
    }

    public void dismissWithDaemon(long delayMillis) {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.getDecorView().removeCallbacks(mDismissDaemon);
        window.getDecorView().postDelayed(mDismissDaemon, delayMillis);

    }

    @Override
    public void show() {
        super.show();
    }

    public void loading(String message) {
        loadingView.setVisibility(View.VISIBLE);
        iconView.setVisibility(View.GONE);

        if (TextUtils.isEmpty(message)) {
            this.contentView.setVisibility(View.GONE);
        } else {
            this.contentView.setVisibility(View.VISIBLE);
            this.contentView.setText(message);
        }
        if (!isShowing()) {
            super.show();
        }

    }

    public void success(String message) {
        showSuccess(message);
    }

    public void error(String message) {
        showError(message);
    }

    public void message(String message) {
        showHint(message);
    }


}
