package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<NewGoodsBean> mList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==I.TYPE_FOOTER){

        }else {
            GoodsViewHolder vh = (GoodsViewHolder) holder;
            NewGoodsBean newGoodsBean = mList.get(position);
            vh.itemGoodsName.setText(newGoodsBean.getGoodsName());
            vh.itemGoodsPrice.setText(newGoodsBean.getCurrencyPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size()+1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Footer)
        TextView Footer;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_goods_imageView)
        ImageView itemGoodsImageView;
        @BindView(R.id.item_goods_name)
        TextView itemGoodsName;
        @BindView(R.id.item_goods_price)
        TextView itemGoodsPrice;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
