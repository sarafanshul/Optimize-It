package com.projectdelta.optimize.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker

data class ProjectWithWorkers(
	@Embedded val project : Project,
	@Relation(
		parentColumn = "projectName" ,
		entityColumn = "projectName"
	)
	val workers : List<Worker>
)
