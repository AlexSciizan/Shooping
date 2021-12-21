package com.shooping.Components;
public class ItemObject {
    private String[] isiList;
    public ItemObject(String[] isi_list) {
        isiList = isi_list;
    }
    public String getItem(int i) {
        return isiList[i];
    }
}