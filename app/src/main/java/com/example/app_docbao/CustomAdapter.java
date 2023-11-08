package com.example.app_docbao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<DocBao>{
    public CustomAdapter(Context context, int resource, List<DocBao> items){
        super(context,resource,items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view =convertView;
        if(view==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.layout_listview,null);

        }
        DocBao p =getItem(position);
        if(p!=null){
            TextView txttitle =(TextView) view.findViewById(R.id.textViewtitle);
            txttitle.setText(p.title);
            TextView txtpubdate=(TextView) view.findViewById(R.id.textViewpubDate);
            txtpubdate.setText(p.pubDate);
            ImageView imageView=view.findViewById(R.id.imageView2);
            Picasso.with(getContext()).load(p.image).into(imageView);
        }
        return view;
    }
}
