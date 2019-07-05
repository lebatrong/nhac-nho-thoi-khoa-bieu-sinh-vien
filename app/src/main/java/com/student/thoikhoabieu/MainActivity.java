package com.student.thoikhoabieu;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.util.TimeUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.SelectDialog;
import com.student.thoikhoabieu.Models.DatabaseHandler;
import com.student.thoikhoabieu.Models.objectClass.objthoikhoabieu;
import com.student.thoikhoabieu.Services.AlarmReceiver;
import com.student.thoikhoabieu.customAdapter.aRclvThoiKhoaBieu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final float END_SCALE = 0.7f;

    private TextView tvHoTen,tvMSSV;
    private RecyclerView rclvThoiKhoaBieu;
    private aRclvThoiKhoaBieu adapter;
    private ArrayList<objthoikhoabieu> mListTKB;

    private Toolbar toolbar;


    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Intent intent;

    private int REQUEST_CODE = 27;

    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private View contentView;

    private long thoigianconlaimilisecond;
    private objthoikhoabieu tkbGanNhat;

    private boolean isDaXem = false;

    Button btnDaXem;
    TextView tvPhong,tvMonHoc,tvTuan,tvThoiGianConLai,tvTiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setupDrawerLayout();
        actionDaXem();
    }

    private void actionDaXem() {
        btnDaXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cài đặt alarm
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                intent = new Intent(MainActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                        REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent.putExtra("data","off");
                sendBroadcast(intent);
                Toast.makeText(MainActivity.this, "Đã tắt thông báo", Toast.LENGTH_SHORT).show();
                isDaXem = true;
                btnDaXem.setVisibility(View.GONE);
                laythoikhoabieu(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        laythoikhoabieu(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    // thời gian báo trước:
    public long tgbaotruoc()
    {
        long mili = 0;
        SharedPreferences preferences = getSharedPreferences("caidat",MODE_PRIVATE);
        int gio = preferences.getInt("gio",0);
        int phut = preferences.getInt("phut",0);

        String strGio = String.valueOf(gio);
        String strPhut = String.valueOf(phut);
        if(gio<10)
            strGio = "0"+gio;
        if(phut<10)
            strPhut = "0"+phut;

        String strThoiGian = strGio+":"+strPhut;
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

        try {
            Date date = dateFormat.parse(strThoiGian);
            mili = date.getTime();
        } catch (ParseException e) {
            Log.e("Loi", e.toString());
            //e.printStackTrace();
        }
        return TimeUnit.MINUTES.toMillis(phut) + TimeUnit.HOURS.toMillis(gio) ;
    }
    //Cài đặt menu trái
    @SuppressLint("RestrictedApi")
    private void setupDrawerLayout() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);

        //Hiện nút menu trái trên thanh toolbar
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);

        //Bắt sự kiện click cho menu trái
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int ID = menuItem.getItemId();
                switch (ID){
                    case R.id.nav_item_NhapTKB :
                        startActivity(new Intent(MainActivity.this,NhapTKBActivity.class));
                        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
                        //1s sau đóng menu lại
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawerLayout.closeDrawers();
                            }
                        },1000);
                        return true;
                    case R.id.nav_item_XoaDuLieu :
                        SelectDialog.show(MainActivity.this,
                                "Thông báo",
                                "Bạn muốn xóa dữ liệu thời khóa biểu?",
                                "Xóa",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        XoaDuLieuThoiKhoaBieu();
                                    }
                                },
                                "Hủy",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_item_CaiDat:
                        startActivity(new Intent(MainActivity.this,CaiDatActivity.class));
                        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawerLayout.closeDrawers();
                            }
                        },1000);
                        return true;


                    case R.id.nav_item_Thoat :
                        SelectDialog.show(MainActivity.this,
                                "Thông báo",
                                "Bạn muốn thoát ứng dụng?",
                                "Thoát",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                },
                                "Hủy",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        return true;
                }
            }
        });

        //Set animation cho menu trái
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawerView, float slideOffset) {
                                               // Scale the View based on current slide offset
                                               final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                               final float offsetScale = 1 - diffScaledOffset;
                                               contentView.setScaleX(offsetScale);
                                               contentView.setScaleY(offsetScale);
                                               // Translate the View, accounting for the scaled width
                                               final float xOffset = drawerView.getWidth() * slideOffset;
                                               final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                               final float xTranslation = xOffset - xOffsetDiff;
                                               contentView.setTranslationX(xTranslation);
                                           }
                                       }
        );
    }

    private void laythoikhoabieu(final boolean next) {
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        String strhoten = sharedPreferences.getString("hoten","");

        if(!strhoten.matches("")){
            String[] arr = strhoten.split("-");
            tvHoTen.setText(arr[0]);
            tvMSSV.setVisibility(View.VISIBLE);
            tvMSSV.setText("("+arr[1].substring(6,arr[1].length()-1)+")");
        }else {
            tvHoTen.setText("Vui lòng nhập thời khóa biểu");
            tvMSSV.setVisibility(View.INVISIBLE);
        }

        //Lấy dữ liệu thời khóa biểu từ sqlite
        DatabaseHandler database = new DatabaseHandler(this);
        mListTKB = database.getAllThoiKhoaBieu();
        //Setup RecyclerView thời khóa biểu
        @SuppressLint("WrongConstant")
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new aRclvThoiKhoaBieu(mListTKB,this, fragmentManager);
        rclvThoiKhoaBieu.setAdapter(adapter);
        rclvThoiKhoaBieu.setLayoutManager(manager);



        if(mListTKB.size()>0){

            //Cài đặt alarm
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            intent = new Intent(this,AlarmReceiver.class);
            intent.putExtra("data","on");
            pendingIntent = PendingIntent.getBroadcast(this,
                    REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            //======================================
            //Add môn gần tới nhất vào panel
            //======================================================
            tkbGanNhat = objthoikhoabieu.getThongTinMonHocGanNhat(mListTKB,this);
            tvTuan.setText(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) +"");
            tvTiet.setText(objthoikhoabieu.getStringThoiGianGanNhat(tkbGanNhat,this));

            tvMonHoc.setText(tkbGanNhat.getTenMonHoc());
            tvPhong.setText(tkbGanNhat.getPhong());

            //============================================
            //Đếm ngược thời gian và hiện button dừng báo
            //==========================================
            thoigianconlaimilisecond = tkbGanNhat.chuyenTietVeThoiGian(this) - Calendar.getInstance().getTimeInMillis();
            if(thoigianconlaimilisecond > 0){
                new CountDownTimer(thoigianconlaimilisecond,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long timeInSeconds = thoigianconlaimilisecond / 1000;
                        long hours, minutes, seconds;
                        hours = timeInSeconds / 3600;
                        timeInSeconds = timeInSeconds - (hours * 3600);
                        minutes = timeInSeconds / 60;
                        timeInSeconds = timeInSeconds - (minutes * 60);
                        seconds = timeInSeconds;

                        String strThoiGian = "";
                        if(hours > 0){
                            strThoiGian = hours +" giờ " +minutes +" phút " + seconds +" giây";
                        }else if(minutes > 0 && seconds > 0){
                            strThoiGian =  minutes +" phút " + seconds +" giây";
                        }else
                            strThoiGian =  seconds +" giây";
                        tvThoiGianConLai.setText(strThoiGian);

                        if(hours == 0 && minutes <= tgbaotruoc() / 60000 && !isDaXem){
                            btnDaXem.setVisibility(View.VISIBLE);
                        }
                        thoigianconlaimilisecond = tkbGanNhat.chuyenTietVeThoiGian(MainActivity.this) - Calendar.getInstance().getTimeInMillis();
                    }

                    @Override
                    public void onFinish() {
                        laythoikhoabieu(false);
                        isDaXem = false;
                    }
                }.start();
            }

//            ===============================================================
//            Lặp qua mảng thời khóa biểu add những môn học chuẩn bị học
//            ===============================================================
            for(int i = 0; i<mListTKB.size(); i++){
                objthoikhoabieu tkb;
                if(!next)
                    tkb = mListTKB.get(i);
                else {
                    //Kiểm tra còn next được hay không
                    int iNext = i++;
                    if(iNext < mListTKB.size())
                        tkb = mListTKB.get(iNext);
                    else
                        tkb = mListTKB.get(mListTKB.size()-1);
                }

                long time = tkb.chuyenTietVeThoiGian(this) - tgbaotruoc();
                if(time!=-1 && time > Calendar.getInstance().getTimeInMillis()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time ,pendingIntent);
                        Log.e("kiemtra", time+"");
                    }
                    break;
                }
            }
        }
    }

    private void initView() {
        nav = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerlayout);

        rclvThoiKhoaBieu = findViewById(R.id.rclvTKB);
        toolbar = findViewById(R.id.toolbarMain);
        //CAI DAT TOOLBAR
        setSupportActionBar(toolbar);

        tvHoTen = nav.getHeaderView(0).findViewById(R.id.tvTenSV);
        tvMSSV = nav.getHeaderView(0).findViewById(R.id.tvMaSV);

        //CÀi đặt dialog
        DialogSettings.style = DialogSettings.STYLE_IOS;
        DialogSettings.blur_alpha = 200;

        tvMonHoc = findViewById(R.id.tvTenMonHocChuanBi);
        tvPhong = findViewById(R.id.tvPhongChuanBi);
        tvThoiGianConLai = findViewById(R.id.tvThoiGianConLaiChuanBi);
        tvTiet = findViewById(R.id.tvTietChuanBi);
        tvTuan = findViewById(R.id.tvTuanChuanBi);
        btnDaXem = findViewById(R.id.btnDaXemChuanBi);

        contentView = findViewById(R.id.content);

        findViewById(R.id.imvAddTKB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DialogEditActivity.class));
                overridePendingTransition(R.anim.anim_enter_dialog,R.anim.anim_exit);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogSettings.unloadAllDialog();
    }

    private void XoaDuLieuThoiKhoaBieu() {
        DatabaseHandler database = new DatabaseHandler(this);
        database.deleteAllRecord();
        laythoikhoabieu(false);
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }


}