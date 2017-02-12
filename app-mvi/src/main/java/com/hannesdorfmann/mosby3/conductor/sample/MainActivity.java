package com.hannesdorfmann.mosby3.conductor.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler;
import com.hannesdorfmann.mosby3.conductor.sample.databinding.ActivityMainBinding;

/**
 * Created by Michael on 12.02.2017.
 */

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private Router mRouter;
    private MainController mMainController = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mRouter = Conductor.attachRouter(this, mBinding.container, savedInstanceState);
        if (!mRouter.hasRootController())
        {
            mMainController = new MainController();
            mRouter.setRoot(RouterTransaction
                    .with(mMainController));
        }
    }

    @Override
    public void onDestroy()
    {
        mBinding.unbind();
        super.onDestroy();
    }
}