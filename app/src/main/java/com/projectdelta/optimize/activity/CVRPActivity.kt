package com.projectdelta.optimize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.projectdelta.optimize.databinding.ActivityCvrpactivityBinding
import com.projectdelta.optimize.service.RetrofitInstance
import com.projectdelta.optimize.util.CVRPDataConverter
import com.projectdelta.optimize.viewModel.CVRPViewModel
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException

class CVRPActivity : AppCompatActivity() {

	lateinit var binding : ActivityCvrpactivityBinding
	lateinit var viewModel : CVRPViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCvrpactivityBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( CVRPViewModel::class.java )

		setContentView(binding.root)

		val projectName = intent.getStringExtra("PROJECT_NAME") as String

		if( projectName.isNullOrEmpty() )
			finish()

		launchRequest( projectName )

	}

	private fun launchRequest(projectName: String) {

		lifecycleScope.launchWhenCreated {
			val projectD = async { viewModel.getProject(projectName) }
			val containersD = async { viewModel.getProjectWithContainers(projectName) }
			val workersD = async { viewModel.getProjectWithWorkers(projectName) }

			viewModel.converter = CVRPDataConverter()
			viewModel.project = projectD.await() // OR use from  *project-with-containers*
			viewModel.containers = containersD.await().first().containers
			viewModel.workers = workersD.await().first().workers

			viewModel.converter.set( viewModel.project )
			viewModel.converter.set( viewModel.containers , viewModel.workers )

			val model = viewModel.converter.fromRawToRequest()

			Toast.makeText(this@CVRPActivity , "Waiting for response" , Toast.LENGTH_LONG).show()
			val response = try {
				RetrofitInstance.apiCVRP.post( model )
			} catch (e: IOException) {
				e.printStackTrace()
				Log.d("CVRP-API", "IO Exception")
				Toast.makeText(this@CVRPActivity , "Some error occurred!" , Toast.LENGTH_LONG).show()
				return@launchWhenCreated
			} catch (e: HttpException) {
				e.printStackTrace()
				Log.d("CVRP-API", "HTTP Exception")
				Toast.makeText(this@CVRPActivity , "Some error occurred!" , Toast.LENGTH_LONG).show()
				return@launchWhenCreated
			}
			if (response.isSuccessful && response.body() != null) {
				Log.d("RESPONSE", response.body().toString())
				viewModel.converter.set(response.body()!!)
				viewModel.converter.fromResponseToRaw()
				viewModel.workers = viewModel.converter.workers
				// binding.binPackingLl.visibility = View.VISIBLE
				viewModel.updateWorkers( )
				createView( projectName )
			}

		}

	}

	private fun createView(projectName: String) {

	}
}