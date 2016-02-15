package com.andwho.myplan.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andwho.myplan.R;

/**
 * Created by zhouf on 2016/1/21.
 */
public class CustomEditView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private WordLimitedEditText mEditText;
    private ImageView mDeleteBtn;
    private ImageView mDownBtn;
    private ImageView mOtherBtn;
    private CustomEditViewListner mCustomEditViewInterface;
    private LinearLayout mAllView;

    /**
     * 编辑框实际长度
     */
    private int mCurrentLenght;
    /**
     * 是否需要自动填充数据标志
     */
    private boolean autoCompleteFlag = true;
    /**
     * 表示数据是自动填充的，自动填充的数据定义为非编辑框实际长度
     */
    private boolean isAutoComplete = false;
    private boolean isVisible = false;


    public CustomEditView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CustomEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_customeditview, this);
        mEditText = (WordLimitedEditText) findViewById(R.id.customeditview_eidt);
        mDeleteBtn = (ImageView) findViewById(R.id.customeditview_deletebtn);
        mDownBtn = (ImageView) findViewById(R.id.customeditview_downbtn);
        mOtherBtn = (ImageView) findViewById(R.id.customeditview_otherbtn);
        mAllView = (LinearLayout) findViewById(R.id.ll_all);

        if (mEditText.getText().toString().trim().equals("")) {
            mDeleteBtn.setVisibility(View.GONE);
        } else {
            if (isVisible) {
                mDeleteBtn.setVisibility(View.VISIBLE);
            } else {
                mDeleteBtn.setVisibility(View.GONE);
            }
        }
        mEditText.addTextChangedListener(textWatcher);
        mEditText.setFocusable(true);
        mEditText.requestFocus();
        // modify by chenyl 2015-5-29 如果失去焦点需要把删除整句按钮消失掉
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (isVisible && !mEditText.getText().toString().trim().equals("")) {
                    if (!hasFocus) {
                        mDeleteBtn.setVisibility(View.GONE);
                    } else {
                        mDeleteBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mDeleteBtn.setOnClickListener(this);
        mDownBtn.setOnClickListener(this);
        mOtherBtn.setOnClickListener(this);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void setDeleteBtnVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setOnSelectionChangedListener(WordLimitedEditText.OnSelectionChangedListener onSelectionChangedListener) {
        mEditText.setOnSelectionChangedListener(onSelectionChangedListener);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            CharSequence c = mEditText.getText();
            if (c.toString().trim().equals("")) {
                mDeleteBtn.setVisibility(View.GONE);
            } else {
                if (isVisible) {
                    mDeleteBtn.setVisibility(View.VISIBLE);
                } else {
                    mDeleteBtn.setVisibility(View.GONE);
                }
            }
            autoCompleteFlag = true;
            if (mCurrentLenght >= s.length()) {
                autoCompleteFlag = false;// 退格不需要自动填充
            }
            if (!isAutoComplete) {
                mCurrentLenght = s.length();// 自动填充数字时不当做编辑框实际长度
            }
            isAutoComplete = false;
            if (mCustomEditViewInterface != null) {
                mCustomEditViewInterface.onFuzzyMatch(s, autoCompleteFlag);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.customeditview_deletebtn:
                mEditText.setText("");
                break;
            case R.id.customeditview_downbtn:
                if (mCustomEditViewInterface != null) {
                    mCustomEditViewInterface.onDownBtnClick();
                }
                break;
            case R.id.customeditview_otherbtn:
                if (mCustomEditViewInterface != null) {
                    mCustomEditViewInterface.onOtherBtnClick();
                }
                break;
            default:
                break;
        }
    }

    public void setOtherEnable(boolean flag) {
        mOtherBtn.setEnabled(flag);
    }

    public void setEditTextEnable(boolean flag) {
        mEditText.setEnabled(flag);
    }

    public void setDownBtnImg(int resource) {
        mDownBtn.setImageResource(resource);
    }

    public void setOtherBtnImg(int resource) {
        mOtherBtn.setImageResource(resource);
    }

    /**
     * 设置是否为自动填充数据
     *
     * @param flag
     */
    public void setIsAutoComplete(boolean flag) {
        isAutoComplete = flag;
    }

    /**
     * 设置光标位置
     *
     * @param index
     */
    public void setSelection(int index) {
        if (index < 0) {
            return;
        }
        mEditText.setSelection(index);
    }

    /**
     * 设置监听器
     *
     * @param customEditViewInterface
     */
    public void setCustomEditViewLister(CustomEditViewListner customEditViewInterface) {
        mCustomEditViewInterface = customEditViewInterface;
    }

    public void setDownBtnVisibility(boolean flag) {
        if (flag) {
            mDownBtn.setVisibility(View.VISIBLE);
        } else {
            mDownBtn.setVisibility(View.GONE);
        }
    }

    public void setOtherBtnVisibility(boolean flag) {
        if (flag) {
            mOtherBtn.setVisibility(View.VISIBLE);
        } else {
            mOtherBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 设置编辑框内容
     *
     * @param content
     */
    public void setEditViewContent(Object content) {
        if (content instanceof CharSequence) {
            mEditText.setText((CharSequence) content);
        }
    }

    /**
     * 获取编辑框内容
     *
     * @return
     */
    public String getEditViewContent() {
        return mEditText.getText().toString();
    }

    public Editable getEditViewText() {
        return mEditText.getText();
    }

    /**
     * 获取编辑框内容
     *
     * @return
     */
    public WordLimitedEditText getEditView() {
        return mEditText;
    }

    /**
     * 编辑框错误
     *
     * @param error
     */
    public void setError(String error) {
        mEditText.setError("" + error);
    }

    /**
     * 设置编辑框hint
     *
     * @param hint
     */
    public void setEditViewHint(String hint) {
        mEditText.setHint(hint);
        mEditText.setHintTextColor(getResources().getColor(R.color.my_item_small_right_tv));
    }

    public String getEditViewHint() {
        return mEditText.getHint().toString();
    }

    /**
     * 设置编辑框密码输入方式
     */
    public void setPassWord() {
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    /**
     * 设置编辑框数字输入方式
     */
    public void setNumberInput() {
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * 设置编辑框输入方式和最大输入字符
     */
    public void setStringLength(int max, int type) {
        mEditText.setInputType(type);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    /**
     * 设置编辑框密码输入方式
     */
    public void setSingleLine(boolean singleLine) {
        mEditText.setSingleLine(true);
    }

    /**
     * 设置聚焦
     */
    public void setFocus() {
        mEditText.requestFocus();
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mEditText.removeTextChangedListener(textWatcher);
        mEditText.addTextChangedListener(watcher);
    }

    /**
     * 设置整个背景颜色
     *
     * @param color
     */
    public void setViewBackground(int color) {
        mAllView.setBackgroundColor(color);
    }

    public void setContextColor(int color) {
        mEditText.setTextColor(color);
    }

    public interface CustomEditViewListner {
        void onDownBtnClick();

        void onOtherBtnClick();

        void onFuzzyMatch(Editable s, boolean autoCompleteFlag);
    }
}
