package com.projectdelta.optimize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.projectdelta.optimize.databinding.ActivityEditContainerBinding
import com.projectdelta.optimize.viewModel.EditContainerViewModel

class EditContainerActivity : AppCompatActivity() {

	lateinit var binding : ActivityEditContainerBinding
	lateinit var viewModel: EditContainerViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( EditContainerViewModel::class.java )
		binding = ActivityEditContainerBinding.inflate(layoutInflater)

		val projectName = intent.getStringExtra("PROJECT_NAME") as String
		val containerName = intent.getStringExtra("CONTAINER_NAME") as String


		setContentView( binding.root )

//		Log.d( "NAMES" , container.toString())

	}
}