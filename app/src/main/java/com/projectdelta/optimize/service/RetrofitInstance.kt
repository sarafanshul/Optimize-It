package com.projectdelta.optimize.service

import com.projectdelta.optimize.constant.APIConstant
import com.projectdelta.optimize.constant.MAX_TIME_OUT_SECONDS
import com.projectdelta.optimize.service.models.BinPackingApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

	private val newClient = OkHttpClient.Builder()
		.connectTimeout( MAX_TIME_OUT_SECONDS, TimeUnit.SECONDS)
		.readTimeout( MAX_TIME_OUT_SECONDS, TimeUnit.SECONDS)
		.writeTimeout( MAX_TIME_OUT_SECONDS, TimeUnit.SECONDS)
		.build()

	val apiTest : TestApi by lazy {
		Retrofit.Builder()
			.client( newClient )
			.baseUrl(APIConstant.BASE_API)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create( TestApi::class.java )
	}

	val apiBinPacking : BinPackingApi by lazy {
		Retrofit.Builder()
			.baseUrl(APIConstant.BASE_API)
			.client( newClient )
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create( BinPackingApi::class.java )
	}

//	val apiBinPacking : BinPackingApi by lazy {}

}