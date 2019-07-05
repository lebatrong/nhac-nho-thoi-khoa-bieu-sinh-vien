package com.student.thoikhoabieu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        initPermission();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else
                Toast.makeText(this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
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
