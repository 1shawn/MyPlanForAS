package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andwho.myplan.R;
import com.andwho.myplan.constants.ConfigParam;
import com.andwho.myplan.model.Posts;
import com.andwho.myplan.model.UserSettings;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.FilesUtil;
import com.andwho.myplan.utils.Log;
import com.andwho.myplan.utils.ToastUtil;
import com.andwho.myplan.view.RemoteImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by ys_1shawn on 2016/2/21.
 */
public class EditPostAct extends SlideAct implements View.OnClickListener {

    private static final String TAG = CommunityDetailAct.class.getSimpleName();

    private Activity myselfContext;

    private LinearLayout ll_leftIcon;
    private TextView tv_leftIcon;
    private TextView tv_title;
    private ImageView iv_rightIcon;

    private FrameLayout image_layout1, image_layout2;

    private EditText et;
    private TextView tv;
//    private ImageView iv11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post_act);

        myselfContext = this;

        initHeader();
        findViews();
        setListener();
        init();
    }

    private void initHeader() {
        ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
        tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);

        ll_leftIcon.setOnClickListener(this);
        iv_rightIcon.setOnClickListener(this);

        tv_leftIcon.setText("小区");

        tv_title.setText("");
        ll_leftIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setImageResource(R.drawable.icon_save);
    }

    private RemoteImageView image_layout1_iv, image_layout2_iv;
    private ImageView image_layout1_delete, image_layout2_delete;

    private void findViews() {
        image_layout1 = (FrameLayout) this.findViewById(R.id.image_layout1);
        image_layout2 = (FrameLayout) this.findViewById(R.id.image_layout2);

        image_layout1_iv = (RemoteImageView) image_layout1.findViewById(R.id.iv);
        image_layout2_iv = (RemoteImageView) image_layout2.findViewById(R.id.iv);

        image_layout1_delete = (ImageView) image_layout1.findViewById(R.id.iv_delete);
        image_layout2_delete = (ImageView) image_layout2.findViewById(R.id.iv_delete);

        et = (EditText) this.findViewById(R.id.et);
        tv = (TextView) this.findViewById(R.id.tv);
//        iv11 = (ImageView) findViewById(R.id.iv11);
    }

    private void setListener() {
        image_layout1.setOnClickListener(this);
        image_layout2.setOnClickListener(this);
        image_layout1_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file1 = null;
                image_layout1_iv.setImageResource(R.drawable.btn_addphoto);
                image_layout1_delete.setVisibility(View.GONE);
                updateTip();
            }
        });
        image_layout2_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file2 = null;
                image_layout2_iv.setImageResource(R.drawable.btn_addphoto);
                image_layout2_delete.setVisibility(View.GONE);
                updateTip();
            }
        });
    }


    private void init() {

    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.ll_leftIcon:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.iv_rightIcon:
//                save();
                uploadFile();//提交
                break;
            case R.id.image_layout1:
                CLICK_IMAGE1 = true;
                openAlbum();
                break;
            case R.id.image_layout2:
                CLICK_IMAGE1 = false;
                openAlbum();
                break;
            default:
                break;
        }
    }

    private File file1, file2;
    private boolean CLICK_IMAGE1 = true;

    private InputMethodManager imm;

    private void showInputMethod() {
        imm = (InputMethodManager) myselfContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    public void hideSoftKeyboard() {
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }


    //    private void takePicture() {
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)
//                && Environment.getExternalStorageDirectory().exists()) {
//            String fileName = String.valueOf(System.currentTimeMillis())
//                    + ".jpg";
//            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//            Uri pictureUri = Uri.fromFile(new File(Environment
//                    .getExternalStorageDirectory(), fileName));
//            MyPlanPreference.getInstance(myselfContext).setTempPicUrl(pictureUri.toString());
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
//            myselfContext.startActivityForResult(intent, REQUEST_CODE_CROP);
//
//        } else {
//            Toast.makeText(myselfContext, R.string.take_photo_msg_no_sdcard,
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
    private Uri photoUri;
    private void takePicture() {
        if (isSdcardExisting()) {
            Intent cameraIntent = new Intent(
                    "android.media.action.IMAGE_CAPTURE");//拍照
            /*cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            MyPlanPreference.getInstance(myselfContext).setTempPicUrl(
                    getImageUri().toString());*/
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(cameraIntent, REQUEST_CODE_CROP);

        } else {
            Toast.makeText(myselfContext, "请插入sd卡", Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        myselfContext
                .startActivityForResult(openAlbumIntent, REQUEST_CODE_CROP);
    }
    private boolean isSdcardExisting() {//判断SD卡是否存在
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    private static final int REQUEST_CODE_CROP = 1245;


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode) {
            return;
        }

        try {
            switch (requestCode) {
                case REQUEST_CODE_CROP:
/*
                    // 拍照
                    Uri uri = Uri.parse(MyPlanPreference.getInstance(myselfContext).getTempPicUrl());

                    Log.e(TAG, "@@...onActivityResult...拍照的路径 uri  =  "
                            + uri.toString());

                    // 相册取图片
                    if (data != null) {
                        uri = data.getData();
                        Log.e(TAG, "@@...onActivityResult...取照片的路径 uri  =  "
                                + uri.toString());
//                        File file = getFile(uri);
//                        Log.e(TAG, "@@...updateUserIcon...取照片的路径 上传文件大小： "
//                                + FilesUtil.FormetFileSize(FilesUtil.getFileSize(file)));
                    }*/
                    String picPath = "";
                    photoUri = data.getData();
                    if (photoUri == null) {
                        Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String[] pojo = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                        cursor.moveToFirst();
                        picPath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
//                        if (uri != null) {
//                        noCompress(uri);
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = 4;
//                        selectBitmap = BitmapFactory.decodeFile(uri.getPath(),
//                                options);
//                        selectBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                        bmpFactoryOptions.inSampleSize = 4;
                        bmpFactoryOptions.inJustDecodeBounds = false;
                        selectBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri), null, bmpFactoryOptions);
                        saveComPressImage();
                    }
                    break;
            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap selectBitmap; // 拍照后裁剪的背景图片
    public static String fileDirStr = "sp/sp_tmp";

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    // 压缩图片数据
    private void saveComPressImage() {
        Log.e(TAG, "@@...updateUserIcon....");
        try {
            if (selectBitmap != null && !selectBitmap.isRecycled()) {

                Log.e(TAG, "@@...updateUserIcon....selectBitmap != null");
                // 创建文件夹
                String imgDir = Environment.getExternalStorageDirectory()
                        + System.getProperty("file.separator") + fileDirStr;
                Log.e(TAG, "@@...updateUserIcon...imgDir： "
                        + imgDir);
                File dirFile = new File(imgDir);
                dirFile.mkdirs();
                String fileName="";
                if (CLICK_IMAGE1) {
                    fileName= ConfigParam.IMAGE_COMM_FILE1;
                }else{
                    fileName=ConfigParam.IMAGE_COMM_FILE2;
                }
                // 创建文件
                String imgPath = imgDir + System.getProperty("file.separator")
                        + fileName;//System.currentTimeMillis() + ".jpg";
                File f = new File(imgPath);
                f.createNewFile();
                // 写文件
                FileOutputStream fOut = new FileOutputStream(f);
                selectBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);// 把Bitmap对象解析成流
                fOut.flush();
                Log.e(TAG, "@@...updateUserIcon...flush： ");

                fOut.close();
                // 原图像素比 4， 压缩率 50 压缩后 5.5M 对应 115k  7.4M 对应167K 大概10M处理后为210K
                // 原图像素比 4， 压缩率 80 压缩后 5.5M 对应 195k  7.4M 对应291K
                // 原图像素比 2, 压缩率 50 压缩后 5.5M 对应 355k  7.4M 对应594k
                // 原图像素比 4, 压缩率 100 压缩后 5.5M 对应 796k  7.4M 对应1.1M
                // 原图像素比 8, 压缩率 100 压缩后 5.5M 对应 225k  7.4M 对应271k
                Log.e(TAG, "@@...updateUserIcon...上传文件大小： "
                        + FilesUtil.FormetFileSize(FilesUtil.getFileSize(f)));
                //Log.e(TAG, "@@...updateUserIcon...上传bitmap大小： " + printBimtmapSize(selectBitmap));
                if (CLICK_IMAGE1) {
                    file1 = f;
                    image_layout1_delete.setVisibility(View.VISIBLE);
                    image_layout1_iv.setImageBitmap(selectBitmap);
                } else {
                    file2 = f;
                    image_layout2_delete.setVisibility(View.VISIBLE);
                    image_layout2_iv.setImageBitmap(selectBitmap);
                }

                //updateTip();
            } else {
                Toast.makeText(myselfContext, R.string.str_operation_failed,
                        Toast.LENGTH_SHORT).show();

                Log.e(TAG, "@@...updateUserIcon...失败 ");

                //iv_userHeader.setImageResource(R.drawable.default_header);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTip() {

        if (file1 == null && file2 == null) {
            tv.setText("已选0张，还可以选2张图");
        } else if (file1 == null || file2 == null) {
            tv.setText("已选1张，还可以选1张图");
        } else {
            tv.setText("已选2张，还可以选0张图");
        }

    }

//    private int printBimtmapSize(Bitmap bitmap) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){     //API 19
//                return bitmap.getAllocationByteCount();
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
//                return bitmap.getByteCount();
//            }
//            return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
//    }

//    private void noCompress(Uri uri) throws Exception {
//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
////        bmpFactoryOptions.inSampleSize = 1;
//        bmpFactoryOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, bmpFactoryOptions);
//
//        long imageHeight = (long) bmpFactoryOptions.outHeight;
//        long imageWidth = (long) bmpFactoryOptions.outWidth;
//
//        File auxFile = new File(uri.toString());
//        Log.e(TAG, "@@...noCompress...压缩前上传文件大小： "
//                + FilesUtil.FormetFileSize(imageHeight * imageWidth));
//    }

    private synchronized void   save(ArrayList<String> list) {
        UserSettings author = new UserSettings();
        author.setObjectId(MyPlanPreference.getInstance(myselfContext).getUserSettingId());
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("http://file.bmob.cn/M02/55/34/oYYBAFafpLeAaY9EAAg29H_KHxU734.png");


        Posts post = new Posts();
        post.isDeleted = "0";
        post.isHighlight = "0";
        post.isTop = "0";
        post.content = et.getText().toString();
        post.updatedTime = new BmobDate(new Date());
        post.author = author;
        post.imgURLArray = list;
        post.save(myselfContext, new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.showShortToast(myselfContext, "发表成功");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.showShortToast(myselfContext, "Sorry，发表失败！");
            }
        });

    }

    private boolean isUploaded1 = false;
    private boolean isUploaded2 = false;
    final ArrayList<String> list = new ArrayList<String>();
    private void uploadFile() {
        String userId = MyPlanPreference.getInstance(myselfContext).getUserId();
        if (TextUtils.isEmpty(userId)) {
//            ToastUtil.showShortToast(myselfContext, "您还未登陆，请登陆");
            IntentHelper.showLogin(myselfContext);//跳转登录页面
            return;
        }
//        list.add("http://file.bmob.cn/M02/55/34/oYYBAFafpLeAaY9EAAg29H_KHxU734.png");
        if (isUploaded1&&isUploaded2){
            save(list);
        }else if(file1==null&&file2==null&&!isUploaded1&&!isUploaded2){
            save(list);
        }
        if (file1 != null&&!isUploaded1) {
            final BmobFile bmobFile = new BmobFile(file1);
            bmobFile.upload(myselfContext, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    isUploaded1=true;
                    Log.e(TAG, "@@...upload file  file = " + bmobFile.getFileUrl(myselfContext));
                    list.add(bmobFile.getFileUrl(myselfContext));//第一张上传

                    //上传第二张图
                    if (file2 != null&&!isUploaded2) {
                        final BmobFile bmobFile2 = new BmobFile(file2);
                        bmobFile2.upload(myselfContext, new UploadFileListener() {

                            @Override
                            public void onSuccess() {
                                isUploaded2=true;
                                Log.e(TAG, "@@...upload file  file = " + bmobFile2.getFileUrl(myselfContext));
                                list.add(bmobFile2.getFileUrl(myselfContext));//第二张上传
                                save(list);//更新帖子表内容
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                save(list);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtil.showShortToast(myselfContext, "Sorry，发表失败！");
                }
            });
        }

    }

}
