package com.projectdelta.optimize.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.ViewModelProvider
import com.projectdelta.optimize.R
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.databinding.ActivityEditWorkerBinding
import com.projectdelta.optimize.fragment.LoadProjectFragment
import com.projectdelta.optimize.fragment.ShowJobsFragment
import com.projectdelta.optimize.viewModel.EditWorkerViewModel

class EditWorkerActivity : AppCompatActivity() {

	private lateinit var binding : ActivityEditWorkerBinding
	lateinit var viewModel: EditWorkerViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( EditWorkerViewModel::class.java )
		val worker : Worker = intent.getSerializableExtra("WORKER") as Worker

		binding = ActivityEditWorkerBinding.inflate(layoutInflater)
		setContentView( binding.root )

		setDefaults(worker)

		binding.editWorkerTwCancel.setOnClickListener {
			resultCancel()
		}
		binding.editWorkerTwSave.setOnClickListener {
			saveAndFinish( worker )
		}
		binding.editWorkerIvDelete.setOnClickListener {
			deleteAndFinish( worker )
		}

		binding.editWorkerAddCwJobs.setOnClickListener {
			startShowJobsFragment( worker )
		}

	}

	private fun startShowJobsFragment(worker: Worker) {
		val bundle = Bundle()
		bundle.putSerializable("WORKER" , worker)
		val fragment = ShowJobsFragment()
		fragment.arguments = bundle

		supportFragmentManager.beginTransaction().apply {
			setCustomAnimations(
				R.anim.enter_anim,
				R.anim.exit_anim,
				R.anim.enter_anim,
				R.anim.exit_anim
			)
			replace(binding.editWorkerFc.id, fragment)
			addToBackStack(null)
			commit()
		}
	}


	private fun deleteAndFinish(worker: Worker) {
		viewModel.delete( worker )
		resultOk()
	}

	private fun saveAndFinish(worker: Worker) {

		worker.capacity = binding.editWorkerEtCapacity.text.toString().toLong()
		worker.maxDistance = binding.editWorkerEtDist.text.toString().toLong()

		viewModel.update( worker )

		resultOk()
	}

	private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

	private fun setDefaults(worker: Worker) {
		binding.editWorkerTwName.text = worker.workerId.toString()
		binding.editWorkerTwProject.text = worker.projectName
		binding.editWorkerEtCapacity.text = worker.capacity.toString().toEditable()
		binding.editWorkerEtDist.text = worker.maxDistance.toString().toEditable()
	}

	private fun resultOk(){
		setResult( Activity.RESULT_OK)
		finish()
	}
	private fun resultCancel(){
		setResult( Activity.RESULT_CANCELED )
		finish()
	}

}