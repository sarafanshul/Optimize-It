package com.projectdelta.optimize.data.entities

import androidx.room.Entity
import java.io.Serializable

@Entity( primaryKeys = [ "containerName" , "projectName" ] )
data class Container(

	val containerName : String , // containerName + projectName ( for collision management )

	val projectName : String ,

	var value : Long ,
	var weight : Long ,
	var count : Int ,
	var latitude : Long = 0 , // implement with maps api
	var longitude : Long = 0

):Serializable
