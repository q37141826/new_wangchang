package com.qixiu.newoulingzhu.engine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.bean.StringConstants;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.utils.Preference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.qixiu.newoulingzhu.application.BaseApplication.getContext;
import static com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant.REQUESTCODE_START_CHAT;


/**
 * Created by Administrator on 2017/11/7 0007.
 * 封装环信sdk方法
 */

public class HyEngine {

    /**
     * 初始化环信Options
     *
     * @return
     */
    public static EMOptions initChatOptions() {

        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
//        options.setAppKey("1168171106178080#wanchanglawfirms");//IS_GROUP 如果是单聊用这个，群聊用下面的这个
        options.setAppKey("1168171106178080#groupchat");

        return options;
    }

    public static void init() {
//初始化
        EMOptions emOptions = initChatOptions();
        EMClient.getInstance().init(getContext(), emOptions);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //EaseUI 初始化
        EaseUI easeUI = EaseUI.getInstance();
        if (easeUI.init(getContext(), emOptions)) {
            setEaseUIProviders(easeUI);
        }
        EMClient.getInstance().chatManager().loadAllConversations();//加载记录
        EMClient.getInstance().groupManager().loadAllGroups();
    }


    public static void login(final Activity activity, final String userName, final boolean toChat, String toChatId, final Bundle bundle) {
        final ZProgressHUD zProgressHUD = new ZProgressHUD(activity);
        if (toChat) {

            zProgressHUD.show();
        }

        EMClient.getInstance().login(userName, "123456", new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                if (toChat) {
                    Intent intent = new Intent(activity, ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, bundle.getString(EaseConstant.EXTRA_USER_ID));
                    intent.putExtra(EaseConstant.CURRENT_UID, bundle.getString(EaseConstant.CURRENT_UID));
                    intent.putExtra(EaseConstant.LAWYER, bundle.getString(EaseConstant.LAWYER));
                    intent.putExtra(EaseConstant.PRO_ID, bundle.getString(EaseConstant.PRO_ID));
                    intent.putExtra(EaseConstant.PROBLEM, bundle.getString(EaseConstant.PROBLEM));
                    intent.putExtra(EaseConstant.TYPE, bundle.getString(EaseConstant.TYPE));
                    intent.putExtra(EaseConstant.TOCHAT_NAME, bundle.getString(EaseConstant.TOCHAT_NAME));
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    activity.startActivityForResult(intent, REQUESTCODE_START_CHAT);

                }
                if (zProgressHUD.isShowing()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            zProgressHUD.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                if (zProgressHUD.isShowing()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            zProgressHUD.dismissWithFailure("登录聊天服务器失败");
                        }
                    });

                }

            }
        });
    }

    public static boolean isLogin() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    public static void logout() {

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });
    }


    public static void addConnectionListener(EMConnectionListener emConnectionListener) {

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(emConnectionListener);


    }

    public static EaseUser getUserInfo(String username) {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        EaseUser user = null;
        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            user = new EaseUser(Preference.get(ConstantString.NICK_NAME, StringConstants.EMPTY_STRING));
            user.setAvatar(Preference.get(ConstantString.HEAD, StringConstants.EMPTY_STRING));
            return user;
        } else {
            user = new EaseUser(Preference.get(ConstantString.OTHER_NAME, StringConstants.EMPTY_STRING));
            user.setAvatar(Preference.get(ConstantString.OTHER_HEAD, StringConstants.EMPTY_STRING));
        }

        // if user is not in your contacts, set inital letter for him/her
        if (user == null) {
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }
        return user;
    }

    public static void setEaseUIProviders(EaseUI easeUI) {
        //set user avatar to circle shape
        EaseAvatarOptions avatarOptions = new EaseAvatarOptions();
        avatarOptions.setAvatarShape(1);
        easeUI.setAvatarOptions(avatarOptions);

        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });


    }

    public static void startConversationSingle(Activity activity, String toChatId, Bundle extBundle, Class<?> activtiyClass) {
        Intent intent = new Intent(activity, activtiyClass);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, toChatId);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        intent.putExtra(EaseConstant.PROBLEM, extBundle.getString(EaseConstant.PROBLEM));
        intent.putExtra(EaseConstant.TYPE, extBundle.getString(EaseConstant.TYPE));
        intent.putExtra(EaseConstant.CURRENT_UID, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        intent.putExtra(EaseConstant.TOCHAT_NAME, extBundle.getString(EaseConstant.TOCHAT_NAME));
        intent.putExtra(EaseConstant.PRO_ID, extBundle.getString(EaseConstant.PRO_ID));
        activity.startActivityForResult(intent, REQUESTCODE_START_CHAT);
    }
    public static void startConversationGroup(Activity activity, String toChatId, Bundle extBundle, Class<?> activtiyClass) {
        Intent intent = new Intent(activity, activtiyClass);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, toChatId);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
        intent.putExtra(EaseConstant.PROBLEM, extBundle.getString(EaseConstant.PROBLEM));
        intent.putExtra(EaseConstant.TYPE, extBundle.getString(EaseConstant.TYPE));
        intent.putExtra(EaseConstant.CURRENT_UID, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        intent.putExtra(EaseConstant.TOCHAT_NAME, extBundle.getString(EaseConstant.TOCHAT_NAME));
        intent.putExtra(EaseConstant.PRO_ID, extBundle.getString(EaseConstant.PRO_ID));
        activity.startActivityForResult(intent, REQUESTCODE_START_CHAT);
    }
    public static int getUnReadMsgCount() {
        int unreadMsgCount = 0;
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        if (allConversations != null && allConversations.size() > 0) {
            for (String key : allConversations.keySet()) {
                EMConversation emConversation = allConversations.get(key);
                unreadMsgCount += emConversation.getUnreadMsgCount();
//                for (int i = 0; i <emConversation.getAllMessages().size() ; i++) {
//                    if(emConversation.getAllMessages().get(i).isUnread()){
//                        Log.e("chat",emConversation.getAllMessages().get(i).getBody().toString());
//                        Log.e("chatId",emConversation.getAllMessages().get(i).getMsgId());
//                    }
//                }
            }
        }
        return unreadMsgCount;
    }

    public static int getSingleUnReadMsgCount() {
        int unreadMsgCount = 0;
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        if (allConversations != null && allConversations.size() > 0) {
            for (String key : allConversations.keySet()) {
                EMConversation emConversation = allConversations.get(key);
                if(emConversation.getType().toString().equals(EMConversation.EMConversationType.Chat.toString())){
                    unreadMsgCount += emConversation.getUnreadMsgCount();
                }
            }
        }
        return unreadMsgCount;
    }



    //删除某个对话的所有消息记录  username是对话的ID
    public static void deleteAllMessage(String username) {
        List<EMMessage> allMessageRecord = getAllMessageRecord(username);
        for (int i = 0; i < allMessageRecord.size(); i++) {
            deleteRecord(username, allMessageRecord.get(i).getMsgId());
        }
    }

    //删除某条消息  username是对话的ID    messageId是这个对话的某条消息的ID
    public static void deleteRecord(String username, String messageId) {
        Log.e("data", EMClient.getInstance().chatManager().toString());
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        conversation.removeMessage(messageId);
    }

    //删除和某个人的对话
    public static void deleteConversation(String username) {
        EMClient.getInstance().chatManager().deleteConversation(username, true);
    }

    //导入消息到数据库
    public static void importMessageRecord(String key, List<EMMessage> msgs) {
        EMClient.getInstance().chatManager().importMessages(msgs);
//        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(key);
//        for (int i = 0; i <msgs.size() ; i++) {
//            conversation.insertMessage(msgs.get(i));
//        }
    }

    //获取所有对话
    public static Map<String, EMConversation> getAllConversations() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        return conversations;
    }


    //获取消息数目
    public static List<EMMessage> getAllMessageRecord(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
//获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
//SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
//获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        return messages;
    }

    public static List<EMMessage> creatMessage() {
        List<EMMessage> messages = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            EMMessage mes = EMMessage.createReceiveMessage(EMMessage.Type.TXT);//"ls23","ls23"
            mes.setMsgTime(System.currentTimeMillis() - 10000);
            mes.setChatType(EMMessage.ChatType.Chat);
            Map<String, Object> ext = mes.ext();
            messages.add(mes);
        }
        return messages;
    }


    //一键清空所有对话记录
    public static void deleteAllConversation() {
        Map<String, EMConversation> allConversations = getAllConversations();//获取所有的对话
        Set<String> strings = allConversations.keySet();//获取所有的对话key
        for (String key : strings) {
            deleteAllMessage(key);
//            deleteConversation(key);//调试之中保留对话
        }
    }

    public static void addMessageListenner(EMMessageListener msgListener) {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }
    public  static  void removeMessageListenner(EMMessageListener msgListener){
//        记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    public static void loadAllConversationAndMessage() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();//上面是加载了所有群组和对话，但是下面才是加载所有消息
        Map<String, EMConversation> allConversations = HyEngine.getAllConversations();//
        Set<String> strings = allConversations.keySet();
        for (String key : strings) {
            onConversationInit(allConversations.get(key));
        }


    }
    public static void onConversationInit(EMConversation conversation) {
        int pagesize = 20;
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
    }
}
