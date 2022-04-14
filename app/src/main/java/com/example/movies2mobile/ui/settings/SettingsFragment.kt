package com.example.movies2mobile.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movies2mobile.data.Constants
import com.example.movies2mobile.databinding.FragmentSettingsBinding
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class SettingsFragment : Fragment() {

    private val OPEN_FILE_REQUEST_CODE: Int = 101
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
                ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnSelectMovieDataFile: Button = binding.btnSelectMovieDataFile
        btnSelectMovieDataFile.setOnClickListener {
            startActivityForResult(
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                        "text/json", // .json
                        "text/plain" // .txt
                    ))
                },
                OPEN_FILE_REQUEST_CODE
            )
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == OPEN_FILE_REQUEST_CODE){
            var uri = data?.data

            try {
                val inputStream: InputStream? = activity?.contentResolver?.openInputStream(uri!!)
                if(inputStream == null){
                    throw Exception("The input stream was null")
                }
                val size: Int = inputStream?.available() ?: 0
                val bytes = ByteArray(size)
                inputStream.read(bytes)
                inputStream.close()
                val jsonString = String(bytes, StandardCharsets.UTF_8)

                val targetFilePath = this.context?.filesDir?.path + "/${Constants.MOVIE_DATA_FILE}"
                File(targetFilePath).writeText(jsonString)

                Toast.makeText(this.context, "Movies data imported", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: IOException) {
                Log.e("Select Movie Data", e.localizedMessage)
                Toast.makeText(this.context, "Fail to read or write file", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}