package com.example.movies2mobile.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.databinding.FragmentMoviesBinding

class MovieFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private var _layoutManager: RecyclerView.LayoutManager? = null
    private var _adapter: RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val movieViewModel = MovieViewModel(DataService(this.requireContext().filesDir.path))

        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _layoutManager = LinearLayoutManager(this.context)
        binding.lstMovies.layoutManager = _layoutManager

        binding.btnSearch.setOnClickListener {
            movieViewModel.searchText = binding.txtSearch.text.toString()
            movieViewModel.searchMovies()

            _adapter = MovieRecyclerAdapter()
            binding.lstMovies.adapter = _adapter
            (_adapter as MovieRecyclerAdapter).setMovies(movieViewModel.movieList)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

