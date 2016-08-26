package com.webstore.webstore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webstore.webstore.R;
import com.webstore.webstore.entity.CateItem;
import com.webstore.webstore.network.DownLoadImageTask;

import java.util.List;

/**
 * Created by LAP10572-local on 8/25/2016.
 */
public class TextImageAdapter extends ArrayAdapter<CateItem> {
    private final Activity context;
    List<CateItem> items;
    public TextImageAdapter(Activity context, int resource, List<CateItem> items) {
        super(context, resource,items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.text_image_item, null, true);
        ImageView imageView = (ImageView)view.findViewById(R.id.item_image);
        new DownLoadImageTask(imageView).execute(items.get(position).getImageUrl());
        TextView textView = (TextView)view.findViewById(R.id.item_text);
        textView.setText(items.get(position).getLabel());
        return view;
    }
}
