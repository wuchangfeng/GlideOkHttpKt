package com.itscoder.allenwu.glideokhttpkt

import android.util.Log
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


class ProgressResponseBody(url: String, responseBody: ResponseBody?) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null
    private var responseBody: ResponseBody? = responseBody
    private var listener: ProgressListener? = ProgressInterceptor.LISTENER_MAP[url]

    override fun contentType(): MediaType? {
        return responseBody!!.contentType()
    }

    override fun contentLength(): Long {
        return responseBody!!.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(ProgressSource(responseBody!!.source()))
        }
        return bufferedSource
    }

    inner class ProgressSource internal constructor(source: Source) : ForwardingSource(source) {
        private var totalBytesRead: Long = 0
        private var currentProgress: Int = 0
        @Throws(IOException::class)
        override  fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength = responseBody!!.contentLength()
            if (bytesRead.toInt() == -1) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }
            val progress = (100f * totalBytesRead / fullLength).toInt()
            Log.d(Consants.TAG, "download progress is $progress")
            if (listener != null && progress != currentProgress) {
                listener!!.onProgress(progress)
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null
            }
            currentProgress = progress
            return bytesRead
        }
    }

    class Consants{
        companion object {
            val TAG = "ProgressResponseBody"
        }
    }
}