package com.projectdelta.optimize.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Project (
	@PrimaryKey(autoGenerate = false)
	val projectName : String ,

	var numWorkers : Int = 0

):Serializable