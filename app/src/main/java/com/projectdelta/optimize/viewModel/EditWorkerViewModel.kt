package com.projectdelta.optimize.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditWorkerViewModel(application: Application) : AndroidViewModel( application ) {
	private val repository : ProjectRepository
	init {
		val projectDao = ProjectDatabase.getInstance(application).projectDao()
		repository = ProjectRepository(projectDao)
	}

	fun update( worker: Worker){
		viewModelScope.launch (Dispatchers.IO){
			repository.updateWorker( worker )
		}
	}

	fun delete( worker: Worker ){
		GlobalScope.launch(Dispatchers.IO) {
			repository.deleteWorker( worker )
		}
	}

}