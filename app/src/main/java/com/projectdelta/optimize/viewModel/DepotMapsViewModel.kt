package com.projectdelta.optimize.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.projectdelta.optimize.constant.COORDINATE_MULTIPLIER
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * coordinates are stored in Long hence for retrieving
 *  	- Long(Stored) -> Double => ( val to double ) / COORDINATE_MULTIPLIER
 *  	- Double -> Long(Store) => (val * COORDINATE_MULTIPLIER) to Long
 * **/

class DepotMapsViewModel( application: Application ) : AndroidViewModel( application ) {
	private val repository : ProjectRepository
	lateinit var project : Project
	init {
		val projectDao = ProjectDatabase.getInstance(application).projectDao()
		repository = ProjectRepository(projectDao)
	}

	fun setProject( projectName : String ) = viewModelScope.launch(Dispatchers.IO) {
		project = async { repository.getProjectWithName( projectName ) }.await()
	}

	fun setPosition( pos : LatLng ){
		project.depotLatitude = (pos.latitude * COORDINATE_MULTIPLIER).toLong()
		project.depotLongitude = (pos.longitude * COORDINATE_MULTIPLIER).toLong()
		updateProject()
	}

	private fun updateProject(  ){
		if( this::project.isInitialized )
			 viewModelScope.launch(Dispatchers.IO) { repository.updateProject(project) }
	}

	fun getDepotLocation() : LatLng {
		if (!this::project.isInitialized) {
			Log.e(
				"UninitializedPropertyAccessException",
				"lateinit property project has not been initialized"
			)
			return LatLng(-33.8473567, 150.6517845)
		}
		if (project.depotLatitude != 0L || project.depotLongitude != 0L) {
			return LatLng(
				project.depotLatitude.toDouble() / COORDINATE_MULTIPLIER ,
				project.depotLongitude.toDouble() / COORDINATE_MULTIPLIER
			)
		}
		return LatLng(-33.8473567, 150.6517845) // some default location
	}
}