package com.samuelkiszka.virtuallibrary.data.models

import BooksWrapperSerializer
import kotlinx.serialization.Serializable

//
// Retrieve books by isbn
//
@Serializable
data class Authors (
    var url: String = "",
    var name: String = ""
)

@Serializable
data class Cover (
    var small: String = "",
    var medium: String = "",
    var large: String = ""
)

@Serializable
data class BookApiModel (
    var title: String = "",
    var authors: ArrayList<Authors> = arrayListOf(),
    var number_of_pages: Int = 0,
    var publish_date: String = "",
    var cover: Cover? = null
)

@Serializable(with = BooksWrapperSerializer::class)
data class BooksWrapper (
    var books: Map<String, BookApiModel> = mapOf(),
)

//
// Retrieve the ISBNs from the list of works
//
@Serializable
data class Edition (
    var isbn: ArrayList<String> = arrayListOf()
)

@Serializable
data class ListOfEditions (
    var docs: ArrayList<Edition> = arrayListOf()
)

@Serializable
data class Work (
    var editions: ListOfEditions = ListOfEditions()
)

@Serializable
data class ListOfWorks (
    var numFound: Int? = null,
    var docs: ArrayList<Work> = arrayListOf()
)