package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.EaseSmileUtils;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;
    private ImageView imageViewEmoji;
    private RecyclerView recyclerEnmoji;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
        imageViewEmoji = (ImageView) findViewById(R.id.imageViewEmoji);
        recyclerEnmoji = findViewById(R.id.recyclerEnmoji);
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();message.ext();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());

        //判断是否存在emoji
        int emoRes = EmojiParse.isHaveThis(txtBody.getMessage());
        if((message.ext().get("em_expression_id")!=null)){
            emoRes = EmojiParse.isHaveThis(message.ext().get("em_expression_id").toString());
        }
//        int emoRes = EmojiParse.isHaveThis(message.ext().get("em_expression_id").toString());
        if (emoRes != 0) {
            imageViewEmoji.setVisibility(VISIBLE);
            contentView.setVisibility(GONE);
            Glide.with(context).load(emoRes).into(imageViewEmoji);
        } else {
            imageViewEmoji.setVisibility(GONE);
            contentView.setText(span, TextView.BufferType.SPANNABLE);
            contentView.setVisibility(VISIBLE);
        }

//        // todo 设置超级链接
//        URLSpan url01 = new URLSpan("https://www.baidu.com/");
//        URLSpan url02 = new URLSpan("http://www.qq.com/");
//        span.setSpan(url01, 0, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        span.setSpan(url02, 11, 12, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        contentView.setText(span);
//        contentView.setMovementMethod(LinkMovementMethod.getInstance());//打开超链接的方法
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        switch (msg.status()) {
            case CREATE:
                onMessageCreate();
                break;
            case SUCCESS:
                onMessageSuccess();
                break;
            case FAIL:
                onMessageError();
                break;
            case INPROGRESS:
                onMessageInProgress();
                break;
        }
    }

    private void onMessageCreate() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageSuccess() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageError() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);
    }

    private void onMessageInProgress() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }





}
