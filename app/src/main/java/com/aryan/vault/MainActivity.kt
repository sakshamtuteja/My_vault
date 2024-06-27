package com.aryan.vault

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.aryan.vault.Adapter.MainAdapter
import com.aryan.vault.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFile: Uri? = null
   // private lateinit var mAction: FloatingActionButton
    //private lateinit var mUpload: FloatingActionButton
    //private lateinit var mProfile: FloatingActionButton
    private lateinit var uploadText: TextView
    private lateinit var profileText: TextView
    private var isAllFabsVisible: Boolean? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false
    private var isLocationPermissionGranted = false
    private val imageSelector =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            result.data?.data?.let {

                if (result.resultCode == RESULT_OK) {

                    currentFile = it
                    // val inputStream = this.contentResolver.openInputStream(it)
                    // inputStream?.readBytes()?.let {
                    //uploadFile(it)
                    val intent=Intent(this,UploadActivity::class.java)
                    intent.putExtra("UploadType","Image")
                    intent.putExtra("File",it.toString())
                    startActivity(intent)
                }
            }
        }
    private val PDFSelector =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            result.data?.data?.let {

                if (result.resultCode == RESULT_OK) {

                    currentFile = it
                    // val inputStream = this.contentResolver.openInputStream(it)
                    // inputStream?.readBytes()?.let {
                    //uploadFile(it)
                    val intent=Intent(this,UploadActivity::class.java)
                    intent.putExtra("UploadType","PDF")
                    intent.putExtra("File",it.toString())
                    startActivity(intent)
                    Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()

                }
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      /*  permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                isReadPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: isReadPermissionGranted
                isLocationPermissionGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION]
                    ?: isLocationPermissionGranted


            }
       requestPermission()*/




        // FAB button
        //mUpload = binding.floatingActionButton
        //mProfile = binding.profilefab

        // Also register the action name text, of all the FABs.
        //uploadText = findViewById(R.id.add_alarm_action_text)
        //profileText = findViewById(R.id.add_person_action_text)

        // Now set all the FABs and all the action name texts as GONE
        binding.floatingActionButton.visibility = View.GONE
        binding.profilefab.visibility = View.GONE
        binding.uploadText.visibility = View.GONE
        binding.profileText.visibility = View.GONE

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are invisible
        isAllFabsVisible = false

        binding.fab.setOnClickListener(View.OnClickListener {
            (if (!isAllFabsVisible!!) {
                // when isAllFabsVisible becomes true make all
                // the action name texts and FABs VISIBLE
                binding.floatingActionButton.show()
                binding.profilefab.show()
                binding.uploadText.visibility = View.VISIBLE
                binding.profileText.visibility = View.VISIBLE

                // make the boolean variable true as we
                // have set the sub FABs visibility to GONE
                true
            } else {
                // when isAllFabsVisible becomes true make
                // all the action name texts and FABs GONE.
                binding.floatingActionButton.hide()
                binding.profilefab.hide()
                binding.uploadText.visibility = View.GONE
                binding.profileText.visibility = View.GONE

                // make the boolean variable false as we
                // have set the sub FABs visibility to GONE
                false
            }).also { isAllFabsVisible = it }
        })


        binding.floatingActionButton.setOnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("UPLOAD TYPE")
            builder.setMessage("PLEASE CHOOSE UPLOAD TYPE")
            builder.apply {
                setPositiveButton("IMAGE") { dialog, id ->
                    intent.type="image/*"
                    imageSelector.launch(intent)
                }
                setNegativeButton("PDF") { dialog, id ->
                    intent.type="application/pdf"
                    PDFSelector.launch(intent)
                }
            }
            val dialog:AlertDialog =builder.create()
            dialog.show()
            //val intent=Intent(this,showUpload::class.java)
            //startActivity(Intent(this,ReadExternalData::class.java))

        }
        binding.profilefab.setOnClickListener {
            val intent = Intent(this, AboutAct::class.java)
            startActivity(intent)
        }

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        tabLayout.addTab(tabLayout.newTab().setText("Images"))
        tabLayout.addTab(tabLayout.newTab().setText("Pdfs"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL


        val adapter = MainAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun readTypeDialog(){

    }



    private fun requestPermission(){

        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED



        val permissionRequest : MutableList<String> = ArrayList()

        if (!isReadPermissionGranted){

            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        }

        if (!isLocationPermissionGranted){

            permissionRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)

        }


        if (permissionRequest.isNotEmpty()){

            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }


    }

