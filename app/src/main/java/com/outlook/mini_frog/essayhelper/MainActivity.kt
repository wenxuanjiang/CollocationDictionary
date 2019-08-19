package com.outlook.mini_frog.essayhelper

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private val NAVIGATION_MARK_SEARCH = 0
        private val NAVIGATION_MARK_COLLECTION = 1
    }

    var navigationMark = NAVIGATION_MARK_SEARCH
    val searchFragment = SearchFragment()
    val collectionFragment = CollectFragment()

    init {
        searchFragment.setHasOptionsMenu(true)
        collectionFragment.setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initManagers()
        initFragments()
        initActionBar()
    }

    override fun onPause() {
        super.onPause()
        CollectionManager.render(this)
    }

    private fun initManagers() {
        WordManager.init(this)
        CollectionManager.init(this)
    }

    private fun initFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, searchFragment)
        transaction.commit()
    }

    private fun initActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val navigator: NavigationView = findViewById(R.id.navigation_view)
        navigator.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_search -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, searchFragment)
                transaction.commit()
                navigationMark = NAVIGATION_MARK_SEARCH
            }
            R.id.nav_collection -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, collectionFragment)
                transaction.commit()
                navigationMark = NAVIGATION_MARK_COLLECTION
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
