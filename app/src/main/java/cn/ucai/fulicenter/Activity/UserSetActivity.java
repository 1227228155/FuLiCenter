package cn.ucai.fulicenter.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharedPreferencesUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;

public class UserSetActivity extends BaseActivity {

    @BindView(R.id.user_set_avatar)
    ImageView userSetAvatar;
    @BindView(R.id.user_set_nick)
    TextView userSetNick;
    UserSetActivity mContext;
    @BindView(R.id.user_into_update_nick)
    ImageView userIntoUpdateNick;
    User user;
    OnSetAvatarListener mOnSetAavatarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_set);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
         user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, userSetAvatar);
            userSetNick.setText(user.getMuserNick());
        } else {
            MFGT.gotoLogin(mContext);
            finish();
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){
            return;
        }
        if (data==null){
            return;
        }
        mOnSetAavatarListener.setAvatar(requestCode,data,userSetAvatar);
        if (requestCode== I.REQUEST_CODE_NICK){
            CommonUtils.showLongToast(R.string.update_user_nick);
        }
        if (requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
                    updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext,
                user.getMavatarPath()+"/"+user.getMuserName()
                +I.AVATAR_SUFFIX_JPG));
        L.e("FILE+"+file.exists());
        L.e("FILE"+file.getAbsolutePath());
        final ProgressDialog pd =new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.logining));
        pd.show();
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result==null){
                    CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                }else {
                    User u = (User) result.getRetData();
                    if (result.isRetMsg()){
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u),mContext,userSetAvatar);
                        CommonUtils.showLongToast(R.string.update_user_avatar_success);
                    }else {
                        CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                pd.dismiss();
            }
        });
    }

    @OnClick({R.id.back, R.id.user_set_exit,R.id.user_set_avatar,R.id.user_into_update_nick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                MFGT.finish(mContext);
                break;
            //用户退出登录，删除用户信息
            case R.id.user_set_exit:
                SharedPreferencesUtils.getInstance(mContext).removeUser();
                FuLiCenterApplication.setUser(null);
                MFGT.gotoLogin(mContext);
                break;
            //跳转到修改用户昵称的界面
            case R.id.user_into_update_nick:
                MFGT.gotoUpdateNick(mContext);
                break;
            //修改用户头像
           case R.id.user_set_avatar:
               mOnSetAavatarListener =new OnSetAvatarListener(mContext,R.id.activity_user_set,user.getMuserName(),I.AVATAR_TYPE_USER_PATH);
            break;
        }
    }


}
