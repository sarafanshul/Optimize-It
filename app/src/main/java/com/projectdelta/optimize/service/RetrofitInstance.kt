package com.projectdelta.optimize.service

import com.projectdelta.optimize.constant.APIConstant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

	val apiTest : TestApi by lazy {
		Retrofit.Builder()
			.baseUrl(APIConstant.BASE_API)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create( TestApi::class.java )
	}

//	val apiBinPacking : BinPackingApi by lazy {}

}