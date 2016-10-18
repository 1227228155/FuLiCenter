package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailActivity extends AppCompatActivity {
    int goodsId;
    GoodsDetailActivity mContext;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tvgoodenglish)
    TextView tvgoodnameenglish;
    @BindView(R.id.tvgoodname)
    TextView tvgoodname;
    @BindView(R.id.tvgoodpriceshop)
    TextView tvgoodpriceshop;
    @BindView(R.id.tvgoodpricecurrent)
    TextView tvgoodpricecurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.goodbref)
    WebView wvgoodbrief;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details", "goodsid" + goodsId);
        if (goodsId == 0) {
            L.e("ccccccccccccccccccccccccc");
            finish();
        }
        initView();
        initData();
        setListener();
    }

    private void setListener() {

    }

    private void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.e("qqqqqqqq"+goodsId);
                L.i("details=" + result);
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    L.e("wocaonime cccccccccccccccc");
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details,error=" + error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        tvgoodnameenglish.setText(details.getGoodsEnglishName());
        tvgoodname.setText(details.getGoodsName());
        tvgoodpricecurrent.setText(details.getCurrencyPrice());
        tvgoodpriceshop.setText(details.getShopPrice());
        salv.startPlayLoop(indicator,getAlbumImgUrl(details),getAlbumImgCount(details));
        wvgoodbrief.loadDataWithBaseURL(null,details.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums=details.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i=0;i<albums.length;i++) {
                urls[i] = albums[i].getImgUrl();
            }

        }

        return urls;
    }

    private void initView() {


    }
    @OnClick(R.id.backClickArea)
    public void onBackClick(){
        MFGT.finish(this);
    }

}
