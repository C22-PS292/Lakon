package com.bangkit2022.capstone.lakon.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit2022.capstone.lakon.data.DataAdapter
import com.bangkit2022.capstone.lakon.R
import com.bangkit2022.capstone.lakon.data.Wayang
import com.bangkit2022.capstone.lakon.databinding.ActivityListBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListActivity : AppCompatActivity(), DataAdapter.OnItemClickCallback {
    private lateinit var binding: ActivityListBinding
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = " "

        setAdapter()
        fetchData()
        showLoading(true)
    }

    // Fetch data from api
    private fun fetchData(){
        db = FirebaseFirestore.getInstance()
        db.collection("lakon")
            .get()
            .addOnSuccessListener { doc ->
                for (document in doc){
                    val wayang = doc.toObjects(Wayang::class.java)
                    binding.rvWayang.adapter = DataAdapter( wayang, this)
                    showLoading(false)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Fetching Firestore Database Error", Toast.LENGTH_SHORT).show()
            }
    }

    // Set adapter for recycler view
    private fun setAdapter(){
        binding.rvWayang.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapter
            addItemDecoration( DividerItemDecoration
                (context, DividerItemDecoration.VERTICAL)
            )
        }
        binding.tvInfo.visibility = View.VISIBLE
    }

    // set condition for loading
    private fun showLoading(state: Boolean) {
        if (state){
            binding.rvWayang.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            binding.tvInfo.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvWayang.visibility = View.VISIBLE
            binding.tvInfo.visibility = View.GONE
        }
    }

    // Create options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    // Set action for options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_scan -> {
                startActivity(Intent(this, HomeActivity::class.java) )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Set condition when resuming app
    override fun onResume() {
        super.onResume()
        fetchData()
    }

    // Set app navigation
    override fun onBackPressed() {
        val moveIntentWithParcelable = Intent(this@ListActivity, MainActivity::class.java)
        startActivity(moveIntentWithParcelable)
    }

    // Set condition when item clicked
    override fun onItemClicked(data: Wayang) {
        val moveIntentWithParcelable = Intent(this@ListActivity, DetailActivity::class.java)
        moveIntentWithParcelable.putExtra(DetailActivity.EXTRA_WAYANG, data)
        startActivity(moveIntentWithParcelable)
    }
}