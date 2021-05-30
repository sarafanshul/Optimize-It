package com.projectdelta.optimize.service.response

import com.google.gson.annotations.SerializedName


data class CVRPResponse (

	@SerializedName("MaxDistance") var MaxDistance : Long,
	@SerializedName("ObjectiveValue") var ObjectiveValue : Long,
	@SerializedName("RouteDistance") var RouteDistance : Map<String , Long>,
	@SerializedName("VehicleRoute") var VehicleRoute : Map<String , List<Int>>

)