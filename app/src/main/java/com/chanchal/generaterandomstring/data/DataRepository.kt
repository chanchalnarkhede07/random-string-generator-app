package com.chanchal.generaterandomstring.data

interface DataRepository {
    suspend fun fetchRandomString(length: Int): Result<RandomStringDataItem>
}