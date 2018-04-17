package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import leaflet.miaoa.qmxh.leaflet_simple.Login.ResetPasswordActivity;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.UploadCallBack;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.PersonalUserHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.UploadQNY;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.PhotoUtils;
import leaflet.miaoa.qmxh.leaflet_simple.utils.StringUtils;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getToken;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.modifyQueryImg;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.usersaction_modifyBusiness;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getStrFromDate;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getTimeFromLong;


public class Personal_mine_info_Activity extends BaseOtherActivity implements View.OnClickListener,UploadCallBack {

    private RelativeLayout iv_personal_information_back;
    private ImageView iv_photo;
    private TextView tv_personal_information_nickname;
    private RelativeLayout rl_personal_information_modify_head_portrait;
    private RelativeLayout rl_personal_information_modify_nickname;
    private TextView tv_personal_information_sex;
    private RelativeLayout rl_personal_information_modify_sex;
    private TextView tv_personal_information_birthday;
    private RelativeLayout rl_personal_information_modify_birthday;
    private RelativeLayout rl_personal_information_resetpassword;
    private RelativeLayout rl_personal_information_resetpaypassword;
    private TextView tv_personal_information_telephone;
    private RelativeLayout rl_personal_information_modify_phonenumber;
    private TextView tv_modify_sex_man;
    private TextView tv_modify_sex_woman;
    private RelativeLayout rl_cancel;
    private TextView take_photo;
    private TextView choose_picture;

    LayoutInflater inflater;
    AlertDialog dialog;

    private TextView tv_title;
    private EditText ed_updatainfo;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String[] permissions = { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri ;
    private File fileCropUri;
    private Uri imageUri;
    private Uri cropImageUri;
    private String uNick="";
    private String uSex="";
    private Long uBirthday= Long.valueOf(0);
    private String uImg="";
    private Bitmap bitmap;
    private QMUITipDialog uploadDialog;
    private String token=""  ;//七牛云token
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_mine_info_);
        initView();
        sharedPreferences = Personal_mine_info_Activity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        inflater = LayoutInflater.from(this);
        getToken();
        try{
            Intent intent=this.getIntent();
            uNick =intent.getStringExtra("uNick");
            uSex =intent.getStringExtra("uSex");

            uImg =intent.getStringExtra("uImg");
            tv_personal_information_telephone.setText(Usertel);
            tv_personal_information_nickname.setText(uNick);
            tv_personal_information_sex.setText(uSex);
            Picasso.with(this).load( uImg).into(iv_photo);
            uBirthday =Long.parseLong(intent.getStringExtra("uBirthday"));
            tv_personal_information_birthday.setText(getStrFromDate(getTimeFromLong(uBirthday)));

        }catch (Exception e){
            System.out.println(e);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" +getPhotoFileName());
        fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/"+getPhotoFileName());
    }
    private void initView() {
        iv_personal_information_back = (RelativeLayout) findViewById(R.id.iv_personal_information_back);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        tv_personal_information_nickname = (TextView) findViewById(R.id.tv_personal_information_nickname);
        rl_personal_information_modify_head_portrait = (RelativeLayout) findViewById(R.id.rl_personal_information_modify_head_portrait);
        rl_personal_information_modify_nickname = (RelativeLayout) findViewById(R.id.rl_personal_information_modify_nickname);
        tv_personal_information_sex = (TextView) findViewById(R.id.tv_personal_information_sex);
        rl_personal_information_modify_sex = (RelativeLayout) findViewById(R.id.rl_personal_information_modify_sex);
        tv_personal_information_birthday = (TextView) findViewById(R.id.tv_personal_information_birthday);
        rl_personal_information_modify_birthday = (RelativeLayout) findViewById(R.id.rl_personal_information_modify_birthday);
        rl_personal_information_resetpassword = (RelativeLayout) findViewById(R.id.rl_personal_information_resetpassword);
        rl_personal_information_resetpaypassword = (RelativeLayout) findViewById(R.id.rl_personal_information_resetpaypassword);
        tv_personal_information_telephone = (TextView) findViewById(R.id.tv_personal_information_telephone);
        rl_personal_information_modify_phonenumber = (RelativeLayout) findViewById(R.id.rl_personal_information_modify_phonenumber);
        iv_personal_information_back.setOnClickListener(this);

        rl_personal_information_modify_head_portrait.setOnClickListener(this);
        rl_personal_information_modify_nickname.setOnClickListener(this);
        rl_personal_information_modify_sex.setOnClickListener(this);
        rl_personal_information_modify_birthday.setOnClickListener(this);
        rl_personal_information_resetpassword.setOnClickListener(this);
        rl_personal_information_resetpaypassword.setOnClickListener(this);
        rl_personal_information_modify_phonenumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.iv_personal_information_back:
                Intent intent1 = Personal_mine_info_Activity.this.getIntent();
                Bundle bundle = intent1.getExtras();
                bundle.putString("photoName", uImg);//添加要返回给页面1的数据
                intent1.putExtras(bundle);
                Personal_mine_info_Activity.this.setResult(Activity.RESULT_OK, intent1);//返回页面1
                finish();
                break;
            case R.id.rl_personal_information_modify_birthday:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = View.inflate(this, R.layout.date_time_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);

                builder.setView(view);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);


//                datePicker.setMinDate(cal.getTimeInMillis());
//                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 15);
//                datePicker.setMaxDate(cal.getTimeInMillis());

