package org.cn.google.hook_import.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cn.google.mode.SkuDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class SkuListAdapter extends BaseAdapter {

    private List<SkuDetailsModel> skuDetailsModels;

    private Context mContext;

    public SkuListAdapter(Context context, List<SkuDetailsModel> skuDetailsModels) {
        this.mContext = context;
        this.skuDetailsModels = skuDetailsModels;
    }

    public void setNewInstance(List<SkuDetailsModel> skuDetailsModels) {
        this.skuDetailsModels.clear();
        this.skuDetailsModels.addAll(skuDetailsModels);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return skuDetailsModels.size();
    }

    @Override
    public Object getItem(int i) {
        return skuDetailsModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LinearLayout linearLayout = new LinearLayout(this.mContext);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

            viewHolder.skuName = new TextView(this.mContext);
            viewHolder.skuName.setGravity(Gravity.CENTER);
            viewHolder.skuName.setTextColor(Color.BLACK);
            viewHolder.skuName.setTextSize(12.0f);
            viewHolder.skuName.setPadding(0, 35, 0, 35);
            viewHolder.skuName.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1.0f));

            viewHolder.skuPrice = new TextView(this.mContext);
            viewHolder.skuPrice.setGravity(Gravity.CENTER);
            viewHolder.skuPrice.setTextColor(Color.BLACK);
            viewHolder.skuPrice.setTextSize(12.0f);
            viewHolder.skuPrice.setPadding(0, 35, 0, 35);
            viewHolder.skuPrice.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 2.0f));

            linearLayout.addView(viewHolder.skuName);
            linearLayout.addView(viewHolder.skuPrice);
            linearLayout.setTag(viewHolder);
            view = linearLayout;
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.skuName.setText(this.skuDetailsModels.get(i).getTitle());
        viewHolder.skuPrice.setText(this.skuDetailsModels.get(i).getMoney());

        return view;
    }

    static class ViewHolder {
        TextView skuName, skuPrice;
    }

}
