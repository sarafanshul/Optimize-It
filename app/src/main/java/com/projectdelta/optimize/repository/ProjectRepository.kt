package com.projectdelta.optimize.repository

import androidx.lifecycle.LiveData
import com.projectdelta.optimize.data.ProjectDao
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.relations.ProjectWithContainers
import com.projectdelta.optimize.data.entities.relations.ProjectWithWorkers

class ProjectRepository( private val projectDao: ProjectDao ) {


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

}