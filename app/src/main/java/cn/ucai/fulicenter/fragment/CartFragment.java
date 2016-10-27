package cn.ucai.fulicenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2016/10/27 0027.
 */

public class CartFragment extends BaseFragment {
    MainActivity mContext;
    ArrayList<CartBean> mlist;
    CartAdapter mAdapter;
    LinearLayoutManager llm;
    @BindView(R.id.tv_rfresh)
    TextView tvRfresh;
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
        NetDao.findCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                srl.setRefreshing(false);
                tvRfresh.setVisibility(View.GONE);
                if (result != null && result.length > 0) {
                    ArrayList<CartBean> list = ConvertUtils.array2List(result);
                    mAdapter.initData(list);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
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


}

    @Override
    protected void initData() {
        downloadCart();
    }
}
