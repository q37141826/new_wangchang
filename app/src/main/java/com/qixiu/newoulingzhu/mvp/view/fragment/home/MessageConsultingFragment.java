package com.qixiu.newoulingzhu.mvp.view.fragment.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.application.LoginStatus;
import com.qixiu.newoulingzhu.beans.messge.MessageListBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.mvp.view.adapter.message.MessageAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.wight.loading.RefreshHeader;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowDialog;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.XrecyclerViewUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.OnRecyclerItemLongListener;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

import static com.hyphenate.easeui.bean.ConstantString.CONSULTING_ID;
import static com.qixiu.newoulingzhu.constant.ConstantUrl.deleteUrl;

/**
 * Created by my on 2018/8/13.
 */

public class MessageConsultingFragment extends BaseFragment implements OKHttpUIUpdataListener, XRecyclerView.LoadingListener, OnRecyclerItemListener, OnRecyclerItemLongListener {
    public static final int ON_THE_WAY = 1;
    public static final int FINISHED = 2;
    XRecyclerView recyclerChatOngoing;
    //    SwipeRefreshLayout swipRefreshMessageChat;
    private int type;//1咨询中的消息；2已结束咨询的消息；默认1
    private int pageNo = 1, pageSize = 1000;
    ImageView imageViewNothing;
    private OKHttpRequestModel okHttpRequestModel;
    MessageAdapter adapter;
    private MessageListBean bean;
    //未读消息监听
    private OnUpdataMessageCountListener onUpdataMessageCountListener;
    private ZProgressHUD zProgressHUD;
    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void onInitData() {
        zProgressHUD=new ZProgressHUD(getContext());
        okHttpRequestModel = new OKHttpRequestModel(this);
//        swipRefreshMessageChat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageNo = 1;
//                requestData();
//            }
//        });
        adapter.setOnItemClickListener(this);
        adapter.setOnLongItemClickListener(this);
    }

    private void setDialog(String message, final MessageListBean.EMessageInfo.ListBean bean) {
        ArshowDialog.Builder builder = new ArshowDialog.Builder(mContext);
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> map = new HashMap<>();
                map.put("problemId", bean.getId());
                map.put("uid", Preference.get(ConstantString.USERID, ""));
                okHttpRequestModel.okhHttpPost(deleteUrl, map, new BaseBean<>());
                dialog.dismiss();
            }
        });

        builder.setMessage(message);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void requestData() {
        Map<String, String> map = new HashMap();
        map.put("uid", LoginStatus.getToken());
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        map.put("doing", type + "");
        zProgressHUD.show();
        okHttpRequestModel.okhHttpPost(ConstantUrl.messageList, map, new MessageListBean());
    }

    @Override
    protected void onInitView(View view) {
        imageViewNothing = findViewById(R.id.imageViewNothing);
//        swipRefreshMessageChat = view.findViewById(R.id.swipRefreshMessageChat);
        recyclerChatOngoing = view.findViewById(R.id.recyclerChatOngoing);
        adapter = new MessageAdapter();
        XrecyclerViewUtil.setXrecyclerView(recyclerChatOngoing, this, getContext(), true, 1, null);
        recyclerChatOngoing.setAdapter(adapter);
        RefreshHeader refreshHeader = new RefreshHeader(getContext());
        refreshHeader.setArrowImageView(R.mipmap.refresh_head_arrow);
        recyclerChatOngoing.setRefreshHeader(refreshHeader);
//        swipRefreshMessageChat.setColorSchemeResources(R.color.theme_color);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message_consilting;
    }


    @Override
    public void onStart() {
        super.onStart();
        /*    EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();//上面是加载了所有群组和对话，但是下面才是加载所有消息
        * */
//        Map<String, EMConversation> allConversations = HyEngine.getAllConversations();//
//        Set<String> strings = allConversations.keySet();
//        for (String key : strings) {
//            onConversationInit(allConversations.get(key));
//        }
        requestData();
    }

