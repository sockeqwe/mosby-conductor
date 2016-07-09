package com.hannesdorfmann.mosby.conductor.sample.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.bluelinelabs.conductor.RouterTransaction
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates2.ListDelegationAdapter
import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.create.contactspicker.ContactAdapterDelegate
import com.hannesdorfmann.mosby.conductor.sample.create.contactspicker.ContactsPickerController
import com.hannesdorfmann.mosby.conductor.sample.dagger.DaggerTaskCreationComponent
import com.hannesdorfmann.mosby.conductor.sample.dagger.TaskCreationComponent
import com.hannesdorfmann.mosby.conductor.sample.daggerComponent
import com.hannesdorfmann.mosby.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskBuilder
import com.hannesdorfmann.mosby.conductor.sample.navigation.changehandlers.ContactsPickerChaneHandler
import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController
import com.jakewharton.rxbinding.widget.textChanges
import com.primetime.utils.recyclerview.GridSpacingItemDecoration
import javax.inject.Inject

/**
 * Controller to create a new Task
 *
 * @author Hannes Dorfmann
 */
class CreateTaskController : CreateTaskView, MvpViewStateController<CreateTaskView, CreateTaskPresenter, CreateTaskViewState>() {

  private val PHOTO_INTENT_CODE = 1234

  private lateinit var title: EditText
  private lateinit var description: EditText
  private lateinit var selectedPersonRecyclerView: RecyclerView
  private lateinit var selectedPersonAdapter: ListDelegationAdapter<List<Contact>>
  private lateinit var imagesRecyclerView: RecyclerView
  private lateinit var selectedImagesAdapter: ListDelegationAdapter<List<Uri>>

  val createTaskComponent: TaskCreationComponent by lazy {
    val component = DaggerTaskCreationComponent
        .builder()
        .controllerComponent(daggerComponent)
        .build()
    component.inject(this@CreateTaskController)
    addLifecycleListener(taskBuilderLifecycleListener)
    component
  }

  @Inject lateinit var taskBuilderLifecycleListener: TaskBuilderLifecycleListener

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    createTaskComponent // will ensure that dagger component will be initilaized lazily.
    // called before TaskBuilderLifecycleListener restores his state
  }

  override fun createPresenter(): CreateTaskPresenter {
    return createTaskComponent.createTaskPresenter()
  }

  override fun createViewState() = CreateTaskViewState()

  override fun onViewStateInstanceRestored(instanceStateRetained: Boolean) {
    // Not needed
  }

  override fun onNewViewStateInstance() {
    showForm()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_create_task, container, false)

    title = view.findViewById(R.id.title) as EditText
    description = view.findViewById(R.id.description) as EditText
    selectedPersonRecyclerView = view.findViewById(R.id.personRecyclerView) as RecyclerView
    imagesRecyclerView = view.findViewById(R.id.imageRecyclerView) as RecyclerView

    title.textChanges().skip(1).map { it.toString() }.subscribe { presenter.setTaskTitle(it) }
    description.textChanges().skip(
        1).map { it.toString() }.subscribe { presenter.setTaskDescription(it) }

    val addPersonButton = view.findViewById(R.id.addPerson)
    val personPickerContainer = view.findViewById(R.id.personPickerContainer) as ViewGroup
    addPersonButton.setOnClickListener {
      val childRouter = getChildRouter(personPickerContainer, null)
      if (!childRouter.hasRootController()) {
        childRouter.setRoot(RouterTransaction.with(ContactsPickerController())
            .popChangeHandler(ContactsPickerChaneHandler())
        )
        childRouter.setPopsLastView(true)
      }
    }

    view.findViewById(R.id.addImage).setOnClickListener {
      val photoPickerIntent = Intent(Intent.ACTION_PICK)
      photoPickerIntent.setType("image/*")
      startActivityForResult(photoPickerIntent, PHOTO_INTENT_CODE)
    }


    val manager = AdapterDelegatesManager<List<Contact>>()
        .addDelegate(ContactAdapterDelegate(activity.layoutInflater, {}))

    selectedPersonAdapter = ListDelegationAdapter(manager)
    selectedPersonRecyclerView.adapter = selectedPersonAdapter
    selectedPersonRecyclerView.layoutManager = LinearLayoutManager(activity)


    val imageManager = AdapterDelegatesManager<List<Uri>>()
        .addDelegate(
            ImageAdapterDelegate(daggerComponent.picasso(), activity.layoutInflater, { showImageDetails(it) }))
    selectedImagesAdapter = ListDelegationAdapter(imageManager)
    imagesRecyclerView.adapter = selectedImagesAdapter

    val columnCount = resources.getInteger(R.integer.images_grid_columns)
    val itemSpace = resources.getDimensionPixelOffset(R.dimen.image_grid_space)

    imagesRecyclerView.layoutManager = GridLayoutManager(activity, columnCount)
    imagesRecyclerView.addItemDecoration(GridSpacingItemDecoration(columnCount, itemSpace))
    return view
  }

  override fun showForm() {
    viewState.setShowForm()
    title.isEnabled = true
    description.isEnabled = true
  }

  override fun showLoading() {
    viewState.setShowLoading()
    title.isEnabled = false
    description.isEnabled = false
  }

  override fun showTaskCreated() {
    viewState.setShowTaskCreated()
    router.popCurrentController()
  }

  override fun setTaskSnapshot(taskSnapshot: TaskBuilder.TaskSnapshot) {

    if (title.text.toString() != taskSnapshot.title) {
      title.setText(taskSnapshot.title)
    }

    if (description.text.toString() != taskSnapshot.description) {
      description.setText(taskSnapshot.description)
    }


    selectedPersonAdapter.items = taskSnapshot.contacts
    selectedPersonAdapter.notifyDataSetChanged()

    selectedImagesAdapter.items = taskSnapshot.images
    selectedImagesAdapter.notifyDataSetChanged()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == Activity.RESULT_OK && data != null) {
      val uri = data.data
      presenter.addImage(uri)
    }
  }

  private fun showImageDetails(uri: Uri) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.setDataAndType(uri, "image/*")
    startActivity(intent)
  }
}