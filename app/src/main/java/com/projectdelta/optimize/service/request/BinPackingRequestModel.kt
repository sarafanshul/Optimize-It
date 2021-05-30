package com.projectdelta.optimize.service.request

import com.google.gson.annotations.SerializedName


data class BinPackingRequestModel (

	@SerializedName("weights") var weights : List<Long>,
	@SerializedName("values") var values : List<Long>,
	@SerializedName("bin_capacities") var binCapacities : List<Long>

)