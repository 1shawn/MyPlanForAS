package com.andwho.myplan.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.andwho.myplan.utils.AndroidUtil;
import com.andwho.myplan.utils.ToastUtil;

/**
 * 类说明：自定义字数限制EditText，中文字符占两个字节长度，英文字符占一个字节长度
 * Created by zhouf on 2016/1/21.
 */
public class WordLimitedEditText extends EditText {
    private Context mContext = null;
    private int mLimitedLen = 0;

    private boolean isOverideTextChanged = true;

    public WordLimitedEditText(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    public WordLimitedEditText(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public WordLimitedEditText(Context context){
        super(context);
        init(context);
    }

    public void setLimitedLen(int mLimitedLen) {
        this.mLimitedLen = mLimitedLen;
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mOnSelectionChangedListener != null) {
            mOnSelectionChangedListener.onSelectionChanged(selStart,selEnd);
        }
    }

    private OnSelectionChangedListener mOnSelectionChangedListener;
    public void setOnSelectionChangedListener(OnSelectionChangedListener onSelectionChangedListener){
        mOnSelectionChangedListener = onSelectionChangedListener;
    }
    public interface OnSelectionChangedListener {
        void onSelectionChanged(int selStart, int selEnd);
    }

    private void init(Context context) {
        mContext =context;
        this.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isOverideTextChanged && mLimitedLen > 0) {
                    if (calculateLength(s) > mLimitedLen) {
                        CharSequence insert = s.subSequence(start, start + count);
                        if (!TextUtils.isEmpty(insert)) {
                            String posStr = "";
                            if (start + count < s.length()) {
                                posStr = s.subSequence(start + count, s.length()).toString();
                            }
                            String preStr = s.subSequence(0, start).toString();
                            isOverideTextChanged = false;
                            int len1 = calculateLength(preStr + posStr);
                            int len2 = calculateLength(insert);
                            if (mLimitedLen > len1) {
                                if (mLimitedLen <= len1 + len2) {
                                    preStr = preStr + generateString4Lenght(insert, mLimitedLen - len1) + posStr;
                                    setText(preStr);
                                    setSelection(preStr.length());
                                    ToastUtil.showShortToast(mContext, "已达到最大字符限制");
                                }
                            } else {
                                preStr = generateString4Lenght(preStr + posStr, mLimitedLen);
                                setText(preStr);
                                setSelection(preStr.length());
                                ToastUtil.showShortToast(mContext, "已达到最大字符限制");
                            }
                        }
                    }
                }
                isOverideTextChanged = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 计算内容的字数，一个汉字=两个英文字母
     *
     * @param c
     * @return
     */
    private int calculateLength(CharSequence c) {
        int len = 0;
        for (int i = 0; i < c.length(); i++) {
            boolean isChinese = AndroidUtil.isChinese(c.charAt(i));
            if (isChinese) {
                len += 2;
            } else {
                len++;
            }
        }
        return len;
    }

    /**
     * 计算长度为len(一个汉字=两个英文字母)的字符串
     *
     * @param c
     * @param len
     * @return
     */
    private String generateString4Lenght(CharSequence c, int len) {
        int len1 = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c.length(); i++) {
            boolean isChinese = AndroidUtil.isChinese(c.charAt(i));
            if (isChinese) {
                len1 += 2;
            } else {
                len1++;
            }
            if (len1 > len) {
                break;
            }
            sb.append(c.charAt(i));
        }
        return sb.toString();
    }

}
