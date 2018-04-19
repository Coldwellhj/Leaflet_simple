package leaflet.miaoa.qmxh.leaflet_simple.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.Constants;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.SellerHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor.BuyAfterActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor.ConfirmOrderActivity;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Body;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("Tag", "onPayFinish, errCode = " + resp.errCode+resp.errStr);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage("微信支付结果："+ String.valueOf(resp.errCode)+resp.errStr);
//			builder.show();
			if(resp.errCode==0){
//				buyCoinGoodsWX();

				Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				finish();
				if("广告支付".equals(Body)){
					Intent intent=new Intent(WXPayEntryActivity.this,SellerHomePageActivity.class);
					intent.putExtra("pay","true");
					startActivity(intent);
				}else {
					Intent intent=new Intent(WXPayEntryActivity.this,ConfirmOrderActivity.class);
					intent.putExtra("finish",true);
					startActivity(intent);
				}

			}else {
				Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

}