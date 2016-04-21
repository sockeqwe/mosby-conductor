package com.hannesdorfmann.mosby.conductor.sample.create.contactspicker

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
class ContactsPickerController : MvpLceViewStateController<RecyclerView, List<Contact>, ContactsPickerView, ContactsPickerPresenter>() {

  private lateinit var adapter: ListDelegationAdapter<List<Contact>>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {

    val view = inflater.inflate(R.layout.controller_contacts_picker, container, false)

    val recyclerView = view.findViewById(R.id.contentView) as RecyclerView
    recyclerView.layoutManager = LinearLayoutManager(activity)


    val manager = AdapterDelegatesManager<List<Contact>>()
        .addDelegate(ContactAdapterDelegate(activity.layoutInflater, { // TODO implement
        }))

    adapter = ListDelegationAdapter(manager)

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
}