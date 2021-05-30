package com.projectdelta.optimize.service.request

import com.google.gson.annotations.SerializedName

data class CVRPRequestModel (
	@SerializedName("vCount") var vCount : Int,
	@SerializedName("depot") var depot : List< List<Long> >,
	@SerializedName("locations") var locations : List< List<Long> >,
	@SerializedName("vCap") var vCap : List<Long>,
	@SerializedName("demands") var demands : List<Long>
)