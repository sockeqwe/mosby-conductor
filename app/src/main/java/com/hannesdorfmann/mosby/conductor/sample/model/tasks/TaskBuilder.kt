package com.hannesdorfmann.mosby.conductor.sample.model.tasks

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.hannesdorfmann.mosby.conductor.sample.model.contacts.Contact
import rx.Observable
import rx.Observer
import rx.subjects.BehaviorSubject
import java.util.*


/**
 * A class responsible to build a [Task] along some
 *
 * @author Hannes Dorfmann
 */
class TaskBuilder {

  //
  // private internal API
  //
  private val KEY_BUNDLE = "TaskBuilder.latest"
  private var latestTaskSnapshot: TaskSnapshot? = null
  private val titleBehavior by lazy {
    BehaviorSubject.create<String>(latestTaskSnapshot?.title ?: "")
  }
  private val descriptionBehavior by lazy {
    BehaviorSubject.create<String>(latestTaskSnapshot?.description ?: "")
  }
  private val contactBehavior by lazy {
    BehaviorSubject.create<ArrayList<Contact>>(
        latestTaskSnapshot?.contacts ?: ArrayList<Contact>())
  }


  //
  // Public API
  //
  val titleObserver: Observer<String> = titleBehavior
  val descriptionObserver: Observer<String> = descriptionBehavior
  val contactsListObserver: Observer<ArrayList<Contact>> = contactBehavior

  /**
   * Public API to observe
   */
  val observable: Observable<TaskSnapshot> = Observable.combineLatest(
      titleBehavior,
      descriptionBehavior,
      contactBehavior.scan { old, new ->
        val res = ArrayList(old)
        res.addAll(new)
        res
      },
      { title, description, contacts -> TaskSnapshot(title, description, contacts) }
  ).doOnNext { latestTaskSnapshot = it } // Sideeffect I know, but I can't think of any better option

  fun saveInstanceState(b: Bundle) {
    b.putParcelable(KEY_BUNDLE, latestTaskSnapshot)
  }

  fun restoreFromBundle(b: Bundle) {
    latestTaskSnapshot = b.getParcelable(KEY_BUNDLE)
  }

  /**
   * Represents the Input Data (from UI / user) while creating a task (i.e. through multiple screen wizard)
   */
  data class TaskSnapshot(
      var title: String = "",
      var description: String = "",
      val contacts: ArrayList<Contact> = ArrayList<Contact>()

  ) : Parcelable {
    constructor(
        source: Parcel) : this(source.readString(), source.readString(), ArrayList<Contact>().apply
    {
      source.readList(this, Contact::class.java.classLoader)
    })

    override fun describeContents(): Int {
      return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
      dest.writeString(title)
      dest.writeString(description)
      dest.writeList(contacts)
    }

    companion object {
      @JvmField final val CREATOR: Parcelable.Creator<TaskSnapshot> = object : Parcelable.Creator<TaskSnapshot> {
        override fun createFromParcel(source: Parcel): TaskSnapshot {
          return TaskSnapshot(source)
        }

        override fun newArray(size: Int): Array<TaskSnapshot?> {
          return arrayOfNulls(size)
        }
      }
    }
  }

}