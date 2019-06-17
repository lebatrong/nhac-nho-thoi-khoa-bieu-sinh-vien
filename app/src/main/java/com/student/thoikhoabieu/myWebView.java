package com.student.thoikhoabieu;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.kongzue.dialog.v2.WaitDialog;

public class myWebView extends WebViewClient {

    Button btnNhap;
    Context context;

    final String urlFinish = "https://qldt.vlute.edu.vn/VLUTE-Web/hocvien/tkbForm.action";

    public myWebView(Button btnNhap, Context context) {
        this.btnNhap = btnNhap;
        this.context = context;
    }

    // Khi bạn click vào link bên trong trình duyệt (Webview)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        WaitDialog.show(context,"Loading");
        return super.shouldOverrideUrlLoading(view, url);
    }


    // Khi trang bắt đầu được tải
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        WaitDialog.show(context,"Loading");
        if(!url.matches(urlFinish)){
            btnNhap.setVisibility(View.GONE);
        }
    }


    // Khi trang tải xong
    @Override
    public void onPageFinished(WebView view, String url) {
        WaitDialog.dismiss();
        if(url.matches(urlFinish)){
            btnNhap.setVisibility(View.VISIBLE);
        }else{
            btnNhap.setVisibility(View.GONE);
        }

        super.onPageFinished(view, url);
    }


    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

}
