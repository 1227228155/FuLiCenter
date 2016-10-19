package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<NewGoodsBean> mList;
    boolean isMore;

    public GoodsAdapter(Boolean isMore) {
        this.isMore = isMore;
    }
    public  boolean isMore(){
        return  isMore;
    }



    public Boolean getMore() {
        return isMore;
    }

    public void setMore(Boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public GoodsAdapter(Context mContext, List<NewGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

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
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.Footer.setText(getFootString());

        }else {
            GoodsViewHolder vh = (GoodsViewHolder) holder;
            NewGoodsBean newGoodsBean = mList.get(position);
            ImageLoader.downloadImg(mContext,vh.itemGoodsImageView,newGoodsBean.getGoodsThumb());
            vh.itemGoodsName.setText(newGoodsBean.getGoodsName());
            vh.itemGoodsPrice.setText(newGoodsBean.getCurrencyPrice());
            vh.mLayoutGoods.setTag(newGoodsBean.getGoodsId());
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

    public void initData(ArrayList<NewGoodsBean> list) {
        if (mList!=null){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }


    public void addData(ArrayList<NewGoodsBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    private int getFootString() {
        return isMore?R.string.load_more:R.string.no_more;
    }


     class GoodsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_goods_imageView)
        ImageView itemGoodsImageView;
        @BindView(R.id.item_goods_name)
        TextView itemGoodsName;
        @BindView(R.id.item_goods_price)
        TextView itemGoodsPrice;
        @BindView(R.id.NewGoodsly)
        LinearLayout mLayoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.NewGoodsly)
        public  void  onGoodsItemClick(){
            int mLayoutGoodsId = (int) mLayoutGoods.getTag();
            MFGT.gotoGoodsDetails(mContext,mLayoutGoodsId);
        }
    }
}
