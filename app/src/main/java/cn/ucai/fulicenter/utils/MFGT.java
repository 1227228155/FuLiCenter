package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.Activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.Activity.CategoryChildActivity;
import cn.ucai.fulicenter.Activity.CollectsActivity;
import cn.ucai.fulicenter.Activity.GoodsDetailActivity;
import cn.ucai.fulicenter.Activity.LoginActivity;
import cn.ucai.fulicenter.Activity.MainActivity;

import cn.ucai.fulicenter.Activity.RegisterActivity;
import cn.ucai.fulicenter.Activity.SetNickActivity;
import cn.ucai.fulicenter.Activity.UserSetActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Context context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
       startActivity(context,intent);
    }
    public static void gotoGoodsDetails(Context context, int goodsID){
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsID);
        startActivity(context,intent);
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoBoutiqueChildActivity(Context context, BoutiqueBean bean){
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueChildActivity.class);
        intent.putExtra(I.Boutique.CAT_ID,bean);
        startActivity(context,intent);
    }
    public static void gotoCategoryChildActivity(Context context, int catID, String name, ArrayList<CategoryChildBean> list){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catID);
        intent.putExtra(I.CategoryGroup.NAME,name);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
    }
    public static void gotoLogin(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_LOGIN);
    }
    public  static  void gotoRegister(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_REGISTER);

    }
    public static  void startActivityForResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoUserSet(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, UserSetActivity.class);
        startActivity(context,intent);
    }
    public static void gotoUpdateNick(Activity context){
        startActivityForResult(context,new Intent(context,SetNickActivity.class),I.REQUEST_CODE_NICK);
    }
    public static void gotoCollects(Activity context){
        startActivity(context, CollectsActivity.class);
    }

}
