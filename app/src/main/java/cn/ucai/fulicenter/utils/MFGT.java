package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.Activity.NewGoodsDetailActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;



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
        intent.setClass(context, NewGoodsDetailActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsID);
        startActivity(context,intent);
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
