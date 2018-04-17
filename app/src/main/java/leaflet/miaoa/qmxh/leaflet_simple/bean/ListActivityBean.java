package leaflet.miaoa.qmxh.leaflet_simple.bean;

import java.util.List;

/**
 *
 */

public class ListActivityBean {


    public  List<Head_adv> headdata;
        /**
         *
         */

        public  class Head_adv{
            public String  hpId;
            public String  hpNum;


            public String  hpArea;
            public int  hpDays;
            public String  hpPerson;
            public String  hpImgs;
            public int  hpMark;
            public String  lianjie;

            public String getHpImgs() {
                return hpImgs;
            }

            public void setHpImgs(String hpImgs) {
                this.hpImgs = hpImgs;
            }

            public String getLianjie() {
                return lianjie;
            }

            public void setLianjie(String lianjie) {
                this.lianjie = lianjie;
            }
        }

    public  class Fans_increate{
        public String  id;
        public String  fanCount;
        public Long  increaseTime;


        public String  storeName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFanCount() {
            return fanCount;
        }

        public void setFanCount(String fanCount) {
            this.fanCount = fanCount;
        }

        public Long getIncreaseTime() {
            return increaseTime;
        }

        public void setIncreaseTime(Long increaseTime) {
            this.increaseTime = increaseTime;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }
    }
    public  class SellerNews extends Entity {
        public String  newId;
        public String  isRead;
        public String  newsTheme;
        public String  newsContent;
        public Long  newsTime;

        public String getNewId() {
            return newId;
        }

        public void setNewId(String newId) {
            this.newId = newId;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        public String getNewsTheme() {
            return newsTheme;
        }

        public void setNewsTheme(String newsTheme) {
            this.newsTheme = newsTheme;
        }

        public String getNewsContent() {
            return newsContent;
        }

        public void setNewsContent(String newsContent) {
            this.newsContent = newsContent;
        }

        public Long getNewsTime() {
            return newsTime;
        }

        public void setNewsTime(Long newsTime) {
            this.newsTime = newsTime;
        }
    }
    public  class Advadver{
        public String  userlike;

        public String getUserlike() {
            return userlike;
        }

        public void setUserlike(String userlike) {
            this.userlike = userlike;
        }
    }
    public  class GoodRules{
        public String  rules;

        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }
    }
    public  class Product{
        public String  cId;
        public String  cName;
        public String  cIntro;
        public String  cNowPrice;
        public String  cFormerPrice;
        public String  cCover;
        public String  cSales;

        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public String getcIntro() {
            return cIntro;
        }

        public void setcIntro(String cIntro) {
            this.cIntro = cIntro;
        }

        public String getcNowPrice() {
            return cNowPrice;
        }

        public void setcNowPrice(String cNowPrice) {
            this.cNowPrice = cNowPrice;
        }

        public String getcFormerPrice() {
            return cFormerPrice;
        }

        public void setcFormerPrice(String cFormerPrice) {
            this.cFormerPrice = cFormerPrice;
        }

        public String getcCover() {
            return cCover;
        }

        public void setcCover(String cCover) {
            this.cCover = cCover;
        }

        public String getcSales() {
            return cSales;
        }

        public void setcSales(String cSales) {
            this.cSales = cSales;
        }
    }
    public  class Shop{
        public String  sName;
        public Long  beginTime;
        public Long  endTime;
        public String  distance;
        public String  sLogo;
        public String  sAddress;

        public String getsName() {
            return sName;
        }

        public void setsName(String sName) {
            this.sName = sName;
        }

        public Long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Long beginTime) {
            this.beginTime = beginTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getsLogo() {
            return sLogo;
        }

        public void setsLogo(String sLogo) {
            this.sLogo = sLogo;
        }

        public String getsAddress() {
            return sAddress;
        }

        public void setsAddress(String sAddress) {
            this.sAddress = sAddress;
        }
    }
    public  class Goods_shopping_mall{
        public String  cName;
        public Long  beginTime;
        public Long  endTime;
        public Long  cDays;
        public String  distance;
        public String  cSoldOut;

        public String  cIntro;
        public String  cNowPrice;
        public String  cFormerPrice;
        public String  cImgs;
        public String  sName;
        public String  sAddress;
        public String  sLegalPhone;

        public String getcSoldOut() {
            return cSoldOut;
        }

