package com.projectdelta.optimize.service.response

import com.google.gson.annotations.SerializedName

data class TestBinpackingResponseModel (

	@SerializedName("bin_capacities") var binCapacities : String,
	@SerializedName("values") var values : String,
	@SerializedName("weights") var weights : String

)
