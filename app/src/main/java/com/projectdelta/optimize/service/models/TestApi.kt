package com.projectdelta.optimize.service.models

import com.projectdelta.optimize.constant.APIConstant
import com.projectdelta.optimize.service.response.TestResponse
import retrofit2.Response
import retrofit2.http.GET

interface TestApi {

	@GET(APIConstant.TEST_API)
	suspend fun getTest() : Response<TestResponse>

}