        public void setcSoldOut(String cSoldOut) {
            this.cSoldOut = cSoldOut;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public Long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Long beginTime) {
            this.beginTime = beginTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public Long getcDays() {
            return cDays;
        }

        public void setcDays(Long cDays) {
            this.cDays = cDays;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getcIntro() {
            return cIntro;
        }

        public void setcIntro(String cIntro) {
            this.cIntro = cIntro;
        }

        public String getcNowPrice() {
            return cNowPrice;
        }

        public void setcNowPrice(String cNowPrice) {
            this.cNowPrice = cNowPrice;
        }

        public String getcFormerPrice() {
            return cFormerPrice;
        }

        public void setcFormerPrice(String cFormerPrice) {
            this.cFormerPrice = cFormerPrice;
        }

        public String getcImgs() {
            return cImgs;
        }

        public void setcImgs(String cImgs) {
            this.cImgs = cImgs;
        }

        public String getsName() {
            return sName;
        }

        public void setsName(String sName) {
            this.sName = sName;
        }

        public String getsAddress() {
            return sAddress;
        }

        public void setsAddress(String sAddress) {
            this.sAddress = sAddress;
        }

        public String getsLegalPhone() {
            return sLegalPhone;
        }

        public void setsLegalPhone(String sLegalPhone) {
            this.sLegalPhone = sLegalPhone;
        }
    }
    public  class Adv {
        public String aId;
        public String aCover;
        public String aContent;
        public String aMatter;
        public List<String>  Imagelist;
        public String aResidue;
        public String ifRead;
        public String aType;
        public String aPrice;
        public String aStatus;
        public String uImg;
        public String watchCount;
        public String aAdress;
        public String coveRage;
        public String aSum;
        public String uName;
        public String sumMoney;
        public String residueMoney;
        public String adOutTradeNo;
        public String timeStamp;
        public String ifPay;
        public int timeCount;
        public Long uploadBegin;
        public Long uploadEnd;
        public boolean redpacket;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getAdOutTradeNo() {
            return adOutTradeNo;
        }

        public void setAdOutTradeNo(String adOutTradeNo) {
            this.adOutTradeNo = adOutTradeNo;
        }

        public String getIfPay() {
            return ifPay;
        }

        public void setIfPay(String ifPay) {
            this.ifPay = ifPay;
        }

        public String getaAdress() {
            return aAdress;
        }

        public void setaAdress(String aAdress) {
            this.aAdress = aAdress;
        }

        public String getCoveRage() {
            return coveRage;
        }

        public void setCoveRage(String coveRage) {
            this.coveRage = coveRage;
        }

        public String getaSum() {
            return aSum;
        }

        public void setaSum(String aSum) {
            this.aSum = aSum;
        }

        public List<String> getImagelist() {
            return Imagelist;
        }

        public void setImagelist(List<String> imagelist) {
            Imagelist = imagelist;
        }

        public int getTimeCount() {
            return timeCount;
        }

        public void setTimeCount(int timeCount) {
            this.timeCount = timeCount;
        }

        public String getaStatus() {
            return aStatus;
        }

        public void setaStatus(String aStatus) {
            this.aStatus = aStatus;
        }

        public String getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(String sumMoney) {
            this.sumMoney = sumMoney;
        }

        public String getResidueMoney() {
            return residueMoney;
        }

        public void setResidueMoney(String residueMoney) {
            this.residueMoney = residueMoney;
        }

        public String getaId() {
            return aId;
        }

        public void setaId(String aId) {
            this.aId = aId;
        }

        public String getaCover() {
            return aCover;
        }

        public void setaCover(String aCover) {
            this.aCover = aCover;
        }

        public String getaContent() {
            return aContent;
        }

        public void setaContent(String aContent) {
            this.aContent = aContent;
        }

        public String getaMatter() {
            return aMatter;
        }

        public String getaType() {
            return aType;
        }

        public void setaType(String aType) {
            this.aType = aType;
        }

        public void setaMatter(String aMatter) {
            this.aMatter = aMatter;
        }

        public String getuImg() {
            return uImg;
        }

        public void setuImg(String uImg) {
            this.uImg = uImg;
        }

        public String getuName() {
            return uName;
        }

        public void setuName(String uName) {
            this.uName = uName;
        }

        public String getaPrice() {
            return aPrice;
        }

        public void setaPrice(String aPrice) {
            this.aPrice = aPrice;
        }

        public String getaResidue() {
            return aResidue;
        }

        public void setaResidue(String aResidue) {
            this.aResidue = aResidue;
        }

        public String getIfRead() {
            return ifRead;
        }

        public void setIfRead(String ifRead) {
            this.ifRead = ifRead;
        }

        public String getWatchCount() {
            return watchCount;
        }

        public void setWatchCount(String watchCount) {
            this.watchCount = watchCount;
        }

        public Long getUploadBegin() {
            return uploadBegin;
        }

        public void setUploadBegin(Long uploadBegin) {
            this.uploadBegin = uploadBegin;
        }

        public Long getUploadEnd() {
            return uploadEnd;
        }

        public void setUploadEnd(Long uploadEnd) {
            this.uploadEnd = uploadEnd;
        }

        public boolean isRedpacket() {
            return redpacket;
        }

        public void setRedpacket(boolean redpacket) {
            this.redpacket = redpacket;
        }
    }
    public class Attribute{
            public String attributeId;
            public String specification;
            public Double attributePrice;
            public Long inventory;
            public String productId;
            public String attributeImg;
            public String attribute;
            public String cName;

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public Double getAttributePrice() {
            return attributePrice;
        }

        public void setAttributePrice(Double attributePrice) {
            this.attributePrice = attributePrice;
        }

        public Long getInventory() {
            return inventory;
        }

        public void setInventory(Long inventory) {
            this.inventory = inventory;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getAttributeImg() {
            return attributeImg;
        }

        public void setAttributeImg(String attributeImg) {
            this.attributeImg = attributeImg;
        }
    }
    public class Address {
        public String addressId;
        public String consigneeName;
        public String consigneeNum;
        public String consigneeAddress;
        public String defaultIf;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getConsigneeName() {
            return consigneeName;
        }

        public void setConsigneeName(String consigneeName) {
            this.consigneeName = consigneeName;
        }

        public String getConsigneeNum() {
            return consigneeNum;
        }

        public void setConsigneeNum(String consigneeNum) {
            this.consigneeNum = consigneeNum;
        }

        public String getConsigneeAddress() {
            return consigneeAddress;
        }

        public void setConsigneeAddress(String consigneeAddress) {
            this.consigneeAddress = consigneeAddress;
        }

        public String getDefaultIf() {
            return defaultIf;
        }

        public void setDefaultIf(String defaultIf) {
            this.defaultIf = defaultIf;
        }
    }
    public class   Pay_goods {
        public String cId;
        public String cName;
        public String buyId;
        public String buyCount;
        public String attribute;
        public String attributePrice;
        public String expressNumber;
        public int shippingStatus;
        public String payMoney;
        public String specification;
        public String attributeImg;
        public String orderId;

        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int ifRefund;

        public Long buyTime;

        public Long getBuyTime() {
            return buyTime;
        }

        public void setBuyTime(Long buyTime) {
            this.buyTime = buyTime;
        }

        public int getIfRefund() {
            return ifRefund;
        }

        public void setIfRefund(int ifRefund) {
            this.ifRefund = ifRefund;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public String getBuyId() {
            return buyId;
        }

        public String getAttributePrice() {
            return attributePrice;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public void setAttributePrice(String attributePrice) {
            this.attributePrice = attributePrice;
        }

        public void setBuyId(String buyId) {
            this.buyId = buyId;
        }

        public String getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(String buyCount) {
            this.buyCount = buyCount;
        }

        public String getExpressNumber() {
            return expressNumber;
        }

        public void setExpressNumber(String expressNumber) {
            this.expressNumber = expressNumber;
        }

        public int getShippingStatus() {
            return shippingStatus;
        }

        public void setShippingStatus(int shippingStatus) {
            this.shippingStatus = shippingStatus;
        }

        public String getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(String payMoney) {
            this.payMoney = payMoney;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public String getAttributeImg() {
            return attributeImg;
        }

        public void setAttributeImg(String attributeImg) {
            this.attributeImg = attributeImg;
        }
    }

    public class LogisticsData {

        private String context;//内容
        private String time;//时间

        public String getContext() {
            return context;
        }

        public LogisticsData setContext(String context) {
            this.context = context;
            return this;
        }


        public String getTime() {
            return time;
        }

        public LogisticsData setTime(String time) {
            this.time = time;
            return this;
        }

    }
}
