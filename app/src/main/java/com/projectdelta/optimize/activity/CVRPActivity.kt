package com.projectdelta.optimize.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectdelta.optimize.R
import com.projectdelta.optimize.adapter.RecyclerViewCVRPAdapter
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.databinding.ActivityCvrpBinding
import com.projectdelta.optimize.service.RetrofitInstance
import com.projectdelta.optimize.util.CVRPDataConverter
import com.projectdelta.optimize.util.RecyclerItemClickListenr
import com.projectdelta.optimize.util.StatesRecyclerViewAdapter
import com.projectdelta.optimize.viewModel.CVRPViewModel
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException

class CVRPActivity : AppCompatActivity() {

	lateinit var binding : ActivityCvrpBinding
	lateinit var viewModel : CVRPViewModel
	lateinit var adapter : RecyclerViewCVRPAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCvrpBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( CVRPViewModel::class.java )

		setContentView(binding.root)

		val projectName = intent.getStringExtra("PROJECT_NAME") as String

		if( projectName.isNullOrEmpty() )
			finish()

		binding.cvrpPb.visibility = View.VISIBLE
		binding.cvrpLl.visibility = View.GONE

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
			Log.d("RESPONSE REQUEST MODEL" , model.toString())
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
			if (response.isSuccessful && response.body() != null && !response.body()!!.RouteDistance.isNullOrEmpty()) {
				Log.d("RESPONSE", response.body().toString())
				viewModel.converter.set(response.body()!!)
				viewModel.converter.fromResponseToRaw()
				viewModel.workers = viewModel.converter.workers
				// binding.binPackingLl.visibility = View.VISIBLE
				viewModel.updateWorkers( )
				createView( projectName )
			}else {
				Toast.makeText(this@CVRPActivity , "Some error occurred!" , Toast.LENGTH_LONG).show()
				finish()
			}

		}

	}

	private fun createView(projectName: String) {
		binding.cvrpTwCount.text = viewModel.converter.count.toString()
		binding.cvrpTwDistance.text = viewModel.converter.responseModel.RouteDistance.values.sum().toString()
		binding.cvrpTwValue.text = "${viewModel.converter.usedValue}/${viewModel.converter.totalValue}"
		binding.cvrpTwWeight.text = "${viewModel.converter.usedWeight}/${viewModel.converter.totalWeight}"

		binding.cvrpLl.visibility = View.VISIBLE
		binding.cvrpPb.visibility = View.GONE

		binding.cvrpRv.layoutManager = LinearLayoutManager(this)
		adapter = RecyclerViewCVRPAdapter(this)

		val emptyView : View = layoutInflater.inflate( R.layout.layout_empty_view , binding.cvrpRv , false )

		val statesAdapter = StatesRecyclerViewAdapter(adapter , emptyView , emptyView , emptyView)
		binding.cvrpRv.adapter = statesAdapter
		adapter.set( viewModel.workers )

		binding.cvrpRv.addOnItemTouchListener(RecyclerItemClickListenr( this , binding.cvrpRv ,
			object : RecyclerItemClickListenr.OnItemClickListener{
				override fun onItemClick(view: View, position: Int) {
					launchActivityAndFinish( adapter.data[position] )
				}
				override fun onItemLongClick(view: View?, position: Int) { }
			}
		))

	}

	private fun launchActivityAndFinish(worker: Worker) {
		val i = Intent( this , EditWorkerActivity::class.java )
			.putExtra( "WORKER" , worker )

		// not added to stack | async calls hence both are called
		finish()
		startActivity( i )
	}
}