package com.projectdelta.optimize.repository

import androidx.lifecycle.LiveData
import com.projectdelta.optimize.data.ProjectDao
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.relations.ProjectWithContainers
import com.projectdelta.optimize.data.entities.relations.ProjectWithWorkers
import javax.inject.Inject

class ProjectRepository  @Inject constructor(private val projectDao: ProjectDao ) {

	val getAllProjects : LiveData<List<Project>> = projectDao.getAllProjects()

	fun insertProject( project: Project ){
		projectDao.insertProject( project )
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

	suspend fun getContainer( projectName: String , containerName : String ) : Container {
		return projectDao.getContainer( projectName, containerName )
	}

	fun insertContainer(container: Container){
		projectDao.insertContainer( container )
	}
}