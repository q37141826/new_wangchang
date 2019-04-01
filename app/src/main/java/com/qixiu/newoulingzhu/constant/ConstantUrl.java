package com.qixiu.newoulingzhu.constant;

/**
 * Created by HuiHeZe on 2017/6/19.
 */

public class ConstantUrl {
    //分享跳转页面
    public static final String SHARE_CONTENT = "欧伶猪法务法律咨询，是一款由专业律师团队在线，能及时、高效的协助处理法律事务问题的在线交流平台。";
    public static final String TITILE = "欧伶猪法务咨询";
    public static String SHARE_ICON = "https://mmbiz.qlogo.cn/mmbiz_png/ibyw6gtcD9ImWPdLcBHo9V29cJ6N2LgLqnZL7vMovMWhusqpfDdVmWk6XaIic1PwVLBajGDoo14lqerb87BEnJng/0?wx_fmt=png";
    //主页  匿名的是115地址，外部网络可以访问  147的是内部网络专用
//    public static String hosturl = "http://wc.qixiuu.com/";
//    public static String hosturl = "http://sk.qixiuu.com/";
    public static String hosturl = "https://app.wanchangorange.com/";
//        public static String hostImageurl = "http://cherry.whtkl.cn/";

//    public static String hosturl = "http://192.168.168.39/attorney/";
//    public static String hostImageurl = "http://192.168.168.39/attorney/";

    public static String SHARE_CLICK_GO_URL = hosturl+"/web-im/share.html";//http://192.168.168.39/attorney/web-im/share.html//// TODO: 2017/12/15 注意这个地方一定要是外网路径
    public static String WEBURL_PREFIX = hosturl + "/smartCommunity/#";
    /**
     * 加载WebPage的URL
     */

    public static String HomeInfoURl = hosturl + "api/Homepage/home";
    /**
     * 模板tab数据
     */
    public static String TemplateTypeURl = hosturl + "api/Homepage/type";
    public static String HomeTemplateListURl = hosturl + "api/Homepage/template";
    public static String HotTemplateListURl = hosturl + "api/Homepage/templates";
    public static String HotInformationListURl = hosturl + "api/Homepage/hotzx";
    public static String HotCaseListURl = hosturl + "/api/Homepage/hotal";
    public static String ConsultURl = hosturl + "api/Homepage/issue";
    public static String EditAddressURl = hosturl + "api/Personal/address";
    public static String EditSexURl = hosturl + "api/Personal/vitaSex";
    public static String EditVitaNameURl = hosturl + "api/Personal/vitaName";
    public static String EditVitaTNameURl = hosturl + "api/Personal/vitaTname";
    public static String EditVitaEmliaURl = hosturl + "api/Personal/vitaEmlia";
    public static String SendEmail = hosturl + "api/Homepage/mail";
    public static String ProblemDetialUrl = hosturl + "api/Homepage/details";
    public static String ProblemDetialCompleteUrl = hosturl + "api/Homepage/complete";
    public static String ZxInfoUrl = hosturl + "api/Homepage/zxInfo";
    public static String caseInfo = hosturl + "/api/Homepage/alInfo";
    public static String myVipUrl = hosturl + "api/Personal/myvip";
    public static String dredgeVipUrl = hosturl + "api/Personal/kvip";
    public static String dredgeVipPriceUrl = hosturl + "api/Personal/vipm";
    public static String upgradeVipPriceUrl = hosturl + "api/Personal/vips";
    public static String promptlyPayUrl = hosturl + "api/Center/Topup";

    public static String HomeMessageURl = hosturl + "/App/Home/homeMessage";

    public static String AllCateURl = hosturl + "/App/Home/shopCate";
    public static String HotWordsURl = hosturl + "/App/Index/hotWords";
    public static String GoodsDetailURl = hosturl + "/App/Shop/goods";
    public static String CommentListURl = hosturl + "/App/Shop/comment_list";
    public static String GoodsDetailHotURl = hosturl + "App/Shop/hot";
    public static String GoodsCollectURl = hosturl + "App/Shop/doCollect";

