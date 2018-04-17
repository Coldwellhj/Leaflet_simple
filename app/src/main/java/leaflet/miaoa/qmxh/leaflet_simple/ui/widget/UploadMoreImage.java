package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.File;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.UploadCallBack;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.picture_yun;

/**
 * Created by gaofeng on 2018/1/17.
 */

public class UploadMoreImage {
    Activity activity;
    String url;
    UploadCallBack uploadCallBack;
    public UploadMoreImage(UploadCallBack uploadCallBack, Activity activity, String url){
        this.uploadCallBack=uploadCallBack;
        this.activity=activity;
        this.url=url;
    }
    /**
     * 上传图片
     * @param imagePath
     */
    public void uploadmoreImage(List<String> imagePath) {
        new NetworkTask().execute(imagePath);
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<List<String> , Integer, String> {


        @Override
        protected String doInBackground(List<String>[] lists) {
            return doPost(lists[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

//        @Override
//        protected String doInBackground(String...  params) {
//            return doPost(params[0]);
//        }

        @Override
        protected void onPostExecute(String result) {
            if("error".equals(result)){
                uploadCallBack.UploadMoreImageStr("","");
            }else{
                String Str[]=result.split("<-->");
                String aMatter="";
                for (int i=1;i<Str.length;i++){
                    if(i==Str.length-1){
                        aMatter+=picture_yun+Str[i];
                    }else {
                        aMatter+=picture_yun+Str[i]+"<-->";
                    }
                }
                if("true".equals(Str[0])) {
//                Log.i(TAG, "图片地址 " + Constant.BASE_URL + result);
                    uploadCallBack.UploadMoreImageStr(picture_yun+Str[1],aMatter);
                }
            }

        }
    }

    private String doPost(List<String> imagePathList) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
        builder.addFormDataPart("userId", "20160519142605");
        for (int i=0;i<imagePathList.size();i++){
            builder.addFormDataPart("image", imagePathList.get(i),
                    RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePathList.get(i))));
        }
//        builder.addFormDataPart("image", imagePathList+ "/" +getPhotoFileName(),
//                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePathList)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(url)
                .post(requestBody)
                .build();

//        Log.d(TAG, "请求地址 " + Constant.BASE_URL + "/uploadimage");
        try{
            okhttp3.Response response = mOkHttpClient.newCall(request).execute();
//            Log.d(TAG, "响应码 " + response.code());
            if (response.isSuccessful()) {
                result = response.body().string();
//                Log.d(TAG, "响应体 " + resultValue);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