                final int inType = tv_personal_information_birthday.getInputType();
                tv_personal_information_birthday.setInputType(InputType.TYPE_NULL);
//                tv_personal_information_birthday.onTouchEvent(event);
                tv_personal_information_birthday.setInputType(inType);
                //etStartTime.setSelection(etStartTime.getText().length());

                builder.setTitle("选择你的生日");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        String birthday=sb.toString();
                        progressDialog = new ProgressDialog(Personal_mine_info_Activity.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("正在保存中...");
                        progressDialog.show();
                        updateuserinfo_birthday(birthday);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog_birthday = builder.create();
                dialog_birthday.show();

                break;
            case R.id.rl_personal_information_resetpassword:
                intent.setClass(this, ResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_personal_information_resetpaypassword:
                intent.setClass(this, ModifyPayPsdActivity.class);
                startActivity(intent);
                break;
            //修改手机号码
            case R.id.rl_personal_information_modify_phonenumber:
                intent.setClass(this,ModifyPhonenumberActivity.class);
                startActivityForResult(intent, 0);


                break;
            //修改性别
            case R.id.rl_personal_information_modify_sex:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                        Personal_mine_info_Activity.this);
                View view1 = inflater.inflate(R.layout.dilog_personalupdatesex, null);
                tv_modify_sex_man = (TextView) view1.findViewById(R.id.tv_modify_sex_man);
                tv_modify_sex_woman = (TextView) view1.findViewById(R.id.tv_modify_sex_woman);
                rl_cancel = (RelativeLayout) view1.findViewById(R.id.rl_cancel);
                tv_modify_sex_man.setOnClickListener(this);

                tv_modify_sex_woman.setOnClickListener(this);
                builder1.setView(view1);
                dialog = builder1.create();

                dialog.show();
                break;
            case R.id.tv_modify_sex_man:
                progressDialog = new ProgressDialog(Personal_mine_info_Activity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("正在保存中...");
                progressDialog.show();
                updateuserinfo_Sex("男");
                dialog.dismiss();
                break;
            case R.id.tv_modify_sex_woman:
                progressDialog = new ProgressDialog(Personal_mine_info_Activity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("正在保存中...");
                progressDialog.show();
                updateuserinfo_Sex("女");
                dialog.dismiss();
                break;
            case R.id.iv_personal_information_jumptohome:
                finish();
                intent.setClass(this, PersonalUserHomePageActivity.class);
                startActivity(intent);
                break;
            //修改昵称
            case R.id.rl_personal_information_modify_nickname:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(
                        Personal_mine_info_Activity.this);
                View view2 = inflater.inflate(R.layout.dilog_personalupdatenickname, null);
                tv_title = (TextView) view2.findViewById(R.id.tv_title);
                tv_title.setText("输入新的昵称");

                ed_updatainfo = (EditText) view2.findViewById(R.id.ed_updatainfo);
                ed_updatainfo.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int index = ed_updatainfo.getSelectionStart() - 1;
                        if (index > 0) {
                            if (StringUtils.isEmojiCharacter(s.charAt(index))) {
                                Editable edit = ed_updatainfo.getText();
                                edit.delete(s.length() - 2, s.length());
                                ToastUtils.showShort(Personal_mine_info_Activity.this, "不支持输入表情符号");
                            }
                        }

                    }
                });
//                ed_updatainfo.setText(tv_personal_information_nickname.getText().toString());
                builder2.setView(view2);
                builder2.setPositiveButton(
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface,
                                    int which) {
                                // TODO Auto-generated method
                                progressDialog = new ProgressDialog(Personal_mine_info_Activity.this,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("正在保存中...");
                                progressDialog.show();
                                submit();

                            }
                        });
                builder2.setNegativeButton(
                        getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface,
                                    int which) {
                                // TODO Auto-generated method
                                // stub
                                dialogInterface.dismiss();
                            }
                        });
                builder2.create().show();
                break;
            //修改头像
            case R.id.rl_personal_information_modify_head_portrait:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(
                        Personal_mine_info_Activity.this);
                View view3 = inflater.inflate(R.layout.upload_photo, null);
                take_photo = (TextView) view3.findViewById(R.id.take_photo);
                take_photo.setOnClickListener(this);
                choose_picture = (TextView) view3.findViewById(R.id.choose_picture);
                choose_picture.setOnClickListener(this);
                builder3.setView(view3);

                dialog = builder3.create();

                dialog.show();
                break;

            case R.id.take_photo:
                autoObtainCameraPermission();

                dialog.dismiss();
