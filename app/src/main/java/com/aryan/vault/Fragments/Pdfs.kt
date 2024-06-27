package com.aryan.vault.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryan.vault.Adapter.ImageAdapter
import com.aryan.vault.Adapter.PdfAdapter
import com.aryan.vault.Modules.UserId
import com.aryan.vault.Modules.UserViewModel
import com.aryan.vault.R
import com.aryan.vault.databinding.FragmentImagesBinding
import com.aryan.vault.databinding.FragmentPdfsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class Pdfs : Fragment() {

    private var _binding: FragmentPdfsBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: PdfAdapter
    private lateinit var list: ArrayList<UserId>
    var databaseReference: DatabaseReference? = null
    var eventListener : ValueEventListener? = null
    var userEmail:String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPdfsBinding.inflate(inflater, container, false)


        val gridLayoutManager = GridLayoutManager(context, 1)
        binding.recyclerViewPdf.layoutManager = gridLayoutManager

        val builder = context?.let { AlertDialog.Builder(it) }

        builder!!.setCancelable(false)

        builder!!.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userEmail = user.email
        } else {
            // No user is signed in
        }
        val id= userEmail?.substring(0, userEmail!!.indexOf('@'))

        list = ArrayList()
        adapter = PdfAdapter(context, list)
        binding.recyclerViewPdf.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().reference
        val reference = databaseReference!!.child(id!!).child("PDF")
        dialog.show()
        eventListener = reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (itemSnapshot in snapshot.children) {
                    //val dataClass = itemSnapshot.getValue(UserId::class.java)
                    val name=itemSnapshot.child("fileName").value.toString()
                    val url=itemSnapshot.child("url").value.toString()
                    list.add(UserId(name,url))

                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })



        return binding.root


    }
    fun searchList(text: String) {
        val searchList = ArrayList<UserId>()
        //val dataClass = itemSnapshot.getValue(UserId::class.java)

        for (dataClass in list) {
            if (dataClass.fileName?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  userRecyclerView = view.findViewById(R.id.recyclerViewPdf)
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        userRecyclerView.setHasFixedSize(true)
        adapter = PdfAdapter()
        userRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        viewModel.allUsers.observe(viewLifecycleOwner, Observer {

            adapter.updateUserList(it)

        })*/

    }
}





