package cn.wsy.generallib.utillib.sys;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 日志打印类
 * Created by wsy on 2016/7/15.
 */
public class L {

    private static final String CACHE_DIR_NAME = "DdbugAppLog";

    /**
     * @return 当前类名(simpleName)
     */
    private static String getClassName() {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        String className = thisMethodStack.getClassName();
        int lastIndex = className.lastIndexOf(".");
        return className.substring(lastIndex + 1, className.length());
    }

    //----------   控制台打印 --------------//
    public static void v(String message) {
        v(getClassName(), message);
    }

    public static void v(String tag, String message) {
        Log.v(tag, message);
    }

    public static void i(String message) {
        v(getClassName(), message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void e(String message) {
        v(getClassName(), message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void w(String message) {
        v(getClassName(), message);
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    public static void d(String message) {
        v(getClassName(), message);
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    //----------   日志输出文件 --------------//
    public static void writeFile(final String tag, final String msg){
        write("[ " +TimeUtils.getCurrentTimeInString() +" ] tag --> "+msg);
    }

    /**
     * 保存到日志文件
     * @param content
     */
    private static synchronized void write(final String content)
    {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            FileWriter writer = new FileWriter(getLogFilePath(), true);
                            writer.write(content);
                            writer.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    private static String getLogFilePath(){
        File sdDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + CACHE_DIR_NAME);
        if (!cacheDir.exists())
            cacheDir.mkdir();

        File filePath = new File(cacheDir + File.separator + TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE) + ".txt");

        return filePath.toString();
    }

}
