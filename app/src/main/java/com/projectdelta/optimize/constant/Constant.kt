package com.projectdelta.optimize.constant

const val DATABASE_NAME : String = "project-db"

const val BASE_API : String = "https://operation-tools.herokuapp.com"

const val COORDINATE_MULTIPLIER : Double = 1000000.0
const val MAX_TIME_OUT_SECONDS : Long = 100

const val BIN_PACKING = "Bin packing"
const val VRP = "Vehicle routing"
const val CVRP = "Constrained vehicle routing"

val OPS_TYPE = listOf(BIN_PACKING , VRP , CVRP)