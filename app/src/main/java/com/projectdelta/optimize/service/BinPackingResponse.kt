package com.projectdelta.optimize.service

import com.google.gson.annotations.SerializedName



data class BinPackingResponse (

	@SerializedName("bins") var bins : Map<String ,List<Int> >,
	@SerializedName("totalValuePacked") var totalValuePacked : Int,
	@SerializedName("totalWeightPacked") var totalWeightPacked : Int

)