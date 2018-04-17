package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.publish;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditPictureAdvActivity extends BaseOtherActivity implements EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate ,View.OnClickListener{
    private BGASortableNinePhotoLayout mPhotosSnpl;
    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    private TextView send;
    private List<String> imagePathList = new ArrayList<String>();
    private RelativeLayout iv_back;
    private EditText et_description;
    private RelativeLayout rl_next;
    private String videoTime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture_adv);
        initView();
        init();
    }

    private void init() {
        //加号
        mPhotosSnpl.setEditable(true);
        mPhotosSnpl.setPlusEnable(true);
        //可以拖拽
        mPhotosSnpl.setSortable(false);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        mPhotosSnpl.setMaxItemCount(12);
    }

    private void initView() {
        mPhotosSnpl = (BGASortableNinePhotoLayout) findViewById(R.id.snpl_moment_add_photos);
//        send = (TextView) findViewById(R.id.send);
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                UploadImage myuploadImage=new UploadImage(MainActivity.this,"http://10.10.24.9:8080/MiaoA/uploadimage");
//                myuploadImage.uploadImage(imagePathList.get(0));
//            }
//        });
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        et_description = (EditText) findViewById(R.id.et_description);

        rl_next = (RelativeLayout) findViewById(R.id.rl_next);
        rl_next.setOnClickListener(this);
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
        imagePathList.remove(position);

    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
//        Toast.makeText(this, "排序发生变化", Toast.LENGTH_SHORT).show();
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mPhotosSnpl.getMaxItemCount() ) // 图片选择张数的最大值
                    .selectedPhotos(mPhotosSnpl.getData()) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            imagePathList = BGAPhotoPickerActivity.getSelectedPhotos(data);
            int size=mPhotosSnpl.getItemCount();
            for (int i=0;i<size;i++){
                mPhotosSnpl.removeItem(0);
            }

            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));

        } else if (requestCode == RC_PHOTO_PREVIEW) {
            imagePathList = BGAPhotoPickerActivity.getSelectedPhotos(data);
            for (int i=0;i<mPhotosSnpl.getItemCount();i++){
                mPhotosSnpl.removeItem(0);
            }
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                    finish();
                break;
            case R.id.rl_next:
                if(Common.isNOT_Null(et_description.getText().toString().trim())&&imagePathList.size()!=0){
                    videoTime=imagePathList.size()*2+"";
                    Intent intent=new Intent(EditPictureAdvActivity.this,PublishscopeActivity.class);
                    intent.putExtra("content",et_description.getText().toString().trim());
                    intent.putExtra("aType","false");
                    intent.putExtra("videoTime",videoTime);
                    intent.putStringArrayListExtra("imagePathList", (ArrayList<String>) imagePathList);
                    startActivity(intent);
                }else {
                    ToastUtils.showShort(this,"请填写完整信息");
                }

                break;

        }
    }
}
