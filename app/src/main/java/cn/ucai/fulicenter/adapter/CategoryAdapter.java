package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/20 0020.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;


    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        mContext = context;
        mGroupList = new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList = new ArrayList<>();
        mChildList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return mChildList != null && mChildList.get(i) != null ?
                mChildList.get(i).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int i) {
        return mGroupList!=null?mGroupList.get(i):null;
    }

    @Override
    public CategoryChildBean getChild(int i, int i1) {
        return mChildList != null && mChildList.get(i) != null ?
                mChildList.get(i).get(i1) : null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            view.getTag();
            holder = (GroupViewHolder) view.getTag();
        }
        CategoryGroupBean group = getGroup(i);
        if (group != null) {
            ImageLoader.downloadImg(mContext, holder.groupIv, group.getImageUrl());
            holder.groupTv.setText(group.getName());
            holder.groupIv2.setImageResource(b?R.mipmap.expand_off:R.mipmap.expand_on);
        }
        return view;
    }

    @Override
    public View getChildView(final int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_child, null);
            holder =new ChildViewHolder(view);
            view.setTag(holder);
        }else {
            view.getTag();
            holder= (ChildViewHolder) view.getTag();
        }
        final CategoryChildBean child = getChild(i,i1);
        if (child!=null){
            ImageLoader.downloadImg(mContext,holder.childIv,child.getImageUrl());
            holder.childTv.setText(child.getName());
            holder.mlinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<CategoryChildBean> list = mChildList.get(i);
                    String groupName = mGroupList.get(i).getName();
                    MFGT.gotoCategoryChildActivity(mContext,child.getId(),groupName,list);
                }
            });


        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> GroupList, ArrayList<ArrayList<CategoryChildBean>> ChildList) {
        if (mGroupList!=null){
            mGroupList.clear();
        }
        mGroupList.addAll(GroupList);
        if (mChildList!=null){
            mChildList.clear();
        }
        mChildList.addAll(ChildList);
        notifyDataSetChanged();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.group_iv)
        ImageView groupIv;
        @BindView(R.id.group_tv)
        TextView groupTv;
        @BindView(R.id.group_iv2)
        ImageView groupIv2;

        GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.child_iv)
        ImageView childIv;
        @BindView(R.id.child_tv)
        TextView childTv;
        @BindView(R.id.ll)
        LinearLayout mlinearLayout;

        ChildViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
