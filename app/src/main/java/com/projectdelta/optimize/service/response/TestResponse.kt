package com.projectdelta.optimize.service.response

import com.google.gson.annotations.SerializedName

data class TestResponse(
	@SerializedName("binpackingModel") var testBinpackingResponseModel : TestBinpackingResponseModel,
	@SerializedName("cvrpModel") var testCVRPResponseModel : TestCVRPResponseModel,
	@SerializedName("vrpModel") var testVRPResponseModel : TestVRPResponseModel
)
