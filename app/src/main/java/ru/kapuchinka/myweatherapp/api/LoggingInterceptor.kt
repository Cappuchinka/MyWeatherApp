package ru.kapuchinka.myweatherapp.api

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset


class LoggingInterceptor : Interceptor {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Логирование запроса
        val requestBody = request.body
        val requestLog =
            "Sending request to ${request.url}\n" +
                    "Method: ${request.method}\n" +
                    "Headers: ${request.headers}\n" +
                    "Request body: ${requestBody?.bodyToString()}"

        val response = chain.proceed(request)

        // Логирование ответа
        val responseBody = response.body
        val responseLog =
            "Received response for ${response.request.url}\n" +
                    "Code: ${response.code}\n" +
                    "Headers: ${response.headers}\n" +
                    "Response body: ${responseBody?.bodyToString()}"

        // Вывод логов в консоль
        println("Request:\n$requestLog")
        println("Response:\n$responseLog")

        return response
    }

    private fun okhttp3.RequestBody?.bodyToString(): String {
        return try {
            val buffer = Buffer()
            this?.writeTo(buffer)
            val charset = Charset.forName("UTF-8")
            buffer.readString(charset)
        } catch (e: IOException) {
            "Error reading request body"
        }
    }

    private fun okhttp3.ResponseBody?.bodyToString(): String {
        return try {
            val source = this?.source()
            source?.request(Long.MAX_VALUE)
            val buffer = source?.buffer
            val charset = Charset.forName("UTF-8")
            buffer?.clone()?.readString(charset) ?: "Empty response body"
        } catch (e: IOException) {
            "Error reading response body"
        }
    }
}