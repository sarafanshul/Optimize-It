package com.projectdelta.optimize.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class EditContainerViewModel(application: Application) : AndroidViewModel( application ) {
	private val repository : ProjectRepository
	lateinit var container: Container
	init {
		val projectDao = ProjectDatabase.getInstance(application).projectDao()
		repository = ProjectRepository(projectDao)
	}

	fun getContainerDataLive( projectName: String , containerName : String ) : LiveData<Container>{
		return repository.getContainerLive(projectName, containerName)
	}

	fun update( container: Container ){
		viewModelScope.launch (Dispatchers.IO){
			repository.updateContainer(container)
		}
	}

	fun updateContainer(){
		viewModelScope.launch(Dispatchers.IO) {
			container = async { repository.getContainer( container.projectName , container.containerName ) } .await()
		}
	}

	fun delete( container: Container ){
		GlobalScope.launch(Dispatchers.IO) {
			repository.deleteContainer( container )
		}
	}
}