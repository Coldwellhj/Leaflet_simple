package leaflet.miaoa.qmxh.leaflet_simple.ui.Interface;

/**
 * Created by gaofeng on 2018/1/17.
 */

public interface UploadCallBack {
    public void UploadImageStr(String imageName);//上传图片和视频（单个）
    public void UploadMoreImageStr(String coverimg, String picture);//上传图片多张
    public void sendUploadprogress(double percent);//回调上传进度
}
