package com.projectdelta.optimize.repository

import androidx.lifecycle.LiveData
import com.projectdelta.optimize.data.ProjectDao
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.data.entities.relations.ProjectWithContainers
import com.projectdelta.optimize.data.entities.relations.ProjectWithWorkers
import javax.inject.Inject

class ProjectRepository  @Inject constructor(private val projectDao: ProjectDao ) {

	val getAllProjects : LiveData<List<Project>> = projectDao.getAllProjects()

	fun insertProject( project: Project ){
		projectDao.insertProject( project )
	}

	suspend fun getProjectWithContainers(projectName : String) : List<ProjectWithContainers> {
		return projectDao.getProjectWithContainers( projectName )
	}

	suspend fun getProjectWithWorkers(projectName: String) : List<ProjectWithWorkers> {
		return projectDao.getProjectWithWorkers( projectName )
	}

	fun getProjectWithContainersLive(projectName : String) : LiveData<List<ProjectWithContainers>> {
		return projectDao.getProjectWithContainersLive( projectName )
	}

	fun getProjectWithWorkersLive(projectName: String) : LiveData<List<ProjectWithWorkers>> {
		return projectDao.getProjectWithWorkersLive(projectName)
	}

	fun getContainerLive( projectName: String , containerName : String ) : LiveData<Container> {
		return projectDao.getContainerLive( projectName, containerName )
	}

	suspend fun getProjectWithName( projectName: String ) : Project{
		return projectDao.getProjectWithName( projectName )
	}

	suspend fun getContainer( projectName: String , containerName : String ) : Container {
		return projectDao.getContainer( projectName, containerName )
	}

	fun updateProject( project: Project ){
		projectDao.updateProject( project )
	}

	fun insertContainer(container: Container){
		projectDao.insertContainer( container )
	}

	fun updateContainer(container: Container){
		projectDao.updateContainer(container)
	}

	fun insertWorker( worker: Worker ){
		projectDao.insertWorker( worker )
	}

	fun updateWorker( worker: Worker ){
		projectDao.updateWorker( worker )
	}

	fun deleteWorker( worker: Worker ){
		projectDao.deleteWorker( worker )
	}

	fun deleteContainer( container: Container ){
		projectDao.deleteContainer( container )
	}
}