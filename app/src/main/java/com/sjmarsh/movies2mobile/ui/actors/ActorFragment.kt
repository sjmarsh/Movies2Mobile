package com.sjmarsh.movies2mobile.ui.actors

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.sjmarsh.movies2mobile.R
import com.sjmarsh.movies2mobile.databinding.FragmentActorsBinding
import com.sjmarsh.movies2mobile.ui.search.DebouncingQueryTextListener

class ActorFragment : Fragment() {

    private var _searchView: SearchView? = null
    private var _initActorId: Int? = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentActorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentActorsBinding.inflate(inflater, container, false)

        _searchView = ((context as com.sjmarsh.movies2mobile.MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_actors, menu)
                menu.findItem(R.id.miSearchActors).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    actionView = _searchView
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.miSearchActors -> {
                      true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _initActorId = arguments?.getInt("id", 0)
        _searchView?.setOnQueryTextListener(DebouncingQueryTextListener(lifecycle) { newText -> search(newText) })
        if(_initActorId == null || _initActorId == 0) {
            search("")
        }
    }

    private fun search(query: String?) : Boolean {
        if(_initActorId != null && _initActorId!! > 0) {
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