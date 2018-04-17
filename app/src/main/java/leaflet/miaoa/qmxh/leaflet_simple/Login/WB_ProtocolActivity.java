package leaflet.miaoa.qmxh.leaflet_simple.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;


public class WB_ProtocolActivity extends BaseOtherActivity {

    private RelativeLayout back;
    private RelativeLayout iv_refresh;
    private WebView wb_purl;
    private String pUrl;
    private String downloadUrl;
    // 下载应用的进度条
    private ProgressDialog progressDialog;
    File file;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();//关闭进度条
                    installApk();

                    break;
                case 2:


                    break;
                case 3:



                    break;

            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_wb_protocol_url);

        Intent intent =this.getIntent();
        pUrl=intent.getStringExtra("pUrl");
//        downloadUrl=intent.getStringExtra("downloadUrl");

        initView();
    }

    public  void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        iv_refresh = (RelativeLayout) findViewById(R.id.iv_refresh);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wb_purl.loadUrl(pUrl);
            }
        });
        wb_purl = (WebView) findViewById(R.id.wb_purl);
        wb_purl.setWebChromeClient(new WebChromeClient());
        wb_purl.getSettings().setJavaScriptEnabled(true);
        wb_purl.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb_purl.setWebViewClient(new MyWebViewClient());
        wb_purl.setDownloadListener(new MyWebViewDownLoadListener());
        wb_purl.getSettings().setUseWideViewPort(true);
        wb_purl.getSettings().setLoadWithOverviewMode(true);
        wb_purl.getSettings().setDomStorageEnabled(true);
        wb_purl.getSettings().setDatabaseEnabled(true);
        wb_purl.getSettings().setSupportZoom(true); // 支持缩放

        wb_purl.loadUrl(pUrl);
//        wb_purl.loadUrl("http://gdown.baidu.com/data/wisegame/7ca0b9691abd23ac/baidufanyi_73.apk");



    }

//    private class MyWebViewDownLoadListener implements DownloadListener {
//
//        @Override
//        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
//                                    long contentLength) {
//            Uri uri = Uri.parse(url);
//            Message message = handler.obtainMessage();
//            message.what = 1;
//            handler.sendMessage(message);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//        }
//
//    }
    public class MyWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //暂时处理url获取混乱的问题
            try{
                if(url.contains("http://a.app.qq.com/o/simple.jsp")){
//                url="http://a.app.qq.com/o/simple.jsp?pkgname=com.qihoo.loan";
                    if(downloadUrl!=null){
                        url=downloadUrl;

                    }

                }
                if(url.contains(".apk")){
                    showDownloadDialog();
                }


                view.loadUrl(url);
                 view.setDownloadListener(new MyWebViewDownLoadListener());

//                if (!TextUtils.isEmpty(url) && url.endsWith("apk")) {
//                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(viewIntent);
//                }else{
//                    view.loadUrl(url);
//                    view.setDownloadListener(new MyWebViewDownLoadListener());
//                }
            }catch(Exception e){
                view.loadUrl(url);
                view.setDownloadListener(new MyWebViewDownLoadListener());
            }





            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            view.loadUrl(url);
//            view.setDownloadListener(new MyWebViewDownLoadListener());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                view.loadUrl("file:///android_asset/index.html");



        }
    }

//    //内部类
//    public class MyWebViewClient extends WebViewClient {
//        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
//        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
//        public boolean shouldOverviewUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            showProgressDialog();
//        }
//
//        public void onPageFinished(WebView view, String url) {
//            closeProgressDialog();
//        }
//
//        public void onReceivedError(WebView view, int errorCode,
//                                    String description, String failingUrl) {
//            closeProgressDialog();
//            view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//                view.loadUrl("file:///android_asset/index.html");
//        }
//    }

    //内部类
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast t=Toast.makeText(WB_ProtocolActivity.this, "需要SD卡。", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();

                return;
            }

            downFile(url);
//            DownloaderTask task=new DownloaderTask();
//            task.execute("http://gdown.baidu.com/data/wisegame/7ca0b9691abd23ac/baidufanyi_73.apk");
        }

    }
    //内部类
