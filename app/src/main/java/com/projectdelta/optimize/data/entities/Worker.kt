package com.projectdelta.optimize.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.projectdelta.optimize.util.Converters
import java.io.Serializable

@Entity( primaryKeys = [ "workerId" , "projectName" ] )
data class Worker(

	val workerId : Long ,// use time epoch for id

	val projectName : String ,

	var capacity : Long ,
	var maxDistance : Long = 1_000_000_000 ,

	@TypeConverters( Converters::class )
	var route : List<Int> = emptyList() ,

	@TypeConverters( Converters::class )
	var jobs : List<Int> = emptyList()

):Serializable
