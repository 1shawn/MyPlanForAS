package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Posts;
import com.andwho.myplan.utils.Log;
import com.andwho.myplan.view.touchGallery.GalleryWidget.GalleryViewPager;
import com.andwho.myplan.view.touchGallery.GalleryWidget.UrlPagerAdapter;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by ys_1shawn on 2016/2/22.
 */

@SuppressWarnings("ALL")
public class ImageGalleryAct extends BaseAct implements View.OnClickListener {

    private static final String TAG = ImageGalleryAct.class.getSimpleName();

    private Activity myselfContext;

    private TextView tv, tv_save;
    private GalleryViewPager viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery_act);

        myselfContext = this;

        findViews();
        setListener();
        init();
    }

    private void findViews() {

        tv = (TextView) this.findViewById(R.id.tv);
        tv_save = (TextView) this.findViewById(R.id.tv_save);
        viewer = (GalleryViewPager) this.findViewById(R.id.viewer);
    }

    private void setListener() {
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveBitmapImg(myselfContext, iv_rightIcon,
//                        filmName, mCurUrl);
            }
        });
    }

    private void init() {

        Posts post = (Posts) myselfContext.getIntent().getSerializableExtra("Posts");
        final List<String> items = post.imgURLArray;
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        GalleryViewPager mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);

        pagerAdapter.setOnPositionChangeListener(new UrlPagerAdapter.OnPositionChangeListener() {
            @Override
            public void onPositionChange(int position) {
                Log.e(TAG, "@@...mp..position = " + position);
                tv.setText((position + 1) + "/" + items.size());
            }
        });

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.ll_leftIcon:
                finish();
                break;
            default:
                break;
        }
    }

    private static final String CLASSIC_PIC_FILE_DIR = "myPlan/image";

    /**
     * 保存图片
     *
     * @param filmName
     * @param url
     * @return
     */
//    public boolean saveBitmapImg(Context context, ImageView imageView,
//                                 String filmName, String url) {
//
//        Log.d(TAG, "@@...yxt....saveBitmapImg url = " + url);
//
//        boolean result = false;
//        ImageSize targetSize = getImageSizeScaleTo(imageView);
//        String memoryCacheKey = MemoryCacheUtil.generateKey(url, targetSize);
//        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache()
//                .get(memoryCacheKey);
//        if (bitmap == null) {
//            Toast.makeText(context, getString(R.string.load_pic_wait),
//                    Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        String[] strings = url.split("/");
//        String imgName = strings[strings.length - 1];
//
//        String sdPath = getExternalStoragePath();
//
//        Log.w(TAG, "@@zhai:sd card:" + sdPath);
//
//        if (sdPath == null) {
//            Toast.makeText(context, getString(R.string.faid_save_pic_check),
//                    Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        File file = new File(sdPath + "/" + CLASSIC_PIC_FILE_DIR);
//        if (!file.exists()) {
//            file.mkdir();
//        }
//
//        String filePath = file.getPath() + "/" + filmName + "_" + imgName;
//
//        String[] files = filePath.split("\\?");
//
//        Log.w(TAG, "@@zhai:sd card path:" + files[0]);
//
//        File desFile = new File(files[0]);
//
//        if (!desFile.exists()) {
//            try {
//                desFile.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(
//                    context,
//                    getString(R.string.save_pic_location,
//                            CLASSIC_PIC_FILE_DIR), Toast.LENGTH_SHORT)
//                    .show();
//            return false;
//        }
//        ProgressDialog progressDialog = ProgressDialog.show(context, null,
//                getString(R.string.saving_pic));
//
//        FileOutputStream bos = null;
//        try {
//            bos = new FileOutputStream(desFile);
//            if (imgName.contains(".jpg")) {
//                result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            } else {
//                result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            }
//
//            bos.flush();
//            bos.close();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            if (!isFinishing() && progressDialog != null
//                    && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//
//            if (result) {
//                Toast.makeText(
//                        context,
//                        getString(R.string.save_pic_location,
//                                CLASSIC_PIC_FILE_DIR),
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, getString(R.string.faid_save_pic),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//        Log.w(TAG, "@@zhai:compress bitmap!" + result);
//        return result;
//    }
    private ImageSize getImageSizeScaleTo(ImageView imageView) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int width = params.width; // Get layout width parameter
        if (width <= 0)
            width = getFieldValue(imageView, "mMaxWidth"); // Check maxWidth
        // parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;

        int height = params.height; // Get layout height parameter
        if (height <= 0)
            height = getFieldValue(imageView, "mMaxHeight"); // Check maxHeight
        // parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;

        // Consider device screen orientation
        int screenOrientation = imageView.getContext().getResources()
                .getConfiguration().orientation;
        if ((screenOrientation == Configuration.ORIENTATION_PORTRAIT && width > height)
                || (screenOrientation == Configuration.ORIENTATION_LANDSCAPE && width < height)) {
            int tmp = width;
            width = height;
            height = tmp;
        }

        return new ImageSize(width, height);
    }


    private int getFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return value;
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public String getExternalStoragePath() {
        // 获取SdCard状态
        String state = android.os.Environment.getExternalStorageState();

        // 判断SdCard是否存在并且是可用的

        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {

            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {

                return android.os.Environment.getExternalStorageDirectory()
                        .getPath();

            }

        }

        return null;

    }
}
