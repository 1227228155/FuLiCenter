package cn.ucai.fulicenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.fragment.PersonalCenterFragment;
import cn.ucai.fulicenter.utils.MFGT;


public class MainActivity extends BaseActivity {
    @BindView(R.id.rbGoodNews)
    RadioButton mrbGoodNews;
    @BindView(R.id.rbBoutique)
    RadioButton mrbBoutique;
    @BindView(R.id.rbCart)
    RadioButton mrbCart;
    @BindView(R.id.rbCategory)
    RadioButton mrbCategory;
    @BindView(R.id.rbContact)
    RadioButton mrbContact;
    @BindView(R.id.tvCartHint)
    TextView tvCartHint;

    RadioButton[] rbs;
    int index;

    NewGoodsFragment ng;
    BoutiqueFragment bf;
    CategoryFragment cf;
    PersonalCenterFragment pf;

    Fragment[] fragments;

    int currentIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    public void initFragment() {
        fragments = new Fragment[5];
        ng = new NewGoodsFragment();
        bf = new BoutiqueFragment();
        cf = new CategoryFragment();
        pf = new PersonalCenterFragment();
        fragments[0] = ng;
        fragments[1] = bf;
        fragments[2] = cf;
        fragments[4] = pf;
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.rl, ng)
                .add(R.id.rl,bf)
                .add(R.id.rl,cf)
                .hide(cf)
                .hide(bf)
                .show(ng)
                .commit();
    }

    @Override
    protected  void initView() {
        rbs = new RadioButton[5];
        rbs[0]=mrbGoodNews;
        rbs[1]=mrbBoutique;
        rbs[2]=mrbCategory;
        rbs[3]=mrbCart;
        rbs[4]=mrbContact;


    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void setListener() {

    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rbGoodNews:
                index=0;
                break;
            case R.id.rbBoutique:
                index=1;
                break;
            case R.id.rbCategory:
                index=2;
                break;
            case R.id.rbCart:
                index=3;
                break;
            case R.id.rbContact:
                if (FuLiCenterApplication.getUser()==null){
                    MFGT.gotoLogin(this);
                }else {
                    index=4;
                }
                break;
        }
        setFragment();
    }
    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                ft.add(R.id.rl, fragments[index]);
            }
            ft.show(fragments[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex=index;
    }

    private void setRadioButtonStatus() {
        for (int i=0;i<rbs.length;i++){
            if (i==index){
                rbs[i].setChecked(true);
            }else {
                rbs[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUser() != null) {
            index = 4;
        }
    }
}
