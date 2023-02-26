package com.sjmarsh.movies2mobile.ui.actors

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.sjmarsh.movies2mobile.R
import com.sjmarsh.movies2mobile.databinding.FragmentActorsBinding

class ActorFragment : Fragment() {

    private var _searchView: SearchView? = null
    private var _initActorId: Int? = 0

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

        _searchView = ((context as com.sjmarsh.movies2mobile.MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(it)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_actors, menu)

        _initActorId = arguments?.getInt("id", 0)

        menu.findItem(R.id.miSearchActors).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = _searchView
        }

        _searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return search(query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return search(newText)
            }
        })
    }

    private fun search(query: String?) : Boolean {
        if(_initActorId != null && _initActorId!! > 0){
            // id is passed as an argument when navigating from a detail dialog
            return searchById(_initActorId)
        }
        val searchComponent = binding.actorsSearchComponent
        return searchComponent.search(query)
    }

    private fun searchById(id: Int?) : Boolean {
        val searchComponent = binding.actorsSearchComponent
        val hasResult = searchComponent.searchById(id)
        _initActorId = 0
        return hasResult
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _searchView?.setOnQueryTextListener(null)
        _binding = null
    }
}