//    protected void onConversationInit(EMConversation conversation) {
//        int pagesize = 20;
//        final List<EMMessage> msgs = conversation.getAllMessages();
//        int msgCount = msgs != null ? msgs.size() : 0;
//        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
//            String msgId = null;
//            if (msgs != null && msgs.size() > 0) {
//                msgId = msgs.get(0).getMsgId();
//            }
//            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
//        }
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        requestData();
    }

    @Override
    public void onLoadMore() {

    }


    @Override
    public void onSuccess(Object data, int i) {
        zProgressHUD.dismiss();
        BaseBean baseBean = (BaseBean) data;
//        swipRefreshMessageChat.setRefreshing(false);
        recyclerChatOngoing.loadMoreComplete();
        recyclerChatOngoing.refreshComplete();
        if (data instanceof MessageListBean) {
            bean = (MessageListBean) data;
            /*
            * 测试数据
            * */
            MessageListBean.EMessageInfo.ListBean testBean = new MessageListBean.EMessageInfo.ListBean();
            testBean.setGroup_id("62660690378753");
            testBean.setId("610");
            testBean.setLawyer("68");
            testBean.setAddtime("20160927");
            testBean.setNum("0");
            testBean.setProblem("测试死wenti");
            testBean.setTitle("测试标题");
            testBean.setAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3130907200,2318148153&fm=27&gp=0.jpg");
//            bean.getE().getList().add(testBean);//todo 发布版本的时候把这个关掉
             /*
            * --------------------
            * */
            if (pageNo == 1) {
                adapter.refreshData(bean.getE().getList());
            } else {
                adapter.addDatas(bean.getE().getList());
            }
            if(Preference.getBoolean(ConstantString.IS_GROUP)){
                refreshMessageListFristCountGroup();
            }else {
                refreshMessageListFristCount();
            }
        }
        if (baseBean.getUrl().equals(deleteUrl)) {
            onRefresh();
        }
        if (adapter.getDatas().size() == 0) {
            imageViewNothing.setVisibility(View.VISIBLE);
        } else {
            imageViewNothing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
//        swipRefreshMessageChat.setRefreshing(false);
        recyclerChatOngoing.loadMoreComplete();
        recyclerChatOngoing.refreshComplete();
        zProgressHUD.dismiss();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
//        swipRefreshMessageChat.setRefreshing(false);
        recyclerChatOngoing.loadMoreComplete();
        recyclerChatOngoing.refreshComplete();
        zProgressHUD.dismiss();
    }

    private void refreshMessageListFristCount() {
        int allUnread = 0;
        List<MessageListBean.EMessageInfo.ListBean> datas = adapter.getDatas();
        final Map<String, EMConversation> allConversations = HyEngine.getAllConversations();//本地所有对话
        Log.e("unreadAll", HyEngine.getUnReadMsgCount() + "");
        Set<String> strings = allConversations.keySet();
        List<EMMessage> messages = new ArrayList<>();
        if (datas != null && datas.size() > 0) {
            for (final String key : strings) {
                messages.addAll(allConversations.get(key).getAllMessages());
            }
            for (int i = 0; i < datas.size(); i++) {
                int unread = 0;
                long lastTime = 0;
                for (int j = 0; j < messages.size(); j++) {
                    if (messages.get(j).ext().get(CONSULTING_ID) != null && (messages.get(j).ext().get(CONSULTING_ID) + "").equals(datas.get(i).getId())) {//
                        if (messages.get(j).isUnread()) {
                            unread++;
                            allUnread++;
                        }
                        lastTime = messages.get(j).getMsgTime();
                        datas.get(i).setLastMessageTime(lastTime);
                        if (messages.get(j).getMsgTime() > lastTime) {
                            lastTime = messages.get(j).getMsgTime();
                            datas.get(i).setLastMessageTime(lastTime);
                        }
                    }

                }
                datas.get(i).setNum(unread + "");
            }
            //按照最后时间进行一下冒泡顺序
            arrayList(adapter.getDatas());
            adapter.notifyDataSetChanged();
        }
        MainActivity.unreadMsgCount = allUnread;
        if (onUpdataMessageCountListener != null) {
            onUpdataMessageCountListener.onUpdataMessageCount();
        }
    }

    public void refreshMessageListFristCountGroup() {
        int allUnread = 0;
        HyEngine.loadAllConversationAndMessage();
        List<MessageListBean.EMessageInfo.ListBean> datas = adapter.getDatas();
        final Map<String, EMConversation> allConversations = HyEngine.getAllConversations();//本地所有对话
        Set<String> strings = allConversations.keySet();
        if (datas != null && datas.size() > 0) {
            for (String key : strings) {
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getGroup_id().equals(key)) {
                        datas.get(i).setNum(allConversations.get(key).getUnreadMsgCount() + "");
                        allUnread += allConversations.get(key).getUnreadMsgCount();
                        long lastTime = allConversations.get(key).getLastMessage().getMsgTime();
                        datas.get(i).setLastMessageTime(lastTime);
                    }
                }
            }
        }
        MainActivity.unreadMsgCount = allUnread;
        if (onUpdataMessageCountListener != null) {
            onUpdataMessageCountListener.onUpdataMessageCount();
        }
        //按照最后时间进行一下冒泡顺序
        arrayList(adapter.getDatas());
        adapter.notifyDataSetChanged();
    }

    private List<MessageListBean.EMessageInfo.ListBean> arrayList(List<MessageListBean.EMessageInfo.ListBean> datas) {
        for (int i = 0; i < datas.size() - 1; i++) {
            for (int j = 0; j < datas.size() - 1 - i; j++) {
                if (datas.get(j).getLastMessageTime() < datas.get(j + 1).getLastMessageTime()) {
                    MessageListBean.EMessageInfo.ListBean dataTemp = datas.get(j + 1);
                    datas.remove(j + 1);
                    datas.add(j, dataTemp);
                }
            }
        }
        return datas;
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof MessageListBean.EMessageInfo.ListBean) {
            MessageListBean.EMessageInfo.ListBean listBean = (MessageListBean.EMessageInfo.ListBean) data;
            String lsId = "ls" + listBean.getLawyer();
            Preference.put(ConstantString.OTHER_HEAD, listBean.getAvatar());
            Preference.put(ConstantString.OTHER_NAME, listBean.getName());
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.PRO_ID, listBean.getId());
            bundle.putString(EaseConstant.TOCHAT_NAME, listBean.getName());
            bundle.putString(EaseConstant.PROBLEM, listBean.getProblem() == null ? listBean.getTitle() : listBean.getProblem());
            bundle.putString(EaseConstant.TYPE, listBean.getType());
            if (HyEngine.isLogin()) {
                if (Preference.getBoolean(ConstantString.IS_GROUP)) {
                    HyEngine.startConversationGroup(getActivity(), listBean.getGroup_id(), bundle, ChatActivity.class);
                } else {
                    HyEngine.startConversationSingle(getActivity(), lsId, bundle, ChatActivity.class);
                }
//                HyEngine.startConversationSingle(getActivity(), lsId, bundle, ChatActivity.class);
            } else {
                HyEngine.login(getActivity(), Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING), true, lsId, bundle);
                Log.d("LOGCAT", "userId" + Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
            }
        }
    }

    @Override
    public void onItemLongClick(View v, Object data) {
        if (data instanceof MessageListBean.EMessageInfo.ListBean && type == 2) {
            MessageListBean.EMessageInfo.ListBean bean = (MessageListBean.EMessageInfo.ListBean) data;
            setDialog("确认删除该消息？", bean);
        }
    }


    public interface OnUpdataMessageCountListener {
        void onUpdataMessageCount();
    }

    public void setOnUpdataMessageCountListener(OnUpdataMessageCountListener onUpdataMessageCountListener) {
        this.onUpdataMessageCountListener = onUpdataMessageCountListener;
    }
}
