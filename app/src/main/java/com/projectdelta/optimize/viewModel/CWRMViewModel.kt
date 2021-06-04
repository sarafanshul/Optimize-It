package com.projectdelta.optimize.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.data.entities.relations.ProjectWithContainers
import com.projectdelta.optimize.data.entities.relations.ProjectWithWorkers
import com.projectdelta.optimize.repository.ProjectRepository
import com.projectdelta.optimize.util.CVRPDataConverter
import com.projectdelta.optimize.util.Converters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class CWRMViewModel( private val mContext: Application) : AndroidViewModel( mContext ) {
	private val repository : ProjectRepository

	lateinit var project : Project
	lateinit var converter: CVRPDataConverter
	lateinit var workers : List<Worker>
	lateinit var containers: List<Container>
	lateinit var polylineData : List< List<Container> >
	lateinit var depot : LatLng

	init {
		val projectDao = ProjectDatabase.getInstance(mContext).projectDao()
		repository = ProjectRepository(projectDao)
	}

	private suspend fun getProject( projectName: String ) : Project{
		return repository.getProjectWithName( projectName )
	}

	suspend fun getProjectWithContainers(projectName : String) : List<ProjectWithContainers> {
		return repository.getProjectWithContainers(projectName)
	}

	private suspend fun getProjectWithWorkers(projectName : String) : List<ProjectWithWorkers> {
		return repository.getProjectWithWorkers(projectName)
	}

	fun generatePolylineData( projectName : String ){
		viewModelScope.launch(Dispatchers.IO) {
			val pr = async { getProject( projectName ) }
			val wr = async { getProjectWithWorkers( projectName ) }
			val ct = async { getProjectWithContainers( projectName ) }
			project = pr.await()
			workers = wr.await().first().workers
			containers = ct.await().first().containers
			depot = Converters().fromLongToLanLng( project.depotLatitude , project.depotLongitude )
			polylineData = workers.map{ W -> W.route.map { containers[it] } }
		}
	}
}