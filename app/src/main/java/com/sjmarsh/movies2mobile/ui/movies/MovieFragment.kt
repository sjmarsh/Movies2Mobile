package com.sjmarsh.movies2mobile.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.sjmarsh.movies2mobile.MainActivity
import com.sjmarsh.movies2mobile.R
import com.sjmarsh.movies2mobile.data.IDataService
import com.sjmarsh.movies2mobile.data.MovieSortBy
import com.sjmarsh.movies2mobile.databinding.FragmentMoviesBinding
import com.sjmarsh.movies2mobile.ui.search.DebouncingQueryTextListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class MovieFragment : Fragment() {

    private val dataService: IDataService by inject()

    private var _searchView: SearchView? = null
    private var _categories: List<String>? = null
    private var _filterMenu: MenuItem? = null
    private var _initMovieId: Int? = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        _searchView = ((context as MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_movies, menu)
                menu.findItem(R.id.miSearch).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    actionView = _searchView
                }

                runBlocking {
                    coroutineScope {
                        val getCategoriesAsync = async(Dispatchers.IO) { dataService.getMovieCategories() }
                        withContext(Dispatchers.IO){
                            _categories = getCategoriesAsync.await()
                        }
                    }
                }
                _filterMenu = menu.findItem(R.id.miFilter)
                if(_filterMenu != null){
                    val groupId = 0
                    for(i in _categories!!.indices) {
                        val itemId = i + 100
                        _filterMenu?.subMenu?.add(groupId, itemId, i, _categories!![i])
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                if(_categories!!.contains(menuItem.title.toString())) {
                    val categoryFilter = menuItem.title.toString()
                    toggleFilterMenuIcon(true)
                    search(null, categoryFilter)
                    return true
                }

                if(menuItem.title == "Filter") {
                    val categoryFilter = null
                    toggleFilterMenuIcon(false)
                    search(null, categoryFilter)
                    return true
                }

                val sortBys = MovieSortBy.values().map { it.toString()  }  // TODO use this as the basis for the sub menus (instead of hardcode xml duplication)
                if(sortBys.contains(menuItem.titleCondensed.toString())) {
                    val movieSortBy = MovieSortBy.valueOf(menuItem.titleCondensed.toString())
                    search(null,null, movieSortBy)
                    return true
                }

                return when (menuItem.itemId) {
                    R.id.miSearch -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        this._initMovieId = arguments?.getInt("id", 0)

        _searchView?.setOnQueryTextListener(DebouncingQueryTextListener(lifecycle) { newText -> search(newText) })

        if(_initMovieId == null || _initMovieId == 0) {
            search("")
        }
    }

    private fun search(searchText: String?, categoryFilter: String? = null, movieSortBy: MovieSortBy? = null) : Boolean {
        if(_initMovieId != null && _initMovieId!! > 0){
            return searchById(_initMovieId)
        }
        val searchComponent = binding.moviesSearchComponent
        return searchComponent.search(searchText, categoryFilter, movieSortBy)
    }

    private fun searchById(id: Int?) : Boolean {
        val searchComponent = binding.moviesSearchComponent
        val hasResult =  searchComponent.searchById(id)
        _initMovieId = 0
        return hasResult
    }

    private fun toggleFilterMenuIcon(hasFilter: Boolean){
        val filledIcon = R.drawable.ic_filter_white_24dp
        val outlineIcon = R.drawable.ic_filter_outline_white_24dp

        val filterIcon = if(hasFilter) filledIcon else outlineIcon

        if(_filterMenu != null){
            _filterMenu?.setIcon(filterIcon)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _searchView?.setOnQueryTextListener(null)
        _binding = null
    }
}
