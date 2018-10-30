package com.qixiu.wigit.myedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qixiu.qixiu.R;
import com.qixiu.qixiu.utils.DrawableUtils;


public class MyEditTextView extends RelativeLayout implements TextWatcherAdapterInterface {
    private RelativeLayout relativeLayout;
    private EditText editText;
    // 命名空间，在引用这个自定义组件的时候，需要用到
    private String namespace = "http://schemas.android.com/apk/res/com.example.myedittext";
    private ImageView imageView,imageViewLeft;
    //可编辑方法
    private int with, height;
    private String text, hint, textcorlor, textsize, hintcolor;
    private FocusChangeListenner focusChangeListenner;
    private boolean isEmojiEnable = false;
    private Context mContext;
    /*
    * 去除掉emoji吧表情的一些变量
    * */
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    //emoji表情的输入允许开关
    private IntelligentTextWatcher textwatech;

    public MyEditTextView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.my_edittext_onkey_delete, this);
//        ButterKnife.bind(this, view);

        mContext = context;
        editText = (EditText) view.findViewById(R.id.edittext_onekey_delete);
        imageView = (ImageView) view.findViewById(R.id.imageView_myEdittextDeleteBtn);
        imageViewLeft = (ImageView) view.findViewById(R.id.imageView_myedittext);
//        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout_myEdittext);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        imageView.setVisibility(GONE);
        textwatech = new IntelligentTextWatcher(context,editText.getId(), this);
        textwatech.setEmojiDisabled(true, editText);
        editText.addTextChangedListener(textwatech);
        //焦点判断
        editText.setOnFocusChangeListener(new
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!TextUtils.isEmpty(getText().toString().toString())) {
                        imageView.setVisibility(VISIBLE);
                    } else {
                        imageView.setVisibility(GONE);
                    }
                    // 此处为得到焦点时的处理内容
                    if (focusChangeListenner != null) {
                        focusChangeListenner.getFocus(editText);
                    }
                } else {
                    // 此处为失去焦点时的处理内容，吧那个图标隐藏,提供一个对外的监听接口
                    imageView.setVisibility(GONE);
                    if (focusChangeListenner != null) {
                        focusChangeListenner.loseFocus(editText);
                    }
                }
            }
        });
        //增加xml里面的可编辑属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyEditTextView);
        String text = array.getString(R.styleable.MyEditTextView_my_edittext_text);
        String hint = array.getString(R.styleable.MyEditTextView_my_edittext_hint);
        int textColor = array.getColor(R.styleable.MyEditTextView_my_edittext_text_color, getResources().getColor(R.color.txt_4D4D4D));
        int hintColor = array.getColor(R.styleable.MyEditTextView_my_edittext_hint_color, getResources().getColor(R.color.txt_8D8D8D));
        float textSize = array.getDimension(R.styleable.MyEditTextView_my_edittext_text_size, getResources().getDimension(R.dimen.margin10dp));
        int input_type = array.getInt(R.styleable.MyEditTextView_input_type, -1);
        int maxLenth = array.getInt(R.styleable.MyEditTextView_my_edittext_max_lenth, 1000);
        int gravity = array.getInt(R.styleable.MyEditTextView_my_edittext_gravity, 10);
//        int leftRes = array.getInt(R.styleable.MyEditTextView_my_edittext_left_image, 10);
        int leftRes = array.getInt(R.styleable.MyEditTextView_my_edittext_drawble_left, 10);
        setLeftImageResource(leftRes);
        editText.setText(text);
        if (TextUtils.isEmpty(text)) {
            editText.setHint(hint);
            editText.setHintTextColor(hintColor);
        }
        editText.setText(text);
        editText.setTextColor(textColor);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        switch (input_type) {
            case 0:
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                break;
            case 1:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLenth)});
        switch (gravity) {
            case 1:
                editText.setGravity(Gravity.TOP);
                break;
            case 2:
                editText.setGravity(Gravity.RIGHT);
                break;
            case 3:
                editText.setGravity(Gravity.LEFT);
                break;
            case 4:
                editText.setGravity(Gravity.BOTTOM);
                break;
            case 5:
                editText.setGravity(Gravity.CENTER);
                break;
            case 6:
                editText.setGravity(Gravity.CENTER_VERTICAL);
                break;
            case 7:
                editText.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case 8:
                editText.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.TOP);
                break;
            case 9:
                editText.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.BOTTOM);
                break;
            case 10:
                editText.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
                break;
            case 11:
                editText.setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);
                break;
        }
        array.recycle();
    }


    public void setInputType(int type) {
        editText.setInputType(type);
    }

    public void setGravity(int gravity) {
        editText.setGravity(gravity);
    }

    //获取输入的文本
    public Editable getText() {
        return editText.getText();
    }

    //设置文本
    public void setText(CharSequence text) {
        editText.setText(text);
    }

    @Override
    public void beforeTextChanged(int editTextId, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(int editTextId, CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(int editTextId, Editable s) {
        if (s.toString().equals("")) {
            imageView.setVisibility(GONE);
        } else {
            imageView.setVisibility(VISIBLE);
        }
    }


    public interface FocusChangeListenner {
        void loseFocus(View v);

        void getFocus(View v);
    }

    public void setFousChangeListener(FocusChangeListenner focusChangeListenner) {
        this.focusChangeListenner = focusChangeListenner;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    public View getEdittextId() {
        return editText;
    }

    public void setIsEmoji(boolean IS_EMOJI) {
        textwatech.setEmojiDisabled(IS_EMOJI, editText);
    }

    public void setSelection(int position) {

        editText.setSelection(position);
    }

    public void setLeftImageResource(int res){
        if(res!=10){
            DrawableUtils.setDrawableResouce(editText,getContext(),res,0,0,0);
        }
    }

    public void setEnabled(boolean f){
        editText.setEnabled( f);
    }
    public void setHint (String hint){
        editText.setHint(hint);
    }
    public void setHint (int res){
        editText.setHint(res);
    }
}
