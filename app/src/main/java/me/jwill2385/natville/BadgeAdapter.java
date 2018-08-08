package me.jwill2385.natville;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class BadgeAdapter extends BaseAdapter{
    private Context mContext;

    public BadgeAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
    private Integer[] mThumbIds = {
            R.drawable.ic_badge, R.drawable.ic_badge2, R.drawable.ic_locked, R.drawable.ic_locked,
            R.drawable.ic_locked, R.drawable.ic_locked, R.drawable.ic_locked, R.drawable.ic_locked,
            R.drawable.ic_locked, R.drawable.ic_locked, R.drawable.ic_locked, R.drawable.ic_locked,
            R.drawable.ic_locked, R.drawable.ic_locked, R.drawable.ic_locked
    };

}
