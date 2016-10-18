package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;

import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    MainActivity mContext;
    @BindView(R.id.tv_rfresh)
    TextView tvRfresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mlist;
    
    int pageID =1;
    GridLayoutManager manager;
    boolean isMore;


    public NewGoodsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mlist= new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext,mlist);
        initView();
        initData();
        setListener();
        return layout;
    }

    private void initData() {
        downloadNewGoods(I.ACTION_DOWNLOAD);
    }

    private void setListener() {
        setPullDownListener();
        setPullUoListener();
    }

    private void setPullUoListener() {
    rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
            if (newState==RecyclerView.SCROLL_STATE_IDLE
                            &&lastVisibleItemPosition==mAdapter.getItemCount()-1
                            && mAdapter.isMore()){
                pageID++;
                downloadNewGoods(I.ACTION_PULL_UP);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
            srl.setEnabled(firstVisibleItemPosition==0);
        }
    });
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRfresh.setVisibility(View.VISIBLE);
                pageID=1;
                downloadNewGoods(I.ACTION_PULL_DOWN);
            }


        });
    }

    private void downloadNewGoods(final int action) {
        NetDao.downloadNewGoods(mContext, pageID, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvRfresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list= ConvertUtils.array2List(result);
                    if (action==I.ACTION_PULL_DOWN||action==I.ACTION_DOWNLOAD){
                        mAdapter.initData(list);
                    }else {
                        mAdapter.addData(list);
                    }
                    if (list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                }else {
                    mAdapter.setMore(false);
                }
    }



            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRfresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.e("error"+error);

            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        manager = new GridLayoutManager(mContext, I.COLUM_NUM);
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
    }


}
