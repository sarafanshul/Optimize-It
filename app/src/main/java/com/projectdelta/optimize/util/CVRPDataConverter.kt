package com.projectdelta.optimize.util

import android.util.Log
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.service.request.CVRPRequestModel
import com.projectdelta.optimize.service.response.CVRPResponse
import java.lang.Exception

class CVRPDataConverter {

	lateinit var containers : List<Container>
	lateinit var workers : List<Worker>
	lateinit var project: Project
	lateinit var responseModel : CVRPResponse
	var totalValue = 0L
	var totalWeight = 0L
	var usedValue = 0L
	var usedWeight = 0L
	var count = 0
	var prefixContainerIdx = mutableListOf<Int>()

	fun set( model: CVRPResponse){
		responseModel = model
	}

	fun set( _project : Project ) {
		project = _project
	}

	fun set( _containers : List<Container> , _workers : List<Worker> ){
		containers = _containers
		workers = _workers
	}

	fun fromRawToRequest() : CVRPRequestModel {
		if( ! this::containers.isInitialized || ! this::workers.isInitialized ||
				! this::project.isInitialized )
			return CVRPRequestModel( 0 , List(0){ emptyList()} ,
				List(0){ emptyList()} , emptyList() , emptyList() )

		totalValue = 0
		totalWeight = 0
		val depot = List( 1 ) { listOf( project.depotLatitude , project.depotLongitude ) }

		prefixContainerIdx = MutableList( containers.size) { i -> containers[i].count }

		for( i in 1 until prefixContainerIdx.size )
			prefixContainerIdx[ i ] += prefixContainerIdx[ i - 1 ]

		val loc = mutableListOf< List<Long> >() ; val demands = mutableListOf<Long>()
		containers.forEach { container ->
			loc.addAll( MutableList(container.count){  listOf( container.latitude , container.longitude )  } )
			demands.addAll( MutableList(container.count){ container.weight } )
			totalValue += container.count * container.value
			totalWeight += container.count * container.weight
		}

		val cap = List( workers.size ){ workers[it].capacity }

		return CVRPRequestModel( workers.size , depot , loc.toList() , cap , demands )
	}

	/* 1 INDEXED  =>
	* 		depot at start -> model returned 1 indexed
	*  		- avoid (0) valued files
	*   	- change 1 indexed values to 0 indexed
	* */
	fun fromResponseToRaw(){
		if( ! this::project.isInitialized    ||
			! this::containers.isInitialized ||
			! this::workers.isInitialized    ||
			! this::responseModel.isInitialized ) return

		for( i in responseModel.VehicleRoute ){
			workers[i.key.trim().toInt()].route = getContainer( i.value )
			workers[i.key.trim().toInt()].jobs = workers[i.key.trim().toInt()].route // routed jobs
		}

		for( i in responseModel.RouteDistance ){
			workers[ i.key.trim().toInt() ].distanceTravelled = i.value
		}

		Log.d("NEW WORKERS" , workers.toString())
	}

	private fun getContainer( data : List<Int> ) : List<Int>{
		// binary search for container idx
		val result = Array( data.size - 2 ){ 0 } // because start = depot & end = depot
		for( i in 1 until data.size - 1 ) {
			var l = -1 ; var r = containers.size ; var m = l
			while (l + 1 < r) {
				m = (l + r) / 2
				if (prefixContainerIdx[m] >= (data[i] - 1) ) // cahnge from 1Indexed to 0Indexed
					r = m
				else
					l = m
			}
			usedValue += containers[r].value
			usedWeight += containers[r].weight
			count++
			result[i - 1] = r
		}
		return result.toList() // fuck type
	}

}