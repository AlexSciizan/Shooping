package com.shooping.Components;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shooping.HomeActivity;
import com.shooping.R;

import java.util.List;

public class RecyclerviewAdapterStoreCart extends RecyclerView.Adapter<RecyclerViewHolderStoreCart> {
    public List<ItemObject> itemList;

    public RecyclerviewAdapterStoreCart(List<ItemObject> itemLists) {
        itemList = itemLists;
    }

    public List<ItemObject> getItemList() {
        return itemList;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerViewHolderStoreCart onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layoutView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_list_store_cart, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        layoutView.setLayoutParams(lp);
        return new RecyclerViewHolderStoreCart(layoutView);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderStoreCart holder, int position) {/*final int position*/
        try {
            holder.root.setOnLongClickListener(v -> {
                if (holder.jenisLayout.equals("2")) {
                    itemList.remove(holder.getAbsoluteAdapterPosition());
                    notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Berhasil menghapus " + holder.tv_nama.getText(), Toast.LENGTH_SHORT).show();
                }
                return false;
            });
            holder.tv_kode.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getItem(0));
            holder.tv_nama.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getItem(1));
            holder.tv_stok.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getItem(2));
            holder.tv_harga.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getItem(3));
            holder.lokasi_di_store = itemList.get(holder.getAbsoluteAdapterPosition()).getItem(4);
            holder.jenisLayout = itemList.get(holder.getAbsoluteAdapterPosition()).getItem(5);

            holder.btn_add.setOnClickListener(v -> {
                if (holder.jenisLayout.equals("1") && !holder.isClicked) {
                    HomeActivity.addRowListItemCart(
                            "" + holder.tv_kode.getText(),
                            "" + holder.tv_nama.getText(),
                            "1",
                            "" + holder.tv_harga.getText(),
                            holder.lokasi_di_store,
                            "2"); //add ke cart,lalu di setJenis = 2
                } else if (holder.jenisLayout.equals("2")) {
                    HomeActivity.showDialogAdd(
                            holder.getAbsoluteAdapterPosition(),
                            "" + holder.tv_kode.getText(),
                            "" + holder.tv_nama.getText(),
                            "" + holder.tv_stok.getText(),
                            "" + holder.tv_harga.getText(),
                            holder.lokasi_di_store,
                            "2");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return itemList.size();
        } catch (NullPointerException ignored) {
        }
        return 0;
    }
}

class RecyclerViewHolderStoreCart extends RecyclerView.ViewHolder {
    LinearLayout root;
    TextView tv_kode, tv_nama, tv_stok, tv_harga, btn_add;
    String lokasi_di_store;
    String jenisLayout;
    boolean isClicked = false;
    RecyclerViewHolderStoreCart(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.root);
        tv_kode = itemView.findViewById(R.id.tv_kode);
        tv_nama = itemView.findViewById(R.id.tv_nama);
        tv_stok = itemView.findViewById(R.id.tv_stok);
        tv_harga = itemView.findViewById(R.id.tv_harga);
        btn_add = itemView.findViewById(R.id.btn_add);
    }
}