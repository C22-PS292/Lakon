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
import com.bangkit2022.capstone.lakon.R
import com.bangkit2022.capstone.lakon.data.DataAdapter
import com.bangkit2022.capstone.lakon.data.Wayang
import com.bangkit2022.capstone.lakon.databinding.ActivityListFromDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListFromDetailActivity : AppCompatActivity(), DataAdapter.OnItemClickCallback {
    private lateinit var binding: ActivityListFromDetailBinding
    private lateinit var db : FirebaseFirestore
    private var data : String = "Bima"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFromDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = " "

        data = intent.getStringExtra("nama").toString()
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        setAdapter()
        fetchData(data)
        showLoading(true)
    }

    private fun fetchData(data: String){

        db = FirebaseFirestore.getInstance()
        db.collection("lakon")
            .whereEqualTo("nama", data)
            .get()
            .addOnSuccessListener { doc ->
                for (document in doc){
                    val wayang = doc.toObjects(Wayang::class.java)
                    binding.rvWayang.adapter = DataAdapter(wayang, this)
                    showLoading(false)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Fetching Firestore Database Error", Toast.LENGTH_SHORT).show()
            }
    }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_scan -> {
                startActivity(Intent(this, HomeActivity::class.java) )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*override fun onResume(data: String) {
        super.onResume()
        fetchData(data)
    }*/

    override fun onBackPressed() {
        val moveIntentWithParcelable = Intent(this@ListFromDetailActivity, MainActivity::class.java)
        startActivity(moveIntentWithParcelable)
    }

    override fun onItemClicked(data: Wayang) {
        val moveIntentWithParcelable = Intent(this@ListFromDetailActivity, DetailActivity::class.java)
        moveIntentWithParcelable.putExtra(DetailActivity.EXTRA_WAYANG, data)
        startActivity(moveIntentWithParcelable)
    }
}