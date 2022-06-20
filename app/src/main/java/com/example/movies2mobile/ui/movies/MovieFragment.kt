package com.example.movies2mobile.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.movies2mobile.MainActivity
import com.example.movies2mobile.R
import com.example.movies2mobile.databinding.FragmentMoviesBinding
import com.example.movies2mobile.ui.shared.SearchContext

class MovieFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater?.inflate(R.menu.menu_movies, menu)

        val searchView = ((context as MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(it)
        }

        menu.findItem(R.id.miSearch).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return search(query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return search(newText)
            }
        })
    }

    private fun search(query: String?) : Boolean {

        val searchComponent = _binding?.moviesSearchComponent
        if (query != null) {
            return searchComponent?.search(query)!!
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
