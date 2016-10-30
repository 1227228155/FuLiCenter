package cn.ucai.fulicenter.Activity;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;

public class AddressActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String s = getIntent().getStringExtra(I.Cart.ID);

    }

    @Override
    protected void setListener() {

    }
}
