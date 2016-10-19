package cn.ucai.fulicenter.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.Footer)
   public TextView Footer;

    public FooterViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

}
