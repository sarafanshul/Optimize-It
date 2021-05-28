package com.projectdelta.optimize

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.projectdelta.optimize.activity.ProjectInfoActivity
import com.projectdelta.optimize.constant.MAX_TIME_OUT_SECONDS
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.databinding.ActivityMainBinding
import com.projectdelta.optimize.fragment.LoadProjectFragment
import com.projectdelta.optimize.service.RetrofitInstance
import com.projectdelta.optimize.viewModel.MainViewModel
import okhttp3.OkHttpClient
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

	lateinit var binding : ActivityMainBinding
	lateinit var viewModel: MainViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( MainViewModel::class.java )
		setTheme(R.style.Theme_OptimizeIt)
		binding = ActivityMainBinding.inflate(layoutInflater)

		setContentView(binding.root)

		// add new project
		binding.mainLlNew.setOnClickListener {
			createNew()
		}

		// load fragment
		binding.mainBtLoad.setOnClickListener {
			load()
		}

		// api test
		binding.mainIvTestApi.setOnClickListener {
			testApiMain()
		}

	}

	private fun testApiMain() {
		val progressBar = ProgressDialog(this , R.style.ProgressDialogStyle).apply {
			setCancelable(false)
			setMessage("Waiting for response")
		}
		progressBar.show()

		lifecycleScope.launchWhenCreated {
			val response = try {
				RetrofitInstance.apiTest.getTest()
			} catch (e: IOException) {
				e.printStackTrace()
				Log.d("TestAPI", "IO Exception")
				Toast.makeText(this@MainActivity, "Some error occurred!", Toast.LENGTH_LONG).show()
				progressBar.dismiss()
				return@launchWhenCreated
			} catch (e: HttpException) {
				e.printStackTrace()
				Log.d("TestAPI", "HTTP Exception")
				Toast.makeText(this@MainActivity, "Some error occurred!", Toast.LENGTH_LONG).show()
				progressBar.dismiss()
				return@launchWhenCreated
			}
			if (response.isSuccessful && response.body() != null) {
				Log.d("RESPONSE", response.body().toString())
				Toast.makeText(this@MainActivity, "Server online!", Toast.LENGTH_LONG).show()
				progressBar.dismiss()
			}
		}

	}

	private fun createNew(){
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

	private fun load(){
		supportFragmentManager.beginTransaction().apply {
			setCustomAnimations(R.anim.enter_anim , R.anim.exit_anim , R.anim.enter_anim , R.anim.exit_anim)
			add( binding.mainFcvMain.id , LoadProjectFragment() )
			addToBackStack(null) // not needed actually
			commit()
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