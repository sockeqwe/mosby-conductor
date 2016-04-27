package com.hannesdorfmann.mosby.conductor.sample.create.contactspicker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates2.ListDelegationAdapter
import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.daggerComponent
import com.hannesdorfmann.mosby.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby.conductor.viewstate.lce.MvpLceViewStateController
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState

/**
 * A [ContactsPickerView]
 * @author Hannes Dorfmann
 */
class ContactsPickerController : ContactsPickerView, MvpLceViewStateController<CardView, List<Contact>, ContactsPickerView, ContactsPickerPresenter>() {

  private val permissionRequestCode = 1234

  private lateinit var adapter: ListDelegationAdapter<List<Contact>>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {

    val view = inflater.inflate(R.layout.controller_contacts_picker, container, false)

    val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
    recyclerView.layoutManager = LinearLayoutManager(activity)


    val manager = AdapterDelegatesManager<List<Contact>>()
        .addDelegate(ContactAdapterDelegate(activity.layoutInflater, { // TODO implement
        }))

    adapter = ListDelegationAdapter(manager)
    recyclerView.adapter = adapter

    return view
  }

  override fun getData(): List<Contact>? = adapter.items

  override fun createViewState() = RetainingLceViewState<List<Contact>, ContactsPickerView>()

  override fun setData(data: List<Contact>?) {
    adapter.items = data
    adapter.notifyDataSetChanged()
  }

  override fun loadData(pullToRefresh: Boolean) = presenter.loadContacts()

  override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String =
      activity.getString(R.string.error)

  override fun createPresenter() = daggerComponent.contactsPickerPresenter()

  override fun onNewViewStateInstance() {
    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(activity,
        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), permissionRequestCode)
    } else {
      super.onNewViewStateInstance()
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    if (requestCode == permissionRequestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      super.onNewViewStateInstance()
    } else {
      showError(RuntimeException("Permission not granted"), false)
    }
  }


  override fun animateContentViewIn() {
    TransitionManager.beginDelayedTransition(view.parent.parent as ViewGroup)
    loadingView.visibility = View.GONE
    errorView.visibility = View.GONE
    contentView.visibility = View.VISIBLE
  }

  override fun animateErrorViewIn() {
    TransitionManager.beginDelayedTransition(view.parent.parent as ViewGroup)
    loadingView.visibility = View.GONE
    contentView.visibility = View.GONE
    errorView.visibility = View.VISIBLE
  }

  override fun animateLoadingViewIn() {
    TransitionManager.beginDelayedTransition(view.parent.parent as ViewGroup)
    contentView.visibility = View.GONE
    errorView.visibility = View.GONE
    loadingView.visibility = View.VISIBLE
  }
}