package com.hannesdorfmann.mosby3.conductor.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.hannesdorfmann.mosby3.conductor.sample.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private var mBinding:ActivityStartBinding? = null
    private lateinit var mRouter: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        mRouter = Conductor.attachRouter(this, mBinding!!.container, savedInstanceState)
        if (!mRouter.hasRootController()) {
            mRouter.setRoot(RouterTransaction.with(StartController(null)))
        }
    }

    override fun onDestroy() {
        mBinding!!.unbind()
        mBinding = null
        super.onDestroy()
    }
}