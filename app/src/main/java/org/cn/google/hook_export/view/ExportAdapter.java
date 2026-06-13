package org.cn.google.hook_export.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cn.google.R;
import org.cn.google.mode.ExportDetails;

import java.util.List;

public class ExportAdapter extends BaseAdapter {

    private Context mContext;
    private ItemOnClickInterface mItemOnClickInterface;
    private List<ExportDetails> exportDetailsList;

    public ExportAdapter(Context context, List<ExportDetails> exportDetailsList, ItemOnClickInterface itemOnClickInterface) {
        this.mContext = context;
        this.exportDetailsList = exportDetailsList;
        this.mItemOnClickInterface = itemOnClickInterface;
    }

    @Override
    public int getCount() {
        return exportDetailsList.size();
    }

    @Override
    public Object getItem(int i) {
        return exportDetailsList.get(i);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_export_count, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.button = (TextView) view.findViewById(R.id.tv_button);
            viewHolder.skuName = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.count = (TextView) view.findViewById(R.id.tv_count);
            viewHolder.skuPrice = (TextView) view.findViewById(R.id.tv_price);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        ExportDetails exportDetailed = this.exportDetailsList.get(i);
        viewHolder.skuName.setText(exportDetailed.getTitle());
        viewHolder.count.setText(exportDetailed.getNum() + "");
        viewHolder.skuPrice.setText(exportDetailed.getPrice());
        viewHolder.button.setOnClickListener(view1 -> {
            if (mItemOnClickInterface != null) {
                mItemOnClickInterface.onItemClick(view1, i);
            }
        });
        return view;

    }

    public void setNewInstance(List<ExportDetails> exportDetailsList) {
        this.exportDetailsList.clear();
        this.exportDetailsList.addAll(exportDetailsList);
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView button;
        TextView count;
        TextView skuName;
        TextView skuPrice;

        ViewHolder() {
        }
    }

    public interface ItemOnClickInterface {
        void onItemClick(View view, int position);
    }
}
