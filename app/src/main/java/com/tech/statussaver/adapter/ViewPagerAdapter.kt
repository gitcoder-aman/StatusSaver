package com.tech.statussaver.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tech.statussaver.fragment.ImageFragment
import com.tech.statussaver.fragment.VideoFragment

open class ViewPagerAdapter(private val fm : FragmentManager, private val lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle)
{
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return ImageFragment()
            }
            1 -> {
                return VideoFragment()
            }

        }
        return ImageFragment()
    }
}