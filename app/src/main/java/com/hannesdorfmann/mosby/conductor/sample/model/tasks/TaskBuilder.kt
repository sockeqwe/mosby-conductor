package com.hannesdorfmann.mosby.conductor.sample.model.tasks

import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.hannesdorfmann.mosby.conductor.sample.model.contacts.Contact
import org.threeten.bp.Instant
import rx.Observable
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

  private val imageBehavior by lazy {
    BehaviorSubject.create<ArrayList<Uri>>(
        latestTaskSnapshot?.images ?: ArrayList<Uri>())
  }

  private val dateBehavior by lazy {
    BehaviorSubject.create<Instant?>(
        latestTaskSnapshot?.dateTime
    )
  }


  /**
   * Public API to observe
   */
  val observable: Observable<TaskSnapshot> = Observable.combineLatest(
      titleBehavior,
      descriptionBehavior,
      contactBehavior,
      imageBehavior,
      dateBehavior,
      { title, description, contacts, images, date ->
        TaskSnapshot(title, description, contacts, images, date)
      }
  ).doOnNext { latestTaskSnapshot = it } // Sideeffect I know, but I can't think of any better option

  fun addContact(c: Contact) {
    val currentContacts = latestTaskSnapshot?.contacts ?: ArrayList()
    val newContacts = ArrayList(currentContacts)
    newContacts.add(c)
    contactBehavior.onNext(newContacts)
  }

  fun addImage(uri: Uri) {
    val current = latestTaskSnapshot?.images ?: ArrayList()
    val newList = ArrayList(current)
    newList.add(uri)
    imageBehavior.onNext(newList)
  }

  fun setTitle(title: String) {
    titleBehavior.onNext(title)
  }

  fun setDescription(description: String) {
    descriptionBehavior.onNext(description)
  }

  fun setDateTime(dateTime: Instant?) {
    dateBehavior.onNext(dateTime)
  }

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
      val contacts: ArrayList<Contact> = ArrayList<Contact>(),
      val images: ArrayList<Uri> = ArrayList<Uri>(),
      val dateTime: Instant?

  ) : Parcelable {
    constructor(source: Parcel) : this(source.readString(),
        source.readString(),
        ArrayList<Contact>().apply { source.readList(this, Contact::class.java.classLoader) },
        ArrayList<Uri>().apply { source.readList(this, Uri::class.java.classLoader) },
        source.readLong().let { if (it == -1L) null else Instant.ofEpochMilli(it) }
    )

    override fun describeContents(): Int {
      return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
      dest.writeString(title)
      dest.writeString(description)
      dest.writeList(contacts)
      dest.writeList(images)
      dest.writeLong(dateTime?.toEpochMilli() ?: -1L)
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