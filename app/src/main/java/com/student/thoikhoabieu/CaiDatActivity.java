package com.student.thoikhoabieu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CaiDatActivity extends AppCompatActivity {

    EditText edgio, edphut, edtTenNhacNho;
    Button btluu, btnChonAmThanh;
    private Toolbar toolbar;
    SharedPreferences sharedPreferences;

    final int CODE = 3;

    String filename = "Chọn âm thanh";
    String uriAmThanh = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        toolbar=(Toolbar)findViewById(R.id.toolbarcaidat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edgio=(EditText)findViewById(R.id.edtgio);
        edphut=(EditText)findViewById(R.id.edtphut);
        edtTenNhacNho=(EditText)findViewById(R.id.edttennhacnho);
        btluu=(Button)findViewById(R.id.btnluu);
        btnChonAmThanh = findViewById(R.id.btnChonAmThanh);
        // lay gia tri ra:

        sharedPreferences = getSharedPreferences("caidat",MODE_PRIVATE);

        edgio.setText(sharedPreferences.getInt("gio",0)+"");
        edphut.setText(sharedPreferences.getInt("phut",10)+"");
        btnChonAmThanh.setText(sharedPreferences.getString("amthanh","Chọn âm thanh"));
        edtTenNhacNho.setText(sharedPreferences.getString("noidung","Chào bạn, gần đến giờ học, vui lòng chuẩn bị"));

        btluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gio = Integer.parseInt(edgio.getText().toString());
                int phut = Integer.parseInt(edphut.getText().toString());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("gio",gio);
                editor.putInt("phut",phut);
                editor.putString("noidung", edtTenNhacNho.getText().toString());
                editor.putString("uriamthanh",uriAmThanh);
                editor.putString("amthanh",filename);
                if(editor.commit())
                    Toast.makeText(CaiDatActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
            }
        });

        btnChonAmThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent,CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*check whether you're working on correct request using requestCode , In this case 1*/

        if(requestCode == CODE && resultCode == Activity.RESULT_OK){
            Uri audioFileUri = data.getData();
            uriAmThanh = audioFileUri.toString();
            filename = getFileName(audioFileUri);
            btnChonAmThanh.setText(filename);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
