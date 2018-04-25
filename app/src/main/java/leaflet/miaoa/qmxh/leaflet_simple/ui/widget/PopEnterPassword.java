package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.OnPasswordInputFinish;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.OnPasswordPayClickListener;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.ModifyPayPsdActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.AesCBC;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Base64;


/**
 * 输入支付密码
 *
 * @author lining
 */
public class PopEnterPassword extends PopupWindow {

    private PasswordView pwdView;
    private PayEditText payEditText;
    private String psd_jiami;

    private View mMenuView;
    private Keyboard keyboard;
    private ImageView img_cancel;
    private TextView tv_forgetPwd;
    private Activity mContext;
    private String psd;
    private Boolean flag;
    private OnPasswordPayClickListener passwordPayClickListener;
    private static final String[] KEY = new String[] {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "X"
    };
    public PopEnterPassword(final OnPasswordPayClickListener passwordPayClickListener, final Activity context, final String psd, final Boolean flag) {

        super(context);

        this.mContext = context;
        this.psd=psd;
        this.flag=flag;
        this.passwordPayClickListener=passwordPayClickListener;

        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      LayoutInflater inflater = LayoutInflater.from(mContext);
        mMenuView = inflater.inflate(R.layout.pop_enter_password, null);
//        pwdView = (PasswordView) mMenuView.findViewById(R.id.pwd_view);
        payEditText = (PayEditText) mMenuView.findViewById(R.id.PayEditText_pay);
        keyboard = (Keyboard)mMenuView.findViewById(R.id.KeyboardView_pay);
        img_cancel = (ImageView)mMenuView.findViewById(R.id.img_cancel);
        tv_forgetPwd = (TextView)mMenuView.findViewById(R.id.tv_forgetPwd);

        setSubView();
        initEvent();
        // 监听X关闭按钮
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 监听键盘上方的返回
        keyboard.getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//添加密码输入完成的响应
        payEditText.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                try {
                    byte[ ] encrypted = AesCBC.AES_CBC_Encrypt(password.getBytes("UTF-8"), AesCBC.key.getBytes("UTF-8"), AesCBC.iv.getBytes("UTF-8"));
                    psd_jiami = Base64.encode(encrypted);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(psd_jiami.equals(psd)){
                    dismiss();
//                    Toast.makeText(mContext, "支付成功" , Toast.LENGTH_SHORT).show();
                        passwordPayClickListener.psdSuccess(flag);

                }else {
                    dismiss();
                    passwordPayClickListener.psdFailed();
                    Toast.makeText(mContext, "支付密码不正确" , Toast.LENGTH_SHORT).show();
                }
            }
        });

//        // 监听忘记密码的按钮
        tv_forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //跳转到支付密码修改界面、
                Intent intent = new Intent(mContext, ModifyPayPsdActivity.class);
                mContext.startActivity(intent);
            }
        });



        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x66000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }
    private void setSubView() {
        //设置键盘
        keyboard.setKeyboardKeys(KEY);
    }

    private void initEvent() {
        keyboard.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    payEditText.add(value);
                } else if (position == 9) {
//                    payEditText.remove();
                } else if (position == 11) {
                    payEditText.remove();
                    //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
//                    Toast.makeText(getApplication(), "您的密码是：" + payEditText.getText(), Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
        });
    }
}
