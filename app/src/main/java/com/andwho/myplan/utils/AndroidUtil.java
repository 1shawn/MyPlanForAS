package com.andwho.myplan.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhouf on 2016/1/21.
 */
public class AndroidUtil {
    public static void hideKeyboard(Context context) {
        // 隐藏软键盘
        try {
            InputMethodManager imm = ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE));
            if (((Activity) context).getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 根据Unicode编码完美的判断中文汉字和符号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
                ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ||
                ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static final String headName = ".avatar";
    static public File getHeadDir(Context context) {
        File dir = new File(getAppExternalStorage(context), headName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        createNomediaFile(dir,context);
        return dir;
    }
    private static File mRootDir = null;
    static public File getAppExternalStorage(Context context) {
        return new File(mRootDir, context.getPackageName());
    }


    private static void createNomediaFile(File dir,Context context) {
        File file = new File(dir, ".nomedia");
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public File getLocalAvatarDir(Context context) {
        File dir = new File(getHeadDir(context).getAbsoluteFile(), ".l_avatar");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        createNomediaFile(dir,context);
        return dir;
    }
}
