package com.projectdelta.optimize.service.response

import com.google.gson.annotations.SerializedName


data class TestVRPResponseModel (

	@SerializedName("depot") var depot : String,
	@SerializedName("locations") var locations : String,
	@SerializedName("vCap") var vCap : String,
	@SerializedName("vCount") var vCount : String

)