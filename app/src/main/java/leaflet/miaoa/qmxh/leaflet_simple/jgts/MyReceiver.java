package leaflet.miaoa.qmxh.leaflet_simple.jgts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.PersonalNewsList;

import static leaflet.miaoa.qmxh.leaflet_simple.utils.GetMacAddress.getAdresseMAC;

/**
 * Created by gaofeng on 2018/3/9.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG ="MyReceiver";
    public static String logout;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String mac=bundle.getString(JPushInterface.EXTRA_TITLE);
            String content=bundle.getString(JPushInterface.EXTRA_MESSAGE);
            if(getAdresseMAC(context).equals(mac)&&"重复登录".equals(content)){
                //清除本地账号缓存
                SharedPreferences sharedPreferences =context.getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putString("tel", "");
                editor.putInt("umark", 2);
                editor.putString("islogin", "");
                editor.commit();
                logout="true";

            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Toast.makeText(context, "点开了通知", Toast.LENGTH_SHORT).show();
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, PersonalNewsList.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }


}