    public static String SelectAllSpecURl = hosturl + "/App/Shop/chooseSku";
    public static String getGoodsSpecInfoURl = hosturl + "/App/Shop/goodsSku";
    public static String AddToShopCarURl = hosturl + "/App/Shop/addtobasket";
    public static String ClearShopCarURl = hosturl + "App/Shop/clearbasket";

    public static String HomeSeekURl = hosturl + "/app/Homepage/seek";
    public static String HomeListDefault = hosturl + "/app/Homepage/mo";
    public static String HomeListDone = hosturl + "/app/Homepage/done";
    public static String HomeListRaise = hosturl + "/app/Homepage/raise";
    //发送验证码
    public static String sendCodeUrl = hosturl + "api/User/sendPhoneCode";
    //登录
    public static String loginUrl = hosturl + "api/User/login";
    //注册
    public static String registUrl = hosturl + "api/User/register";


    //忘记密码
    public static String lostPswdUrl = hosturl + "/api/User/lostPwd";
    //获取个人信息
    public static String myCenterUrl = hosturl + "/api/Personal/myCard";
    //退出登录
    public static String outLoginUrl = hosturl + "api/User/logout";
    //编辑资料
    public static String editUserUrl = hosturl + "App/User/saveInfo";
    public static String editUserHead = hosturl + "api/Personal/vitaHaed";
    public static String eitdPhone = hosturl + "api/User/eitdPhone";
    public static String messageList = hosturl + "api/Center/message";
    public static String systemMessageList = hosturl + "api/Center/xtmessage";

    public static String eitdPhones = hosturl + "api/User/eitdPhones";

    //意见反馈
    public static String feedBackUrl = hosturl + "/api/Personal/feedBack";
    //消息列表
    public static String messageUrl = hosturl + "App/Index/messages";
    public static String messageInfoUrl = hosturl + "App/Index/messageInfo";
    //地址列表
    public static String addressUrl = hosturl + "App/User/address";
    //企业资质
    public static String enterpriseAptitude = hosturl + "/app/Enterprise/aptitude";
    //企业文化
    public static String enterpriseCulture = hosturl + "/app/Enterprise/culture";
    //员工介绍
    public static String enterpriseStaff = hosturl + "/app/Enterprise/staff";
    //成功案例
    public static String enterpriseSucceed = hosturl + "/app/Enterprise/succeed";
    public static String addProject = hosturl + "/App/Project/addProject";

    //用户资料认证
    public static String userDetailsUrl = hosturl + "/App/Verify/userVerify";
    public static String projectBuy = hosturl + "/App/Project/buy";
    //我的积分
    public static String myPointsUrl = hosturl + "/app/Center/integral";
    //我发布的项目
    public static String myPublishListUrl = hosturl + "/App/User/userProjects";
    //我发布项目的详情
    public static String myPublishDetailsUrl = hosturl + "/App/User/projectInfo";
    //我发布的项目确认收款
    public static String confirmGetMoneyUrl = hosturl + "/App/Project/gathering";
    //我发布的项目款项有误
    public static String moneyIsErrorUrl = hosturl + "/App/Project/receiveError";
    //我购买的项目详情
    public static String myPayedDetailsUrl = hosturl + "/App/User/fundingInfo";
    //我购买的项目列表
    public static String myPayedListUrl = hosturl + "/App/User/userFundings";
    public static String uploadCredentials = hosturl + "/App/Project/uploadCredentials";
    public static String amend = hosturl + "/app/Center/amend";
    public static String bands = hosturl + "/app/Enterprise/bands";

    //积分查看的那个红点点
    public static String pointsDotUrl = hosturl + "/app/Center/save";
    //我的好友两个列表
    public static String friendsUrl = hosturl + "/app/Center/friend";
    public static String areaUrl = hosturl + "/app/Center/area";

