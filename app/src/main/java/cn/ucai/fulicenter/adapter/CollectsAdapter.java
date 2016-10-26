package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FooterViewHolder;


public class CollectsAdapter extends RecyclerView.Adapter {
    public CollectsAdapter(List<CollectBean> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }
    //
    public void setSoryBy(int soryBy) {
        this.soryBy = soryBy;
        notifyDataSetChanged();
    }

    Context mContext;
    List<CollectBean> mList;

    int soryBy=I.SORT_BY_ADDTIME_DESC;
    //3
    boolean isMore;
    //..
//绑定我们的布局文件
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new CollectsViewHolder(View.inflate(mContext, R.layout.item_collect, null));
        }
        return holder;
    }
//绑定数据

    public boolean isMore() {
        return isMore;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder vh= (FooterViewHolder) holder;
            vh.Footer.setText(getFootString());
        } else {

            CollectsViewHolder vh= (CollectsViewHolder) holder;
            CollectBean goods = mList.get(position);
            ImageLoader.downloadImg(mContext,vh.itemGoodsImageView,goods.getGoodsThumb());
            vh.itemGoodsName.setText(goods.getGoodsName());
            vh.mLayoutGoods.setTag(goods);
        }
    }
    //4
    private int getFootString() {
        return isMore?R.string.load_more:R.string.no_more;
    }
    //..
    //商品图片显示
//显示商品的总量
    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<CollectBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setMore(boolean more) {
        isMore=more;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CollectBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }




    class CollectsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_goods_imageView)
        ImageView itemGoodsImageView;
        @BindView(R.id.item_goods_name)
        TextView itemGoodsName;
        @BindView(R.id.NewGoodsly)
        RelativeLayout mLayoutGoods;


        CollectsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
       @OnClick(R.id.item_goods_imageView)
        public void onGoodsItemClick(){
            CollectBean goods= (CollectBean) mLayoutGoods.getTag();
            MFGT.gotoGoodsDetails(mContext,goods.getGoodsId());

        }
      @OnClick(R.id.iv_Collect_del)
        public void deleteCollect(){
            final CollectBean goods= (CollectBean) mLayoutGoods.getTag();
            String username= FuLiCenterApplication.getUser().getMuserName();
            NetDao.deleteCollect(mContext, username, goods.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    L.e("ppppppppppppppp"+result);
                    if (result != null && result.isSuccess()) {
                        mList.remove(goods);
                        notifyDataSetChanged();

                    } else {
                        CommonUtils.showLongToast(result != null ? result.getMsg() :
                                mContext.getResources().getString(R.string.delete_collect_fail));
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showLongToast(mContext.getResources().getString(R.string.delete_collect_fail));

                }
            });
        }
    }

}
