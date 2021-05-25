package com.projectdelta.optimize.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.projectdelta.optimize.R
import com.projectdelta.optimize.adapter.RecyclerViewEditContainersAdapter
import com.projectdelta.optimize.adapter.RecyclerViewEditWorkerAdapter
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.databinding.ActivityProjectInfoBinding
import com.projectdelta.optimize.util.RecyclerItemClickListenr
import com.projectdelta.optimize.viewModel.ProjectInfoViewModel

class ProjectInfoActivity : AppCompatActivity() {

	private lateinit var binding : ActivityProjectInfoBinding
	private lateinit var viewModel : ProjectInfoViewModel
	lateinit var adapterContainer : RecyclerViewEditContainersAdapter
	lateinit var adapterWorker : RecyclerViewEditWorkerAdapter

	// https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
	private val startForResultContainer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK) {
			if( this::binding.isInitialized )
				Snackbar.make(binding.root , "Container updated" , Snackbar.LENGTH_LONG).apply {
					anchorView = binding.editCvSubmit
				}.show()
			else
				Toast.makeText(this , "Container updated" , Toast.LENGTH_LONG).show()
		}
	}

	private val startForResultWorker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK) {
			if( this::binding.isInitialized )
				Snackbar.make(binding.root , "Worker updated" , Snackbar.LENGTH_LONG).apply {
					anchorView = binding.editCvSubmit
				}.show()
			else
				Toast.makeText(this , "Worker updated" , Toast.LENGTH_LONG).show()
		}
	}

	private val REQUEST_CODE = 101

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( ProjectInfoViewModel::class.java )

		binding = ActivityProjectInfoBinding.inflate(layoutInflater)
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

		binding.editRvWorkers.addOnItemTouchListener(RecyclerItemClickListenr( this , binding.editRvWorkers ,
			object : RecyclerItemClickListenr.OnItemClickListener{
				override fun onItemClick(view: View, position: Int) {
					if( position == adapterWorker.itemCount - 1 )
						addNewWorker( projectName )
					else editWorker( view , position , projectName)
				}
				override fun onItemLongClick(view: View?, position: Int) { }
			}
		))

	}

	private fun launchWorkerActivity(worker: Worker) {
		Intent(this , EditWorkerActivity::class.java).apply {
			putExtra("WORKER" , worker)
		}.also {
			startForResultWorker.launch( it )
		}
	}

	private fun editWorker(view: View, position: Int, projectName: String) {
		launchWorkerActivity( adapterWorker.data[position] )
	}

	private fun addNewWorker(projectName: String) {
		val dialogueLayout = layoutInflater.inflate(R.layout.dialog_text , null)
		val workerName = dialogueLayout.findViewById<EditText>(R.id.dialog_et_name)
		workerName.hint = "Worker ID"
		workerName.inputType = InputType.TYPE_CLASS_NUMBER
		val displayTextAlertDialog = MaterialAlertDialogBuilder( this ).apply {
			setView( dialogueLayout )
			setTitle("Add new worker")
			setMessage("ID cannot be changed")
			setPositiveButton("CREATE"){_ , _ ->
				// [Start new Activity]
				createNewWorker( projectName , workerName.text.toString().toLong() )
			}
			setNegativeButton("CANCEL"){_ , _ ->

			}
		}.create()
		displayTextAlertDialog.show()

		// Initially disable the button for empty fields
		displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = false

		// Now set the textchange listener for edittext
		workerName.addTextChangedListener ( object : TextWatcher {
			override fun beforeTextChanged( s: CharSequence?, start: Int, count: Int, after: Int ) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
			override fun afterTextChanged(s: Editable?) {
				displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = !TextUtils.isEmpty(s)
			}
		} )
	}

	private fun createNewWorker(projectName: String, workerID: Long) {
		val newWorker = Worker( workerID , projectName , 0 )
		viewModel.insertWorker( newWorker )
		launchWorkerActivity( newWorker )
	}


	private fun setRvContainers(projectName: String) {

		binding.editRvContainers.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		val snapHelper = PagerSnapHelper()
		snapHelper.attachToRecyclerView(binding.editRvContainers)
		adapterContainer = RecyclerViewEditContainersAdapter(this)
		binding.editRvContainers.adapter = adapterContainer
		viewModel.getContainers(projectName).observe(this, { data ->
			if (data.isNullOrEmpty()) return@observe
			adapterContainer.set(data.first().containers)
		})

		binding.editRvContainers.addOnItemTouchListener(RecyclerItemClickListenr( this , binding.editRvContainers ,
			object : RecyclerItemClickListenr.OnItemClickListener{
				override fun onItemClick(view: View, position: Int) {
					if( position == adapterContainer.itemCount - 1 )
						addNewContainer( projectName )
					else editContainer( view , position , projectName)
				}
				override fun onItemLongClick(view: View?, position: Int) { }
			}
		))

	}

	private fun launchContainerActivity( container: Container ){

		Intent(this , EditContainerActivity::class.java).apply {
			putExtra("CONTAINER" , container)
		}.also {
			startForResultContainer.launch( it )
		}
		
	}
	
	private fun editContainer( view: View, position: Int , projectName: String) {
		launchContainerActivity( adapterContainer.data[position] )
	}

	private fun addNewContainer(projectName: String) {
		val dialogueLayout = layoutInflater.inflate(R.layout.dialog_text , null)
		val containerName = dialogueLayout.findViewById<EditText>(R.id.dialog_et_name)
		containerName.hint = "Container name"
		val displayTextAlertDialog = MaterialAlertDialogBuilder( this ).apply {
			setView( dialogueLayout )
			setTitle("Add new container")
			setMessage("Name cannot be changed")
			setPositiveButton("CREATE"){_ , _ ->
				// [Start new Activity]
				createNewContainer( projectName , containerName.text.toString() )
			}
			setNegativeButton("CANCEL"){_ , _ ->

			}
		}.create()
		displayTextAlertDialog.show()

		// Initially disable the button for empty fields
		displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = false

		// Now set the textchange listener for edittext
		containerName.addTextChangedListener ( object : TextWatcher {
			override fun beforeTextChanged( s: CharSequence?, start: Int, count: Int, after: Int ) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
			override fun afterTextChanged(s: Editable?) {
				displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = !TextUtils.isEmpty(s)
			}
		} )

	}

	private fun createNewContainer(projectName: String, containerName: String) {
		val newContainer = Container(containerName , projectName , 0 , 0 , 0)
		viewModel.insertContainer( newContainer )
		launchContainerActivity( newContainer )
	}
}