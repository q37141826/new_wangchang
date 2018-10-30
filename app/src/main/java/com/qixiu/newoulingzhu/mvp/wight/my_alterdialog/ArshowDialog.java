package com.qixiu.newoulingzhu.mvp.wight.my_alterdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixiu.wanchang.R;


public class ArshowDialog extends Dialog {
    public ArshowDialog(Context context) {
        super(context);
    }

    public ArshowDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; // 上下文对象
        private String title = "提示"; // 对话框标题
        private String message = ""; // 对话框内容
        private String confirm_btnText; // 按钮名称“确定”
        private String cancel_btnText; // 按钮名称“取消”
        private String neutral_btnText; // 按钮名称“隐藏”
        private View contentView; // 对话框中间加载的其他布局界面
        /* 按钮坚挺事件 */
        private OnClickListener confirm_btnClickListener;
        private OnClickListener cancel_btnClickListener;
        private OnClickListener neutral_btnClickListener;
        private TextView tv_msg;
        private TextView tv_title;
        private int gravity = Gravity.CENTER;
        private int viewWidth = LayoutParams.WRAP_CONTENT;
        private int viewHeight = LayoutParams.WRAP_CONTENT;
        private ArshowDialog dialog;
        private int type;
        private String name;

        public Builder(Context context) {
            this.context = context;
            dialog = new ArshowDialog(context, R.style.DialogTheme);

        }

        public boolean isShowing() {
            if (dialog != null) {
                return isShowing();
            }
            return false;
        }

        public void setState(int type) {
            this.type = type;
        }

        /* 设置对话框信息 */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置对话框界面
         *
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(int confirm_btnText, OnClickListener listener) {
            this.confirm_btnText = (String) context.getText(confirm_btnText);
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the positive button and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(String confirm_btnText, OnClickListener listener) {
            this.confirm_btnText = confirm_btnText;
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton(int cancel_btnText, OnClickListener listener) {
            this.cancel_btnText = (String) context.getText(cancel_btnText);
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button and it's listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton(String cancel_btnText, OnClickListener listener) {
            this.cancel_btnText = cancel_btnText;
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the netural button resource and it's listener
         *
         * @param neutral_btnText
         * @return
         */
        public Builder setNeutralButton(int neutral_btnText, OnClickListener listener) {
            this.neutral_btnText = (String) context.getText(neutral_btnText);
            this.neutral_btnClickListener = listener;
            return this;
        }

        /**
         * Set the netural button and it's listener
         *
         * @param neutral_btnText
         * @return
         */
        public Builder setNeutralButton(String neutral_btnText, OnClickListener listener) {
            this.neutral_btnText = neutral_btnText;
            this.neutral_btnClickListener = listener;
            return this;
        }

        public ArshowDialog create() {
            LinearLayout ll_dialog = (LinearLayout) View.inflate(context, R.layout.arshow_dialog,
                    null);
            tv_title = ((TextView) ll_dialog.findViewById(R.id.tv_title));
            tv_title.getPaint().setFakeBoldText(true);
            tv_title.setGravity(Gravity.CENTER);
            tv_msg = ((TextView) ll_dialog.findViewById(R.id.tv_message));
            tv_msg.setGravity(gravity);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            //设置空间大小
            int marginTop = ArshowContextUtil.dp2px(25);
            int marginLeft = ArshowContextUtil.dp2px(15);
            params.setMargins(marginLeft, marginTop, marginLeft, marginTop);
            tv_msg.setLayoutParams(params);
            if (TextUtils.isEmpty(title))
                tv_title.setVisibility(View.GONE);
            tv_title.setText(title);
            if (!TextUtils.isEmpty(name)) {
                String data = message + "  " + name;
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
                        data);
                stringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.txt_737373)), 0, message.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.txt_01C956)), message.length(), data.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_msg.setText(stringBuilder);
            } else {
                tv_msg.setText(message);
            }
            if (neutral_btnText != null && confirm_btnText != null && cancel_btnText != null) {
                ((TextView) ll_dialog.findViewById(R.id.tv_confirm)).setText(confirm_btnText);
                ((TextView) ll_dialog.findViewById(R.id.tv_neutral)).setText(neutral_btnText);
                if (neutral_btnClickListener != null) {
                    (ll_dialog.findViewById(R.id.tv_neutral)).setOnClickListener(
                            new View.OnClickListener() {
                                public void onClick(View v) {
                                    neutral_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEUTRAL);
                                }
                            });
                } else {
                    (ll_dialog.findViewById(R.id.tv_neutral)).setOnClickListener(
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                ll_dialog.findViewById(R.id.tv_neutral).setVisibility(View.GONE);
                ll_dialog.findViewById(R.id.single_line).setVisibility(View.GONE);
                if (type == 1) {
                    ((TextView) ll_dialog.findViewById(R.id.tv_confirm)).setTextColor(
                            context.getResources().getColor(R.color.txt_FD5865));
                }
            }
            if (confirm_btnText != null) {
                ((TextView) ll_dialog.findViewById(R.id.tv_confirm)).setText(confirm_btnText);
                if (confirm_btnClickListener != null) {
                    (ll_dialog.findViewById(R.id.tv_confirm)).setOnClickListener(
                            new View.OnClickListener() {
                                public void onClick(View v) {
                                    confirm_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                } else {
                    (ll_dialog.findViewById(R.id.tv_confirm)).setOnClickListener(
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                ll_dialog.findViewById(R.id.tv_confirm).setVisibility(View.GONE);
                ll_dialog.findViewById(R.id.second_line).setVisibility(View.GONE);
                ll_dialog.findViewById(R.id.tv_cancel).setBackgroundResource(
                        R.drawable.single_btn_select);
            }
            if (cancel_btnText != null) {
                ((TextView) ll_dialog.findViewById(R.id.tv_cancel)).setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    (ll_dialog.findViewById(R.id.tv_cancel)).setOnClickListener(
                            new View.OnClickListener() {
                                public void onClick(View v) {
                                    cancel_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    (ll_dialog.findViewById(R.id.tv_cancel)).setOnClickListener(
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                ll_dialog.findViewById(R.id.tv_cancel).setVisibility(View.GONE);
                ll_dialog.findViewById(R.id.second_line).setVisibility(View.GONE);
                ll_dialog.findViewById(R.id.tv_confirm).setBackgroundResource(
                        R.drawable.single_btn_select);
            }
            dialog.getWindow().setContentView(ll_dialog);
            dialog.getWindow().setLayout(ArshowContextUtil.dp2px(250), viewHeight);
            return dialog;
        }

        public Builder setCancelable(boolean flag) {
            dialog.setCancelable(flag);
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            dialog.setCanceledOnTouchOutside(false);
            return this;
        }

        public ArshowDialog show() {
            gravity = Gravity.CENTER;
            ArshowDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public ArshowDialog show(int position) {
            gravity = position;
            ArshowDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public TextView getTv_msg() {
            return tv_msg;
        }

        public TextView getTv_title() {
            return this.tv_title;
        }

    }
}
