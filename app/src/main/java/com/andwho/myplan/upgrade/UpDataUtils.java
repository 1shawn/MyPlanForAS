package com.andwho.myplan.upgrade;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.andwho.myplan.activity.BaseAct;
import com.andwho.myplan.constants.ConfigParam;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.model.UserSettings;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.BmobAgent;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.utils.ToastUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by zhouf on 2016/3/10.
 * 登录的时候上传更新数据
 */
public class UpDataUtils {
    public void  upAllPlanDate(final Context myselfContext){
        final String userId = MyPlanPreference.getInstance(myselfContext).getUserId();
        if(myselfContext==null|| TextUtils.isEmpty(userId)){
            return;
        }

        BmobAgent.checkUserSettingInfo(myselfContext, userId, new FindListener<UserSettings>() {
            @Override
            public void onSuccess(List<UserSettings> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0) {
                    for (final UserSettings userInfodate : object) {
                        updatePlan(userInfodate, myselfContext);
                    }
                } else {
                    final UserSettings userInfo=new UserSettings();
                    userInfo.userObjectId = userId;
                    userInfo.createdTime = DateUtil.getCurDateYYYYMMDD();
                    userInfo.updatedTime = userInfo.createdTime;
                    /*BmobAgent.saveUserInfo(myselfContext, userInfo, new SaveListener() {
                        @Override
                        public void onSuccess() {
//                                ToastUtil.showLongToast(myselfContext, "更新成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
//                                ToastUtil.showLongToast(myselfContext, "更新失败：" + s);
                        }
                    });*/

                    updatePlan(null,myselfContext);
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtil.showLongToast(myselfContext, "查询失败：" + msg);
            }
        });
    }

    private  void updatePlan(final UserSettings userInfo,final Context myselfContext){
        final ArrayList<Plan> listPlan = DbManger.getInstance(myselfContext)
                .queryPlans();
        final ArrayList<BmobObject> upPlan=new ArrayList<BmobObject>();
        if(listPlan!=null&&listPlan.size()>0){
            for (int i=0;i<listPlan.size();i++){
                if(userInfo==null||TextUtils.isEmpty(userInfo.updatedTime)){
                    upPlan.add(listPlan.get(i));
                }else if(DateUtil.isUpDate(listPlan.get(i).updatetime,userInfo.updatedTime)){ //是否需要更新
                    upPlan.add(listPlan.get(i));
                }
            }

        }
        if(upPlan!=null&&upPlan.size()>0){
            BmobAgent.updatePlanDate(myselfContext,upPlan,new UpdateListener() {
                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
//                    toast("批量添加成功");
                    ToastUtil.showLongToast(myselfContext, "更新成功");

                    //更新本地的计划更新时间
                    String updatedTime= DateUtil.getCurDateYYYYMMDDHHMMSS();
                    localPlanChangeTime(updatedTime,listPlan,upPlan,myselfContext);
                    userInfo.updatedTime=updatedTime;
                    upUserSeting(userInfo,myselfContext);
                }

                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    ToastUtil.showLongToast(myselfContext, "更新失败");
                }
            });
        }
    }
    //更新本地计划更新时间
    private void localPlanChangeTime(String updatedTime,List<Plan> plans,List<BmobObject> upPlan,Context myselfContext){
        for(int i=0;i<plans.size();i++){
            Plan localPlan=plans.get(i);
            for(int j=0;j<upPlan.size();j++){
                if(localPlan.planid==((Plan)upPlan).planid){
                    localPlan.updatetime=updatedTime;
                    DbManger.getInstance(myselfContext)
                            .updatePlan(localPlan);
                }
            }
        }
    }

    //更新用户设置相关信息
    private void upUserSeting(final UserSettings userInfo,final Context myselfContext){
        final String picUrl=MyPlanPreference.getInstance(myselfContext)
                .getTempPicUrl();
        String headPicUrl = MyPlanPreference.getInstance(myselfContext).getHeadPicUrl();
        if(TextUtils.isEmpty(headPicUrl)&&!TextUtils.isEmpty(picUrl)) {//表示头像改变过,先上传头像

            Uri uri = Uri.parse(picUrl);
            //下面方法将获取的uri转为String类型哦！
            String[] imgs1 = {MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
            Cursor cursor = ((BaseAct) myselfContext).managedQuery(uri, imgs1, null, null, null);
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            final String img_url = cursor.getString(index);
            cursor.close();

            final BmobFile bmobFile=new BmobFile(getComPressImage(img_url));
            //3、有时候使用upload方式上传不超过10M的文件的时候会出现OOM异常，故，建议开发者使用uploadblock（分片上传）方法来上传文件。
            bmobFile.uploadblock(myselfContext, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    userInfo.avatarURL =   bmobFile.getFileUrl(myselfContext);


                    BmobAgent.updateUserInfo(myselfContext, userInfo, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            try {
                                MyPlanPreference.getInstance(myselfContext).setHeadPicUrl(userInfo.avatarURL);
                                ((BaseAct)myselfContext).finish();//关闭登录页面
                            }catch (Exception ex){

                            }

                        }

                        @Override
                        public void onFailure(int i, String s) {
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

        }

    }

    private File getComPressImage(String srcPath){
        File file=null;
        try {
            file = new File(Environment.getExternalStorageDirectory(),
                    ConfigParam.IMAGE_FILE_NAME);
            if (file != null) {
                Bitmap bitmap=getimage(srcPath);
                FileOutputStream fOut =new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);// 把Bitmap对象解析成流
                fOut.flush();
                fOut.close();
            }
        }catch (Exception e){
            file=new File(srcPath);
        }
        return file;
    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
