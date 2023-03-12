package com.tech.statussaver

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.tech.statussaver.adapter.ViewPagerAdapter
import com.tech.statussaver.databinding.ActivityWhatsappBinding


val tabArray = arrayOf(
    "Images",
    "Videos",
)
class WhatsappActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatsappBinding
    private lateinit var viewPagerAdapter:ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhatsappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            this.finish()
            startActivity(Intent(this,MainActivity::class.java))
        }
        initView()

    }

    private fun initView() {
        viewPagerAdapter = ViewPagerAdapter(this.supportFragmentManager,lifecycle)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Images"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"))

        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.tabLayout,binding.viewPager,true){tab , index->
            tab.text = tabArray[index]
        }.attach()

//        for (i in 0 until binding.tabLayout.tabCount) {
//            val textView:TextView = LayoutInflater.from(this).inflate(R.layout.custom_tab,null) as TextView
//
//            binding.tabLayout.getTabAt(i)?.customView = textView
//        }
    }

    override fun onBackPressed() {
        this.finishAffinity()
        startActivity(Intent(this,MainActivity::class.java))
    }

}