package com.projectdelta.optimize.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.ViewModelProvider
import com.projectdelta.optimize.constant.COORDINATE_MULTIPLER
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.databinding.ActivityEditContainerBinding
import com.projectdelta.optimize.viewModel.EditContainerViewModel

class EditContainerActivity : AppCompatActivity() {

	lateinit var binding : ActivityEditContainerBinding
	lateinit var viewModel: EditContainerViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( EditContainerViewModel::class.java )
		binding = ActivityEditContainerBinding.inflate(layoutInflater)

		val container : Container = intent.getSerializableExtra("CONTAINER") as Container

		setContentView( binding.root )

		setDefaults(container)

		binding.editContainerTwCancel.setOnClickListener {
			resultCancel()
		}
		binding.editContainerTwSave.setOnClickListener {
			saveAnsFinish( container )
		}

		binding.editContainerIvDelete.setOnClickListener {
			viewModel.delete( container )
			resultOk()
		}

	}

	private fun saveAnsFinish( container: Container ) {
		container.value = binding.editContainerEtValue.text.toString().toLong()
		container.weight = binding.editContainerEtWeight.text.toString().toLong()
		container.count = binding.editContainerEtCount.text.toString().toInt()
		container.latitude = binding.editContainerEtLocationLat.text.toString().toLong()
		container.longitude = binding.editContainerEtLocationLong.text.toString().toLong()
//		container.latitude = (binding.editContainerEtLocationLat.text.toString().trim().toFloat() * COORDINATE_MULTIPLER.toFloat()).toLong()
//		container.longitude = (binding.editContainerEtLocationLong.text.toString().trim().toFloat() * COORDINATE_MULTIPLER.toFloat()).toLong()
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
		binding.editContainerEtLocationLat.text = container.latitude.toString().toEditable()
		binding.editContainerEtLocationLong.text = container.longitude.toString().toEditable()
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