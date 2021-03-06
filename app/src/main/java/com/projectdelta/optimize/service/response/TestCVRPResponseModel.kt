package com.projectdelta.optimize.service.response

import com.google.gson.annotations.SerializedName

data class TestCVRPResponseModel (

	@SerializedName("damands") var damands : String,
	@SerializedName("depot") var depot : String,
	@SerializedName("locations") var locations : String,
	@SerializedName("vCap") var vCap : String,
	@SerializedName("vCount") var vCount : String

)