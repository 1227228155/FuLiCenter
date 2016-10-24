package cn.ucai.fulicenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/10/24 0024.
 */

public class PersonalCenterFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View layout = inflater.inflate(R.layout.fragment_personal, container, false);
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

    }
}
