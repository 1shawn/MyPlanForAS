package com.andwho.myplan.utils;

/**
 * Created by zhouf on 2016/2/14.
 */
public class StringUtil {
    /**
     * add by zhouf 2016-2-14
     * 把邮箱中间用****，错误就返回原字符串
     *
     * @param value
     * @return
     */
    public static String starStrFormatChange(String value) {
        String result=value;
        try{
//            result = result.substring(0,3)+"****"+result.substring(7,result.length());
            result= value.replace(value.substring(1, value.indexOf('@')),"******");
        }catch (Exception ex){

        }
        return result;
    }
}
