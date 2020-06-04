package com.hannesdorfmann.mosby.conductor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bluelinelabs.conductor.RouterTransaction
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController


/**
 * Created by hannes on 28.11.17.
 */
class MvpTestController(bundle: Bundle) : MvpController<MvpView, MvpTestPresenter>(bundle) {
    private val TAG = "MvpTestController"
    private val id: Int

    init {
        id = bundle.getInt("CONTROLLER_ID")
       // retainViewMode = Controller.RetainViewMode.RETAIN_DETACH
    }

    constructor(id: Int) : this(Bundle().apply { putInt("CONTROLLER_ID", id) })

    private var nextControllerButton: Button? = null

    override fun createPresenter(): MvpTestPresenter = MvpTestPresenter(id)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        Log.d(TAG, "Controller$id.onCreateView()")
        val view = inflater.inflate(R.layout.controller, container, false)
        view.findViewById<TextView>(R.id.text).text = "Controller $id"
        nextControllerButton = view.findViewById<Button>(R.id.nextController)
        nextControllerButton?.text = "start Controller${id+1}"
        nextControllerButton?.setOnClickListener {
            router.pushController(RouterTransaction.with(MvpTestController(id + 1)))
        }
        return view
    }


    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        Log.d(TAG, "Controller$id.onDestroyView()")
        nextControllerButton?.setOnClickListener(null)
        nextControllerButton = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Controller$id.onDestroy()")
    }
}