package com.cathy.sensor.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.cathy.sensor.vo.CaptureValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by xianggaofeng on 2018/3/20.
 */

public class FilePresenter {

    private static final String[][] MIME_MapTable = {
//{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public synchronized String saveExcel(List<CaptureValue> list, String filePath) {
        boolean result = false;
        WritableWorkbook workbook = null;


        filePath = filePath + File.separator + System.currentTimeMillis() + ".xls";

        Log.d("xgf", "" + filePath);
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try {
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            // 增加表头
            int r = 0;
            int c = 0;
            sheet.addCell(new Label(c++, r, "GPS time"));
            sheet.addCell(new Label(c++, r, "Latitude"));
            sheet.addCell(new Label(c++, r, "Longitude"));
            sheet.addCell(new Label(c++, r, "Altitude"));
            sheet.addCell(new Label(c++, r, "Accelerometer Time"));
            sheet.addCell(new Label(c++, r, "X"));
            sheet.addCell(new Label(c++, r, "Y"));
            sheet.addCell(new Label(c++, r, "Z"));

            sheet.addCell(new Label(c++, r, "Gyroscope Time"));
            sheet.addCell(new Label(c++, r, "X"));
            sheet.addCell(new Label(c++, r, "Y"));
            sheet.addCell(new Label(c, r, "Z"));


            CaptureValue value;
            for (r = 1; r < list.size(); r++) {
                c = 0;
                value = list.get(r);

                sheet.addCell(new Label(c++, r, value.gpsTime));
                sheet.addCell(new Label(c++, r, value.latitude));
                sheet.addCell(new Label(c++, r, value.longitude));
                sheet.addCell(new Label(c++, r, value.altitude));
                sheet.addCell(new Label(c++, r, value.accelerometerTime));
                sheet.addCell(new Label(c++, r, value.aX));
                sheet.addCell(new Label(c++, r, value.aY));
                sheet.addCell(new Label(c++, r, value.aZ));

                sheet.addCell(new Label(c++, r, value.gyroscopeTime));
                sheet.addCell(new Label(c++, r, value.gX));
                sheet.addCell(new Label(c++, r, value.gY));
                sheet.addCell(new Label(c, r, value.gZ));

//                sheet.addCell(new Label(c, r++, value.gpsTime));
//                sheet.addCell(new Label(c, r++, value.latitude));
//                sheet.addCell(new Label(c, r++, value.longitude));
//                sheet.addCell(new Label(c, r++, value.accelerometerTime));
//                sheet.addCell(new Label(c, r++, value.aX));
//                sheet.addCell(new Label(c, r++, value.aY));
//                sheet.addCell(new Label(c, r++, value.aZ));
//                sheet.addCell(new Label(c, r++, value.gyroscopeTime));
//                sheet.addCell(new Label(c, r++, value.gX));
//                sheet.addCell(new Label(c, r++, value.gY));
//                sheet.addCell(new Label(c, r, value.gZ));

            }

            workbook.write();
            result = true;

        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } catch (RowsExceededException e) {
            e.printStackTrace();
            result = false;
        } catch (WriteException e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (workbook != null) {

                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }

        if (result) {
            return filePath;
        }
        return null;
    }


    // 調用系統方法分享文件
    public static void shareFile(Context context, File file) {
        if (null != file && file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            Log.d("xgf", context.getPackageName());

            share.putExtra(Intent.EXTRA_SUBJECT, "The is excel");
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
                share.putExtra(Intent.EXTRA_STREAM, fileUri);
                Log.d("xgf", fileUri + "");
            } else {
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            Log.d("xgf", getMIMEType(file));
            share.setType(getMIMEType(file));//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            context.startActivity(Intent.createChooser(share, "分享文件"));
        } else {

        }
    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
//获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
//在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }
}
