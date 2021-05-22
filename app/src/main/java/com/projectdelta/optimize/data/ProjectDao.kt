package com.projectdelta.optimize.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.data.entities.relations.ProjectWithContainers
import com.projectdelta.optimize.data.entities.relations.ProjectWithWorkers

@Dao
interface ProjectDao {

	@Insert( onConflict = OnConflictStrategy.REPLACE )
	fun insertProject( project: Project )

	@Insert( onConflict = OnConflictStrategy.REPLACE )
	fun insertContainer( container: Container )

	@Insert( onConflict = OnConflictStrategy.REPLACE )
	fun insertWorker( worker: Worker )

	@Query( "SELECT * FROM project WHERE projectName = :projectName" )
	fun getProjectWithName( projectName : String ) : List<Project>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	fun getProjectWithContainers(projectName: String) : List<ProjectWithContainers>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	fun getProjectWithWorkers(projectName: String) : List<ProjectWithWorkers>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	fun getProjectWithContainersLive(projectName: String) : LiveData<List< ProjectWithContainers >>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	fun getProjectWithWorkersLive(projectName: String) : LiveData<List<ProjectWithWorkers>>

	@Update
	fun updateProject( project: Project )

	@Update
	fun updateContainer( container: Container )

	@Update
	fun updateWorker( worker: Worker)

}