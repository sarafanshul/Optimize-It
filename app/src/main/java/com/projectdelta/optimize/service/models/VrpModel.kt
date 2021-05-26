package com.projectdelta.optimize.service.models

import com.google.gson.annotations.SerializedName


data class VrpModel (

	@SerializedName("depot") var depot : String,
	@SerializedName("locations") var locations : String,
	@SerializedName("vCap") var vCap : String,
	@SerializedName("vCount") var vCount : String

)