package com.samuelkiszka.virtuallibrary.data.models

import com.samuelkiszka.virtuallibrary.utils.BooksWrapperSerializer
import kotlinx.serialization.Serializable
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Data models for processing books retrieved by given ISBN
 */
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
    var cover: Cover = Cover()
) {
    fun getJsonUrlEncoded(): String {
        val data = """
            {
                "title":"$title",
                "author":"${authors[0].name}",
                "yearPublished":"$publish_date",
                "numberOfPages":$number_of_pages,
                "notes":"",
                "coverUrl":"${cover.medium}"
            }
        """
        return URLEncoder.encode(data, StandardCharsets.UTF_8.toString())
    }
}

@Serializable(with = BooksWrapperSerializer::class)
data class BooksWrapper (
    var books: Map<String, BookApiModel> = mapOf(),
)

/*
    * Data models for processing ISBNs retrieved by given query
 */
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