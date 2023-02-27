package com.sjmarsh.movies2mobile.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.sjmarsh.movies2mobile.data.Constants
import com.sjmarsh.movies2mobile.databinding.FragmentSettingsBinding
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnSelectMovieDataFile: Button = binding.btnSelectMovieDataFile
        btnSelectMovieDataFile.setOnClickListener {
            selectMovieFile.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                    "text/json", // .json
                    "text/plain" // .txt
                ))
            })
        }

        return root
    }

    private val selectMovieFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        handleSelectMovieFileResult(result)
    }

    private fun handleSelectMovieFileResult(result: ActivityResult) {
        val data = result.data
        val uri = data?.data

        try {
            val inputStream: InputStream = activity?.contentResolver?.openInputStream(uri!!)
                ?: throw Exception("The input stream was null")
            val size: Int = inputStream.available()
            val bytes = ByteArray(size)
            inputStream.read(bytes)
            inputStream.close()
            val jsonString = String(bytes, StandardCharsets.UTF_8)

            val targetFilePath = this.context?.filesDir?.path + "/${Constants.MOVIE_DATA_FILE}"
            File(targetFilePath).writeText(jsonString)

            Toast.makeText(this.context, "Movies data imported", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.localizedMessage?.let { Log.e("Select Movie Data", it) }
            Toast.makeText(this.context, "Fail to read or write file", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}