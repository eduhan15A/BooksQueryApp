package com.example.android.booksqueryapp.api

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {
    //https://www.googleapis.com/books/v1/volumes?q=Android&maxResults=10&printType=books&projection=lite&key={YOUR_API_KEY}
    @GET("volumes?maxResults=10&printType=books&projection=lite")
    fun searchBooks(@Query("q") query: String): Observable<BooksModel.BooksResponse>

    companion object {
        fun create():BooksApiService{
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://www.googleapis.com/books/v1/")
                    .build()
            return retrofit.create(BooksApiService::class.java)
        }
    }

}