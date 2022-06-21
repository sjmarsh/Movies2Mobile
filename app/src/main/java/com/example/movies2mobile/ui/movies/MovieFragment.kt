package com.example.movies2mobile.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.movies2mobile.MainActivity
import com.example.movies2mobile.R
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.databinding.FragmentMoviesBinding

class MovieFragment : Fragment() {

    private var _query: String? = null
    private var _categoryFilter: String? = null
    private var _categories: List<String>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private var _dataService: DataService? = null
    private val dataService get() = _dataService!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataService = DataService(context?.filesDir?.path ?: "") // TODO use dependency injection
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_movies, menu)

        val searchView = ((context as MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(it)
        }

        menu.findItem(R.id.miSearch).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                _query = query
                return search()
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                _query = newText
                return search()
            }
        })

        _categories = dataService.getMovieCategories()

        val filterMenu = menu.findItem(R.id.miFilter)

        val groupId = 0
        for(i in _categories!!.indices) {
            val itemId = i + 100
            filterMenu.subMenu.add(groupId, itemId, i, _categories!![i])
        }
    }

    private fun search() : Boolean {

        val searchComponent = binding.moviesSearchComponent
        return searchComponent.search(_query, _categoryFilter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(_categories!!.contains(item.title.toString())){
            _categoryFilter = item.title.toString()
            search()
            return true
        }

        if(item.title == "Filter") {
            _categoryFilter = null
            search()
            return true
        }

        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _dataService = null
    }
}
