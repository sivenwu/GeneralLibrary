package cn.wsy.generallib.photolib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;

import cn.wsy.generallib.R;


/**
 * imageLoder框架  加载图片 相机相册
 * Created by Wusy on 2015/10/7.
 */
public class PhotoUtils {

    private static ImageLoader mLoader;
    public static final int CAMERA_CODE = 1;//拍照
    public static final int GALLERY_CODE = 2;//图库

    /**
     * 圆角头像显示配置
     */
    private static DisplayImageOptions roundedAvatarDisplayOptions = null;

    public static final int DEF_WID = 1920;
    public static final int DEF_HEI = 1080;

    public static final int REQ_CHOOSE_IMG_CAMERA = 1001;
    public static final int REQ_CHOOSE_IMG_GALLERY = 1002;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                context);
        builder.diskCacheFileCount(100);
        builder.defaultDisplayImageOptions(getDefDisplayOptionsBuilder()
                .build());
        builder.memoryCache(new LRULimitedMemoryCache(4 * 1024 * 1024));
        builder.writeDebugLogs();
        ImageLoaderConfiguration config = builder.build();
        ImageLoader.getInstance().init(config);
        mLoader = ImageLoader.getInstance();
    }

    /**
     * 配置默认图片
     */
    private static DisplayImageOptions.Builder getDefDisplayOptionsBuilder() {
        DisplayImageOptions.Builder displayOpts = new DisplayImageOptions.Builder();
        displayOpts.showImageOnFail(R.drawable.none_image_defalut);
        displayOpts.showImageForEmptyUri(R.drawable.none_image_defalut);
        displayOpts.showImageOnLoading(R.drawable.none_image_defalut);
        displayOpts.considerExifParams(true);
        displayOpts.bitmapConfig(Bitmap.Config.ARGB_8888);
        displayOpts.imageScaleType(ImageScaleType.EXACTLY);
        displayOpts.cacheOnDisk(true);
        displayOpts.cacheInMemory(true);
        return displayOpts;
    }

    /**
     * 快速滚动的时候不加载图片
     *
     * @param vlist
     * @param listener
     */
    public static void wrapPauseOnScroll(AbsListView vlist,
                                         AbsListView.OnScrollListener listener) {
        PauseOnScrollListener pause;
        if (listener == null) {
            pause = new PauseOnScrollListener(ImageLoader.getInstance(), true,
                    true);
        } else {
            pause = new PauseOnScrollListener(ImageLoader.getInstance(), true,
                    true, listener);
        }
        vlist.setOnScrollListener(pause);
    }

    public static void showImage(ImageView vimg, String url) {
        showImage(vimg, url, null);
    }

    public static void showImage(ImageView vimg, String url, int defImageRes) {
        showImage(vimg, url, getCustomeDefIconOptions(defImageRes), null);
    }

    public static void showImageRound(ImageView vimg, String url) {
        showImage(vimg, url, roundedAvatarDisplayOptions, null);
    }

    public static void showImage(ImageView vimg, String url,
                                 ImageLoadingListener listener) {
        showImage(vimg, url, null, listener);
    }

    public static void showImage(ImageView vimg, String url,
                                 DisplayImageOptions opts, ImageLoadingListener listener) {
        // L.i("加载图片:" + url);
        mLoader.displayImage(url, vimg, opts, listener);
    }

    public static String getUrlFromDrawable(int resid) {
        return "drawable://" + resid;
    }

    public static String getUrlFromFile(File file) {
        return Uri.fromFile(file).toString();
    }

    /**
     * 获取不一样的加载失败效果图
     *
     * @param imgRes
     * @return
     */
    public static DisplayImageOptions getCustomeDefIconOptions(int imgRes) {
        DisplayImageOptions.Builder builder = getDefDisplayOptionsBuilder();
        builder.showImageForEmptyUri(imgRes);
        builder.showImageOnFail(imgRes);
        return builder.build();
    }


    public static void clearMemoryCache() {
        mLoader.clearMemoryCache();
    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null)
            return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public static void releaseBitmap(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled()) {
            return;
        }
        bmp.recycle();
    }

    /**
     * 选择图片或者拍照 配合
     */
    public static void chooseImage(final Activity activity, final File cameraOutFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(new String[]{"拍照", "从相册选择"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraOutFile));
                            activity.startActivityForResult(intent, REQ_CHOOSE_IMG_CAMERA);
                        } else if (which == 1) {
                            Intent galleryIntent = new Intent();
                            galleryIntent.setAction(Intent.ACTION_PICK);
                            galleryIntent.setType("image/*");
                            activity.startActivityForResult(galleryIntent, REQ_CHOOSE_IMG_GALLERY);
                        }
                    }
                });
        builder.show();
    }

    /**
     * 获取返回的 图库图片
     *
     * @param context
     * @param intent
     * @return
     */
    public static String getChooseImageFromGallery(Context context, Intent intent) {
        Uri uri = intent.getData();
        if (uri == null) {
            return null;
        }
        Cursor cursor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = resolver.query(uri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    /**
     * 调用相机 返回保存的文件
     *
     * @param uploadFilePath
     * @param context
     * @return
     */
    public static File photoFromCamera(String uploadFilePath, Activity context) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = new File(uploadFilePath);
        Uri imageUri = Uri.fromFile(tempFile);
        // 指定调用相机拍照后照片的储存路径
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                imageUri);
        context.startActivityForResult(cameraIntent, CAMERA_CODE);
        return tempFile;
    }

    public static void photoFramGallery(Activity context) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        context.startActivityForResult(galleryIntent, GALLERY_CODE);
    }

}
