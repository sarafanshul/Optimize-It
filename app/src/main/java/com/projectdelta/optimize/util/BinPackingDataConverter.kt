package com.projectdelta.optimize.util

import android.os.Parcelable
import android.util.Log
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.service.response.BinPackingResponse
import com.projectdelta.optimize.service.request.BinPackingRequestModel
import kotlinx.parcelize.Parcelize

@Parcelize
class BinPackingDataConverter : Parcelable {

	lateinit var containers : List<Container>
	lateinit var workers : List<Worker>
	lateinit var responseModel : BinPackingResponse

	var prefixContainerIdx = mutableListOf<Int>()

	var totalValue = 0L
	var totalWeight = 0L

	fun set( _containers : List<Container> , _workers : List<Worker> ){
		containers = _containers
		workers = _workers
	}


	fun set( model: BinPackingResponse){
		responseModel = model
	}

	fun fromRawToRequest() : BinPackingRequestModel {
		if( ! this::containers.isInitialized || ! this::workers.isInitialized )
			return BinPackingRequestModel(emptyList() ,emptyList() ,emptyList())

		totalValue = 0
		totalWeight = 0
		val bin_capacities = List( workers.size) { i -> workers[i].capacity }

		prefixContainerIdx = MutableList( containers.size) { i -> containers[i].count }

		for( i in 1 until prefixContainerIdx.size )
			prefixContainerIdx[ i ] += prefixContainerIdx[ i - 1 ]

		val v = mutableListOf<Long>() ; val w = mutableListOf<Long>()
		containers.forEach { container ->
			v.addAll( MutableList(container.count){ container.value } )
			w.addAll( MutableList(container.count){ container.weight } )
			totalValue += container.count * container.value
			totalWeight += container.count * container.weight
		}
		return BinPackingRequestModel( w.toList() , v.toList(), bin_capacities )
	}

	fun fromResponseToRaw(){
		if( ! this::containers.isInitialized ||
			! this::workers.isInitialized ||
			! this::responseModel.isInitialized ) return

		for( i in responseModel.bins ){
			workers[i.key.trim().toInt()].jobs = getContainer( i.value )
		}
	}

	private fun getContainer( data : List<Int> ) : List<Int>{
		// binary search for container idx
		val result = Array( data.size ){ 0 }
		for( i in data.indices ) {
			var l = -1 ; var r = containers.size ; var m = l
			while (l + 1 < r) {
				m = (l + r) / 2
				if (prefixContainerIdx[m] >= data[i] )
					r = m
				else
					l = m
			}
			result[i] = r
		}
		return result.toList() // fuck type
	}
}