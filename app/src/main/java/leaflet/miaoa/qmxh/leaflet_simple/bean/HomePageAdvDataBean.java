package leaflet.miaoa.qmxh.leaflet_simple.bean;

/**
 * Created by Administrator on 2016/10/31.
 */

public class HomePageAdvDataBean {


    public String ad_link;
    public String position_name;
    public String position_id;
    public String ad_width;
    public String ad_height;
    public String ad_code;
    public String position_desc;
    public String img_url;
    public String goods_id;
    public String downloadUrl;


    public HomePageAdvDataBean(String ad_link, String position_name, String position_id, String ad_width, String ad_height, String ad_code, String position_desc, String img_url, String goods_id, String downloadUrl) {
        this.ad_link = ad_link;
        this.position_name = position_name;
        this.position_id = position_id;
        this.ad_width = ad_width;
        this.ad_height = ad_height;
        this.ad_code = ad_code;
        this.position_desc = position_desc;
        this.img_url = img_url;
        this.goods_id = goods_id;
        this.downloadUrl = downloadUrl;
    }
}
