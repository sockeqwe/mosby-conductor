package com.hannesdorfmann.mosby3.conductor.sample.create.contactspicker

import com.hannesdorfmann.mosby3.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby3.conductor.sample.model.contacts.ContactsLoader
import com.hannesdorfmann.mosby3.conductor.sample.model.tasks.TaskBuilder
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * The Presenter that coordinates the [ContactsPickerView]
 *
 * @author Hannes Dorfmann
 */
class ContactsPickerPresenter @Inject constructor(private val contacsLoader: ContactsLoader, private val taskBuilder: TaskBuilder) : MvpBasePresenter<ContactsPickerView>() {

  private var subscription: Subscription? = null

  fun loadContacts() {

    view?.showLoading(false)

    subscription = contacsLoader.loadContacts()
        .subscribeOn(Schedulers.io())
        .map {
          Thread.sleep(1000)
          it
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { view?.setData(it) }, // onNext()
            {
              view?.showError(it, false)
              it.printStackTrace()
            }, // onError()
            { view?.showContent() } // onCompleted
        )
  }

  fun addContact(c: Contact) {
    taskBuilder.addContact(c)
    view?.finish()
  }

  override fun detachView(retainInstance: Boolean) {
    super.detachView(retainInstance)
    if (!retainInstance) {
      subscription?.unsubscribe()
    }
  }

}