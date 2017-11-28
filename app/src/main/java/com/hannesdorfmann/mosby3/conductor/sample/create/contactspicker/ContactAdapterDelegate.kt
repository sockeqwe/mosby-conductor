package com.hannesdorfmann.mosby3.conductor.sample.create.contactspicker

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import com.hannesdorfmann.mosby3.conductor.sample.create.contactspicker.ContactAdapterDelegate.ContactViewHolder

/**
 * Displays an item for [Contact]
 *
 * @author Hannes Dorfmann
 */
class ContactAdapterDelegate(private val inflater: LayoutInflater, private val selectionListener: (Contact) -> Unit) : AbsListItemAdapterDelegate<Contact, Contact, ContactViewHolder>() {

  override fun isForViewType(item: Contact, items: MutableList<Contact>?, position: Int) = true

  override fun onCreateViewHolder(parent: ViewGroup) = ContactViewHolder (
      inflater.inflate(layout.item_contact, parent, false))

  override fun onBindViewHolder(item: Contact, viewHolder: ContactViewHolder) {
    viewHolder.contact = item
    viewHolder.name.text = item.name
  }

  inner class ContactViewHolder(v: View) : ViewHolder(v) {

    lateinit var contact: Contact
    val name: TextView

    init {
      v.setOnClickListener {
        selectionListener(contact)
      }

      name = v.findViewById<TextView>(id.name) as TextView
    }

  }
}