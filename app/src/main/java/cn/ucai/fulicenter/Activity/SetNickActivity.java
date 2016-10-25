package cn.ucai.fulicenter.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharedPreferencesUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

public class SetNickActivity extends BaseActivity {

    @BindView(R.id.set_et_nick)
    EditText setEtNick;
    SetNickActivity mContext;

    User user;
    String nick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_nick);
        ButterKnife.bind(this);
        mContext =this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user!=null){
            nick =FuLiCenterApplication.getUser().getMuserNick().trim();
            setEtNick.setText(nick);
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.set_iv_back, R.id.set_bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_iv_back:
                MFGT.finish(mContext);
                break;
            case R.id.set_bt_save:
                if (TextUtils.isEmpty(nick)){
                    CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
                }else  if (nick.equals(user.getMuserNick())){
                    CommonUtils.showLongToast("昵称未修改");
                }else {
                    final ProgressDialog pd =new ProgressDialog(mContext);
                    pd.setMessage(getResources().getString(R.string.logining));
                    pd.show();
                    NetDao.updateNick(mContext, user.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Result result = ResultUtils.getResultFromJson(s,User.class);
                            if (result==null){
                                CommonUtils.showLongToast(R.string.login_fail);

                            }else {
                                if (result.isRetMsg()){
                                    User user = (User) result.getRetData();
                                    L.e("user"+user);
                                    UserDao dao = new UserDao(mContext);
                                    boolean flag =dao.saveUser(user);
                                    if (flag){
                                        SharedPreferencesUtils.getInstance(mContext).saveUser(user.getMuserName());
                                        FuLiCenterApplication.setUser(user);
                                        MFGT.finish(mContext);
                                    }else {
                                        CommonUtils.showLongToast(R.string.user_database_error);
                                    }
                                }else {
                                    if (result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                                        CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                                    }else if (result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                                        CommonUtils.showLongToast(R.string.login_fail_error_password);
                                    }else {
                                        CommonUtils.showLongToast(R.string.login_fail);
                                    }
                                }
                            }
                            pd.dismiss();
                        }


                        @Override
                    public void onError(String error) {
                        CommonUtils.showLongToast(error);
                    }
                    });
                }
                break;
        }
    }
}
