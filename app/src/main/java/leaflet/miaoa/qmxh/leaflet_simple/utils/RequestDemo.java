package leaflet.miaoa.qmxh.leaflet_simple.utils;

import java.util.HashMap;
import java.util.Map;

public class RequestDemo {




     public static void getTraceInfo(String data,String msg_type) {
         // String url = "http://japi.zto.cn/zto/api_utf8/traceInterface";
          String url = "http://japi.zto.cn/gateway.do";//最新地址
          
          /*http://japi.zto.cn/gateway.do?msg_type=TRACEINTERFACE_LATEST&data_digest=youDataDigest&data=
        	  ["680000000000","680000000001"]&company_id=youCompanyId*/
          String charset = "utf8";
//
//          if("1".equals(msg_type)){
//               msg_type = "TRACEINTERFACE_NEW_TRACES";//获取快件轨迹信息
//          }else {
//               msg_type = "TRACEINTERFACE_LATEST";//获取快件最新一条信息
//          }

        //  String msg_type = "TRACEINTERFACE_LATEST";//获取快件最新一条信息
          String company_id = "40c8e0b2079241a98fffe044c41755d8";
          String key = "9ca263df347c";

          try {
               String data_digest = DigestUtil.digest(data, key, charset);
               Map<String, Object> params = new HashMap<String, Object>();
               params.put("data", data);
               params.put("data_digest", data_digest);
               params.put("company_id", company_id);
               params.put("msg_type", msg_type);
               // 向跟踪信息接口发送请求
               if("TRACEINTERFACE_NEW_TRACES".equals(msg_type)){
                    HttpUtil.post_information(url, charset, params);
               }else if( "TRACEINTERFACE_LATEST".equals(msg_type)){
                    HttpUtil.post(url, charset, params);
               }


         //      System.out.println(response);
          }catch(Exception e){
               e.printStackTrace();
          }

     }
//     public static void main(String[] args) {
//    	 String expressNumber ="470971256179";
//         String response = getTraceInfo("['"+expressNumber+"']");
//         System.out.println(response);
//     }
}



