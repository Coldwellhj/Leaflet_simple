package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.publish;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.iceteck.silicompressorr.VideoCompress;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.UploadCallBack;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.UploadQNY;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.PhotoUtils;
import leaflet.miaoa.qmxh.leaflet_simple.utils.SystemUtil;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getToken;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;

//
//import com.dmcbig.mediapicker.PickerActivity;
//import com.dmcbig.mediapicker.PickerConfig;
//import com.dmcbig.mediapicker.entity.Media;


public class EditVideoActivity extends BaseOtherActivity implements View.OnClickListener ,UploadCallBack {
    private static int REQUEST_VIDEO_CODE = 10;
    String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private RelativeLayout iv_back;
    private RelativeLayout rl_addpicture;
    private RelativeLayout rl_addvideo;
    private EditText et_description;
    private RelativeLayout rl_next;
//    ArrayList<Media> select;
    private ImageView iv_addpicture;
    private ImageView iv_addvideo;
    private TextView take_photo;
    private TextView choose_picture;
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    LayoutInflater inflater;
    AlertDialog dialog;
    private QMUITipDialog LondingDialog;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri ;
    private File fileCropUri;
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    Bitmap bitmap;
    private String imagePath="";
    private String videoPath="";
    private List<String> imagePathList = new ArrayList<String>();
    private String videoTime="";
    private String token=""  ;//七牛云token
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        inflater = LayoutInflater.from(this);
        imagePathList.add(0,"");
        imagePathList.add(1,"");
        getToken();
        initView();
        Intent intent=getIntent();
        if("true".equals(intent.getStringExtra("takeVideo"))){
            videoPath=intent.getStringExtra("videoPath");
            videoTime=intent.getStringExtra("videoTime");
            imagePathList.set(1,videoPath);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
            iv_addvideo.setImageBitmap(bitmap);
        }

    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        rl_addpicture = (RelativeLayout) findViewById(R.id.rl_addpicture);
        rl_addvideo = (RelativeLayout) findViewById(R.id.rl_addvideo);
        et_description = (EditText) findViewById(R.id.et_description);
        rl_next = (RelativeLayout) findViewById(R.id.rl_next);
        iv_back.setOnClickListener(this);
        rl_next.setOnClickListener(this);
        rl_addpicture.setOnClickListener(this);
        rl_addvideo.setOnClickListener(this);
        iv_addpicture = (ImageView) findViewById(R.id.iv_addpicture);
        iv_addvideo = (ImageView) findViewById(R.id.iv_addvideo);
    }
    @Override
    protected void onResume() {
        super.onResume();
        fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" +getPhotoFileName());
        fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/"+getPhotoFileName());
    }
    private boolean submit() {
        // validate
        String description = et_description.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "请填写活动内容，活动描述，活动地点、日期、店铺等", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_addvideo:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission(this);
                }else{
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    startActivityForResult(intent, REQUEST_VIDEO_CODE);
                }
                break;
           
                case R.id.rl_addpicture:
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(
                            EditVideoActivity.this);
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
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_next:
                if(submit()){
                    if(Common.isNOT_Null(videoPath)&&Common.isNOT_Null(imagePath)){
                        Intent intent=new Intent(EditVideoActivity.this,PublishscopeActivity.class);
                        intent.putExtra("content",et_description.getText().toString().trim());
                        intent.putExtra("aType","true");
                        intent.putExtra("videoTime",videoTime);
                        intent.putStringArrayListExtra("imagePathList", (ArrayList<String>) imagePathList);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    private void autoObtainCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission_camera(this);
        } else {
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
    private void autoObtainStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission_picture(this);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
      
    }

    public  void checkPermission(Activity object) {


        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(object, Manifest.permission.READ_EXTERNAL_STORAGE);
    int CAMERA = ContextCompat.checkSelfPermission(object, Manifest.permission.CAMERA);


        int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        if (CAMERA != PERMISSION_GRANTED || READ_EXTERNAL_STORAGE != PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(object,
                    permissions,
                    1);

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("video/*");
            startActivityForResult(intent, REQUEST_VIDEO_CODE);
        }
    }
    public  void checkPermission_picture(Activity object) {


        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(object, Manifest.permission.READ_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(object, Manifest.permission.CAMERA);


        int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        if (READ_EXTERNAL_STORAGE != PERMISSION_GRANTED || CAMERA != PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(object,
                    permissions,
                    2);

        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }
    public  void checkPermission_camera(Activity object) {


        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(object, Manifest.permission.READ_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(object, Manifest.permission.CAMERA);


        int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        if (READ_EXTERNAL_STORAGE != PERMISSION_GRANTED || CAMERA != PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(object,
                    permissions,
                    3);

        } else {
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: 
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                  Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                  intent.setType("video/*");
                    startActivityForResult(intent, REQUEST_VIDEO_CODE);
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2: 
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        imagePath = Uri.decode(filePath);
                        if(Common.isNOT_Null(token)){
                            UploadQNY uploadQNY=new UploadQNY(this,token,imagePath);
                            uploadQNY.upload();

                        }else {
                            getToken();
                            ToastUtils.showShort(this,"token获取失败，请重新上传");
                        }

                        iv_addpicture.setImageBitmap(bitmap);

                    }
                    break;

                default:
            }
        }
        if (requestCode == REQUEST_VIDEO_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                /**
                 * 数据库查询操作。
                 * 第一个参数 uri：为要查询的数据库+表的名称。
                 * 第二个参数 projection ： 要查询的列。
                 * 第三个参数 selection ： 查询的条件，相当于SQL where。
                 * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
                 * 第四个参数 sortOrder ： 结果排序。
                 */
                Cursor cursor = cr.query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // 视频ID:MediaStore.Audio.Media._ID
                        int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        // 视频名称：MediaStore.Audio.Media.TITLE
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        // 视频路径：MediaStore.Audio.Media.DATA
                        videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        // 视频时长：MediaStore.Audio.Media.DURATION
                        int duration=0;
                        String s= SystemUtil.getDeviceBrand();
                        if("Xiaomi".equals(SystemUtil.getDeviceBrand())){

                            duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        }else {
                            duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))/1000;
                        }

                        // 视频大小：MediaStore.Audio.Media.SIZE
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                        // 视频缩略图路径：MediaStore.Images.Media.DATA
                        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        // 缩略图ID:MediaStore.Audio.Media._ID
                        int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));

                        // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                        // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。
                        Bitmap bitmap1 = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

                        // 方法二 ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                        // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
                        final Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                        // 如果追求更好的话可以利用 ThumbnailUtils.extractThumbnail 把缩略图转化为的制定大小
