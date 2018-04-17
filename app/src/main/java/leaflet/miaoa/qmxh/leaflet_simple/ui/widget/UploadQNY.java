package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.UploadCallBack;

import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.picture_yun;

/**
 * Created by gaofeng on 2018/3/23.
 */

public class UploadQNY {
    private volatile boolean isCancelled = false;
    private String token="";
    private String filePath;//文件路径
    private String fileName;//文件名称
    private String firstfileName;//多文件是第一个文件名称
    private String aMatter="";//多文件名称
    private List<String> filePathList;//多文件路径
    UploadCallBack uploadCallBack;
    UploadManager uploadManager;
    int fileSuccessCount=0;
    public UploadQNY(UploadCallBack uploadCallBack, String token, List<String> filePathList){
        this.uploadCallBack=uploadCallBack;
        this.token=token;
        this.filePathList=filePathList;

        //断点上传
        String dirPath = "/storage/emulated/0/Download";
        Recorder recorder = null;
        try{
            File f = File.createTempFile("qiniu_xxxx", ".tmp");
            Log.d("qiniu", f.getAbsolutePath().toString());
            dirPath = f.getParent();
            recorder = new FileRecorder(dirPath);
        } catch(Exception e) {
            e.printStackTrace();
        }

        final String dirPath1 = dirPath;
        //默认使用 key 的url_safe_base64编码字符串作为断点记录文件的文件名。
        //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
        KeyGenerator keyGen = new KeyGenerator(){
            public String gen(String key, File file){
                // 不必使用url_safe_base64转换，uploadManager内部会处理
                // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
                String path = key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                Log.d("qiniu", path);
                File f = new File(dirPath1, UrlSafeBase64.encodeToString(path));

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(f));
                    String tempString = null;
                    int line = 1;
                    try {
                        while ((tempString = reader.readLine()) != null) {
//							System.out.println("line " + line + ": " + tempString);
                            Log.d("qiniu", "line " + line + ": " + tempString);
                            line++;
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try{
                            reader.close();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }




                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return path;
            }
        };

        Configuration config = new Configuration.Builder()
                // recorder 分片上传时，已上传片记录器
                // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .recorder(recorder, keyGen)
                .build();
        // 实例化一个上传的实例
        uploadManager = new UploadManager(config);
    }
    public UploadQNY(UploadCallBack uploadCallBack, String token, String filePath){
        this.uploadCallBack=uploadCallBack;
        this.token=token;
        this.filePath=filePath;

        //断点上传
        String dirPath = "/storage/emulated/0/Download";
        Recorder recorder = null;
        try{
            File f = File.createTempFile("qiniu_xxxx", ".tmp");
            Log.d("qiniu", f.getAbsolutePath().toString());
            dirPath = f.getParent();
            recorder = new FileRecorder(dirPath);
        } catch(Exception e) {
            e.printStackTrace();
        }

        final String dirPath1 = dirPath;
        //默认使用 key 的url_safe_base64编码字符串作为断点记录文件的文件名。
        //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
        KeyGenerator keyGen = new KeyGenerator(){
            public String gen(String key, File file){
                // 不必使用url_safe_base64转换，uploadManager内部会处理
                // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
                String path = key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                Log.d("qiniu", path);
                File f = new File(dirPath1, UrlSafeBase64.encodeToString(path));

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(f));
                    String tempString = null;
                    int line = 1;
                    try {
                        while ((tempString = reader.readLine()) != null) {
//							System.out.println("line " + line + ": " + tempString);
                            Log.d("qiniu", "line " + line + ": " + tempString);
                            line++;
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try{
                            reader.close();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }




                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return path;
            }
        };

        Configuration config = new Configuration.Builder()
                // recorder 分片上传时，已上传片记录器
                // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .recorder(recorder, keyGen)
                .build();
        // 实例化一个上传的实例
        uploadManager = new UploadManager(config);
    }

    public void upload(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("x:phone", "12345678");

        Log.d("qiniu", "click upload");
        isCancelled = false;
        fileName=getPhotoFileName();
        uploadManager.put(filePath,fileName , token,
                new UpCompletionHandler() {
                    public void complete(String key,
                                         ResponseInfo info, JSONObject res) {
                        Log.i("qiniu", key + ",\r\n " + info
                                + ",\r\n " + res);

                        if(info.isOK()==true){
//                            textview.setText(res.toString());
                            //上传成功
                                    uploadCallBack.UploadImageStr(picture_yun+fileName);
                        }
                    }
                }, new UploadOptions(map, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent){
                                Log.i("qiniu", key + ": " + percent);
//                                progressbar.setVisibility(View.VISIBLE);
//                                int progress = (int)(percent*1000);
////											Log.d("qiniu", progress+"");
//                                progressbar.setProgress(progress);
//                                if(progress==1000){
//                                    progressbar.setVisibility(View.GONE);
//                                }
                            }

                        }, new UpCancellationSignal(){
                    @Override
                    public boolean isCancelled() {

                        return isCancelled;
                    }
                }));
    }
    public void uploadVideo(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("x:phone", "12345678");

        Log.d("qiniu", "click upload");
        isCancelled = false;
        fileName=getVideoFileName();
        uploadManager.put(filePath, fileName , token,
                new UpCompletionHandler() {
                    public void complete(String key,
                                         ResponseInfo info, JSONObject res) {
                        Log.i("qiniu", key + ",\r\n " + info
                                + ",\r\n " + res);

                        if(info.isOK()==true){
//                            textview.setText(res.toString());
                            //上传成功
                            uploadCallBack.UploadImageStr(picture_yun+fileName);
                        }
                    }
                }, new UploadOptions(map, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent){

                                Log.i("qiniu", key + ": " + percent);
//                                uploadCallBack.sendUploadprogress(percent);
//                                progressbar.setVisibility(View.VISIBLE);
//                                int progress = (int)(percent*1000);
////											Log.d("qiniu", progress+"");
//                                progressbar.setProgress(progress);
//                                if(progress==1000){
//                                    progressbar.setVisibility(View.GONE);
//                                }
                            }

                        }, new UpCancellationSignal(){
                    @Override
                    public boolean isCancelled() {

                        return isCancelled;
                    }
                }));
    }
    public void uploadMore() {


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("x:phone", "12345678");

        Log.d("qiniu", "click upload");
        isCancelled = false;

        for (int i = 0; i < filePathList.size(); i++) {
                fileName=getPhotoFileName();
                if(i==0){
                    firstfileName=picture_yun+fileName;
                }
                if(i==filePathList.size()-1){
                    aMatter+=picture_yun+fileName;
                }else {
                    aMatter+=picture_yun+fileName+"<-->";
                }

            uploadManager.put(filePathList.get(i), fileName , token,
                    new UpCompletionHandler() {
                        public void complete(String key,
                                             ResponseInfo info, JSONObject res) {
                            Log.i("qiniu", key + ",\r\n " + info
                                    + ",\r\n " + res);

                            if (info.isOK() == true) {
//                            textview.setText(res.toString());
                                //上传成功
                               fileSuccessCount++;
                                if(fileSuccessCount==filePathList.size()){
                                    uploadCallBack.UploadMoreImageStr(firstfileName,aMatter);
                                }
                            }
                        }
                    }, new UploadOptions(map, null, false,
                            new UpProgressHandler() {
                                public void progress(String key, double percent) {
                                    Log.i("qiniu", key + ": " + percent);
//                                    uploadCallBack.sendUploadprogress(percent);
//                                progressbar.setVisibility(View.VISIBLE);
//                                int progress = (int)(percent*1000);
////											Log.d("qiniu", progress+"");
//                                progressbar.setProgress(progress);
//                                if(progress==1000){
//                                    progressbar.setVisibility(View.GONE);
//                                }
                                }

                            }, new UpCancellationSignal() {
                        @Override
                        public boolean isCancelled() {

                            return isCancelled;
                        }
                    }));
        }
    }
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        return sdf.format(date) + "_" + UUID.randomUUID() + ".jpg";
    }
    // 使用系统当前日期加以调整作为视频的名称
    private String getVideoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        return sdf.format(date) + "_" + UUID.randomUUID() + ".mp4";
    }
}
