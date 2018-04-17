package leaflet.miaoa.qmxh.leaflet_simple.bean;

/**
 * Created by hj on 2018/1/6.
 */

public class Https {


//    public static String basehttp="http://10.10.24.9:8080/";//本地ip
    public static String basehttp="https://www.xiaochuandan.club/";//远程服务器
//    public static String basehttp="http://115.159.77.161:8080/";//远程服务器
    public static String picture_yun="http://oxyiwa6jf.bkt.clouddn.com/";//图片远程服务器





    public static int initialTimeOutMs=500000;//默认超时时间

    public static String overdueReminder=basehttp+"MiaoA/overdueReminder.action";//进入APP时触发广告过期动作
    public static String getToken=basehttp+"MiaoA/getToken.action";//获取七牛云上传时的token
    public static String protocolUrl =basehttp+"MiaoA/xcd-gzh/html/protocol.jsp";//用户协议url
    public static String loginpsd=basehttp+"MiaoA/checkloginaction_checkuserlogin.action";//登录
    public static String updateMacLingByNum=basehttp+"MiaoA/updateMacLingByNum.action";//退出登录的时候将Mac值清空
    public static String passwordGet=basehttp+"MiaoA/passwordGet.action";//获得密码
    public static String getCode=basehttp+"MiaoA/sendCode.action";//获取验证码
    public static String checkUserPayWord=basehttp+"MiaoA/checkUserPayWord.action";//获取支付密码
    public static String modifyUserPayword=basehttp+"MiaoA/modifyUserPayword.action";//修改支付密码
    public static String SMS_127135073="SMS_127135073";//登录确认验证码
    public static String SMS_127135071="SMS_127135071";//用户注册验证码
    public static String SMS_127135070="SMS_127135070";//修改密码验证码
    public static String SMS_130928189="SMS_130928189";//修改支付密码验证码
    public static String SMS_127155341="SMS_127155341";//修改手机号验证码


    public static String isExist=basehttp+"MiaoA/checkRegister.action";//查询账号是否存在
    public static String register=basehttp+"MiaoA/addUsers.action";//注册账号
    public static String resetPassword=basehttp+"MiaoA/forgetPassword.action";//重置密码
    public static String loginMsg=basehttp+"MiaoA/checkUserShortLogin.action";//短信登录


    public static String slideShow=basehttp+"MiaoA/getNewHomePageAppCarouselAdver.action";//首页轮播图
    public static String slideShow_error=basehttp+"MiaoA/images/0104.png";//无网络首页轮播图
    public static String slideShow1=basehttp+"MiaoA/getNewHomePageAppSpreadAdver.action";//启动页



    public static String vermicelli_chart=basehttp+"MiaoA/getFanIncreaseByNum.action";//粉丝折线图
    public static String sellerNews=basehttp+"MiaoA/getAllMerchanNews.action";//商家消息
    public static String toaddadver=basehttp+"MiaoA/toaddadver.action";//得到发布广告需要的所有人群特性
    public static String toaddCoin=basehttp+"MiaoA/toaddCoin.action";//查找商家的所有店铺
    public static String coinIndex=basehttp+"MiaoA/coinIndex.action";//查找首页10条商品
    public static String storeIndex=basehttp+"MiaoA/storeIndex.action";//查找十个附近店铺
    public static String getCoinById=basehttp+"MiaoA/getCoinById.action";//商城里的商品详情
    public static String getCoinAttribute=basehttp+"MiaoA/getCoinAttribute.action";//商城里的商品属性详情



    public static String usersaction_modifyBusiness=basehttp+"MiaoA/modifyQueryUsers.action";//修改个人资料
    public static String usersaction_modifyUserImg=basehttp+"MiaoA/uploadimage";//上传图片
    public static String modifyQueryImg=basehttp+"MiaoA/modifyQueryImg.action";//修改个人头像sql
    public static String checkUserPassword=basehttp+"MiaoA/checkUserPassword.action";//验证账号密码
    public static String checkRegister=basehttp+"MiaoA/checkRegister.action";//验证新手机号是否已存在
    public static String usersaction_modifyUserUnum=basehttp+"MiaoA/usersaction_modifyUserUnum.action";//修改手机号
    public static String addUserFeedBack=basehttp+"MiaoA/addUserFeedBack.action";//用户给平台发送留言/反馈
    public static String checkUserLogin=basehttp+"MiaoA/checkUserLogin.action";//判断用户是否登录
    public static String useraction_tomodifyUser=basehttp+"MiaoA/useraction_tomodifyUser.action";//获取用户信息
    public static String useraction_balance=basehttp+"MiaoA/useraction_balance.action";//获取用户余额
//    public static String getQueryPagedUserNews=basehttp+"MiaoA/getQueryPagedUserNews.action";//获取个人消息
    public static String getQueryPagedNews=basehttp+"MiaoA/getQueryPagedNews.action";//查看消息
    public static String deleteNewsByNewId=basehttp+"MiaoA/deleteNewsByNewId.action";//删除消息
    public static String updateNewsByNewId=basehttp+"MiaoA/updateNewsByNewId.action";//将消息设为已读
    public static String updateNewsIfRead=basehttp+"MiaoA/updateNewsIfRead.action";//将消息设为未读



