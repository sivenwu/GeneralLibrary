package cn.wsy.generallib.utillib.phone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 提供手机常见获取方法
 * Created by wsy on 2016/7/15.
 */
public class PhoneUtil {

    /**
     * 调用系统发短信界面
     */
    public static void sendMessage(Context activity, String phoneNumber, String smsContent) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsContent);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(it);
    }

    private static Boolean _hasCamera = null;
    public static float displayDensity = 0.0F;

    /**
     * 获取Density
     *
     * @return (dsplayDensity)
     */
    public static float getDensity(Context context) {
        if (displayDensity == 0.0)
            displayDensity = getDisplayMetrics(context).density;
        return displayDensity;
    }

    /**
     * 获取DisplayMetrics
     *
     * @return (displaymetrics)
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static float getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static float getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕大小
     *
     * @param activity
     * @return int数组
     */
    public static int[] getRealScreenSize(Activity activity) {
        int[] size = new int[2];
        int screenWidth = 0, screenHeight = 0;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                screenWidth = (Integer) Display.class.getMethod("getRawWidth")
                        .invoke(d);
                screenHeight = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d,
                        realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }


    /**
     * 是否有摄像头
     *
     * @return
     */
    public static final boolean hasCamera(Context context) {
        if (_hasCamera == null) {
            PackageManager pckMgr = context.getPackageManager();
            boolean flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front");
            boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
            boolean flag2;
            if (flag || flag1)
                flag2 = true;
            else
                flag2 = false;
            _hasCamera = Boolean.valueOf(flag2);
        }
        return _hasCamera.booleanValue();
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideSoftKeyboard(View view,Context context) {
        if (view == null)
            return;
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    /**
     * 获取版本号(内部识别号)
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    public static int[] computeHomeContentPicDimension(float tempPicWidth,
                                                       float tempPicHeight, Context context) {
        final float TEMP_SCREEN_WIDTH = 480; // 设计稿是按480*800的分辨率来设计的
        final float TEMP_PIC_WIDTH = tempPicWidth;
        final float TEMP_PIC_HEIGHT = tempPicHeight;
        final int SCREEN_WIDTH = (int) getScreenWidth(context);
        int picWidth = (int) (TEMP_PIC_WIDTH * SCREEN_WIDTH / TEMP_SCREEN_WIDTH);
        int picHeight = (int) (TEMP_PIC_HEIGHT * picWidth / TEMP_PIC_WIDTH);
        int[] dimension = new int[2];
        dimension[0] = picWidth;
        dimension[1] = picHeight;
        return dimension;
    }
}