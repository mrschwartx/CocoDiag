package com.dicoding.capstone.cocodiag.features.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.databinding.FragmentSplash1Binding

class SplashFragment1 : Fragment() {
    private lateinit var binding: FragmentSplash1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSplash1Binding.inflate(inflater,container,false)
        return inflater.inflate(R.layout.fragment_splash1, container, false)
    }
}
