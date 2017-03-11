package com.dominic.unionpaydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class MainActivity extends AppCompatActivity {

    private String url = "http://101.231.204.84:8091/sim/getacptn";//银联支付接口，银联提供测试接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void pay(View view){

        //1.提交参数到服务器
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //2.解析服务器返回的流水号
                String tn = s;
                //3.调用银联sdk,传入流水号
                /**
                 * tn:交易流水号
                 * mode："00"启动银联正式环境 ,"01"连接银联测试环境（可以使用测试账号，测试账号参阅文档）
                 */
                String serverMode = "01";
                UPPayAssistEx.startPayByJAR(MainActivity.this, PayActivity.class,null,null,tn,serverMode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


        //4.处理支付结果
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String msg = "";

        // 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消

        String str = data.getExtras().getString("pay_result");
        Log.v("zftphone", "2 "+data.getExtras().getString("merchantOrderId"));
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
        //支付完成,处理自己的业务逻辑!
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
