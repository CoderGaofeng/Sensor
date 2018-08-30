/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.and.middleware.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

/**
 * Common class used by API responses.
 *
 * @param <T>
 */
public class AndResponse<T> implements ResponseState {
    private static final Pattern LINK_PATTERN = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    private static final Pattern PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)");
    private static final String NEXT_LINK = "next";
    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final String errorMessage;
    @NonNull
    public final Map<String, String> links;

    public AndResponse(Throwable error) {
        code = 500;
        body = null;
        if (error instanceof ConnectException) {
            errorMessage = "网络连接失败，请检查网络设置";
        } else if (error instanceof SocketTimeoutException) {
            errorMessage = "连接超时，请检查网络设置";
        } else if (error instanceof SocketException) {
            errorMessage = "网络异常，请稍后重试";
        } else {
            errorMessage = error.getMessage();
        }

        links = Collections.emptyMap();
    }

    public AndResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            Log.d("xgf", body + "xx");
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
//                    Timber.e(ignored, "error while parsing response");
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
        String linkHeader = response.headers().get("link");
        if (linkHeader == null) {
            links = Collections.emptyMap();
        } else {
            links = new ArrayMap<>();
            Matcher matcher = LINK_PATTERN.matcher(linkHeader);

            while (matcher.find()) {
                int count = matcher.groupCount();
                if (count == 2) {
                    links.put(matcher.group(2), matcher.group(1));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", body=" + body +
                ", errorMessage='" + errorMessage + '\'' +
                ", links=" + links +
                '}';
    }

    @Override
    public boolean isSuccessful() {
        boolean result = true;
        if (body == null) {
            result = false;
        } else if (body instanceof ResponseState) {
            result = ((ResponseState) body).isSuccessful();
        }
        return code >= 200 && code < 300 && result;
    }

    @Override
    public String getMessage() {
        String result = "";
        if (body == null) {

        } else if (body instanceof ResponseState) {
            String tmp = ((ResponseState) body).getMessage();
            if (!TextUtils.isEmpty(tmp)) {
                result += tmp;
            }
        }
        if (errorMessage != null) {
            result += errorMessage;
        }
        return result;
    }


}
