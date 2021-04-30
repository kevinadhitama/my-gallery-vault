package com.mygalleryvault.page

import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.mygalleryvault.R
import com.mygalleryvault.databinding.ActivityMainBinding
import com.mygalleryvault.page.factory.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //add security when user go back to home, preview last state will be hidden
        window.setFlags(
            FLAG_SECURE,
            FLAG_SECURE
        );

        viewModel =
            ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initToolbar()
        initDrawerNavigationView()
    }

    private fun initToolbar() {
        val toolbar: Toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)
        viewModel.toolbarTitle.observe(this, {
            toolbar.title = it
        })
    }

    private fun initDrawerNavigationView() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        val navView: NavigationView = binding.navView
        val fragmentContainer =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = fragmentContainer.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_albums, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        binding.pinWidget.activatePinWidget()
        binding.drawerLayout.closeDrawers()
        super.onPause()
    }
}
