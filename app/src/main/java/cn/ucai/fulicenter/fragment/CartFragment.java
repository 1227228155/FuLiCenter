package cn.ucai.fulicenter.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;


public class CartFragment extends BaseFragment {

    /**
     * Created by Administrator on 2016/10/27 0027.
     */

    MainActivity mContext;
    ArrayList<CartBean> mlist;
    CartAdapter mAdapter;
    LinearLayoutManager llm;

    updateCartReceiver updateCartReceiver;

    String cartIds = null;
    @BindView(R.id.tv_cart_sum_price)
    TextView tvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    @BindView(R.id.tv_cart_buy)
    TextView tvCartBuy;
    @BindView(R.id.layout_cart)
    RelativeLayout mLayoutCart;
    @BindView(R.id.tv_rfresh)
    TextView tvRfresh;
    @BindView(R.id.tv_nothing)
    TextView tvNothing;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getContext();
        mlist = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, mlist);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void setListener() {
        setPullDownListener();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_CART);
        updateCartReceiver = new updateCartReceiver();
        mContext.registerReceiver(updateCartReceiver, filter);
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRfresh.setVisibility(View.VISIBLE);
                downloadCart();
            }
        });
    }

    private void downloadCart() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.findCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String result) {
                    ArrayList<CartBean> list = ResultUtils.getCartFromJson(result);
                    srl.setRefreshing(false);
                    tvRfresh.setVisibility(View.GONE);
                    if (list != null && list.size() > 0) {
                        mlist.clear();
                        mlist.addAll(list);
                        mAdapter.initData(mlist);
                        setCartLayout(true);
                    } else {
                        setCartLayout(false);
                    }
                }


                @Override
                public void onError(String error) {
                    tvRfresh.setVisibility(View.VISIBLE);
                    srl.setRefreshing(false);
                    setCartLayout(false);
                }
            });
        }
    }


    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        setCartLayout(false);

    }

    @Override
    protected void initData() {
        downloadCart();
        sumPrice();
    }

    @OnClick(R.id.tv_cart_buy)
    public void onClick() {
        if (cartIds != null) {
            MFGT.gotoBuy(mContext, cartIds);
        } else {
            CommonUtils.showLongToast(R.string.CART_NOTING);
        }

    }


    public void setCartLayout(boolean cartLayout) {
        mLayoutCart.setVisibility(cartLayout ? View.VISIBLE : View.GONE);
       tvNothing.setVisibility(cartLayout ? View.GONE : View.VISIBLE);
       rv.setVisibility(cartLayout ? View.VISIBLE : View.GONE);
        sumPrice();

    }

    private void sumPrice() {
        int sumPrice = 0;
        int ranPrice = 0;
        cartIds = null;
        if (mlist != null && mlist.size() > 0) {
            for (CartBean c : mlist) {
                if (c.isChecked()) {
                    cartIds += c.getId() + ",";
                    sumPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    ranPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                }
            }
            tvCartSumPrice.setText("合计  : ￥" + Double.valueOf(ranPrice));
            tvCartSavePrice.setText("节省  : ￥" + Double.valueOf(sumPrice - ranPrice));
        } else {
            tvCartSumPrice.setText("合计  : ￥0");
            tvCartSavePrice.setText("节省  : ￥0");
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    class updateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            sumPrice();

            setCartLayout(mlist != null && mlist.size() > 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateCartReceiver != null) {
            mContext.unregisterReceiver(updateCartReceiver);
        }
    }
}

