package com.student.thoikhoabieu.customAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.student.thoikhoabieu.DialogEditActivity;
import com.student.thoikhoabieu.Models.objectClass.objthoikhoabieu;
import com.student.thoikhoabieu.R;

import java.util.ArrayList;


public class aRclvThoiKhoaBieu extends RecyclerView.Adapter<aRclvThoiKhoaBieu.ViewHolder> {

    ArrayList<objthoikhoabieu> mListThoiKhoaBieu;
    Context context;

    public aRclvThoiKhoaBieu(ArrayList<objthoikhoabieu> mListThoiKhoaBieu, Context context) {
        this.mListThoiKhoaBieu = mListThoiKhoaBieu;
        this.context = context;
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
                actionEditTKB(viewHolder, thoikhoabieu);
            }
        });

    }

    private void actionEditTKB(ViewHolder viewHolder, objthoikhoabieu thoikhoabieu) {
        Intent intent = new Intent(context, DialogEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("monhoc", thoikhoabieu);
        intent.putExtra("data",bundle);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.anim_enter_dialog,R.anim.anim_exit);
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
