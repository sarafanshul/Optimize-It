package com.projectdelta.optimize.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.data.entities.relations.ProjectWithContainers
import com.projectdelta.optimize.data.entities.relations.ProjectWithWorkers
import com.projectdelta.optimize.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProjectInfoViewModel(application: Application) : AndroidViewModel( application ) {
	private val repository : ProjectRepository

	init {
		val projectDao = ProjectDatabase.getInstance(application).projectDao()
		repository = ProjectRepository(projectDao)
	}

	fun insertProject( project : Project) {
		viewModelScope.launch(Dispatchers.IO) {
			repository.insertProject(project)
		}
	}

	fun getContainers(projectName : String) : LiveData<List<ProjectWithContainers>> {
		return repository.getProjectWithContainersLive(projectName)
	}

	fun getWorkers(projectName : String) : LiveData<List<ProjectWithWorkers>>{
		return repository.getProjectWithWorkersLive(projectName)
	}

	fun insertContainer( container: Container ){
		viewModelScope.launch(Dispatchers.IO) {
			repository.insertContainer( container )
		}
	}

	fun insertWorker( worker: Worker ){
		viewModelScope.launch(Dispatchers.IO) {
			repository.insertWorker( worker )
		}
	}

}