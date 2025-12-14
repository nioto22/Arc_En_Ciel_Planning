package com.aprouxdev.arcencielplanning.data.services.http

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


/**
 * Make a GET request and return a parsed object from JSON.
 *
 * @param url URL of the request to make
 * @param clazz Relevant class object, for Gson's reflection
 * @param headers Map of request headers
 */
class GsonRequest<T>(
    url: String,
    private val clazz: Class<T>,
    private val headers: MutableMap<String, String>?,
    private val listener: Response.Listener<T>,
    errorListener: Response.ErrorListener
) : Request<T>(Method.GET, url, errorListener) {
    private val gson = Gson()

    override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()

    override fun deliverResponse(response: T) = listener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                gson.fromJson(json, clazz),
                HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}

/*
    val textView = findViewById<TextView>(R.id.text)
// ...

// Instantiate the RequestQueue.
    val queue = Volley.newRequestQueue(this)
    val url = "https://www.google.com"

// Request a string response from the provided URL.
    val stringRequest = StringRequest(Request.Method.GET, url,
        Response.Listener<String> { response ->
            // Display the first 500 characters of the response string.
            textView.text = "Response is: ${response.substring(0, 500)}"
        },
        Response.ErrorListener { textView.text = "That didn't work!" })

// Add the request to the RequestQueue.
    queue.add(stringRequest)
}
*/

/*
fun standardRequestExample() {
    val url = "http://my-json-feed"

    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
        Response.Listener { response ->
            textView.text = "Response: %s".format(response.toString())
        },
        Response.ErrorListener { error ->
            // TODO: Handle error
        }
    )

// Access the RequestQueue through your singleton class.
    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
}

 */