    public static String getQueryPagedAdverUlike=basehttp+"MiaoA/getQueryPagedAdverUlike.action";//获所有广告
    public static String getAdverById=basehttp+"MiaoA/getAdverById.action";//获广告详情
    public static String getAdverResidueById=basehttp+"MiaoA/getAdverResidueById.action";//获广告剩余红包数
    public static String updateOfReduceReplacee=basehttp+"MiaoA/updateOfReduceReplacee.action";//看完广告后领取红包,修改金额,修改广告状态
    public static String adverNoResiude=basehttp+"MiaoA/adverNoResiude.action";//沒有红包,修改广告状态
    public static String getQueryPagedAdverWacthed=basehttp+"MiaoA/getQueryPagedAdverWacthed.action";//用户_广告红包_看过的



    public static String getQueryPagedCoin=basehttp+"MiaoA/getQueryPagedCoin.action";//用户进入商城界面
    public static String getQueryPagedCoinBuy=basehttp+"MiaoA/getQueryPagedCoinBuy.action";//查看已购商品
    public static String applyForRefund=basehttp+"MiaoA/applyForRefund.action";//用户申请退款
    public static String updateShippingStatusUser=basehttp+"MiaoA/updateShippingStatusUser.action";//用户确认收货
    public static String deleteCoinBuyBy=basehttp+"MiaoA/deleteCoinBuyBy.action";//用户删除订单


    public static String getDefaultAddress=basehttp+"MiaoA/getDefaultAddress.action";//查询该用户的默认收货地址
    public static String getQueryPagedAddress=basehttp+"MiaoA/getQueryPagedAddress.action";//查询该用户的收货地址
    public static String insertConsigneeAddress=basehttp+"MiaoA/insertConsigneeAddress.action";//添加该用户的收货地址
    public static String updateConsigneeAddress=basehttp+"MiaoA/updateConsigneeAddress.action";//修改该用户的收货地址
    public static String deleteConsigneeAddress=basehttp+"MiaoA/deleteConsigneeAddress.action";//删除该用户的收货地址
    public static String updateDefaultAddress=basehttp+"MiaoA/updateDefaultAddress.action";//设置为默认收货地址
    public static String buyCoinGoods=basehttp+"MiaoA/buyCoinGoods.action";//余额充足
    public static String alipay=basehttp+"MiaoA/alipay.action";//支付宝支付
    public static String WeChatPay=basehttp+"MiaoA/WeChatPay.action";//微信支付
    public static String alipayTransToaccount=basehttp+"MiaoA/alipayTransToaccount.action";//支付宝授权
    public static String transToaccount=basehttp+"MiaoA/transToaccount.action";//支付宝提现
    public static String tiXian=basehttp+"MiaoA/tiXian.action";//支付宝提现改变数据库

    public static String buyCoinGoodsAlipay=basehttp+"MiaoA/buyCoinGoodsAlipay.action";//支付宝sql
    public static String insertCoinBuy=basehttp+"MiaoA/insertCoinBuy.action";//微信sql
    public static String alipayRefund=basehttp+"MiaoA/alipayRefund.action";//支付宝sql失败调用



    public static String getQueryPagedAdverSan=basehttp+"MiaoA/getQueryPagedAdverSan.action";//商家展示中
    public static String getQueryPagedAdverWu=basehttp+"MiaoA/getQueryPagedAdverWu.action";//结束的广告
    public static String getQueryPagedAdverLing=basehttp+"MiaoA/getQueryPagedAdverLing.action";//商家广告页面_其他_待审核/未通过
    public static String addAdver=basehttp+"MiaoA/addAdver.action";//发布广告_余额充足_减余额
    public static String adverAdd=basehttp+"MiaoA/adverAdd.action";//发布广告_sql
    public static String addAdverAlipay=basehttp+"MiaoA/addAdverAlipay.action";//发布广告_支付宝支付
    public static String addAdverWeChat=basehttp+"MiaoA/addAdverWeChat.action";//发布广告_微信支付
    public static String deleteNewAdverByNum=basehttp+"MiaoA/deleteNewAdverByNum.action";//删除登陆者发布的未付款的广告(取消付款)
}
