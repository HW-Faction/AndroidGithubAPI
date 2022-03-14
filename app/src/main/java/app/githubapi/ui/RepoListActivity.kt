package app.githubapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.githubapi.R
import app.githubapi.adapter.RepoAdapter
import app.githubapi.model.RepoModel
import app.githubapi.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.imageView5
import kotlinx.android.synthetic.main.activity_repo_list.*

class RepoListActivity : AppCompatActivity() {

    private val TAG = "RepoListActivity.kt"
    private val list: MutableLiveData<ArrayList<RepoModel>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        imageView5.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }

        lang_tv.text = intent.getStringExtra("lang_name")

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getTrendingRepos(intent.getStringExtra("lang_name")).observe(this, {
            val tempList = ArrayList<RepoModel>()
            for (model in it) {
                //  list.value?.add(model)
                tempList.add(model)
                Log.d(TAG, "repo name: " + model.repositoryName)
            }

            list.value = tempList
            Log.d(TAG, "repo name: " + list.value!![0].username)

            progress_circular_repo.visibility = View.GONE
            recycler_view.apply {
                adapter = RepoAdapter(list.value!!)
                layoutManager = LinearLayoutManager(this@RepoListActivity)
                visibility = View.VISIBLE
            }
        })
    }
}