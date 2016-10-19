package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ucai.fulicenter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public  abstract class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initView();
        initData();
        setListener();
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    protected abstract void setListener();

    protected abstract void initView();

    protected abstract void initData();

}