    public static String account = hosturl + "/app/Center/account";
    public static String explain = hosturl + "/app/Center/explain";
    //用户协议
//    public static String UserRuleUrl = hosturl + "/app/Enterprise/put";
    public static String UserRuleUrl = hosturl + "/index.php?g=Api&m=Homepage&a=getProtocol&type=register";


    //删除购物车
    public static String STORE_SHOPCAR_DELECTED_TAG = "/app/Shop/delGoodsFromCart";
    public static String STORE_SHOPCAR_DELECTED = hosturl + STORE_SHOPCAR_DELECTED_TAG;

    //编辑购物车数量
    public static String STORE_SHOPCAR_EDIT_COUNT_TAG = "/App/Shop/update_num";
    public static String STORE_SHOPCAR_EDIT_COUNT = hosturl + STORE_SHOPCAR_EDIT_COUNT_TAG;
    //获取购物车商品列表
    public static String STORE_GET_SHOPCAR_GOODS_TAG = "/App/Shop/basket";
    public static String STORE_GET_SHOPCAR_GOODS = hosturl + STORE_GET_SHOPCAR_GOODS_TAG;

    //增加地址
    public static String addAddressUrl = hosturl + "App/User/addressSet";
    //删除地址
    public static String addressDelUrl = hosturl + "App/User/addressDel";


    //评价
    public static String goodComments = hosturl + "App/Shop/comment";
    //商城列表
    public static String storeUrl = hosturl + "App/Shop/index";

    /**
     * 确认订单
     */
    //  快速支付下的接口
    public static String fastPayUrl = hosturl + "App/Shop/fastbuy";
    //快速致富生成订单
    public static String fastPayOrderMakeUrl = hosturl + "App/Shop/orderFastMake";
    // 购物车下的接口
    public static String cartsPayUrl = hosturl + "App/Shop/orderMake";
    // 购物车下的接口
    public static String cartsPayOrderMakeUrl = hosturl + "App/Shop/orderMake";
    /**
     * 确认订单
     */

    //获取阿里微信支付数据
    public static String payOrderUrl = hosturl + "App/Shop/pay";
    /**
     * 订单
     */
    //  订单列表
    public static String orderListUrl = hosturl + "App/Shop/orderList";
    //删除订单
    public static String orderDeleteUrl = hosturl + "App/Shop/orderDelete";
    //取消订单
    public static String cancleOrder = hosturl + "App/Shop/orderCancel";
    //确认收货
    public static String getGoodsUrl = hosturl + "App/Shop/orderOK";
    //订单详情
    public static String orderDetailUrl = hosturl + "App/Shop/orderDetail";
    /**
     * 订单
     */

    //我的收藏
    public static String my_collectionUrl = hosturl + "App/Shop/collect";

    //设置相关
    public static String getInfo = hosturl + "/api/Personal/about";


    public static String versionUrl = hosturl + "/api/Personal/vivt";

    //查看物流
    public static String CheckWhereTag = "Shop/getFast";
    public static String CheckWhereUrl = hosturl + "/App/" + CheckWhereTag;
    //我的订单
    public static String myConsiltingUrl = hosturl + "/api/Center/consult";
    public static String problemBuyUrl = hosturl + "/api/Center/gopay";
    //问题购买
    public static String startProblemPayUrl = hosturl + "/api/Center/buys";
    //会员购买升级支付
    public static String startPayUrl = hosturl + "api/Center/goup";
    //消息开关
    public static String messageSwitchUrl = hosturl + "/api/Personal/swit";


    //app各种简短文字、文章（html）	id=9 首页-我要咨询—温馨提示内容;id=10 购买问题—购买介绍
    public static String countMessageUrl = hosturl + "/api/Homepage/post";
    public static String deleteUrl = hosturl + "/api/Center/delProblem";
    public static String is_info_enough = hosturl + "api/Personal/checkOpenVip";

