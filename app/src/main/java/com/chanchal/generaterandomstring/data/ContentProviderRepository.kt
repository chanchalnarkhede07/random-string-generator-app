package com.chanchal.generaterandomstring.data

import android.content.ContentResolver
import android.os.Bundle
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ContentProviderRepository(private val contentResolver: ContentResolver) :
    DataRepository {

    override suspend fun fetchRandomString(length: Int): Result<RandomStringDataItem> {
        return withContext(Dispatchers.IO) {
            try {
                // Used provided URI to query the content provider
                val uri = "content://com.iav.contestdataprovider/text".toUri()

                // Created a bundle object with the string length as a parameter
                val bundle = Bundle()
                bundle.putInt(ContentResolver.QUERY_ARG_LIMIT, length)

                // Query the content provider to get the data
                val cursor = contentResolver.query(uri, null, bundle, null)
                // Check if we got some data or checking null condition
                if (cursor != null && cursor.moveToFirst()) {
                    // Get the "data" column index
                    val dataIndex = cursor.getColumnIndex("data")
                    // Check if data column exists
                    if (dataIndex != -1) {
                        // Get the JSON string from the cursor
                        val jsonString = cursor.getString(dataIndex)
                        val jsonObject = JSONObject(jsonString)
                            .getJSONObject("randomText")
                        val value = jsonObject.getString("value")
                        val resultLength = jsonObject.getInt("length")
                        val created = jsonObject.getString("created")
                        cursor.close()
                        //Retrun success result
                        return@withContext Result.success(RandomStringDataItem(value, resultLength, created))
                    }
                }
                Result.failure(Exception("No data received from content provider"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}