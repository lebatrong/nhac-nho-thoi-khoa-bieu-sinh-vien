package com.student.thoikhoabieu.customAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment;
import com.kongzue.dialog.v2.SelectDialog;
import com.student.thoikhoabieu.DialogEditActivity;
import com.student.thoikhoabieu.MainActivity;
import com.student.thoikhoabieu.Models.DatabaseHandler;
import com.student.thoikhoabieu.Models.objectClass.objthoikhoabieu;
import com.student.thoikhoabieu.R;

import java.util.ArrayList;


public class aRclvThoiKhoaBieu extends RecyclerView.Adapter<aRclvThoiKhoaBieu.ViewHolder> implements BottomSheetListener{

    ArrayList<objthoikhoabieu> mListThoiKhoaBieu;
    Context context;

    FragmentManager fragmentManager;

    public aRclvThoiKhoaBieu(ArrayList<objthoikhoabieu> mListThoiKhoaBieu, Context context, FragmentManager fragmentManager) {
        this.mListThoiKhoaBieu = mListThoiKhoaBieu;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void setData(ArrayList<objthoikhoabieu> ListThoiKhoaBieu){
        mListThoiKhoaBieu = ListThoiKhoaBieu;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        mListThoiKhoaBieu.remove(position);
        notifyDataSetChanged();
    }

    private OnItemClickListener listener;

    @Override
    public void onSheetShown(@NonNull BottomSheetMenuDialogFragment bottomSheet, @Nullable Object object) {

    }

    @Override
    public void onSheetItemSelected(@NonNull BottomSheetMenuDialogFragment bottomSheet, MenuItem item, @Nullable Object object) {
        int id = item.getItemId();
        switch (id){
            case R.id.iMenu_ChinhSua:
                actionEditTKB((objthoikhoabieu) object);
                break;
            case  R.id.iMenu_Xoa:
                actionXoaTKB((objthoikhoabieu) object);
                break;
        }
    }

    @Override
    public void onSheetDismissed(@NonNull BottomSheetMenuDialogFragment bottomSheet, @Nullable Object object, int dismissEvent) {

    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_thoikhoabieu,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final objthoikhoabieu thoikhoabieu = mListThoiKhoaBieu.get(i);

        viewHolder.tvTenMonHoc.setText(thoikhoabieu.getTenMonHoc());
        viewHolder.tvPhongHoc.setText(thoikhoabieu.getPhong());
        viewHolder.tvThoiGian.setText(objthoikhoabieu.getStringThoiGianGanNhat(thoikhoabieu,context));


        viewHolder.imvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheetMenuDialogFragment.Builder(context,R.style.MyBottomSheetMenuStyle)
                        .setSheet(R.menu.menu_bottom)
                        .setTitle(null)
                        .setListener(aRclvThoiKhoaBieu.this)
                        .object(thoikhoabieu)
                        .show(fragmentManager);
            }
        });

    }

    private void actionEditTKB(objthoikhoabieu thoikhoabieu) {
        Intent intent = new Intent(context, DialogEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("monhoc", thoikhoabieu);
        intent.putExtra("data",bundle);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.anim_enter_dialog,R.anim.anim_exit);
    }

    private void actionXoaTKB(final objthoikhoabieu thoikhoabieu){
        SelectDialog.show(context,
                "Xóa môn học",
                "Bạn muốn xóa môn " + thoikhoabieu.getTenMonHoc(),
                "Xóa",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Xóa trong database
                        DatabaseHandler db = new DatabaseHandler(context);
                        db.deleteRecord(thoikhoabieu);
                        //Reload lại
                        mListThoiKhoaBieu.remove(thoikhoabieu);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Xóa thành công môn " + thoikhoabieu.getTenMonHoc() + "!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                },
                "Hủy",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return mListThoiKhoaBieu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvThoiGian, tvTenMonHoc, tvPhongHoc;
        ImageView imvEdit;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvTenMonHoc = itemView.findViewById(R.id.tvTenMonHoc_rowtkb);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian_rowtkb);
            tvPhongHoc = itemView.findViewById(R.id.tvTenPhongHoc_rowtkb);
            imvEdit = itemView.findViewById(R.id.imvEditMonHoc_rowtkb);



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(v, position);
                    return true;
                }
            });

        }
    }
}
