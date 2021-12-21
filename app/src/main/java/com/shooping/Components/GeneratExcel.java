package com.shooping.Components;

import static com.shooping.Components.Utility.DialogLoading;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class GeneratExcel extends AsyncTask<TextView, Void, Void> {
        Context context;
        Dialog dialog_loading;
        String file_path;
        List<ItemObject> rowListItem;
        RecyclerviewAdapterStoreCart adapter;

        public GeneratExcel() {

        }

        public GeneratExcel(Context context_, String file_path_, List<ItemObject> rowListItem_, RecyclerviewAdapterStoreCart adapter_) {
            context = context_;
            dialog_loading = DialogLoading(context_);
            file_path = file_path_;
            rowListItem = rowListItem_;
            adapter = adapter_;
        }

        @Override
        protected void onPreExecute() {
            dialog_loading.show();
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected Void doInBackground(TextView... textViews) {
// /storage/emulated/0/Download/data_barang.xlsx
            textViews[0].postDelayed(() -> {
                File file = new File(file_path);
                try {
                    InputStream ExcelFileToRead = new FileInputStream(file);
                    XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
                    XSSFSheet sheet = wb.getSheetAt(0);
                    XSSFRow row;
                    XSSFCell cell;
                    Iterator rows = sheet.rowIterator();
                    String[] arr = new String[5];
                    int idx_arr = 0;
                    int loop = 0;
                    while (rows.hasNext()) {
                        row = (XSSFRow) rows.next();
                        Iterator cells = row.cellIterator();
                        while (cells.hasNext()) {
                            cell = (XSSFCell) cells.next();
                            arr[idx_arr] = cell.toString();
                            idx_arr++;
                        }
                        System.out.println();
                        idx_arr = 0;
                        if (loop != 0)
                            rowListItem.add(new ItemObject(new String[]{
                                    arr[0],
                                    arr[1],
                                    arr[2].replace(".0", ""),
                                    arr[3].replace(".0", "")
                                            .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)", "$1.$2$3$4.$5$6$7")
                                            .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)", "$1$2$3.$4$5$6")
                                            .replaceAll("(\\d)(\\d)(\\d)(\\d)(\\d)", "$1$2.$3$4$5"),
                                    "" + (loop - 1),
                                    "1"
                            }));
                        //kode,  nama,  stok,  harga,  lokasi_di_store,  jenisLayout
                        loop++;
                    }
                    if (!rows.hasNext()) {
                        dialog_loading.dismiss();
                    }
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 500);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
}
