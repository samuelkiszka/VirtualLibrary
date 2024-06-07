package com.samuelkiszka.virtuallibrary.utils

import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import com.samuelkiszka.virtuallibrary.data.models.BooksWrapper
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

object BooksWrapperSerializer : KSerializer<BooksWrapper> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BooksWrapper")

    override fun serialize(encoder: Encoder, value: BooksWrapper) {
        val jsonEncoder = encoder as? kotlinx.serialization.json.JsonEncoder
            ?: throw IllegalStateException("This class can be saved only by Json")
        val jsonObject = JsonObject(value.books.map { (isbn, book) ->
            isbn to jsonEncoder.json.encodeToJsonElement(BookApiModel.serializer(), book)
        }.toMap())
        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): BooksWrapper {
        val jsonDecoder = decoder as? kotlinx.serialization.json.JsonDecoder
            ?: throw IllegalStateException("This class can be loaded only by Json")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        val books = jsonObject.map { (isbn, jsonElement) ->
            isbn to jsonDecoder.json.decodeFromJsonElement(BookApiModel.serializer(), jsonElement)
        }.toMap()
        return BooksWrapper(books)
    }
}
