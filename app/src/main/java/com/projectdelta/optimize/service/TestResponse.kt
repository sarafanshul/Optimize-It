package com.projectdelta.optimize.service

import com.google.gson.annotations.SerializedName
import com.projectdelta.optimize.service.models.BinpackingModel
import com.projectdelta.optimize.service.models.CvrpModel
import com.projectdelta.optimize.service.models.VrpModel

data class TestResponse(
	@SerializedName("binpackingModel") var binpackingModel : BinpackingModel,
	@SerializedName("cvrpModel") var cvrpModel : CvrpModel,
	@SerializedName("vrpModel") var vrpModel : VrpModel
)