//                Toast.makeText(this,"1111",Toast.LENGTH_SHORT).show();
                break;
            case R.id.choose_picture:
//                Toast.makeText(this,"2222",Toast.LENGTH_SHORT).show();
                autoObtainStoragePermission();
                dialog.dismiss();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent1 = Personal_mine_info_Activity.this.getIntent();
            Bundle bundle = intent1.getExtras();
            bundle.putString("photoName", uImg);//添加要返回给页面1的数据
            intent1.putExtras(bundle);
            Personal_mine_info_Activity.this.setResult(Activity.RESULT_OK, intent1);//返回页面1
            finish();
        }
        return false;
    }
    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
//                    imageUri = FileProvider.getUriForFile(Personal_mine_info_Activity.this, "leaflet.miaoa.qmsh.leaflet.fileProvider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(Personal_mine_info_Activity.this, "leaflet.miaoa.qmsh.leaflet.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
//                            newUri = FileProvider.getUriForFile(this, "leaflet.miaoa.qmsh.leaflet.fileProvider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        //保存到服务器
                        String filePath = cropImageUri.getEncodedPath();
                        final String imagePath = Uri.decode(filePath);
                        if(Common.isNOT_Null(token)){
                            UploadQNY uploadQNY=new UploadQNY(this,token,imagePath);
                            uploadQNY.upload();
                            uploadDialog = new QMUITipDialog.Builder(this)
                                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                    .setTipWord("正在修改")
                                    .create();
                            uploadDialog.show();

                        }else {
                            getToken();
                            ToastUtils.showShort(this,"token获取失败，请重新上传");
                        }

                    }
                    break;
                case Activity.RESULT_OK:
                    Bundle bundle = data.getExtras();
                    String newNum= bundle.getString("newNum");
                    tv_personal_information_telephone.setText(newNum);
                    break;
                default:
            }
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        System.out.println("========1111====" + UUID.randomUUID());
        return sdf.format(date) + "_" + UUID.randomUUID() + ".jpg";
    }
    private void submit() {
        // validate
        String updateinfo = ed_updatainfo.getText().toString().trim();
        if (TextUtils.isEmpty(updateinfo)) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {

                updateuserinfo_nickName( updateinfo);
//            } else if ("usersex".equals(temp)) {
//                updateuserinfo_Sex(updateinfo);
//            }


        }
    }

    private void updateuserinfo_nickName(final String nickName) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                usersaction_modifyBusiness + "?uNum=" + Usertel +"&uNick="+nickName ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                            if("true".equals(response)){
                                tv_personal_information_nickname.setText(nickName);
                                editor.putString("nickName", nickName);
                                editor.commit();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Personal_mine_info_Activity.this, "修改失败，数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Personal_mine_info_Activity.this, "修改失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void updateuserinfo_Sex(final String sex) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                usersaction_modifyBusiness + "?uNum=" + Usertel +"&uSex="+sex ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                            if("true".equals(response)){
                                tv_personal_information_sex.setText(sex);
                                editor.putString("sex", sex);
                                editor.commit();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Personal_mine_info_Activity.this, "修改失败，数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Personal_mine_info_Activity.this, "修改失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void updateuserinfo_birthday(final String birthday) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                usersaction_modifyBusiness + "?uNum=" + Usertel +"&uBirthday="+birthday ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                            if("true".equals(response)){
                                tv_personal_information_birthday.setText(birthday);
                                editor.putString("birthday", birthday);
                                editor.commit();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Personal_mine_info_Activity.this, "修改失败，数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Personal_mine_info_Activity.this, "修改失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    //图片存入数据库
    @Override
    public void UploadImageStr(final String imageName) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                modifyQueryImg + "?uNum=" + Usertel +"&uImg="+imageName ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {

                            uploadDialog.dismiss();
                            iv_photo.setImageBitmap(bitmap);
                            uImg=imageName;
                        } catch (Exception e) {

                            Toast.makeText(Personal_mine_info_Activity.this, "修改失败，数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Personal_mine_info_Activity.this, "修改失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    @Override
    public void UploadMoreImageStr(String coverimg, String picture) {

    }
    @Override
    public void sendUploadprogress(double percent) {


    }
    private void getToken(){




        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getToken,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            token=response;

                        } catch (Exception e) {

                            Toast.makeText(Personal_mine_info_Activity.this, "获取token异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Personal_mine_info_Activity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
