package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CategoryChildActivity extends BaseActivity {
    CategoryChildActivity mContext;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mlist;

    int pageID = 1;
    GridLayoutManager glm;
    BoutiqueBean boutique;
    @BindView(R.id.tvCommonTitle)
    TextView tvCommonTitle;
    @BindView(R.id.tv_rfresh)
    TextView tvRfresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.activity_category_child)
    LinearLayout activityCategoryChild;

    int catID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        mContext=this;
        mlist= new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext,mlist);
       catID = getIntent().getIntExtra(I.CategoryChild.CAT_ID,0);
        if (catID==0){
            finish();
        }
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
        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        rv.setLayoutManager(glm);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
    }

    @Override
    protected void initData() {
        downloadCategoryGoods(I.ACTION_DOWNLOAD);
    }
    @OnClick(R.id.backClickArea)
    public void onCLick(){
        MFGT.finish(this);
    }
    @Override
    protected  void setListener() {
        setPullDownListener();
        setPullUoListener();
    }

    private void setPullUoListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = glm.findLastVisibleItemPosition();
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        &&lastVisibleItemPosition==mAdapter.getItemCount()-1
                        && mAdapter.isMore()){
                    pageID++;
                    downloadCategoryGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = glm.findFirstVisibleItemPosition();
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
                downloadCategoryGoods(I.ACTION_PULL_DOWN);
            }


        });
    }

    private void downloadCategoryGoods(final int action) {
        NetDao.downloadCategoryGoods(mContext,catID, pageID, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
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
}
