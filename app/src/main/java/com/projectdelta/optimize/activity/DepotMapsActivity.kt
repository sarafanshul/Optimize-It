package com.projectdelta.optimize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.projectdelta.optimize.R
import com.projectdelta.optimize.databinding.ActivityDepotMapsBinding
import com.projectdelta.optimize.viewModel.DepotMapsViewModel
import kotlin.Exception

class DepotMapsActivity : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

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
		val depot = viewModel.getDepotLocation()
		setMapStyle( map )
//		googleMap.setMinZoomPreference( 6.0f )
		map.mapType = GoogleMap.MAP_TYPE_NORMAL
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(depot , 16f))
		val marker = MarkerOptions().position(depot).title("Depot location")
		map.addMarker( marker )

		map.setOnMapClickListener {
			map.clear()
			marker.position( it )
			map.addMarker( marker )
			viewModel.setPosition( it )
		}

	}

	lateinit var viewModel : DepotMapsViewModel
	lateinit var binding : ActivityDepotMapsBinding
	lateinit var projectName : String
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get( DepotMapsViewModel::class.java )
		binding = ActivityDepotMapsBinding.inflate( layoutInflater )
		projectName = intent.getStringExtra("PROJECT_NAME") as String
		viewModel.setProject( projectName )
		setContentView( binding.root )

		val mapFragment = supportFragmentManager.findFragmentById( binding.depotFcvMap.id ) as? SupportMapFragment
		mapFragment?.getMapAsync( callback )

	}


	/*
	* set to false for default behaviour , can change later
	* */
	override fun onMarkerClick(marker: Marker?): Boolean {
		if( marker != null ){
			Toast.makeText(this , "Depot Location" , Toast.LENGTH_LONG).show()
		}
		return false
	}

	private fun setMapStyle( map : GoogleMap ){
		try {
			val success = map.setMapStyle(
				MapStyleOptions.loadRawResourceStyle(
					this ,
					R.raw.map_retro_style
				)
			)
			if( !success ){
				Log.e("Maps" , "Failed to change styles ")
			}
		}catch ( e : Exception ){
			Log.e("Maps" , e.toString())
		}
	}

}