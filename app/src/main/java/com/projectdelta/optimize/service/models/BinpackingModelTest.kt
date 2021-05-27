package com.projectdelta.optimize.service.models

import com.google.gson.annotations.SerializedName

data class BinpackingModelTest (

	@SerializedName("bin_capacities") var binCapacities : String,
	@SerializedName("values") var values : String,
	@SerializedName("weights") var weights : String

)
