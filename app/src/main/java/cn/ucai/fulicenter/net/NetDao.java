package cn.ucai.fulicenter.net;

import android.content.Context;

import cn.ucai.fulicenter.Activity.GoodsDetailActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class NetDao {
    public static void downloadNewGoods(Context context, int pageID, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageID))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    public static void downloadGoodsDetail(GoodsDetailActivity mContext, int goodsId, OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener) {
            OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils(mContext);
            utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                    .addParam(I.GoodsDetails.KEY_GOODS_ID, String.valueOf(goodsId))
                    .targetClass(GoodsDetailsBean.class)
                    .execute(listener);

        }
    public  static  void  downloadBoutique(Context context, int pageID, OkHttpUtils.OnCompleteListener<BoutiqueBean> listener){
        OkHttpUtils<BoutiqueBean> utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .addParam(I.Boutique.CAT_ID, String.valueOf(pageID))
                .targetClass(BoutiqueBean.class)
                .execute(listener);
    }

}
