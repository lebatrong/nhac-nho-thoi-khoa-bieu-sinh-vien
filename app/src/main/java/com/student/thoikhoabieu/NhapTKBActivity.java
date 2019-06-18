package com.student.thoikhoabieu;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.student.thoikhoabieu.Models.DatabaseHandler;
import com.student.thoikhoabieu.Models.objectClass.objthoikhoabieu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NhapTKBActivity extends AppCompatActivity {

    Toolbar toolbar;

    WebView wvTBK;
    Button btnNhap;
    final String url = "https://qldt.vlute.edu.vn/VLUTE-Web/home.action";
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_tkb);

        initView();

        setupWebView();
    }

    private void setupWebView() {
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter url", Toast.LENGTH_SHORT).show();
            return;
        }
        wvTBK.getSettings().setLoadsImagesAutomatically(true);
        wvTBK.getSettings().setJavaScriptEnabled(true);
        wvTBK.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvTBK.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");


        wvTBK.setWebViewClient(new myWebView(btnNhap, this));
        //wvTBK.setWebViewClient(new WebViewClient());

        wvTBK.loadUrl(url);

        btnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wvTBK.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('body')[0].innerHTML+'</head>');");
            }
        });

    }

    private void initView() {
        wvTBK = findViewById(R.id.wvTKB);
        btnNhap = findViewById(R.id.btnNhap);
        btnNhap.setVisibility(View.GONE);

        toolbar = findViewById(R.id.toolbarLayTKB);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db = new DatabaseHandler(this);
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            Document doc = Jsoup.parse(html);
            if (doc != null) {

                Elements elements = doc.select("td.cellTKB");
                Element hoten = doc.select("div.pull-left").first();
                for (Element element : elements) {
                    db.addRecord(new objthoikhoabieu(element.select("span").first().text(),
                            element.select("b").text(),
                            element.select("span").last().text(),
                            "[]",
                            "",
                            "",
                            0,
                            "[]"));
                }


                //Xóa dữ liệu tên cũ rồi add lại
                SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("hoten");
                editor.putString("hoten", hoten.text());
                editor.commit();

                finish();
            } else
                Toast.makeText(NhapTKBActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();

        }
    }
}

