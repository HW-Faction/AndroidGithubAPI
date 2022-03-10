package app.githubapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import app.githubapi.R
import app.githubapi.repository.MainRepository
import app.githubapi.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    var toggle: ActionBarDrawerToggle? = null
    var toolbar: Toolbar? = null

    private val TAG = "MainActivity.kt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer)
        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle!!)
        toggle!!.syncState()

        nav_view.setNavigationItemSelectedListener {
            if (it.itemId == R.id.logout) {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "User has been logged out", Toast.LENGTH_SHORT).show()
                Intent(this, LoginActivity::class.java).apply {
                    startActivity(intent)
                }
            }
            true
        }

       nav_view.getHeaderView(0).findViewById<TextView>(R.id.name_nav_drawer).text = FirebaseAuth.getInstance().currentUser?.displayName



        ArrayAdapter.createFromResource(
            this,
            R.array.programming_languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        btnGetRepos.setOnClickListener {
           /* val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            viewModel.getTrendingRepos("java").observe(this, {
                for (model in it) {
                    Log.d(TAG, "repo name: " + model.name)
                }
            })*/

            progress_circular_login.visibility = View.VISIBLE
            btnGetRepos.visibility = View.INVISIBLE
            Thread.sleep(4000L)
            progress_circular_login.visibility = View.INVISIBLE
            btnGetRepos.visibility = View.VISIBLE
        }
    }
}