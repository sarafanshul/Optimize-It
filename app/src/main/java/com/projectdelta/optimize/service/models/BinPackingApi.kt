package com.projectdelta.optimize.service.models

import com.projectdelta.optimize.constant.APIConstant
import com.projectdelta.optimize.service.BinPackingResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BinPackingApi {

	// @Headers("Content-Type: application/json")
	@POST(APIConstant.BIN_PACKING_API)
	suspend fun post(@Body model: BinPackingRequestModel) : Response<BinPackingResponse>
}