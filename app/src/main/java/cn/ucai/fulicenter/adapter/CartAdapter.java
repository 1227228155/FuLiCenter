package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;
    @BindView(R.id.cart_checkbox)
    CheckBox cartCheckbox;
    @BindView(R.id.cart_image)
    ImageView cartImage;
    @BindView(R.id.cart_name)
    TextView cartName;
    @BindView(R.id.cart_add)
    ImageView cartAdd;
    @BindView(R.id.cart_count)
    TextView cartCount;
    @BindView(R.id.cart_del)
    ImageView cartDel;
    @BindView(R.id.cart_price)
    TextView cartPrice;


    public CartAdapter(Context context, ArrayList<CartBean> list) {
        mContext = context;
        mList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder
                holder = new CartViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CartBean CartBean = mList.get(position);
        GoodsDetailsBean goodsDetailsBean = CartBean.getGoods();
        CartViewHolder cartViewHolder = (CartViewHolder) holder;
        if (goodsDetailsBean != null) {
            ImageLoader.downloadImg(mContext, cartViewHolder.cartImage, goodsDetailsBean.getGoodsThumb());
            cartViewHolder.cartName.setText(goodsDetailsBean.getGoodsName());
            cartViewHolder.cartPrice.setText(goodsDetailsBean.getCurrencyPrice());
        }
        cartViewHolder.cartCount.setText("(" + CartBean.getCount() + ")");
        cartViewHolder.cartCheckbox.setChecked(false);
        cartViewHolder.cartCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CartBean.setChecked(b);
                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<CartBean> list) {
        mList =list;
        notifyDataSetChanged();
    }


    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cart_checkbox)
        CheckBox cartCheckbox;
        @BindView(R.id.cart_image)
        ImageView cartImage;
        @BindView(R.id.cart_name)
        TextView cartName;
        @BindView(R.id.cart_add)
        ImageView cartAdd;
        @BindView(R.id.cart_count)
        TextView cartCount;
        @BindView(R.id.cart_del)
        ImageView cartDel;
        @BindView(R.id.cart_price)
        TextView cartPrice;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
/*
        @OnClick(R.id.boutique_linearLayout)
        public void onBoutiqueClick() {
            CartBean bean = (BoutiqueBean) boutiqueLinearLayout.getTag();
            MFGT.gotoBoutiqueChildActivity(mContext, bean);
        }*/
    }
}
