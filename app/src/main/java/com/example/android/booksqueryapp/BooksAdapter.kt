package com.example.android.booksqueryapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.android.booksqueryapp.api.BooksModel
import kotlinx.android.synthetic.main.book_card_item.view.*

class BooksAdapter(booksList: List<BooksModel.BookItem>, listener: OnItemClickListener) : RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {

    private var listBooks: List<BooksModel.BookItem> = booksList

    private var listenerBook: OnItemClickListener = listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        return BooksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_card_item, parent, false))
    }


    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        var current: BooksModel.BookItem = listBooks[position]

        holder.bind(current, listenerBook)
    }

    override fun getItemCount() = listBooks.size

    interface OnItemClickListener {
        fun onItemClick(book: BooksModel.BookItem)
    }

    fun setBooks(books:List<BooksModel.BookItem>){
        listBooks = books
        notifyDataSetChanged()
    }

    class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: BooksModel.BookItem, listener: OnItemClickListener) {
            itemView.tvTitle.text = book.volumeInfo.title
            if(book.volumeInfo.authors != null)
                if(book.volumeInfo.authors.isNotEmpty())
            itemView.tvAuthor.text = book.volumeInfo.authors[0]
            Glide.with(itemView.context).load(book.volumeInfo.imageLinks.smallThumbnail).into(itemView.imgCover)

            itemView.setOnClickListener {
                listener.onItemClick(book)
            }
        }

    }
}