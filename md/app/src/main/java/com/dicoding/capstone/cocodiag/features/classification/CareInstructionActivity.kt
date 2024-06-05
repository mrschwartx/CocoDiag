package com.dicoding.capstone.cocodiag.features.classification

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.data.dummy.dummyCareInstructions
import com.dicoding.capstone.cocodiag.databinding.ActivityCareInstructionsBinding

// TODO: WILL REVOME
class CareInstructionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCareInstructionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCareInstructionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@CareInstructionActivity, binding.bottomNavigation, R.id.nav_camera)

        val rv: RecyclerView = binding.rvCareInstrctions
        rv.layoutManager = LinearLayoutManager(this)

        val adapter = CareInstructionAdapter(dummyCareInstructions)
        rv.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // TODO: move to classification result
        startActivity(Intent(this@CareInstructionActivity, CameraActivity::class.java))
        finish()
    }
}