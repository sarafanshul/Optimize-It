package com.projectdelta.optimize.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectdelta.optimize.R
import com.projectdelta.optimize.adapter.RecyclerViewBinPackingAdapter

import com.projectdelta.optimize.databinding.ActivityBinPackingBinding
import com.projectdelta.optimize.service.RetrofitInstance

import com.projectdelta.optimize.util.BinPackingDataConverter
import com.projectdelta.optimize.util.StatesRecyclerViewAdapter
import com.projectdelta.optimize.viewModel.BinPackingViewModel

import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException

class BinPackingActivity : AppCompatActivity() {

	lateinit var binding : ActivityBinPackingBinding
	lateinit var viewModel : BinPackingViewModel
	lateinit var adapter : RecyclerViewBinPackingAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityBinPackingBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( BinPackingViewModel::class.java )

		val projectName = intent.getStringExtra("PROJECT_NAME") as String

		if( projectName.isNullOrEmpty() )
			finish()

		setContentView( binding.root )
		binding.binPackingPb.visibility = View.VISIBLE
		binding.binPackingLl.visibility = View.GONE

		launchRequest( projectName )

	}

	private fun launchRequest(projectName: String) {
		lifecycleScope.launchWhenCreated{

			val containersD = async { viewModel.getProjectWithContainers(projectName) }
			val workersD = async { viewModel.getProjectWithWorkers(projectName) }

			viewModel.converter = BinPackingDataConverter()
			viewModel.containers = containersD.await().first().containers
			viewModel.workers = workersD.await().first().workers

			viewModel.converter.set( viewModel.containers , viewModel.workers )

			val model = viewModel.converter.fromRawToRequest()

			val response = try {
				RetrofitInstance.apiBinPacking.post( model )
			} catch (e: IOException) {
				e.printStackTrace()
				Log.d("BinPackingAPI", "IO Exception")
				Toast.makeText(this@BinPackingActivity , "Some error occurred!" , Toast.LENGTH_LONG).show()
				return@launchWhenCreated
			} catch (e: HttpException) {
				e.printStackTrace()
				Log.d("BinPackingAPI", "HTTP Exception")
				Toast.makeText(this@BinPackingActivity , "Some error occurred!" , Toast.LENGTH_LONG).show()
				return@launchWhenCreated
			}
			if (response.isSuccessful && response.body() != null) {
				Log.d("RESPONSE", response.body().toString())
				viewModel.converter.set(response.body()!!)
				viewModel.converter.fromResponseToRaw()
				viewModel.workers = viewModel.converter.workers
				binding.binPackingLl.visibility = View.VISIBLE
				viewModel.updateWorkers( )
				createView( projectName )
			}
		}
	}

	private fun createView(projectName: String) {

		binding.binPackingTwValue.text = "${ viewModel.converter.responseModel.totalValuePacked }/${ viewModel.converter.totalValue }"
		binding.binPackingTwWeight.text = "${ viewModel.converter.responseModel.totalWeightPacked }/${ viewModel.converter.totalWeight }"

		binding.binPackingPb.visibility = View.GONE

		binding.binPackingRv.layoutManager = LinearLayoutManager(this)
		val emptyView : View = layoutInflater.inflate( R.layout.layout_empty_view , binding.binPackingRv , false )

		adapter = RecyclerViewBinPackingAdapter(this)
		val statesAdapter = StatesRecyclerViewAdapter(adapter , emptyView , emptyView , emptyView)
		binding.binPackingRv.adapter = statesAdapter
		adapter.set( viewModel.workers )
	}
}