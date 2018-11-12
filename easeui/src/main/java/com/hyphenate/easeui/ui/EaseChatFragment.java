package com.hyphenate.easeui.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.bean.ConsultingId;
import com.hyphenate.easeui.bean.UploadFileBean;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu.ChatInputMenuListener;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.chatrow.EmojiParse;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.FileProviderUtil;
import com.qixiu.qixiu.utils.PictureCut;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_GROUP;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_EXPRESSION_ID;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION;
import static com.hyphenate.easeui.bean.ConstantString.CONSULTING_ID;
import static com.hyphenate.easeui.bean.ConstantString.IS_GROUP;

/**
 * you can new an EaseChatFragment to use or you can inherit it to expand.
 * You need call setArguments to pass chatType and userId
 * <br/>
 * <br/>
 * you can see ChatActivity in demo for your reference
 */
public class EaseChatFragment extends EaseBaseFragment implements EMMessageListener, EMCallBack, OKHttpUIUpdataListener, OnClickListener {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    public static final String MESSAGE_SUCCESS_BROADCAST = "message_success_broadcast";
    public static final String EESSAGE_KEY = "eessage_key";
    private String hostGroupUrl = "http://sk.qixiuu.com/";
    private String hostSingleUrl = "http://wc.qixiuu.com/";
    private String addMessageRecordUrl = "http://sk.qixiuu.com/" + "api/Homepage/log";//todo  记得改正这个地方的域名
//    private String addMessageRecordUrl = "http://wc.qixiuu.com/" + "api/Homepage/log";//todo  记得改正这个地方的域名
    /**
     * params to fragment
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername;
    protected EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;

    protected EMConversation conversation;

    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;

    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;
    protected ChatRoomListener chatRoomListener;
    protected EMMessage contextMenuMessage;

    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;

    protected int[] itemStrings = {R.string.attach_take_pic, R.string.attach_picture};//, R.string.attach_location
    protected int[] itemdrawables = {R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector};//,R.drawable.ease_chat_location_selector
    protected int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE};//, ITEM_LOCATION
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    protected boolean isRoaming = false;
    private ExecutorService fetchQueue;
    private BaseReceiver mBaseReceiver;
    private OKHttpRequestModel mOkHttpRequestModel;
    private String mUid;
    private String mLawyer;
    private String mPro_id;
    private String mTochatName;
    private OnClickChatFragmentListener mOnClickChatFragmentListener;
    private String mProblem;
    private TextView mTv_issue_content;
    private String mType;

    private RelativeLayout ll_chat_titledetail;


    //输入状态改变的时候发送的两个变量
    int time = 3000;//按照自己的需求自定义时间间隔发送状态消息（单位：ms）
    long firstTime = System.currentTimeMillis();//文本框第一次输入内容变化的时间

    //
    final String TYPING_BEGIN = "TypingBegin", TYPING_END = "TypingEnd";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (IS_GROUP) {
            addMessageRecordUrl = hostGroupUrl + "api/Homepage/log";
        } else {
            addMessageRecordUrl = hostSingleUrl + "api/Homepage/log";
        }
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean roaming) {
        isRoaming = roaming;
        if (IS_GROUP) {
            addMessageRecordUrl = hostGroupUrl + "api/Homepage/log";
        } else {
            addMessageRecordUrl = hostSingleUrl + "api/Homepage/log";
        }
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    private void registerMessageOnSuccessBroadcast() {
        ConsultingId.setConsultingId(mPro_id);
//        ConsultingId.setConsultingId("102");
        mBaseReceiver = new BaseReceiver(this);
        mBaseReceiver.setTochatUsername(toChatUsername);
        getActivity().registerReceiver(mBaseReceiver, new IntentFilter(MESSAGE_SUCCESS_BROADCAST));
    }

    private void destroyMessageOnSuccessBroadcast() {
        if (mBaseReceiver != null) {
            getActivity().unregisterReceiver(mBaseReceiver);
        }

    }

    public interface OnClickChatFragmentListener {

        void onClickIssueTitle(String pro_id);

        void onClickComplete(String pro_id);

    }

    public void setOnClickFragmentListener(OnClickChatFragmentListener mOnClickChatFragmentListener) {
        this.mOnClickChatFragmentListener = mOnClickChatFragmentListener;
    }

    @Override
    public void onSuccess(Object data, int i) {

    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(com.qixiu.qixiu.request.bean.C_CodeBean c_codeBean) {

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_issue_title) {
            if (mOnClickChatFragmentListener != null) {
                mOnClickChatFragmentListener.onClickIssueTitle(mPro_id);
            }
        }
    }

    private class BaseReceiver extends BroadcastReceiver {
        private String tochatUsername;

        public void setTochatUsername(String tochatUsername) {
            this.tochatUsername = tochatUsername;
        }

        private final WeakReference<EaseChatFragment> mCommunicationWeakReference;

        public BaseReceiver(EaseChatFragment communication) {

            mCommunicationWeakReference = new WeakReference<>(communication);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            EaseChatFragment communication = mCommunicationWeakReference.get();
            if (communication != null && intent != null && MESSAGE_SUCCESS_BROADCAST.equals(intent.getAction())) {
                EMMessage emMessage = intent.getParcelableExtra(EESSAGE_KEY);
//                communication.requestAddMessage(emMessage);
                if (emMessage != null) {
//                    if (emMessage.getUserName().equals(tochatUsername)) {
//                        mTitleView.setTitle("正在输入中...");
//                    }
                } else {
                    if (!TextUtils.isEmpty(mTochatName)) {
                        mTitleView.setTitle(mTochatName.replace("律师", ""));
                    }
                }
            }
        }
    }

    @Subscribe
    public void onEventMainThread(EMMessage emMessage) {
        if (emMessage != null) {
            if ((emMessage.getTo().equals(toChatUsername) && !mUid.equals(emMessage.getFrom()))||emMessage.getTo().equals(mUid)) {
                if (emMessage.getBody() instanceof EMCmdMessageBody) {
                    EMCmdMessageBody body = (EMCmdMessageBody) emMessage.getBody();
                    String action = body.action();
                    showInputState(action);
                }
            }
        } else {
            if (!TextUtils.isEmpty(mTochatName)) {
                mTitleView.setTitle(mTochatName.replace("律师", ""));
            }
        }
    }

    private void showInputState(String action) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (action.equals(TYPING_BEGIN)) {
                    mTitleView.setTitle("正在输入中...");
                } else {
                    mTitleView.setTitle(mTochatName.replace("律师", ""));
                }
            }
        });
    }

    public void requestAddMessage(EMMessage emMessage) {
        if (TextUtils.isEmpty(mUid) || TextUtils.isEmpty(mPro_id) || TextUtils.isEmpty(mLawyer) || emMessage == null) {
            return;
        }
        String messageContent = "";
        EMMessageBody body = emMessage.getBody();
        Map<String, String> stringMap = new HashMap<>();
        if (body instanceof EMTextMessageBody) {
            stringMap.put("texttype", "1");
            EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
            messageContent = emTextMessageBody.getMessage();
        } else if (body instanceof EMImageMessageBody) {
            EMImageMessageBody emImageMessageBody = (EMImageMessageBody) body;
            messageContent = emImageMessageBody.getRemoteUrl();
            stringMap.put("texttype", "2");
        }
        if (TextUtils.isEmpty(messageContent)) {
            return;
        }
        Log.d("LOGCAT", "messageContent:" + messageContent);

        stringMap.put("uid", mUid);
        stringMap.put("lawyer", mLawyer.replace("ls", ""));
        stringMap.put("pro_id", mPro_id);
        stringMap.put("text", messageContent);
        stringMap.put("type", "1");
        mOkHttpRequestModel.okhHttpPost(addMessageRecordUrl, stringMap, new BaseBean());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        fragmentArgs = getArguments();
        // check if single chat or group chat
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // userId you are chat with or group id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);

        mTochatName = fragmentArgs.getString(EaseConstant.TOCHAT_NAME, EaseConstant.STRING_EMPTY);
        mUid = fragmentArgs.getString(EaseConstant.CURRENT_UID, EaseConstant.STRING_EMPTY);
        mLawyer = toChatUsername;
        mPro_id = fragmentArgs.getString(EaseConstant.PRO_ID, EaseConstant.STRING_EMPTY);
        //问题ID如果是空的 那么就隐藏上面的版块
        ll_chat_titledetail = (RelativeLayout) getView().findViewById(R.id.ll_chat_titledetail);
        if (TextUtils.isEmpty(mPro_id) || mPro_id.equals("0")) {
            ll_chat_titledetail.setVisibility(View.GONE);
        }
        mProblem = fragmentArgs.getString(EaseConstant.PROBLEM, EaseConstant.STRING_EMPTY);
        mType = fragmentArgs.getString(EaseConstant.TYPE, EaseConstant.STRING_EMPTY);
        super.onActivityCreated(savedInstanceState);
        if (mOkHttpRequestModel == null) {
            mOkHttpRequestModel = new OKHttpRequestModel(this);
        }

        registerMessageOnSuccessBroadcast();
        //注册输入状态监听

    }

    /**
     * init view
     */
    protected void initView() {
        EventBus.getDefault().register(this);
        // hold to record voice
        //noinspection ConstantConditions
        voiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);

