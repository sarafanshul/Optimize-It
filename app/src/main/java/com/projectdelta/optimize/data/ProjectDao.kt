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

	@Query("SELECT * FROM project")
	fun getAllProjects() : LiveData<List<Project>>

	@Query( "SELECT * FROM project WHERE projectName = :projectName" )
	fun getProjectWithName( projectName : String ) : List<Project>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	suspend fun getProjectWithContainers(projectName: String) : List<ProjectWithContainers>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	suspend fun getProjectWithWorkers(projectName: String) : List<ProjectWithWorkers>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	fun getProjectWithContainersLive(projectName: String) : LiveData<List< ProjectWithContainers >>

	@Transaction
	@Query("SELECT * FROM project WHERE projectName = :projectName")
	fun getProjectWithWorkersLive(projectName: String) : LiveData<List<ProjectWithWorkers>>

	@Query("SELECT * FROM container WHERE projectName = :projectName AND containerName = :containerName")
	fun getContainerLive(projectName: String , containerName : String) : LiveData<Container>

	@Query("SELECT * FROM container WHERE projectName = :projectName AND containerName = :containerName")
	suspend fun getContainer(projectName: String , containerName : String) : Container

	@Query("SELECT * FROM worker WHERE projectName = :projectName AND workerId = :workerId")
	fun getworkerLive(projectName: String , workerId : Long) : LiveData<Worker>

	@Update
	fun updateProject( project: Project )

	@Update
	fun updateContainer( container: Container )

	@Update
	fun updateWorker( worker: Worker)

	@Delete
	fun deleteWorker( worker: Worker )

}