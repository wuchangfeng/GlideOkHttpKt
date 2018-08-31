package com.itscoder.allenwu.glideokhttpkt

import com.bumptech.glide.Priority
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import java.io.IOException
import java.io.InputStream
import com.bumptech.glide.util.ContentLengthInputStream
import okhttp3.Request

class OkHttpFetcher(clite: OkHttpClient,url: GlideUrl): DataFetcher<InputStream>{
    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null
    private var isCancelled: Boolean = false
    private var url: GlideUrl = url
    private var client: OkHttpClient = clite

    override fun cleanup() {
        try {
            if (stream != null) {
                stream?.close()
            }
            if (responseBody != null) {
                responseBody?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getId(): String {
        return url.getCacheKey()
    }

    override fun cancel() {
        isCancelled = true
    }

    override fun loadData(priority: Priority?): InputStream? {
        val requestBuilder = Request.Builder()
                .url(url.toStringUrl())
        for (headerEntry in url.headers.entries) {
            val key = headerEntry.key
            requestBuilder.addHeader(key, headerEntry.value)
        }
        requestBuilder.addHeader("httplib", "OkHttp")
        val request = requestBuilder.build()
        if (isCancelled) {
            return null
        }
        val response = client.newCall(request).execute()
        responseBody = response.body()
        if (!response.isSuccessful() || responseBody == null) {
            throw IOException("Request failed with code: " + response.code())
        }
        stream = ContentLengthInputStream.obtain(responseBody?.byteStream(),
                responseBody!!.contentLength())
        return stream
    }
}