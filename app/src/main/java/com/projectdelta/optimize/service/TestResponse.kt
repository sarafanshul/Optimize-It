package com.projectdelta.optimize.service

import com.google.gson.annotations.SerializedName
import com.projectdelta.optimize.service.models.BinpackingModelTest
import com.projectdelta.optimize.service.models.CvrpModelTest
import com.projectdelta.optimize.service.models.VrpModelTest

data class TestResponse(
	@SerializedName("binpackingModel") var binpackingModelTest : BinpackingModelTest,
	@SerializedName("cvrpModel") var cvrpModelTest : CvrpModelTest,
	@SerializedName("vrpModel") var vrpModelTest : VrpModelTest
)
