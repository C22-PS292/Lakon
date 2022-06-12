package com.bangkit2022.capstone.lakon.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.bangkit2022.capstone.lakon.R
import com.bangkit2022.capstone.lakon.R.string
import com.bangkit2022.capstone.lakon.databinding.ActivityHomeBinding
import com.bangkit2022.capstone.lakon.helper.rotateBitmap
import com.bangkit2022.capstone.lakon.helper.uriToFile
import com.bangkit2022.capstone.lakon.ml.WayangTypesMetadata
import org.tensorflow.lite.support.image.TensorImage
import java.io.File

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private var getFile: File? = null
    private var result: Bitmap? = null
    private var label: String? = null

    // Set result permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(string.invalid_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    // Set condition when permission granted
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = " "

        getPermission()
        //setMyButtonEnable()
        onClick()
        showLoading(false)
    }

    // Get permission to use camera
    private fun getPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // Set action for clickable button / text
    private fun onClick(){
        binding.imageButtonCam.setOnClickListener { startCameraX() }
        binding.imageButtonGallery.setOnClickListener { startGallery() }
        binding.nama.setOnClickListener{
            val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            val savedString = sharedPreferences.getString("KEY", null)

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=Wayang $savedString"))
            startActivity(intent)
        }
    }

    // Start Camera
    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(this, CameraActivity::class.java))
    }

    // Get data from camera and set it to image preview
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result =
                rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path),
                    isBackCamera
                )
        }
        binding.ivPreview.setImageBitmap(result)
        binding.btnScan.setOnClickListener { result?.let { it1 -> outputGenerator(it1) } }
    }

    // Start Gallery
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    // Get data from gallery and set it to image preview
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@HomeActivity)
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImg)

            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImg))
            binding.btnScan.setOnClickListener { outputGenerator(bitmap) }
        }
    }

    // scan and classified image
    private fun outputGenerator(bitmap: Bitmap){
        val wayangModel = WayangTypesMetadata.newInstance(this)

        // Creates inputs for reference.
        val new = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val image = TensorImage.fromBitmap(new)

        // Runs model inference and gets result.
        val outputs = wayangModel.process(image).probabilityAsCategoryList.apply {
            sortByDescending {
                it.score
            }
        }

        val highProblability = outputs[0]
        label = highProblability.label
        binding.nama.text = label
        val insertedText = label.toString()

        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("KEY", insertedText)
        }.apply()

        // Releases model resources if no longer used.
        wayangModel.close()
    }

    // Set app navigation
    override fun onBackPressed() {
        val moveIntentWithParcelable = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(moveIntentWithParcelable)
    }

    // Create options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Set action for options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                startActivity(Intent(this, ListActivity::class.java) )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // set condition for loading
    private fun showLoading(state: Boolean) {
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    // set companion data for camera
    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}