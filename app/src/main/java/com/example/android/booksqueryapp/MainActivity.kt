package com.example.android.booksqueryapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.android.booksqueryapp.R.id.rvBooks
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.android.booksqueryapp.api.BooksApiService
import com.example.android.booksqueryapp.api.BooksModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BooksAdapter.OnItemClickListener {
    override fun onItemClick(book: BooksModel.BookItem) {
    //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var viewAdapter:BooksAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var disposable: Disposable? = null
    private val booksApiService by lazy {
        BooksApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewManager = LinearLayoutManager(this)
        viewAdapter = BooksAdapter(arrayListOf(),this)

        rvBooks.apply {
            //setHasFixedSize(true)
            // Set the layout for the RecyclerView to be a linear layout, which measures and
            // positions items within a RecyclerView into a linear list
            layoutManager = viewManager

            // Initialize the adapter and attach it to the RecyclerView
            adapter = viewAdapter

            itemAnimator = DefaultItemAnimator()
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Do some magic
                //Toast.makeText(this@MainActivity,"Query:"+ query,Toast.LENGTH_LONG).show()
                if (!isOnline()){
                    Log.d("Error","No hay conexión con la red")
                } else {
                    searchBooks(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })

    }
    private fun isOnline():Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
    private fun searchBooks(query:String){
        //Ejecutar la llamada a nuestra api
        disposable = booksApiService.searchBooks(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result ->
                            //Mostrar la lista de libros regresados en la llamada
                            viewAdapter.setBooks(result.items)
                            Toast.makeText(this@MainActivity,"" + result.items.size +" libros recuperados",Toast.LENGTH_LONG).show()
                        },
                        {error ->
                            showAlert("Error",error.message.toString())
                        }
                )
    }

    private fun showAlert(title:String, message:String){
        AlertDialog.Builder(this@MainActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar",null)
                .create()

    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (search_view.isSearchOpen){
                search_view.closeSearch()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val item = menu.findItem(R.id.action_search)
        search_view.setMenuItem(item)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
