package com.pahelukadam.pahelukadam.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pahelukadam.pahelukadam.databinding.FragmentPlaceholderBinding

class PlaceholderFragment : Fragment() {
    private var _binding: FragmentPlaceholderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaceholderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.placeholderText.text = arguments?.getString(ARG_TEXT) ?: "Coming soon"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TEXT = "text"
        fun new(text: String) = PlaceholderFragment().apply {
            arguments = Bundle().apply { putString(ARG_TEXT, text) }
        }
    }
}
