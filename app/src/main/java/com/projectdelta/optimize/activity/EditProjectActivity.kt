package com.projectdelta.optimize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.projectdelta.optimize.adapter.RecyclerViewEditContainers
import com.projectdelta.optimize.adapter.RecyclerViewEditWorkerAdapter
import com.projectdelta.optimize.databinding.ActivityEditProjectBinding
import com.projectdelta.optimize.util.RecyclerItemClickListenr
import com.projectdelta.optimize.viewModel.EditProjectActivityViewModel
import com.projectdelta.optimize.viewModel.MainActivityViewModel

class EditProjectActivity : AppCompatActivity() {

	lateinit var binding : ActivityEditProjectBinding
	lateinit var viewModel : EditProjectActivityViewModel
	lateinit var adapterContainer : RecyclerViewEditContainers
	lateinit var adapterWorker : RecyclerViewEditWorkerAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( EditProjectActivityViewModel::class.java )

		binding = ActivityEditProjectBinding.inflate(layoutInflater)
		setContentView( binding.root )

		val projectName = intent.getStringExtra("PROJECT_NAME") as String

		binding.editTwName.text = projectName

		setRvContainers( projectName )

		setRvWorkers( projectName )

	}

	private fun setRvWorkers(projectName: String) {
		binding.editRvWorkers.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		val snapHelper = PagerSnapHelper()
		snapHelper.attachToRecyclerView(binding.editRvWorkers)
		adapterWorker = RecyclerViewEditWorkerAdapter(this)
		binding.editRvWorkers.adapter = adapterWorker
		viewModel.getWorkers(projectName).observe(this, { data ->
			if (data.isNullOrEmpty()) return@observe
			adapterWorker.set(data.first().workers)
		})
	}

	private fun setRvContainers(projectName: String) {

		binding.editRvContainers.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		val snapHelper = PagerSnapHelper()
		snapHelper.attachToRecyclerView(binding.editRvContainers)
		adapterContainer = RecyclerViewEditContainers(this)
		binding.editRvContainers.adapter = adapterContainer
		viewModel.getContainers(projectName).observe(this, { data ->
			if (data.isNullOrEmpty()) return@observe
			adapterContainer.set(data.first().containers)
		})

		binding.editRvContainers.addOnItemTouchListener(RecyclerItemClickListenr( this , binding.editRvContainers ,
			object : RecyclerItemClickListenr.OnItemClickListener{
				override fun onItemClick(view: View, position: Int) {
					if( position == adapterContainer.itemCount - 1 )
						addNewContainer( view , position )
					else editContainer( view , position )
				}
				override fun onItemLongClick(view: View?, position: Int) { }
			}
		))

	}

	private fun editContainer(@Nullable view: View, @Nullable position: Int) {

	}

	private fun addNewContainer(@Nullable view: View, @Nullable position: Int) {

	}
}