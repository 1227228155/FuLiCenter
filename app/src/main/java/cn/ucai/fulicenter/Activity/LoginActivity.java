package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.password_et)
    EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.bt_login, R.id.login_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                break;
            case R.id.login_register:
                break;
        }
    }
}
