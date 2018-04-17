package leaflet.miaoa.qmxh.leaflet_simple.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

/**
 * Created by gaofeng on 2018/1/13.
 */

public class CallPhone {
    private Activity activity;
    private String telStr;
    public CallPhone(Activity activity,String telStr) {
        this.activity=activity;
        this.telStr=telStr;
    }

    /**
     * 点击打电话的按钮响应事件
     *
     * @param view view
     */
    public void callButtonClickAction(View view)
    {

        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(activity, "Yes" + which, Toast.LENGTH_SHORT).show();
                        requestPermission();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(activity, "No" + which, Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        Toast.makeText(activity, "Cancel" + which, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //弹窗让用户选择，是否允许申请权限
        DialogUtil.showConfirm(activity, "申请权限", "是否允许获取打电话权限？", dialogOnclicListener, dialogOnclicListener);
    }

    /**
     * 申请权限
     */
    public void requestPermission()
    {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},
                    RequestPermissionType.REQUEST_CODE_ASK_CALL_PHONE);
                return;
            }
            else
            {
                callPhone();
            }
        }
        else
        {
            callPhone();
        }
    }
    public interface RequestPermissionType
    {

        /**
         * 请求打电话的权限码
         */
        int REQUEST_CODE_ASK_CALL_PHONE = 100;
    }
    /**
     * 拨号方法
     */
    public  void callPhone()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+telStr));
        activity.startActivity(intent);
    }
}
