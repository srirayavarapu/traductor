package com.transjan.traductor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoriteAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public FavoriteAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        String[] row = data.get(position).split("@-@kam@-@");
        if (vi == null)
            vi = inflater.inflate(R.layout.row_favorite, null);


        ImageView srcFlag = (ImageView) vi.findViewById(R.id.src_flag);
        srcFlag.setImageResource(context.getResources().getIdentifier("pic" + row[0], "drawable", context.getPackageName()));

        TextView srcText = (TextView) vi.findViewById(R.id.src_text);
        srcText.setText(row[1]);

        ImageView toFlag = (ImageView) vi.findViewById(R.id.to_flag);
        toFlag.setImageResource(context.getResources().getIdentifier("pic" + row[2], "drawable", context.getPackageName()));

        TextView toText = (TextView) vi.findViewById(R.id.to_text);
        toText.setText(row[3]);


        return vi;
    }
}