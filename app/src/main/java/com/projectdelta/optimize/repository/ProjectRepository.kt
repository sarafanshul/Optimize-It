package com.projectdelta.optimize.repository

import androidx.lifecycle.LiveData
import com.projectdelta.optimize.data.ProjectDao
import com.projectdelta.optimize.data.entities.Project

class ProjectRepository( projectDao: ProjectDao ) {

	val getAllProjects : LiveData<List<Project>> = projectDao.getAllProjects()

}