//                        ThumbnailUtils.extractThumbnail(bitmap, width,height ,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//                        setText(tv_VideoPath, R.string.path, videoPath);
//                        setText(tv_VideoDuration, R.string.duration, String.valueOf(duration));
//                        setText(tv_VideoSize, R.string.size, String.valueOf(size));
//                        setText(tv_VideoTitle, R.string.title, title);
                        if(duration<=60){

                            final String destPath = outputDir + File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";

                            final int finalDuration = duration;
                            VideoCompress.compressVideoLow(videoPath, destPath, new VideoCompress.CompressListener() {
                                @Override
                                public void onStart() {

                                    LondingDialog = new QMUITipDialog.Builder(EditVideoActivity.this)
                                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                            .setTipWord("视频处理中···")
                                            .create();

                                    LondingDialog.show();
                                }

                                @Override
                                public void onSuccess() {
                                    LondingDialog.dismiss();
                                    iv_addvideo.setImageBitmap(bitmap2);
                                    imagePathList.set(1,destPath);

                                    videoTime= finalDuration +"";
                                }

                                @Override
                                public void onFail() {
                                    LondingDialog.dismiss();
                                        ToastUtils.showShort(EditVideoActivity.this,"视频处理失败，请重新选择");
                                }

                                @Override
                                public void onProgress(float percent) {
                                    Log.i("tag",String.valueOf(percent) + "%");
                                }
                            });

                        }else {
                            videoPath="";
                        }

                    }
                    cursor.close();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }
    @Override
    public void UploadImageStr(String imageName) {
        imagePathList.set(0,imageName);
    }

    @Override
    public void UploadMoreImageStr(String coverimg, String picture) {

    }
    @Override
    public void sendUploadprogress(double percent) {
//        int progress = (int)(percent*100);
//        if(progress==100){
//            Message msg = new Message();
//            msg.what = STOP;
//            myHandler.sendMessage(msg);
//        }else {
//            Message msg = new Message();
//            msg.what = NEXT;
//            msg.arg1 = progress;
//            myHandler.sendMessage(msg);
//        }

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

                            Toast.makeText(EditVideoActivity.this, "获取token异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(EditVideoActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
