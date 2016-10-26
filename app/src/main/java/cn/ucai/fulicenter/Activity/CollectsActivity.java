package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectsAdapter;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CollectsActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_rfresh)
    TextView tvRfresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.activity_collects)
    LinearLayout activityCollects;
    CollectsActivity mContext;
    ArrayList<CollectBean> mlist;
    int pageID = 1;
    GridLayoutManager manager;
    boolean isMore;
    CollectsAdapter mAdapter;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        mlist = new ArrayList<>();
        mContext = this;
        mAdapter = new CollectsAdapter(mlist, mContext);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
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
        rv.addItemDecoration(new SpaceItemDecoration(12));
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        }
        downloadCollects(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void downloadCollects(final int action) {
        NetDao.downloadCollects(mContext, user.getMuserName(), pageID, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                srl.setRefreshing(false);
                tvRfresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    mAdapter.setMore(false);
                }
            }


            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRfresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.e("error" + error);

            }
        });
    }


    @Override
    protected void setListener() {
        setPullDownListener();
        setPullUoListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRfresh.setVisibility(View.VISIBLE);
                pageID = 1;
                downloadCollects(I.ACTION_PULL_DOWN);
            }


        });
    }

    private void setPullUoListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageID++;
                    downloadCollects(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                srl.setEnabled(firstVisibleItemPosition == 0);
            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        MFGT.finish(mContext);
    }
}
