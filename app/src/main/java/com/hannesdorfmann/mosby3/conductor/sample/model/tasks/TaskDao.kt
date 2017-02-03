package com.hannesdorfmann.mosby3.conductor.sample.model.tasks

import rx.Observable

/**
 * The TaskDao is responsible to store, retrieve and manipulate Tasks
 *
 * @author Hannes Dorfmann
 */
interface TaskDao {

  /**
   * Get a List of all Tasks
   */
  fun getTasks(): Observable<List<Task>>

  /**
   * Create a new Task
   */
  fun createTask(title : String, description : String, contacts : String): Observable<Long>
}