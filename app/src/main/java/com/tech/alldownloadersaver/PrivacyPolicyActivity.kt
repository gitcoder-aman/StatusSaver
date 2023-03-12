package com.tech.alldownloadersaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.tech.alldownloadersaver.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.settings.setJavaScriptEnabled(true)
        binding.webView.loadUrl("file:///android_asset/privacy.html")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        this.finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return super.onOptionsItemSelected(item)
    }
}