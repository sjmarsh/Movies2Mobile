package com.example.movies2mobile.ui.actors

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.movies2mobile.MainActivity
import com.example.movies2mobile.R
import com.example.movies2mobile.databinding.FragmentActorsBinding

class ActorFragment : Fragment() {
    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentActorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentActorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_actors, menu)

        val searchView = ((context as MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(it)
        }

        menu.findItem(R.id.miSearchActors).apply {
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
        var id = arguments?.getInt("id", 0)
        if(id != null && id > 0 && (query == null || query.isEmpty())){
            // id is passed as an argument when navigating from a detail dialog
            return searchById(id)
        }
        val searchComponent = binding.actorsSearchComponent
        return searchComponent.search(query)
    }

    private fun searchById(id: Int?) : Boolean {
        val searchComponent = binding.actorsSearchComponent
        return searchComponent.searchById(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}