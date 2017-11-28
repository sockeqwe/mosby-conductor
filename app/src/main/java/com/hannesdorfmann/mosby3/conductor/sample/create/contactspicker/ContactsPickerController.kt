package com.hannesdorfmann.mosby3.conductor.sample.create.contactspicker

import android.Manifest
import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
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
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.create.CreateTaskController
import com.hannesdorfmann.mosby3.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby3.conductor.viewstate.lce.MvpLceViewStateController
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import com.hannesdorfmann.mosby3.conductor.sample.R.string

/**
 * A [ContactsPickerView]
 * @author Hannes Dorfmann
 */
class ContactsPickerController : ContactsPickerView, MvpLceViewStateController<CardView, List<Contact>, ContactsPickerView, ContactsPickerPresenter>() {

  private val permissionRequestCode = 1234

  private lateinit var adapter: ListDelegationAdapter<List<Contact>>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {

    val view = inflater.inflate(layout.controller_contacts_picker, container, false)

    val recyclerView = view.findViewById<RecyclerView>(id.recyclerView) as RecyclerView
    recyclerView.layoutManager = LinearLayoutManager(activity)


    val manager = AdapterDelegatesManager<List<Contact>>()
        .addDelegate(ContactAdapterDelegate(activity!!.layoutInflater, {
          presenter.addContact(it)
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
      activity!!.getString(string.error)

  override fun createPresenter() = (parentController as CreateTaskController).createTaskComponent.contactsPickerPresenter()

  override fun onNewViewStateInstance() {
    if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(activity!!,
        permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(arrayOf(permission.READ_CONTACTS), permissionRequestCode)
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
    if (!restoringViewState) TransitionManager.beginDelayedTransition(
        view!!.parent.parent as ViewGroup)
    loadingView.visibility = View.GONE
    errorView.visibility = View.GONE
    contentView.visibility = View.VISIBLE
  }

  override fun animateErrorViewIn() {
    if (!restoringViewState) TransitionManager.beginDelayedTransition(
        view!!.parent.parent as ViewGroup)
    loadingView.visibility = View.GONE
    contentView.visibility = View.GONE
    errorView.visibility = View.VISIBLE
  }

  override fun animateLoadingViewIn() {
    if (!restoringViewState) TransitionManager.beginDelayedTransition(
        view!!.parent.parent as ViewGroup)
    contentView.visibility = View.GONE
    errorView.visibility = View.GONE
    loadingView.visibility = View.VISIBLE
  }

  override fun finish() {
    router.popController(this)
  }
}