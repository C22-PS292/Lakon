package com.bangkit2022.capstone.lakon.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bangkit2022.capstone.lakon.R
import com.bangkit2022.capstone.lakon.data.Wayang
import com.bangkit2022.capstone.lakon.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = " "

        getData()
    }

    // Get data from List and set it to view
    private fun getData(){
        val dataWayang = intent.getParcelableExtra<Wayang>(EXTRA_WAYANG)

        if(dataWayang != null){
            Glide.with(this)
                .load(dataWayang.avatar)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.nopp)
                )
                .into(binding.ivPreview)

            binding.namaWayang.text = dataWayang.nama
            binding.deskripsi.text = dataWayang.deskripsi

            binding.hyperlink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataWayang.referensi))
                startActivity(intent)
            }
        }
    }

    // Create options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    // Set action for options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                startActivity(Intent(this, ListActivity::class.java) )
                true
            }
            R.id.action_scan -> {
                startActivity(Intent(this, HomeActivity::class.java) )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_WAYANG = "extra_wayang"
    }
}