package com.student.thoikhoabieu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.student.thoikhoabieu.Models.DatabaseHandler;
import com.student.thoikhoabieu.Models.objectClass.objthoikhoabieu;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;

public class DialogEditActivity extends AppCompatActivity {

    Spinner spinner;
    CheckBox chkTiet1,chkTiet2,chkTiet3,chkTiet4,chkTiet5,chkTiet6,chkTiet7,chkTiet8,chkTiet9,chkTiet10,chkTiet11,chkTiet12,chkTiet13, chkCa1,chkCa2,chkCa3,chkCa4;
    Button btnNhap;
    EditText edtTenMonHoc,edtPhong,edtTuanHoc1,edtTuanHoc2;
    SwitchButton sBtnMonThucHanh;
    ArrayList<CheckBox> arrTiet;
    ArrayList<CheckBox> arrCa;

    LinearLayout lnlCaTH, lnlTietHoc;

    objthoikhoabieu monhoc;
    String strAction;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog_edit);
        this.setFinishOnTouchOutside(false);

        initView();
        setupSpinner();
        actionNhap();
        setupTietMonThucHanh();
        getData();
    }

    private void getData() {
        //Môn học mặt định
        monhoc = new objthoikhoabieu("","","","","","",0,"");
        monhoc.setID(-1);
        //Get Data
        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle != null) {
            monhoc = (objthoikhoabieu) bundle.getSerializable("monhoc");
            edtPhong.setText(monhoc.getPhong());
            edtTenMonHoc.setText(monhoc.getTenMonHoc());

            //Đỗ dữ liệu cho check box tiết
            if(!monhoc.getTiet().equals("[]"))
            {
                ArrayList<Integer> TietTH = new Gson().fromJson(monhoc.getTiet(), new TypeToken<ArrayList<Integer>>() {
                }.getType());

                for(int tiet : TietTH){
                    arrTiet.get(tiet-1).setChecked(true);
                }

            }
            //Đỗ dữ liệu cho check box ca
            if(!monhoc.getCa().equals("[]"))
            {
                ArrayList<Integer> cathuchanh = new Gson().fromJson(monhoc.getCa(), new TypeToken<ArrayList<Integer>>() {
                }.getType());
                for(int ca : cathuchanh){
                    arrCa.get(ca-1).setChecked(true);
                }
            }

            //Cài đặt data cho môn thực hành
            sBtnMonThucHanh.setChecked(monhoc.getMonThucHanh() == 0 ? false : true);
            if(sBtnMonThucHanh.isChecked()) {
                lnlCaTH.setVisibility(View.VISIBLE);
                lnlTietHoc.setVisibility(View.GONE);
            }else{
                lnlCaTH.setVisibility(View.GONE);
                lnlTietHoc.setVisibility(View.VISIBLE);
            }


            spinner.setSelection(adapter.getPosition(monhoc.getThu()));

            if(!monhoc.getTuan().equals("")){
                String[] tuanTKB = monhoc.getTuan().split("-");
                edtTuanHoc1.setText(tuanTKB[0]);
                edtTuanHoc2.setText(tuanTKB[1]);
            }


            strAction = "Cập nhật";
            btnNhap.setText(strAction);
        }else{
            strAction = "Thêm";
            btnNhap.setText(strAction);
        }

    }

    private void setupTietMonThucHanh() {
        sBtnMonThucHanh.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked) {
                    lnlCaTH.setVisibility(View.VISIBLE);
                    lnlTietHoc.setVisibility(View.GONE);
                }else{
                    lnlCaTH.setVisibility(View.GONE);
                    lnlTietHoc.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void actionNhap() {
        btnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> tietHoc = new ArrayList<>();
                for (CheckBox c : arrTiet){
                    if(c.isChecked()){
                        tietHoc.add(Integer.parseInt(c.getText().toString()));
                    }
                }

                ArrayList<Integer> caTH = new ArrayList<>();
                for (CheckBox c : arrCa){
                    if(c.isChecked()){
                        caTH.add(Integer.parseInt(c.getText().toString()));
                    }
                }

                DatabaseHandler db = new DatabaseHandler(DialogEditActivity.this);
                int monthuchanh;
                if(sBtnMonThucHanh.isChecked())
                    monthuchanh = 1;
                else
                    monthuchanh = 0;

                if((caTH.size() > 0 || tietHoc.size() > 0) &&
                        !edtTenMonHoc.getText().toString().equals("") &&
                        !edtTuanHoc1.getText().toString().equals("") &&
                        !edtTuanHoc1.getText().toString().equals("") &&
                        !edtPhong.getText().toString().equals("")){

                    objthoikhoabieu thoikhoabieu = new objthoikhoabieu(edtTenMonHoc.getText().toString(),
                            monhoc.getGiaoVien(),
                            edtPhong.getText().toString(),
                            new Gson().toJson(tietHoc),
                            edtTuanHoc1.getText().toString()+"-"+edtTuanHoc2.getText().toString(),
                            spinner.getSelectedItem().toString(),
                            monthuchanh,
                            new Gson().toJson(caTH));
                    thoikhoabieu.setID(monhoc.getID());

                    if(db.updateRecodeIfExit(thoikhoabieu)){
                        Toast.makeText(DialogEditActivity.this, strAction+" thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(DialogEditActivity.this, strAction+" thất bại thử lại sau!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(DialogEditActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        spinner = findViewById(R.id.spinnerThu_dialog);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.arrThu));
        spinner.setAdapter(adapter);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
    }


    private void initView() {
        chkTiet1 = findViewById(R.id.chkTiet1);
        chkTiet2 = findViewById(R.id.chkTiet2);
        chkTiet3 = findViewById(R.id.chkTiet3);
        chkTiet1 = findViewById(R.id.chkTiet1);
        chkTiet4 = findViewById(R.id.chkTiet4);
        chkTiet5 = findViewById(R.id.chkTiet5);
        chkTiet6 = findViewById(R.id.chkTiet6);
        chkTiet7 = findViewById(R.id.chkTiet7);
        chkTiet8 = findViewById(R.id.chkTiet8);
        chkTiet9 = findViewById(R.id.chkTiet9);
        chkTiet10 = findViewById(R.id.chkTiet10);
        chkTiet11 = findViewById(R.id.chkTiet11);
        chkTiet12 = findViewById(R.id.chkTiet12);
        chkTiet13 = findViewById(R.id.chkTiet13);

        chkCa1 = findViewById(R.id.chkCa1);
        chkCa2 = findViewById(R.id.chkCa2);
        chkCa3 = findViewById(R.id.chkCa3);
        chkCa4 = findViewById(R.id.chkCa4);

        arrTiet = new ArrayList<>();
        arrTiet.add(chkTiet1);
        arrTiet.add(chkTiet2);
        arrTiet.add(chkTiet3);
        arrTiet.add(chkTiet4);
        arrTiet.add(chkTiet5);
        arrTiet.add(chkTiet6);
        arrTiet.add(chkTiet7);
        arrTiet.add(chkTiet8);
        arrTiet.add(chkTiet9);
        arrTiet.add(chkTiet10);
        arrTiet.add(chkTiet11);
        arrTiet.add(chkTiet12);
        arrTiet.add(chkTiet13);

        arrCa = new ArrayList<>();
        arrCa.add(chkCa1);
        arrCa.add(chkCa2);
        arrCa.add(chkCa3);
        arrCa.add(chkCa4);

        findViewById(R.id.btnHuy_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNhap = findViewById(R.id.btnNhap);

        sBtnMonThucHanh = findViewById(R.id.switchMonThucHanh_dialog);

        edtPhong = findViewById(R.id.edtPhongHocNhap_dialog);
        edtTenMonHoc = findViewById(R.id.edtTenMonHocNhap_dialog);
        edtTuanHoc1 = findViewById(R.id.edtTuanHoc1_dialog);
        edtTuanHoc2 = findViewById(R.id.edtTuanHoc2_dialog);

        lnlCaTH = findViewById(R.id.lnlCaTH);
        lnlTietHoc = findViewById(R.id.lnlTietHoc);
    }
}
