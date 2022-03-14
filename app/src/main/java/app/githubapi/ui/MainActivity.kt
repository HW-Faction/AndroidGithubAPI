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
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.descriptionText
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    var toolbar: Toolbar? = null

    private val TAG = "MainActivity.kt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer)
        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        // Create the AccountHeader
        val headerView = AccountHeaderView(this).apply {
            attachToSliderView(slider) // attach to the slider
            addProfiles(
                ProfileDrawerItem().apply {
                    nameText =
                        FirebaseAuth.getInstance().currentUser?.displayName.toString(); descriptionText =
                    FirebaseAuth.getInstance().currentUser?.email.toString(); iconRes =
                    R.drawable.ic_person; identifier = 102
                }
            )
            onAccountHeaderListener = { view, profile, current ->
                // react to profile changes
                Toast.makeText(this@MainActivity, "Acc", Toast.LENGTH_SHORT).show()

                false
            }
            withSavedInstance(savedInstanceState)
        }
        headerView.selectionListEnabledForSingleProfile = false

        val item1 = PrimaryDrawerItem().apply {
            nameRes = R.string.drawer_item_logout; identifier = 1; isEnabled = true; isSelectable =
            true
        }
        slider.itemAdapter.add(item1)
        // specify a click listener
        slider.onDrawerItemClickListener = { v, drawerItem, position ->
            if (drawerItem.identifier == 1L) {
                Intent(this, LoginActivity::class.java).apply {
                    Toast.makeText(this@MainActivity, "Logged Out", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(this)
                    finish()
                }
            }
            false
        }

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
            Intent(this, RepoListActivity::class.java).apply {
                this.putExtra("lang_name", spinner.selectedItem.toString())
                startActivity(this)
            }
        }
    }
}