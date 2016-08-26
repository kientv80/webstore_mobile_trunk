package com.webstore.webstore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.webstore.webstore.R;
import com.webstore.webstore.adapter.TextImageAdapter;
import com.webstore.webstore.entity.CateItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LAP10572-local on 8/25/2016.
 */
public class CategoryListViewActivity extends AppCompatActivity {
    List<CateItem> cateItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cateItemList = new ArrayList<>();
        CateItem item1 = new CateItem();
        item1.setLabel("abc");
        item1.setImageUrl("http://360hay.com/images/icons/category/ic_news.png");
        cateItemList.add(item1);
        setContentView(R.layout.category_listview);
        ListView listView = (ListView)findViewById(R.id.cate_items);
        TextImageAdapter textImageAdapter = new TextImageAdapter(this,R.layout.text_image_item,cateItemList);
        listView.setAdapter(textImageAdapter);
    }
}
