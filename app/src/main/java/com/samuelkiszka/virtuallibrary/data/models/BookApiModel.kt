package com.samuelkiszka.virtuallibrary.data.models

import kotlinx.serialization.Serializable

//@Serializable
//data class BookApiModel {
//}

@Serializable
data class ListOfIsbn (
    var isbn : ArrayList<String> = arrayListOf()
)

@Serializable
data class ListOfWorks (
    var numFound      : Int?                  = null,
    var start         : Int?                  = null,
    var numFoundExact : Boolean?              = null,
    var docs          : ArrayList<ListOfIsbn> = arrayListOf(),
    var q             : String?               = null,
    var offset        : String?               = null
)