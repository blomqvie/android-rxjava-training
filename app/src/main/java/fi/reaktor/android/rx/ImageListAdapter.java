package fi.reaktor.android.rx;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageListAdapter extends BaseAdapter {

    private final List<String> urls;
    private final Context context;

    public ImageListAdapter(List<String> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if(view == null) {
            view = new ImageView(context);
        }
        // TODO use placeholder to avoid glitch
        Picasso.with(context).load(getItem(position)).into(view);
        return view;
    }
}
