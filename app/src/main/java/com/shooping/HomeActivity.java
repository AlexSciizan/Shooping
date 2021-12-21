package com.shooping;

import static com.shooping.Components.Utility.DialogAdd;
import static com.shooping.Components.Utility.DialogLoading;
import static com.shooping.Components.Utility.roundRecWhite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.shooping.Components.GeneratExcel;
import com.shooping.Components.ItemObject;
import com.shooping.Components.RecyclerviewAdapterStoreCart;
import com.shooping.Components.viewpager_containing_layout.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

//task
//item yang dibeli tidak boleh lebih dari stock yang tersedia
//jika stock <=0 product otomatis terhapus
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    // store itu barang2 yang ada di toko
    // cart itu barang yang dibeli dan udah masuk kekeranjang belanja user
    TextView btn_import;
    LinearLayout btn_1, btn_2;
    ActivityResultLauncher<Intent> resultLauncher;
    Intent intent;

    @SuppressLint("StaticFieldLeak")
    static RecyclerviewAdapterStoreCart adapterStore;
    static RecyclerView recyclerViewStore;
    static List<ItemObject> rowListItemStore = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    static RecyclerviewAdapterStoreCart adapterCart;
    static RecyclerView recyclerViewCart;
    static List<ItemObject> rowListItemCart = new ArrayList<>();

    static Dialog dialog;
    ViewPager viewPager;
    ViewPagerAdapter vpa4;
    Button btn_submit, btn_cancel;
    LinearLayout ll_submit_n_cancel;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dialog = DialogLoading(HomeActivity.this);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(v -> {
            adapterCart.getItemList().clear();
            adapterCart.notifyDataSetChanged();
        });
        btn_submit.setOnClickListener(v -> {
            int stock;
            for (int i = 0; i < rowListItemCart.size(); i++) {
                // 0.kode,  1.nama,  2.stok,  3.harga,  4.lokasi_di_store,  5.jenisLayout
                stock = Integer.parseInt(
                        rowListItemStore.get(Integer.parseInt(rowListItemCart.get(i).getItem(4))
                        ).getItem(2)) -
                        Integer.parseInt(rowListItemCart.get(i).getItem(2));
                System.out.println("stock : " + Integer.parseInt(rowListItemStore.get(Integer.parseInt(rowListItemCart.get(i).getItem(4)))
                        .getItem(2)) + " - " + Integer.parseInt(rowListItemCart.get(i).getItem(2)));
                if (stock <= 0) {
                    rowListItemStore.remove(Integer.parseInt(rowListItemCart.get(i).getItem(4)));
                } else
                    // 0.kode,  1.nama,  2.stok,  3.harga,  4.lokasi_di_store,  5.jenisLayout
                    rowListItemStore.set(Integer.parseInt(rowListItemCart.get(i).getItem(4)),
                            new ItemObject(new String[]{
                                    rowListItemCart.get(i).getItem(0),
                                    rowListItemCart.get(i).getItem(1),
                                    "" + stock,
                                    rowListItemStore.get(Integer.parseInt(rowListItemCart.get(i).getItem(4))).getItem(3),
                                    rowListItemCart.get(i).getItem(4),
                                    rowListItemCart.get(i).getItem(5)
                            }));
            }
            adapterStore.notifyDataSetChanged();
            adapterCart.getItemList().clear();
            adapterCart.notifyDataSetChanged();
        });

        ll_submit_n_cancel = findViewById(R.id.ll_submit_n_cancel);
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        try {
                            assert result.getData() != null;
                            new GeneratExcel(
                                    HomeActivity.this,
                                    result.getData().getData().getPath().replaceFirst("/storage_root", ""),
                                    rowListItemStore,
                                    adapterStore).execute(btn_import);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, "");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_1.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_2.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));

        btn_import = findViewById(R.id.btn_import);
        btn_import.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    resultLauncher.launch(intent);
                }
            }
        });

        recyclerViewStore = new RecyclerView(getBaseContext());
        recyclerViewCart = new RecyclerView(getBaseContext());

        adapterStore = new RecyclerviewAdapterStoreCart(rowListItemStore);
        adapterCart = new RecyclerviewAdapterStoreCart(rowListItemCart);
        //awal
        // kode,  nama,  stok,  harga,  lokasi_di_store,  jenisLayout
        rowListItemStore.add(new ItemObject(new String[]{"CB0491", "Topi1", "5", "20000", "0", "1"}));
        rowListItemStore.add(new ItemObject(new String[]{"CB0492", "Topi2", "4", "22000", "1", "1"}));
        rowListItemStore.add(new ItemObject(new String[]{"CB0493", "Topi3", "3", "23000", "2", "1"}));
        rowListItemStore.add(new ItemObject(new String[]{"CB0494", "Topi4", "4", "24000", "3", "1"}));
        rowListItemStore.add(new ItemObject(new String[]{"CB0495", "Topi5", "5", "25000", "4", "1"}));

        LinearLayoutManager ll_cart = new LinearLayoutManager(getBaseContext());
        ll_cart.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager ll_store = new LinearLayoutManager(getBaseContext());
        ll_store.setOrientation(RecyclerView.VERTICAL);

        recyclerViewStore.setNestedScrollingEnabled(true);
        recyclerViewCart.setNestedScrollingEnabled(true);
        recyclerViewStore.setLayoutManager(ll_store);
        recyclerViewCart.setLayoutManager(ll_cart);

        recyclerViewStore.setAdapter(adapterStore);
        recyclerViewCart.setAdapter(adapterCart);
        recyclerViewStore.setHasFixedSize(true);
        recyclerViewCart.setHasFixedSize(true);

        try {
            vpa4 = new ViewPagerAdapter(HomeActivity.this,
                    new RecyclerView[]{recyclerViewStore, recyclerViewCart});
            viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(vpa4);
            viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (viewPager.getCurrentItem() == 0) {
                        btn_1.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
                        btn_2.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
                        ll_submit_n_cancel.setVisibility(View.GONE);
                    } else {
                        btn_1.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
                        btn_2.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
                        ll_submit_n_cancel.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        ll_submit_n_cancel.setVisibility(View.GONE);
    }

    public static void showDialogAdd(int lokasi_di_cart, String kode, String nama, String stok, String harga, String lokasi_di_store, String jenisLayout) {
        DialogAdd(dialog.getContext(),
                kode,
                nama,
                stok,
                harga,
                lokasi_di_store,
                jenisLayout,
                rowListItemCart,
                lokasi_di_cart,
                adapterCart).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_1) {
            viewPager.setCurrentItem(0, true);
            btn_1.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
            btn_2.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
            ll_submit_n_cancel.setVisibility(View.GONE);
        }
        if (v.getId() == R.id.btn_2) {
            viewPager.setCurrentItem(1, true);
            btn_1.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
            btn_2.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
            ll_submit_n_cancel.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void addRowListItemCart(String kode,
                                          String barang,
                                          String stok,
                                          String harga,
                                          String lokasi_di_store,
                                          String jenisLayout) {
        rowListItemCart.add(new ItemObject(new String[]{
                kode,
                barang,
                "" + stok,
                "" + harga,
                lokasi_di_store,
                jenisLayout}));
        adapterCart.notifyDataSetChanged();
    }
}