    public static String mengberUrl = hosturl + "/api/Homepage/postzInfo?id=6";

    public static String commentsUrl = hosturl + "/api/Center/lawyerComments";

    //客服接口传一个uid给我，建立好友关系
    public static String serviceUrl = hosturl + "/api/Homepage/kefu";

    public static String uploadMessage = hosturl + "/api/Homepage/log";

    //判断账号是否被注销了
    public static String checkUserExsit = hosturl + "api/Homepage/checkUserExsit";

    //减少次数
    public static String countDown = hosturl + "api/Homepage/checkUserDownload";


    //案例列表
    public static String clasicCaseMenu = hosturl + "/index.php?g=Api&m=Information&a=getCaseColumns";
    public static String clasicCaseList = hosturl + "/index.php?g=Api&m=Information&a=getArticleList";
    //咨询列表
    public static String clasicConsultMenu = hosturl + "/index.php?g=Api&m=Information&a=getInfoColumns";


    //第三方登录
    public static String appLoginUrl = hosturl + "/index.php?g=Api&m=user&a=thirdPartLogin";

    //升级会员或者购买会员之前的  列表
    public static String getListVip = hosturl + "/index.php?g=Api&m=Personal&a=getListVip";

    //绑定手机
    public static String bingUserPhone = hosturl + "/index.php?g=Api&m=User&a=bingUserPhone";
    //消费记录
    public static String costRecord = hosturl + "/index.php?g=Api&m=Center&a=getUserCostList";

    //资讯详情
    public static String detailsWebConslting = hosturl + "/detail/advisoryDetail.html?URL=" + hosturl + "&id=";
    public static String studyConsult = hosturl + "/detail/technicalProcess.html?URL=" + hosturl;

    //个人资料
    public static String personDetailsUrl = hosturl + "api/Personal/vita";
    /*
    * 个人定制部分
    * */
    //个人定制外面的列表
    public static String customListUrl = hosturl + "/index.php?g=Api&m=Customized&a=getCustList";
    //个人定制的说明
    public static String customWebUrl = hosturl + "/index.php?g=Api&m=Customized&a=getInfoColumns&type=examination";
    //获取个人定制价格
    public static String getOrderPriceUrl = hosturl + "/index.php?g=Api&m=Customized&a=getOrderPrice";
    //创建订单
    public static String customPayUrl = hosturl + "/index.php?g=Api&m=Customized&a=createOrder";
    public static String lawyerUrl = hosturl + "/index.php?g=Api&m=Customized&a=getCommunicatebyOrder";
    //省市区都获取到
    public static String getAddressUrl = hosturl + "/index.php?g=Api&m=Customized&a=getCityList";
    //----------------
    /*
    *计算器
     */
    //地区
    public static String getCityUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=getListProvince";
    //伤残等级
    public static String getInjuryUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=getListLevel";
    //交通伤残计算
    public static String getTrafficUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=Traffic";
    //抚养费计算
    public static String getRaiseUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=trafficperson";
    //案件类型列表
    public static String caseListUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=getlitigationlist";
    //工伤计算
    public static String getInjuryResultUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=Injury";


    //诉讼费计算
    public static String getlitigationUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=getlitigation";


    //律师费计算
    // 律师费的地址
    public static String getLawyarAddressUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=getProvinceList";
    //律师费类型
    public static String getLawyarCaseTypeUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=getLawType";
    //律师费用计算
    public static String getLawFeeUrl = hosturl + "/index.php?g=Api&m=Lawyer&a=getLawFee";

    //----------------
    //流程说明
    public static String getInroduceUrl(String type) {
        return hosturl + "detail/process.html?URL=" + hosturl + "&type=" + type;
    }

    //我的会员web
    public static String myVipWeb = hosturl +  "detail/myVip.html?URL="+hosturl+"&uid=";
}

