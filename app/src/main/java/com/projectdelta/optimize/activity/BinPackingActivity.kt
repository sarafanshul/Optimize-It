package com.projectdelta.optimize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projectdelta.optimize.databinding.ActivityBinPackingBinding

class BinPackingActivity : AppCompatActivity() {

	lateinit var binding : ActivityBinPackingBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityBinPackingBinding.inflate(layoutInflater)

		setContentView( binding.root )


	}
}