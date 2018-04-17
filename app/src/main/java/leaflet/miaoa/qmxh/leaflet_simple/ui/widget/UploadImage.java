package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.UploadCallBack;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.picture_yun;

/**
 * Created by gaofeng on 2018/1/17.
 */

public class UploadImage {
    Activity activity;
    String url;
    UploadCallBack uploadCallBack;
    public UploadImage(UploadCallBack uploadCallBack, Activity activity, String url){
        this.uploadCallBack=uploadCallBack;
        this.activity=activity;
        this.url=url;
    }
    /**
     * 上传图片
     * @param imagePath
     */
    public void uploadImage(String imagePath) {
        NetworkTask networkTask= new NetworkTask();
        networkTask.execute(imagePath);
        new Thread(){
            public void run(){
                try{
                    networkTask.get(1000000, TimeUnit.MILLISECONDS);
                } catch(TimeoutException e){
//                    returnvalue = "请求超时！";
//                    jsCallback(F_IMAGES_PICK, 0,
//                            EUExCallback.F_C_TEXT, returnvalue);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if("error".equals(result)){
                uploadCallBack.UploadImageStr("");
            }else {
                String Str[]=result.split("<-->");
                if("true".equals(Str[0])) {
//                Log.i(TAG, "图片地址 " + Constant.BASE_URL + result);
                    uploadCallBack.UploadImageStr(picture_yun+Str[1]);
                }
            }

        }
    }

    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(url)
                .post(requestBody)
                .build();


//        Log.d(TAG, "请求地址 " + Constant.BASE_URL + "/uploadimage");
        try{
            Response response = mOkHttpClient.newCall(request).execute();
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
