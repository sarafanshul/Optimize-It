package com.projectdelta.optimize.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.projectdelta.optimize.R
import com.projectdelta.optimize.databinding.ActivityClusteredWorkerRouteMapBinding
import com.projectdelta.optimize.util.Converters
import com.projectdelta.optimize.util.RandomColors
import com.projectdelta.optimize.viewModel.CWRMViewModel

class ClusteredWorkerRouteMapActivity : AppCompatActivity() {

	private val callback = OnMapReadyCallback{ map ->
		/**
		 * Manipulates the map once available.
		 * This callback is triggered when the map is ready to be used.
		 * This is where we can add markers or lines, add listeners or move the camera.
		 * In this case, we just add a marker near Sydney, Australia.
		 * If Google Play services is not installed on the device, the user will be prompted to
		 * install it inside the SupportMapFragment. This method will only be triggered once the
		 * user has installed Google Play services and returned to the app.
		 */
		setMapStyle( map )
		addPolyline( map )
		map.mapType = GoogleMap.MAP_TYPE_NORMAL
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.depot , 16f))
		map.addMarker( MarkerOptions()
			.position(viewModel.depot)
			.title("Depot location")
			.snippet("Location of depot")
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
		)

		val containerMarkers = viewModel.polylineData.map{ C -> C.map {
			map.addMarker(MarkerOptions()
				.position( Converters().fromLongToLanLng( it.latitude , it.longitude ) )
				.title("delivery location")
			)
		} }
	}

	lateinit var binding : ActivityClusteredWorkerRouteMapBinding
	lateinit var viewModel : CWRMViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider( this , ViewModelProvider.AndroidViewModelFactory.getInstance(this.application) ).get(CWRMViewModel::class.java)
		binding = ActivityClusteredWorkerRouteMapBinding.inflate( layoutInflater )
		val projectName : String = intent.getStringExtra( "PROJECT_NAME" ) as String
		viewModel.generatePolylineData( projectName )
		setContentView(binding.root)

		val mapFragment = supportFragmentManager.findFragmentById( binding.cwrmFcvMap.id ) as? SupportMapFragment
		mapFragment?.getMapAsync( callback )

	}

	private fun addPolyline( map: GoogleMap ){
		val rc = RandomColors()
		viewModel.polylineData.forEach {
			val cords = mutableListOf<LatLng>(viewModel.depot)
			cords.addAll( it.map{ X -> Converters().fromLongToLanLng( X.latitude , X.longitude ) } )
			cords.add(viewModel.depot)
			map.addPolyline(
				PolylineOptions().apply {
					color( rc.color )
					width(8f)
					geodesic(true)
				}
			).points = cords
//			Log.d("POLYLINE DATA" , cords.toString() )
		}
	}

	private fun setMapStyle( map : GoogleMap){
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

}