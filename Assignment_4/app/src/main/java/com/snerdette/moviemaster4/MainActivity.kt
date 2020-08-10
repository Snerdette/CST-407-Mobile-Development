package com.snerdette.moviemaster4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.internal.BackgroundDetector.initialize
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var btnLogout: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Fragment(), " Movies ")
        adapter.addFragment(FragmentFavorites(), " Favorites ")

        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        initialize()
    }



    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun initialize() {
        btnLogout = findViewById<View>(R.id.btn_logout) as Button
        mAuth = FirebaseAuth.getInstance()
        btnLogout!!.setOnClickListener { logoutUser() }
    }

    private fun logoutUser() {
        mAuth!!.signOut()
        updateUI()
    }

    private fun updateUI() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }


    class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){

        private val fragmentList : MutableList<Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()
        private val movieList : MutableList<String> = ArrayList()
        private val favoritesList : MutableList<String> = ArrayList()


        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String){
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }
}