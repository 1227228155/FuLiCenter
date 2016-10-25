package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharedPreferencesUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

public class UserSetActivity extends BaseActivity {

    @BindView(R.id.user_set_avatar)
    ImageView userSetAvatar;
    @BindView(R.id.user_set_nick)
    TextView userSetNick;
    UserSetActivity mContext;
    @BindView(R.id.user_into_update_nick)
    ImageView userIntoUpdateNick;

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
        User user = FuLiCenterApplication.getUser();
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


    @OnClick({R.id.back, R.id.user_set_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                MFGT.finish(mContext);
                break;
            case R.id.user_set_exit:
                SharedPreferencesUtils.getInstance(mContext).removeUser();
                FuLiCenterApplication.setUser(null);
                MFGT.gotoLogin(mContext);
                break;
        }
        MFGT.finish(mContext);
    }
    @OnClick(R.id.user_into_update_nick)
    public  void updateNick(){
        MFGT.gotoUpdateNick(mContext);
    }


}