        // message list layout
        messageList = (EaseChatMessageList) getView().findViewById(R.id.message_list);
        if (chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
//        messageList.setAvatarShape(1);
        listView = messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);

        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        if (isRoaming) {
            fetchQueue = Executors.newSingleThreadExecutor();
        }

        // getActivity().registerReceiver()
        View rl_issue_title = getView().findViewById(R.id.rl_issue_title);
        rl_issue_title.setOnClickListener(this);
        mTv_issue_content = (TextView) getView().findViewById(R.id.tv_issue_content);
        mTv_issue_content.setText("问题描述 : " + mProblem);
//        EMClient.getInstance().chatManager().markAllConversationsAsRead();//邓拼搏  本来默认是一旦进入就要把所有消息设置成已读的，但是因为一个对话被需求拆分成了两部分，所以这个地方就不标记了
        setCompleteView(mType);

        NotInputRunnable runnable = new NotInputRunnable();
        Handler handler = new Handler();
        //监听输入状态，让对方知道自己正在输入
        inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (System.currentTimeMillis() - firstTime > time) {
                    sendInputmsg(TYPING_BEGIN);
                    firstTime = System.currentTimeMillis();
                }
                handler.postDelayed(runnable, 5000);
                //todo 上面是测试  发送输入状态和结束输入状态的测试代码，正式环境关闭它
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("state", "after");
            }
        });
    }

    //延时发送正在输入结束的状态
    public class NotInputRunnable implements Runnable {
        @Override
        public void run() {
            if (System.currentTimeMillis() - firstTime > time) {
                sendInputmsg(TYPING_END);
            }
        }
    }


    private void sendInputmsg(String action) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP) {
            cmdMsg.setChatType(ChatType.GroupChat);
        }
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        // 设置消息body
        cmdMsg.addBody(cmdBody);
        // 设置要发给谁,用户username或者群聊groupid
        cmdMsg.setFrom(mUid);//群聊状态 要从群ID出发  回归到这个群ID
        cmdMsg.setTo(toChatUsername);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }


    public void setCompleteView(String type) {
        mType = type;
        if (!TextUtils.isEmpty(mPro_id) && !mPro_id.equals("0")) {
            mTitleView.setRightText("结束");
        } else {
            mTitleView.setRightText("");
        }
        if (!TextUtils.isEmpty(type) && "2".equals(type)) {
            inputMenu.setVisibility(View.GONE);
            View tv_complete = getView().findViewById(R.id.tv_complete);
            tv_complete.setVisibility(View.VISIBLE);
            mTitleView.setRightTextVisible(View.GONE);
        } else {
            mTitleView.setRightTextVisible(View.VISIBLE);
        }

    }

    protected void setUpView() {
        //    titleBar.setTitle(toChatUsername);
        if (!TextUtils.isEmpty(mTochatName)) {
            mTitleView.setTitle(mTochatName.replace("律师", ""));
        }

        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
            if (EaseUserUtils.getUserInfo(toChatUsername) != null) {
                EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
                if (user != null) {
                    titleBar.setTitle(user.getNick());
                }
            }
            // titleBar.setRightImageResource(R.drawable.ease_mm_title_remove);
        } else {
            //   titleBar.setRightImageResource(R.drawable.ease_to_group_details_normal);
            if (chatType == CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)

                    //    titleBar.setTitle(group.getGroupName());
//                    mTitleView.setTitle(group.getGroupName());
                    mTitleView.setTitle(mTochatName.replace("律师", ""));
                // listen the event that user moved out group or group is dismissed
                groupListener = new GroupListener();
                EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
            } else {
                chatRoomListener = new ChatRoomListener();
                EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomListener);
                onChatRoomViewCreation();
            }

        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }

