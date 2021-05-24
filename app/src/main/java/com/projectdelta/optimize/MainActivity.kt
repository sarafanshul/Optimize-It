package com.projectdelta.optimize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.projectdelta.optimize.activity.ProjectInfoActivity
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.databinding.ActivityMainBinding
import com.projectdelta.optimize.fragment.LoadProjectFragment
import com.projectdelta.optimize.viewModel.MainViewModel


class MainActivity : AppCompatActivity() {

	lateinit var binding : ActivityMainBinding
	lateinit var viewModel: MainViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( MainViewModel::class.java )

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.mainLlNew.setOnClickListener {
			val dialogueLayout = layoutInflater.inflate(R.layout.dialog_text , null)
			val projectName = dialogueLayout.findViewById<EditText>(R.id.dialog_et_name)

			val displayTextAlertDialog = MaterialAlertDialogBuilder( this ).apply {
				setView( dialogueLayout )
				setTitle("Create new project")
				setMessage("Project name cannot be changed")
				setPositiveButton("CREATE"){_ , _ ->
					// [Start new Activity]
					createNewProjectAndLaunch( projectName.text.toString() )
				}
				setNegativeButton("CANCEL"){_ , _ ->

				}
			}.create()
			displayTextAlertDialog.show()

			// Initially disable the button for empty fields
			displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = false

			// Now set the textchange listener for edittext
			projectName.addTextChangedListener ( object : TextWatcher{
				override fun beforeTextChanged( s: CharSequence?, start: Int, count: Int, after: Int ) {
				}
				override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				}

				override fun afterTextChanged(s: Editable?) {
					displayTextAlertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).isEnabled = !TextUtils.isEmpty(s)
				}
			} )

		}

		binding.mainBtLoad.setOnClickListener {
			supportFragmentManager.beginTransaction().apply {
				setCustomAnimations(R.anim.enter_anim , R.anim.exit_anim , R.anim.enter_anim , R.anim.exit_anim)
				add( binding.mainFcvMain.id , LoadProjectFragment() )
				addToBackStack(null)
				commit()
			}
		}

	}

	private fun createNewProjectAndLaunch(projectName: String) {

		viewModel.insert( Project( projectName ) )

		Intent( this@MainActivity , ProjectInfoActivity::class.java ).apply {
			putExtra("PROJECT_NAME" , projectName)
		}.also {
			startActivity(it)
		}
	}
}