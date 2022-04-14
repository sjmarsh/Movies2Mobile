package com.example.movies2mobile.ui.concerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.databinding.FragmentConcertsBinding
import com.example.movies2mobile.ui.movies.MovieRecyclerAdapter

class ConcertFragment : Fragment() {

    private var _binding: FragmentConcertsBinding? = null
    private var _layoutManager: RecyclerView.LayoutManager? = null
    private var _adapter: RecyclerView.Adapter<ConcertRecyclerAdapter.ViewHolder>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val concertViewModel = ConcertViewModel(DataService(this.requireContext().filesDir.path))

        _binding = FragmentConcertsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _layoutManager = LinearLayoutManager(this.context)
        binding.lstConcerts.layoutManager = _layoutManager

        binding.btnSearch.setOnClickListener {
            concertViewModel.searchText = binding.txtSearch.text.toString()
            concertViewModel.searchConcerts()

            _adapter = ConcertRecyclerAdapter()
            binding.lstConcerts.adapter = _adapter
            (_adapter as ConcertRecyclerAdapter).setConcerts(concertViewModel.concertList)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}