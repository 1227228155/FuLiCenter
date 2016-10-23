package cn.ucai.fulicenter.net;

import android.content.Context;

import cn.ucai.fulicenter.Activity.GoodsDetailActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.utils.MD5;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class NetDao {
    public static void downloadNewGoods(Context context,int catID, int pageID, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catID))
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
   public  static  void  downloadBoutique(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener){
       OkHttpUtils utils = new OkHttpUtils(context);
       utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
               .targetClass(BoutiqueBean[].class)
               .execute(listener);
   }
    public  static void downloadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }
    public  static void downloadCategoryChild(Context context,int parentID,OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(parentID))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
    public static void downloadCategoryGoods(Context context,int catID, int pageID, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catID))
                .addParam(I.PAGE_ID,String.valueOf(pageID))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    public  static void register(Context context, String name, String nickname, String password, OkHttpUtils.OnCompleteListener<Result> listener){
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
           utils.setRequestUrl(I.REQUEST_REGISTER)
                   .addParam(I.User.USER_NAME,name)
                   .addParam(I.User.NICK,nickname)
                   .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                   .targetClass(Result.class)
                   .post()
                   .execute(listener);
    }



}