//        titleBar.setLeftLayoutClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        mTitleView.setBackListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mTitleView.setRightListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
//                    emptyHistory();
//                } else {
//                    toGroupDetails();
//                }
                if (mOnClickChatFragmentListener != null) {
                    mOnClickChatFragmentListener.onClickComplete(mPro_id);
                }
            }
        });
//        titleBar.setRightLayoutClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
//                    emptyHistory();
//                } else {
//                    toGroupDetails();
//                }
//            }
//        });

        setRefreshLayoutListener();

        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }


    protected void onConversationInit() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
//        conversation.markAllMessagesAsRead();//邓拼搏  本来默认是一旦进入就要把所有消息设置成已读的，但是因为一个对话被需求拆分成了两部分，所以这个地方就不标记了
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number

        if (!isRoaming&&conversation!=null) {
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            }
        } else {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, "");
                        final List<EMMessage> msgs = conversation.getAllMessages();
                        int msgCount = msgs != null ? msgs.size() : 0;
                        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                            String msgId = null;
                            if (msgs != null && msgs.size() > 0) {
                                msgId = msgs.get(0).getMsgId();
                            }
                            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
                        }
                        messageList.refreshSelectLast();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected void onMessageListInit() {
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();

        messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });

        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }
                Log.e("message", message.getBody().toString());
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
                if (message.getType().equals(EMMessage.Type.TXT)) {
                    String content = ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage();
                    ClipData clipData = ClipData.newPlainText(null, content);
                    // 把数据集设置（复制）到剪贴板
                    clipboard.setPrimaryClip(clipData);
                    Toast.makeText(getContext(), "消息已经复制", Toast.LENGTH_SHORT).show();
                } else if (message.getType().equals(EMMessage.Type.IMAGE)) {
                    String path = ((EMImageMessageBody) contextMenuMessage.getBody()).getLocalUrl();
//                    Uri uri= FileProviderUtil.getUriForFile(getContext(),new File((url)));
//                    ClipData clipData = ClipData.newRawUri(null,  uri);
//                    // 把数据集设置（复制）到剪贴板
//                    clipboard.setPrimaryClip(clipData);
                    try {
                        Log.e("path", path);
                        FileProviderUtil.copyFileUsingFileChannels(new File(path), new File(PictureCut.creatPath(), PictureCut.getCode(path) + ".jpg"));
                        Toast.makeText(getContext(), "图片保存到本地", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if (chatFragmentHelper == null) {
                    return false;
                }//这个地方的点击事件
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isRoaming) {
                            loadMoreLocalMessage();
                        } else {
                            loadMoreRoamingMessages();
                        }
                    }
                }, 600);
            }
        });
    }

    private void loadMoreLocalMessage() {
        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
            List<EMMessage> messages;
            try {
                messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(),
                        pagesize);
            } catch (Exception e1) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (messages.size() > 0) {
                messageList.refreshSeekTo(messages.size() - 1);
                if (messages.size() != pagesize) {
                    haveMoreData = false;
                }
            } else {
                haveMoreData = false;
            }

            isloading = false;
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadMoreRoamingMessages() {
        if (!haveMoreData) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (fetchQueue != null) {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<EMMessage> messages = conversation.getAllMessages();
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize,
                                (messages != null && messages.size() > 0) ? messages.get(0).getMsgId() : "");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } finally {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreLocalMessage();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // location
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

        if (chatType == CHATTYPE_GROUP) {
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }

        if (chatRoomListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomListener(chatRoomListener);
        }

        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }
        destroyMessageOnSuccessBroadcast();
        EventBus.getDefault().unregister(this);
    }

    public void onBackPressed() {
        if (inputMenu.onBackPressed()) {
            getActivity().finish();
            if (chatType == CHATTYPE_GROUP) {
                EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
                EaseAtMessageHelper.get().cleanToAtUserList();
            }
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Joining......");
        EMClient.getInstance().chatroomManager().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity().isFinishing() || !toChatUsername.equals(value.getId()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        if (room != null) {
                            titleBar.setTitle(room.getName());
                            EMLog.d(TAG, "join room success : " + room.getName());
                        } else {
                            titleBar.setTitle(toChatUsername);
                        }
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                getActivity().finish();
            }
        });
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        Log.d("LOGCAT", "onMessageReceived");
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
                messageList.refreshSelectLast();
