package com.hannesdorfmann.mosby.conductor.sample.create

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.bluelinelabs.conductor.RouterTransaction
import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.create.contactspicker.ContactsPickerController
import com.hannesdorfmann.mosby.conductor.sample.daggerComponent
import com.hannesdorfmann.mosby.conductor.sample.navigation.changehandlers.ContactsPickerChaneHandler
import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController

/**
 * Controller to create a new Task
 *
 * @author Hannes Dorfmann
 */
class CreateTaskController : CreateTaskView, MvpViewStateController<CreateTaskView, CreateTaskPresenter, CreateTaskViewState>() {

  private lateinit var title: EditText
  private lateinit var description: EditText
  private lateinit var selectedPersonRecyclerView: RecyclerView

  override fun createPresenter(): CreateTaskPresenter = daggerComponent.createTaskPresenter()
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
}