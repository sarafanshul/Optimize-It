package com.projectdelta.optimize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.projectdelta.optimize.activity.EditProjectActivity
import com.projectdelta.optimize.databinding.ActivityMainBinding
import com.projectdelta.optimize.databinding.DialogTextBinding
import com.projectdelta.optimize.fragment.LoadProjectFragment


class MainActivity : AppCompatActivity() {

	lateinit var binding : ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

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
					Intent( this@MainActivity , EditProjectActivity::class.java ).apply {
						putExtra("PROJECT_NAME" , projectName.text)
					}.also {
						startActivity(it)
					}
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
				add( binding.mainFcvMain.id , LoadProjectFragment() )
				addToBackStack(null)
				commit()
			}
		}

	}
}