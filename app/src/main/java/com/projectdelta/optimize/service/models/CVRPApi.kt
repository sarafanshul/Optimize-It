package com.projectdelta.optimize.service.models

import com.projectdelta.optimize.constant.APIConstant
import com.projectdelta.optimize.service.request.CVRPRequestModel
import com.projectdelta.optimize.service.response.CVRPResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CVRPApi {

	@POST(APIConstant.CVRP_API)
	suspend fun post(@Body model: CVRPRequestModel) : Response<CVRPResponse>
}