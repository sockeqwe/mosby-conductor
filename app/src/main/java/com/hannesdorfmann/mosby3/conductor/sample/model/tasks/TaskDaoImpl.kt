package com.hannesdorfmann.mosby3.conductor.sample.model.tasks

import android.database.sqlite.SQLiteDatabase
import com.hannesdorfmann.sqlbrite.dao.Dao
import rx.Observable

/**
 * Simple dao that is responsible to talk to the database
 *
 * @author Hannes Dorfmann
 */
class TaskDaoImpl : TaskDao, Dao() {

  companion object {
    const val TABLE = "Tasks"
    const val COL_ID = "_id"
    const val COL_TITLE = "title"
    const val COL_DESCRIPTION = "description"
    const val COL_COMPLETED = "completed"
    const val COL_CONTACTS = "contacts"
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    // Not needed yet, because no version migration yet
  }

  override fun createTable(database: SQLiteDatabase) {
    CREATE_TABLE(TABLE,
        "${COL_ID} INTEGER NOT NULL PRIMARY KEY",
        "${COL_TITLE} TEXT NOT NULL",
        "${COL_DESCRIPTION} TEXT",
        "${COL_COMPLETED} BOOLEAN DEFAULT false",
        "${COL_CONTACTS} TEXT").execute(database)
  }

  override fun getTasks(): Observable<List<Task>> = query(
      SELECT(COL_ID, COL_TITLE, COL_COMPLETED)
          .FROM(TABLE))
      .run()
      .mapToList(TaskMapper.MAPPER)

  override fun createTask(title: String, description: String, contacts:String): Observable<Long> {
    val cv = TaskMapper.contentValues()
        .title(title)
        .description(description)
        .completed(false)
        .build()

    return insert(TABLE, cv)
  }
}