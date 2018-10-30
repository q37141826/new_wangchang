package com.qixiu.newoulingzhu.mvp.view.adapter.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.beans.messge.MessageListBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by my on 2018/8/17.
 */

public class MessageAdapter extends RecyclerBaseAdapter {

    @Override
    public int getLayoutId() {
        return R.layout.item_message_chat;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new MessageHolder(itemView, context, this);
    }


    public class MessageHolder extends RecyclerBaseHolder {
        CircleImageView circularChatMessage;
        TextView textViewChatMessageTypeName;
        RelativeLayout relativeLayoutContent;
        TextView textViewChatMessageContent, textViewTimeChatMessage,tv_message_count;

        public MessageHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            circularChatMessage=itemView.findViewById(R.id.circularChatMessage);
            textViewChatMessageTypeName=itemView.findViewById(R.id.textViewChatMessageTypeName);
            relativeLayoutContent=itemView.findViewById(R.id.relativeLayoutContent);
            textViewChatMessageContent=itemView.findViewById(R.id.textViewChatMessageContent);
            textViewTimeChatMessage=itemView.findViewById(R.id.textViewTimeChatMessage);
            tv_message_count=itemView.findViewById(R.id.tv_message_count);
        }

        @Override
        public void bindHolder(int position) {
                if(mData instanceof MessageListBean.EMessageInfo.ListBean){
                    MessageListBean.EMessageInfo.ListBean listBean= ( MessageListBean.EMessageInfo.ListBean) mData;
                    textViewChatMessageTypeName.setText(listBean.getTitle());
                    textViewTimeChatMessage.setText(listBean.getAddtime());
                    textViewChatMessageContent.setText(listBean.getProblem());
                    Glide.with(mContext).load(listBean.getAvatar()).into(circularChatMessage);
                    if (!TextUtils.isEmpty(listBean.getNum())) {
                        String num = listBean.getNum();
                        int parseInt = Integer.parseInt(num);
                        if (parseInt > 0) {
                            tv_message_count.setVisibility(View.VISIBLE);
                        } else {
                            tv_message_count.setVisibility(View.GONE);
                        }
                        if (parseInt > 99) {
                            tv_message_count.setText(99 + "");
                        } else {
                            tv_message_count.setText(num);
                        }
                    } else {
                        tv_message_count.setVisibility(View.GONE);
                    }
                }
        }
    }
}
