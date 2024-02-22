package com.example.homework_25.presentation.bottom_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homework_25.databinding.FragmentBottomSheetDialogLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    interface LocationSearchListener {
        fun onLocationSearched(locationName: String)
    }

    private var _binding: FragmentBottomSheetDialogLayoutBinding? = null
    private val binding get() = _binding!!

    private var locationSearchListener: LocationSearchListener? = null

    fun setLocationSearchListener(listener: LocationSearchListener) {
        locationSearchListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomSheetDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTakeNewPicture.setOnClickListener {
            locationSearchListener?.onLocationSearched(binding.etSearch.text.toString())
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}