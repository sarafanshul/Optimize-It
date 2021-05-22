package com.projectdelta.optimize.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project

data class ProjectWithContainers (
	@Embedded val project : Project ,
	@Relation (
		parentColumn = "projectName" ,
		entityColumn = "projectName"
	)
	val containers : List<Container>
)