//                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);//这个是环信去提示系统的声音和消息，但是这个现在要根据后台的判断才行
//                conversation.markMessageAsRead(message.getMsgId());//邓拼搏  本来默认是一旦进入就要把所有消息设置成已读的，但是因为一个对话被需求拆分成了两部分，所以这个地方就不标记了
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        Log.d("LOGCAT", "onCmdMessageReceived");
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        Log.d("LOGCAT", "onMessageRead");
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        Log.d("LOGCAT", "onMessageDelivered");
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        Log.d("LOGCAT", "onMessageRecalled");
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        Log.d("LOGCAT", "onMessageChanged");
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onSuccess() {
        Log.e("LOGCAT", "发送的消息:onSuccess");
    }

    @Override
    public void onError(int i, String s) {
        Log.e("LOGCAT", "发送的消息:onError");
    }

    @Override
    public void onProgress(int i, String s) {
        Log.e("LOGCAT", "发送的消息:onProgress:" + i + "::::::" + s);
    }

    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentHelper != null) {
                if (chatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
                case ITEM_LOCATION:
                    startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        if (EMClient.getInstance().getCurrentUser().equals(username) ||
                chatType != CHATTYPE_GROUP) {
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }


    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }


    //send message
    protected void sendTextMessage(String content) {
        if (EaseAtMessageHelper.get().containsAtUsername(content)) {
            sendAtMessage(content);
        } else {
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            sendMessage(message);
        }
    }

    public void sendForeignTextMessage(String content) {

        sendTextMessage(content);
    }


    /**
     * send @ message, only support group chat message
     *
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != CHATTYPE_GROUP) {
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);

    }


    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    //TODO 发送文件消息
    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
        message.setAttribute("type", "图片");
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }


    protected void sendMessage(EMMessage message) {
        //对法送的消息 进行属性添加
        message.setAttribute(CONSULTING_ID, ConsultingId.getConsultingId());//把问题的ID当做一个
        Map<String, Object> ext = message.ext();
        ext.put(CONSULTING_ID, ConsultingId.getConsultingId());
        message.setMessageStatusCallback(this);
        if (message == null) {
            return;
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(ChatType.ChatRoom);
        }

        String emojiCode = EmojiParse.emojiCode(message.getBody().toString());
        if (!TextUtils.isEmpty(emojiCode)) {
            ext.put(MESSAGE_ATTR_EXPRESSION_ID, emojiCode);
            ext.put("em_is_big_expression", "1");
            ext.put("woshishi", "1");
            message.ext().putAll(ext);
            message.setAttribute(MESSAGE_ATTR_IS_BIG_EXPRESSION, true);
            message.setAttribute(MESSAGE_ATTR_EXPRESSION_ID, emojiCode);
        }

        //Add to conversation
        EMClient.getInstance().chatManager().saveMessage(message);
        //refresh ui
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }

        String text = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, "");
        String type = message.getStringAttribute("type", "");
        if (!TextUtils.isEmpty(type)) {
            text = text + type;
        }
        Map<String, String> map = new HashMap();
        map.put("uid", EMClient.getInstance().getCurrentUser());
        map.put("user", mUid);
        map.put("lawyer", toChatUsername);
        map.put("pro_id", mPro_id);
        map.put("text", text);
        map.put("type", "1");
        map.put("texttype", message.getType().toString().equals(EMMessage.Type.TXT.toString()) ? "1" : "2");
        map.put("group_id", toChatUsername);
        if (message.getBody() instanceof EMFileMessageBody) {
            EMFileMessageBody body = (EMFileMessageBody) message.getBody();
            Map<String, File> mapFile = new HashMap<>();
            mapFile.put("files", new File(body.getLocalUrl()));
            OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(new OKHttpUIUpdataListener<UploadFileBean>() {
                @Override
                public void onSuccess(UploadFileBean data, int i) {
                    map.put("filename", data.getO().getName());
                    map.put("text", data.getO().getUrl());
                    uploadRecord(message, map);
                    Log.e("upload", "file");
                }

                @Override
                public void onError(Call call, Exception e, int i) {

                }

                @Override
                public void onFailure(C_CodeBean c_codeBean) {

                }
            });
            okHttpRequestModel.okhHttpPost(hostGroupUrl + "/index.php?g=Api&m=Homepage&a=uploadfile", null, new UploadFileBean(), mapFile);
        } else {
            uploadRecord(message, map);
            Log.e("upload", "text");
        }

    }


    private void uploadRecord(EMMessage message, Map<String, String> map) {
        if (IS_GROUP) {
            if (message.getBody() instanceof EMTextMessageBody) {
                EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
                map.put("text", messageBody.getMessage());
            }
            if (message.getChatType().toString().equals(EMConversation.EMConversationType.GroupChat.toString())) {
                mOkHttpRequestModel.okhHttpPost("http://sk.qixiuu.com" + "/api/Homepage/addlogbygroupid", map, new BaseBean());
            } else {
//                mOkHttpRequestModel.okhHttpPost("http://sk.qixiuu.com" + "/api/Homepage/log", map, new BaseBean());
                mOkHttpRequestModel.okhHttpPost("http://sk.qixiuu.com" + "/index.php?g=Api&m=Homepage&a=addkefulog", map, new BaseBean());
            }
        } else {
            mOkHttpRequestModel.okhHttpPost("http://wc.qixiuu.com" + "/api/Homepage/log", map, new BaseBean());
        }
    }


    //===================================================================================


    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    /**
     * clear the conversation history
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(), null, msg, null, new AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    messageList.refresh();
                    haveMoreData = true;
                }
            }
        }, true).show();
    }

    /**
     * open group detail
     */
    protected void toGroupDetails() {
        if (chatType == CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * forward message
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    // get the content and send it
                    String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                    sendTextMessage(content);
                }
                break;
            case IMAGE:
                // send image
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // send thumb nail if original image does not exist
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     * listen the group event
     */
    class GroupListener extends EaseGroupListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            // prompt group is dismissed and finish this activity
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.the_current_group_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }
    }

    /**
     * listen chat room event
     */
    class ChatRoomListener extends EaseChatRoomListener {

        @Override
        public void onChatRoomDestroyed(final String roomId, final String roomName) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.the_current_chat_room_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onRemovedFromChatRoom(final String roomId, final String roomName, final String participant) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.quiting_the_chat_room, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }


        @Override
        public void onMemberJoined(final String roomId, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member join:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public void onMemberExited(final String roomId, final String roomName, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member exit:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


    }

    protected EaseChatFragmentHelper chatFragmentHelper;

    public void setChatFragmentHelper(EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    public interface EaseChatFragmentHelper {
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         *
         * @param username
         */
        void onAvatarLongClick(String username);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }


    @Override
    public void onStart() {
        super.onStart();
        //将内容延伸到状态栏下面兵并且  不影响  虚拟键
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTitleView.getView().getLayoutParams();
//        layoutParams.topMargin= StatusBarUtils.getStatusBarHeight(getContext());
//        mTitleView.getView().setLayoutParams(layoutParams);
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     * @return
     */
    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                getContext().getResources().getDisplayMetrics());
    }

}