//    private class DownloaderTask extends AsyncTask<String, Void, String> {
//
//        public DownloaderTask() {
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            String url=params[0];
//            String fileName=null;
//            URL myURL = null;
//            try {
//                myURL = new URL(url);
//                URLConnection conn = myURL.openConnection();
//                conn.connect();
//
//                if(((HttpURLConnection) conn).getResponseCode()==200){
//                    String file = conn.getURL().getFile();
//                    fileName = file.substring(file.lastIndexOf('/')+1);
//
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
////          Log.i("tag", "url="+url);
////            if(url.contains(".exe")){
//////                fileName=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf(".exe")+4);
////                fileName="QQPhoneManagerWeb_990499.1028.exe";
////            }else {
////                String urlsub=url.substring(url.indexOf("fsname"),url.);
////                fileName=url.substring(url.lastIndexOf("/")+1);
////            }
//
//
//            fileName= URLDecoder.decode(fileName);
//            Log.i("tag", "fileName="+fileName);
//
//            File directory=Environment.getExternalStorageDirectory();
//            File file=new File(directory,fileName);
//            if(file.exists()){
//                Log.i("tag", "The file has already exists.");
//                return fileName;
//            }
//            try {
//                HttpClient client = new DefaultHttpClient();
////                client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
//                HttpGet get = new HttpGet(url);
//                HttpResponse response = client.execute(get);
//                if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){
//                    HttpEntity entity = response.getEntity();
//                    InputStream input = entity.getContent();
//                    Long fileLength=entity.getContentLength();
//                    writeToSDCard(fileName,input,fileLength);
//
//                    input.close();
////                  entity.consumeContent();
//                    return fileName;
//                }else{
//                    return null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            // TODO Auto-generated method stub
//            super.onCancelled();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            closeProgressDialog();
//            progressDialog.dismiss();//关闭进度条
//            if(result==null){
//                Toast t=Toast.makeText(WB_pUrlActivity.this, "连接错误！请稍后再试！", Toast.LENGTH_LONG);
//                t.setGravity(Gravity.CENTER, 0, 0);
//                t.show();
//                return;
//            }
//
//            Toast t=Toast.makeText(WB_pUrlActivity.this, "已保存到SD卡。", Toast.LENGTH_LONG);
//            t.setGravity(Gravity.CENTER, 0, 0);
//            t.show();
//            File directory= Environment.getExternalStorageDirectory();
//            File file=new File(directory,result);
//            Log.i("tag", "Path="+file.getAbsolutePath());
//
//            Intent intent = getFileIntent(file);
//
//            startActivity(intent);
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            showProgressDialog();
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            // TODO Auto-generated method stub
//            super.onProgressUpdate(values);
//        }
//
//
//    }


    private ProgressDialog mDialog;
    private void showProgressDialog(){
        if(mDialog==null){
            mDialog = new ProgressDialog(this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            mDialog.setMessage("正在加载 ，请等待...");
            mDialog.setIndeterminate(false);//设置进度条是否为不明确
            mDialog.setCancelable(true);//设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    mDialog=null;
                }
            });
            mDialog.show();

        }
    }
    private void closeProgressDialog(){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog=null;
        }
    }
    public Intent getFileIntent(File file){
//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type="+type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public void writeToSDCard(String fileName,InputStream input,Long fileLength){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory,fileName);
//          if(file.exists()){
//              Log.i("tag", "The file has already exists.");
//              return;
//          }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[2048];
                int j = 0;
                long readLength = 0;
                while ((j = input.read(b)) != -1) {
                    fos.write(b, 0, j);
                    readLength += j;
                    int curProgress = (int) (((float) readLength / fileLength) * 100);
                    progressDialog.setProgress(curProgress);
                }
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Log.i("tag", "NO SDCard.");
        }
    }

    private String getMIMEType(File f){
        String type="";
        String fName=f.getName();
      /* 取得扩展名 */
        String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if(end.equals("pdf")){
            type = "application/pdf";//
        }
        else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            type = "audio/*";
        }
        else if(end.equals("3gp")||end.equals("mp4")){
            type = "video/*";
        }
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            type = "image/*";
        }
        else if(end.equals("apk")){
        /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
//      else if(end.equals("pptx")||end.equals("ppt")){
//        type = "application/vnd.ms-powerpoint";
//      }else if(end.equals("docx")||end.equals("doc")){
//        type = "application/vnd.ms-word";
//      }else if(end.equals("xlsx")||end.equals("xls")){
//        type = "application/vnd.ms-excel";
//      }
        else{
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
            type="*/*";
        }
        return type;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wb_purl.canGoBack()) {
            wb_purl.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
//        return false;
    }
//
    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();
    }

    private File downFile(final String httpUrl) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    long fileLength = connection.getContentLength();
                    FileOutputStream fileOutputStream = null;
                    InputStream inputStream;
                    if (connection.getResponseCode() == 200) {
                        inputStream = connection.getInputStream();

                        if (inputStream != null) {
                            file = getFile(httpUrl);
                            fileOutputStream = new FileOutputStream(file);
                            byte[] buffer = new byte[1024];
                            int length = 0;
                            long readLength = 0;
                            while ((length = inputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, length);
                                readLength += length;
                                int curProgress = (int) (((float) readLength / fileLength) * 100);
                                progressDialog.setProgress(curProgress);
                            }
                            fileOutputStream.close();
                            fileOutputStream.flush();
                        }
                        inputStream.close();
                    }

                    System.out.println("已经下载完成");
                    // 往handler发送一条消息 更改button的text属性
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return file;
    }
    /**
     * 根据传过来url创建文件
     *
     */
    private File getFile(String url) {
        File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getFilePath(url));
        return files;
    }
    /**
     * 截取出url后面的apk的文件名
     *
     * @param url
     * @return
     */
    private String getFilePath(String url) {
        return url.substring(url.lastIndexOf("/"), url.length());
    }
    /**
     * 安装APK
     */
    private void installApk() {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);

        Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(WB_ProtocolActivity.this, "leaflet.miaoa.qmxh.leaflet_simple.fileProvider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }catch (Exception e){
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        startActivity(intent);
    }
}
