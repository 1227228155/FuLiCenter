package cn.ucai.fulicenter.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.DisplayUtils;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.et_twopassword)
    EditText etTwopassword;
    @BindView(R.id.nick_et)
    EditText nickEt;
    @BindView(R.id.bt_register)
    Button btRegister;

    RegisterActivity mContext;

    String name;
    String nickname ;
    String password ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext =this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "账户注册");

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_register)
    public void onClick() {
        name =nameEt.getText().toString().trim();
        nickname =nickEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        String twopassword = etTwopassword.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            CommonUtils.showShortToast("用户名不能为空");
            nameEt.requestFocus();
            return;
        }else  if (!name.matches("[a-zA-Z]\\w{5,15}")){
            CommonUtils.showShortToast("非法用户名");
            return;
        }else  if (TextUtils.isEmpty(nickname)){
            CommonUtils.showShortToast("昵称不能为空");
            nickEt.requestFocus();
            return;
        }else  if (TextUtils.isEmpty(password)){
            CommonUtils.showShortToast("密码不能为空");
            passwordEt.requestFocus();
            return;
        }else  if (TextUtils.isEmpty(twopassword)){
            CommonUtils.showShortToast("确认密码不能为空");
            etTwopassword.requestFocus();
            return;
        }else if (!password.equals(twopassword)){
            CommonUtils.showShortToast("两次输入的密码不相同");
            passwordEt.requestFocus();
            return;
        }
        register();
    }

    private void register() {
       final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("注册中");
        pd.show();
        NetDao.register(mContext, name, nickname, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if (result==null){
                    CommonUtils.showShortToast("注册失败");
                }else {
                    if (result.isRetMsg()){
                        CommonUtils.showLongToast("注册成功");
                        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,name));
                        MFGT.finish(mContext);
                    }else {
                        CommonUtils.showLongToast("注册失败");
                        nameEt.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(error);
            }
        });
    }
}
