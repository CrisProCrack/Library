package com.example.library.ui.screens.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library.data.LibraryDatabase
import com.example.library.ui.screens.admin.AdminBooksViewModel
import com.example.library.ui.screens.bookdetail.BookDetailViewModel
import com.example.library.ui.screens.catalog.CatalogViewModel
import com.example.library.ui.screens.search.SearchViewModel

class ViewModelFactory(
    private val database: LibraryDatabase
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(database) as T
        }else if (modelClass.isAssignableFrom(AdminBooksViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AdminBooksViewModel(database) as T
        } else  if (modelClass.isAssignableFrom(CatalogViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(database) as T
        } else if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(database) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(database) as T
        }



        throw IllegalArgumentException("Unknown ViewModel class")
    }

}