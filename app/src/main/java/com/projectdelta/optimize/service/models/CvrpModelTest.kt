package com.projectdelta.optimize.service.models

import com.google.gson.annotations.SerializedName

data class CvrpModelTest (

	@SerializedName("damands") var damands : String,
	@SerializedName("depot") var depot : String,
	@SerializedName("locations") var locations : String,
	@SerializedName("vCap") var vCap : String,
	@SerializedName("vCount") var vCount : String

)