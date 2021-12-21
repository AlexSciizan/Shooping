package com.shooping.Components;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shooping.R;

import java.util.List;

public class Utility {
    public static GradientDrawable roundRecWhite(int rad, String strokeColor, String feelColor) {
// return roundRect(Color.rgb(255, 255, 255), rad, Color.parseColor("#000000"));
        return roundRect(rad, Color.parseColor(strokeColor), Color.parseColor(feelColor));
    }

    public static GradientDrawable roundRect(int rad, int strokeColor, int feelColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(feelColor);
        gd.setCornerRadius(rad);
        gd.setStroke(1, strokeColor);
        return gd;
    }

    public static Dialog DialogLoading(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_loading);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.biru), android.graphics.PorterDuff.Mode.SRC_ATOP);
        WindowManager.LayoutParams lp = window.getAttributes();
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public static Dialog DialogAdd(Context context,
                                   String kode,
                                   String nama,
                                   String stok,
                                   String harga,
                                   String lokasi_di_store,
                                   String jenisLayout,
                                   List<ItemObject> rowListItemCart,
                                   int lokasi_di_cart,
                                   RecyclerviewAdapterStoreCart adapterCart) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);
        TextView tv_nama_barang, btn_min, tv_jml_barang, btn_plus, tv_harga_barang, btn_ok;
        tv_nama_barang = dialog.findViewById(R.id.tv_nama_barang);
        btn_min = dialog.findViewById(R.id.btn_min);
        tv_jml_barang = dialog.findViewById(R.id.tv_jml_barang);
        btn_plus = dialog.findViewById(R.id.btn_plus);
        tv_harga_barang = dialog.findViewById(R.id.tv_harga_barang);
        btn_ok = dialog.findViewById(R.id.btn_ok);
        tv_nama_barang.setText(nama);
        tv_jml_barang.setText(stok);//angka
        tv_harga_barang.setText(harga);//angka
        btn_plus.setOnClickListener(v -> {
            tv_jml_barang.setTag(Integer.parseInt("" + tv_jml_barang.getText()) + 1);
            tv_jml_barang.setText("" + tv_jml_barang.getTag());
            tv_harga_barang.setText("" + (
                    Integer.parseInt("" + tv_jml_barang.getTag()) *
                            Integer.parseInt(harga.replaceAll("\\.", ""))
            ));
            tv_harga_barang.setText("" + tv_harga_barang.getText().toString()
                    .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)", "$1.$2$3$4.$5$6$7")
                    .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)", "$1$2$3.$4$5$6")
                    .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)", "$1$2.$3$4$5"));
        });
        btn_min.setOnClickListener(v -> {
                    if (Integer.parseInt("" + tv_jml_barang.getText()) > 1) {
                        tv_jml_barang.setTag(Integer.parseInt("" + tv_jml_barang.getText()) - 1);
                        tv_jml_barang.setText("" + tv_jml_barang.getTag());
                        tv_harga_barang.setText("" +
                                (Integer.parseInt("" + tv_jml_barang.getTag()) *
                                        Integer.parseInt("" + harga.replaceAll("\\.", "")))
                        );
                        tv_harga_barang.setText("" + tv_harga_barang.getText().toString()
                                .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)", "$1.$2$3$4.$5$6$7")
                                .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)", "$1$2$3.$4$5$6")
                                .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)", "$1$2.$3$4$5"));
                    }
                }
        );
        btn_ok.setOnClickListener(v -> {
            rowListItemCart.set(lokasi_di_cart,
                    new ItemObject(new String[]{
                            kode,
                            nama,
                            "" + tv_jml_barang.getText(),
                            "" + tv_harga_barang.getText(),
                            lokasi_di_store,
                            jenisLayout
                    }));
            adapterCart.notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
