package com.projectdelta.optimize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projectdelta.optimize.databinding.ActivityEditProjectBinding

class EditProjectActivity : AppCompatActivity() {

	lateinit var binding : ActivityEditProjectBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityEditProjectBinding.inflate(layoutInflater)
		setContentView( binding.root )

	}
}