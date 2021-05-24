package com.projectdelta.optimize.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.projectdelta.optimize.R
import com.projectdelta.optimize.adapter.RecyclerViewEditContainersAdapter
import com.projectdelta.optimize.adapter.RecyclerViewEditWorkerAdapter
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.databinding.ActivityProjectInfoBinding
import com.projectdelta.optimize.util.RecyclerItemClickListenr
import com.projectdelta.optimize.viewModel.ProjectInfoViewModel

class ProjectInfoActivity : AppCompatActivity() {

	lateinit var binding : ActivityProjectInfoBinding
	lateinit var viewModel : ProjectInfoViewModel
	lateinit var adapterContainer : RecyclerViewEditContainersAdapter
	lateinit var adapterWorker : RecyclerViewEditWorkerAdapter

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

	private fun editContainer( view: View, position: Int , projectName: String) {
		Intent(this , EditContainerActivity::class.java).apply {
			putExtra("PROJECT_NAME" , projectName)
			putExtra("CONTAINER_NAME" , adapterContainer.data[position].containerName)
		}.also {
			startActivity( it )
		}
	}

	private fun addNewContainer(projectName: String) {
		val dialogueLayout = layoutInflater.inflate(R.layout.dialog_text , null)
		val containerName = dialogueLayout.findViewById<EditText>(R.id.dialog_et_name)
		containerName.hint = "Container name"
		val displayTextAlertDialog = MaterialAlertDialogBuilder( this ).apply {
			setView( dialogueLayout )
			setTitle("Create new container")
			setMessage("Container name cannot be changed")
			setPositiveButton("CREATE"){_ , _ ->
				// [Start new Activity]
				createNewContainerAndLaunch( projectName , containerName.text.toString() )
			}
			setNegativeButton("CANCEL"){_ , _ ->

			}
		}.create()
		displayTextAlertDialog.show()

		// Initially disable the button for empty fields
		displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = false

		// Now set the textchange listener for edittext
		containerName.addTextChangedListener ( object : TextWatcher {
			override fun beforeTextChanged( s: CharSequence?, start: Int, count: Int, after: Int ) {
			}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			}

			override fun afterTextChanged(s: Editable?) {
				displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = !TextUtils.isEmpty(s)
			}
		} )

	}

	private fun createNewContainerAndLaunch(projectName: String, containerName: String) {
		viewModel.insertContainer( Container(containerName , projectName , 0 , 0 , 0) )
		Intent(this , EditContainerActivity::class.java).apply {
			putExtra("PROJECT_NAME" , projectName)
			putExtra("CONTAINER_NAME" , containerName)
		}.also {
			startActivity( it )
		}
	}
}