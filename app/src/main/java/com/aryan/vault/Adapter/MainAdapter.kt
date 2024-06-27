package com.aryan.vault.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aryan.vault.Fragments.Images
import com.aryan.vault.Fragments.Pdfs

internal class MainAdapter(var context: Context, fm:FragmentManager,var totalTabs: Int):FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when(position)
        {
            0 -> {Images()}
            1 -> {Pdfs()}
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs

    }
}