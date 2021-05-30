package com.projectdelta.optimize.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.data.entities.relations.ProjectWithContainers
import com.projectdelta.optimize.data.entities.relations.ProjectWithWorkers
import com.projectdelta.optimize.repository.ProjectRepository
import com.projectdelta.optimize.util.CVRPDataConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CVRPViewModel ( private val mContext: Application) : AndroidViewModel( mContext ) {
	private val repository : ProjectRepository

	lateinit var project : Project
	lateinit var converter: CVRPDataConverter
	lateinit var workers : List<Worker>
	lateinit var containers: List<Container>

	init {
		val projectDao = ProjectDatabase.getInstance(mContext).projectDao()
		repository = ProjectRepository(projectDao)
	}

	suspend fun getProject( projectName: String ) : Project{
		return repository.getProjectWithName( projectName )
	}

	suspend fun getProjectWithContainers(projectName : String) : List<ProjectWithContainers> {
		return repository.getProjectWithContainers(projectName)
	}

	suspend fun getProjectWithWorkers(projectName : String) : List<ProjectWithWorkers> {
		return repository.getProjectWithWorkers(projectName)
	}

	fun updateWorkers( ){
		viewModelScope.launch {
			workers.map{
				GlobalScope.launch(Dispatchers.IO) {
					repository.updateWorker( it )
				}
			}
		}
	}
}