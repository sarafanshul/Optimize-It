package com.projectdelta.optimize.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.projectdelta.optimize.constant.COORDINATE_MULTIPLIER
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContainerMapsViewModel( application: Application) : AndroidViewModel( application ) {
	private val repository : ProjectRepository
	lateinit var container : Container
	lateinit var project: Project
	lateinit var depotLocation : LatLng

	init {
		val projectDao = ProjectDatabase.getInstance(application).projectDao()
		repository = ProjectRepository(projectDao)
	}

	fun set( _container: Container ){
		container = _container
		getLocation()
	}

	fun getContainerLocation( ) : LatLng {
		if (!this::container.isInitialized) {
			Log.e(
				"UninitializedPropertyAccessException",
				"lateinit property project has not been initialized"
			)
			return getLocation() // some random location
		}
		if (container.latitude != 0L || container.longitude != 0L) {
			return LatLng(
				container.latitude.toDouble() / COORDINATE_MULTIPLIER ,
				container.longitude.toDouble() / COORDINATE_MULTIPLIER
			)
		}
		return getLocation() // some default location
	}

	fun getLocation() : LatLng {
		if( ! this::depotLocation.isInitialized ){
			viewModelScope.launch(Dispatchers.IO) {
				project = async { repository.getProjectWithName( container.projectName ) }.await()
				depotLocation = LatLng( project.depotLatitude / COORDINATE_MULTIPLIER , project.depotLongitude / COORDINATE_MULTIPLIER )
			}
			return LatLng(0.0 , 0.0 )
		}
		return depotLocation
	}

	fun setPosition( pos : LatLng ){
		container.latitude = (pos.latitude * COORDINATE_MULTIPLIER).toLong()
		container.longitude = (pos.longitude * COORDINATE_MULTIPLIER).toLong()

		updateContianer()
	}

	private fun updateContianer(){
		if( this::container.isInitialized )
			viewModelScope.launch(Dispatchers.IO) { repository.updateContainer( container ) }
	}
}