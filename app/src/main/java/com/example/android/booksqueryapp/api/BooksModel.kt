package com.example.android.booksqueryapp.api

object BooksModel {

    data class BooksResponse(var kind:String, var totalItems:Int, var items:List<BookItem>)

    data class BookItem(var id:String,var volumeInfo:BookVolumeInfo )

    data class BookVolumeInfo(var title:String, var authors:List<String>, var imageLinks:BookImageLinks )

    data class BookImageLinks(var smallThumbnail:String, var thumbnail:String)
}