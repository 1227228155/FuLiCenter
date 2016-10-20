package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends BaseFragment {

    CategoryAdapter mAdapter;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    @BindView(R.id.category_expandableListView)
    ExpandableListView categoryExpandableListView;

    MainActivity mContext;
    int groupCount;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mContext = (MainActivity) getContext();
        mAdapter = new CategoryAdapter(mContext, mGroupList, mChildList);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {

        categoryExpandableListView.setGroupIndicator(null);
        categoryExpandableListView.setAdapter(mAdapter);


    }

    @Override
    protected void initData() {
        downloadGroup();
    }

    private void downloadGroup() {
        NetDao.downloadCategoryGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result!=null&&result.length>0){
                    ArrayList<CategoryGroupBean> GroupList= ConvertUtils.array2List(result);
                    mGroupList.addAll(GroupList);
                    for (int i=0;i<GroupList.size();i++){
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        CategoryGroupBean g = GroupList.get(i);
                        downloadChild(g.getId(),i);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e("error="+error);
            }
        });
    }

    private void downloadChild(int id, final int index) {
        NetDao.downloadCategoryChild(mContext, id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {

            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                if (result != null && result.length > 0) {
                    ArrayList<CategoryChildBean> childList = ConvertUtils.array2List(result);
                    mChildList.set(index, childList);
                }
                if (groupCount==mGroupList.size()){
                    mAdapter.initData(mGroupList,mChildList);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
