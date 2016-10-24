package cn.ucai.fulicenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/24 0024.
 */

public class PersonalCenterFragment extends BaseFragment {
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    MainActivity mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getActivity();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user==null){
            MFGT.gotoLogin(mContext);
        }else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserNick());
        }
    }

    @OnClick(R.id.tv_center_set)
    public void onClick() {
    }
}
