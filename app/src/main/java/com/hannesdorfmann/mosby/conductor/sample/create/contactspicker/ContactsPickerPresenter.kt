package com.hannesdorfmann.mosby.conductor.sample.create.contactspicker

import com.hannesdorfmann.mosby.conductor.sample.model.contacts.ContactsLoader
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * The Presenter that coordinates the [ContactsPickerView]
 *
 * @author Hannes Dorfmann
 */
class ContactsPickerPresenter @Inject constructor(private val contacsLoader: ContactsLoader) : MvpBasePresenter<ContactsPickerView>() {

  private lateinit var subscription: Subscription

  fun loadContacts() {

    view?.showLoading(false)

    subscription = contacsLoader.loadContacts()
        .subscribeOn(Schedulers.io())
        .map {
          Thread.sleep(2000)
          it
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe (
            { view?.setData(it) }, // onNext()
            { view?.showError(it, false) }, // onError()
            { view?.showContent() } // onCompleted
        )
  }

  override fun detachView(retainInstance: Boolean) {
    super.detachView(retainInstance)
    /*
    if (!retainInstance) {
      subscription.unsubscribe()
    }
    */
  }

}