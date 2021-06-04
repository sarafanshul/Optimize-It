package com.projectdelta.optimize.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.projectdelta.optimize.constant.COORDINATE_MULTIPLIER
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.databinding.ActivityEditContainerBinding
import com.projectdelta.optimize.viewModel.EditContainerViewModel

class EditContainerActivity : AppCompatActivity() {

	lateinit var binding : ActivityEditContainerBinding
	lateinit var viewModel: EditContainerViewModel

	private val startForResultMaps = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK || true ) { // redundant for now
			viewModel.updateContainer()
			binding.editContainerEtLocationLat.text = (viewModel.container.latitude / COORDINATE_MULTIPLIER ).toString().toEditable()
			binding.editContainerEtLocationLong.text = (viewModel.container.longitude / COORDINATE_MULTIPLIER ).toString().toEditable()
			Toast.makeText(this , "Position updated!" , Toast.LENGTH_LONG).show()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( EditContainerViewModel::class.java )
		binding = ActivityEditContainerBinding.inflate(layoutInflater)

	 	viewModel.container = intent.getSerializableExtra("CONTAINER") as Container

		setContentView( binding.root )

		setDefaults(viewModel.container)

		binding.editContainerTwCancel.setOnClickListener {
			resultCancel()
		}
		binding.editContainerTwSave.setOnClickListener {
			saveAnsFinish( viewModel.container )
		}

		binding.editContainerIvDelete.setOnClickListener {
			viewModel.delete( viewModel.container )
			resultOk()
		}

		binding.edContainerAddIvMap.setOnClickListener {
			Intent( this , ContainerMapsActivity::class.java ).apply{
				putExtra("CONTAINER" , viewModel.container)
			}.also {
				startForResultMaps.launch( it )
			}
		}

	}

	private fun saveAnsFinish( container: Container ) {
		container.value = binding.editContainerEtValue.text.toString().toLong()
		container.weight = binding.editContainerEtWeight.text.toString().toLong()
		container.count = binding.editContainerEtCount.text.toString().toInt()
		viewModel.update( container )
		resultOk()
	}

	private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

	private fun setDefaults( container: Container ){
		binding.editContainerTwId.text = container.containerName
		binding.editContainerTwProject.text = container.projectName
		binding.editContainerEtValue.text = container.value.toString().toEditable()
		binding.editContainerEtWeight.text = container.weight.toString().toEditable()
		binding.editContainerEtCount.text = container.count.toString().toEditable()
		binding.editContainerEtLocationLat.text = (container.latitude / COORDINATE_MULTIPLIER ).toString().toEditable()
		binding.editContainerEtLocationLong.text = (container.longitude / COORDINATE_MULTIPLIER ).toString().toEditable()
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