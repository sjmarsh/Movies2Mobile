package com.example.movies2mobile.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.movies2mobile.MainActivity
import com.example.movies2mobile.R
import com.example.movies2mobile.data.IDataService
import com.example.movies2mobile.data.MovieSortBy
import com.example.movies2mobile.databinding.FragmentMoviesBinding
import org.koin.android.ext.android.inject

class MovieFragment : Fragment() {

    private val dataService: IDataService by inject()

    private var _searchView: SearchView? = null
    private var _searchText: String? = null
    private var _categoryFilter: String? = null
    private var _movieSortBy: MovieSortBy? = null
    private var _categories: List<String>? = null
    private var _filterMenu: MenuItem? = null
    private var _initMovieId: Int? = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        _searchView = ((context as MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(it)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_movies, menu)

        _initMovieId = arguments?.getInt("id", 0)

        menu.findItem(R.id.miSearch).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = _searchView
        }

        _searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                _searchText = query
                return search()
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                _searchText = newText
                return search()
            }
        })

        _categories = dataService.getMovieCategories()

        _filterMenu = menu.findItem(R.id.miFilter)

        if(_filterMenu != null){
            val groupId = 0
            for(i in _categories!!.indices) {
                val itemId = i + 100
                _filterMenu?.subMenu?.add(groupId, itemId, i, _categories!![i])
            }
        }

    }

    private fun search() : Boolean {
        if(_initMovieId != null && _initMovieId!! > 0){
            return searchById(_initMovieId)
        }
        val searchComponent = binding.moviesSearchComponent
        return searchComponent.search(_searchText, _categoryFilter, _movieSortBy)
    }

    private fun searchById(id: Int?) : Boolean {
        val searchComponent = binding.moviesSearchComponent
        val hasResult =  searchComponent.searchById(id)
        _initMovieId = 0
        return hasResult
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(_categories!!.contains(item.title.toString())) {
            _categoryFilter = item.title.toString()
            toggleFilterMenuIcon(true)
            search()
            return true
        }

        if(item.title == "Filter") {
            _categoryFilter = null
            toggleFilterMenuIcon(false)
            search()
            return true
        }

        val sortBys = MovieSortBy.values().map { it.toString()  }  // TODO use this as the basis for the sub menus (instead of hardcode xml duplication)
        if(sortBys.contains(item.titleCondensed.toString())) {
            _movieSortBy = MovieSortBy.valueOf(item.titleCondensed.toString())
            search()
            return true
        }

        return false
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
