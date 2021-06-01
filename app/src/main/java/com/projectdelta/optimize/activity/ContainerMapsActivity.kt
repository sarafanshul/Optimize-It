package com.projectdelta.optimize.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.projectdelta.optimize.R
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.databinding.ActivityContainerMapsBinding
import com.projectdelta.optimize.viewModel.ContainerMapsViewModel

class ContainerMapsActivity : AppCompatActivity() {

	private val callback = OnMapReadyCallback { map ->
		/**
		 * Manipulates the map once available.
		 * This callback is triggered when the map is ready to be used.
		 * This is where we can add markers or lines, add listeners or move the camera.
		 * In this case, we just add a marker near Sydney, Australia.
		 * If Google Play services is not installed on the device, the user will be prompted to
		 * install it inside the SupportMapFragment. This method will only be triggered once the
		 * user has installed Google Play services and returned to the app.
		 */
		val depot = viewModel.getContainerLocation()
		setMapStyle( map )
//		googleMap.setMinZoomPreference( 6.0f )
		map.mapType = GoogleMap.MAP_TYPE_NORMAL
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(depot , 16f))
		val marker = MarkerOptions().position(depot).title("Destination")
		map.addMarker( marker )

		map.setOnMapClickListener {
			map.clear()
			marker.position( it )
			map.addMarker( marker )
			viewModel.setPosition( it )
		}

	}

	lateinit var binding : ActivityContainerMapsBinding
	lateinit var viewModel : ContainerMapsViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get(ContainerMapsViewModel::class.java)
		binding = ActivityContainerMapsBinding.inflate( layoutInflater )
		viewModel.set( intent.getSerializableExtra("CONTAINER") as Container )
		setContentView( binding.root )

		val mapFragment = supportFragmentManager.findFragmentById( binding.containerFcvMap.id ) as? SupportMapFragment
		mapFragment?.getMapAsync( callback )

	}

	private fun setMapStyle( map : GoogleMap ){
		try {
			val success = map.setMapStyle(
				MapStyleOptions.loadRawResourceStyle(
					this ,
					R.raw.map_dark_style
				)
			)
			if( !success ){
				Log.e("Maps" , "Failed to change styles ")
			}
		}catch ( e : Exception ){
			Log.e("Maps" , e.toString())
		}
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