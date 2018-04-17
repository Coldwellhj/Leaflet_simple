package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import leaflet.miaoa.qmxh.leaflet_simple.MyApplicition.MyApplication;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.utils.CallPhone;

/**
 * @author WYH_Healer
 * @email 3425934925@qq.com
 * Created by xz on 2017/2/22.
 * Role:点击手机号码弹出的Dialog用于拨打电话
 */
public class CallPhoneDialog extends Dialog implements View.OnClickListener {

    private Activity activity;

    private String phoneNumber;

    private TextView tv_call_phone;

    private TextView tv_cancle;

    public CallPhoneDialog(Activity activity, String phoneNumber) {
        super(activity, R.style.DialogStyle);
        this.activity = activity;
        this.phoneNumber = phoneNumber;

        View view = LayoutInflater.from(activity).inflate(R.layout.layout_call_phone, null);
        setContentView(view);
        // 设置窗口大小
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            lp.width = MyApplication.getScreenWidth(activity);
        } else {
            lp.width = MyApplication.getScreenHeight(activity);
        }
        mWindow.setAttributes(lp);
        // 设置可以动画
        mWindow.setWindowAnimations(R.style.Animation);
        // 位置设置在底部
        mWindow.setGravity(Gravity.BOTTOM);
        // 设置可取消
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        initView();
        initData();

    }


    private void initView() {
        tv_call_phone = (TextView) findViewById(R.id.tv_call_phone);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);

        tv_call_phone.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
    }

    private void initData() {
        tv_call_phone.setText(phoneNumber + "");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        tv_call_phone.setText(phoneNumber + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_call_phone:
                //实现拨打电话的操作
                CallPhone callPhone=new CallPhone(activity,phoneNumber);
                callPhone.requestPermission();
                break;
            case R.id.tv_cancle:
                this.dismiss();
                break;
        }
    }
}
