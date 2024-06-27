package com.aryan.vault.Repositories

import androidx.lifecycle.MutableLiveData
import com.aryan.vault.Modules.UserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserImgRepo{
    var userEmail: String? = null
    fun getEmail()
    {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userEmail = user.email
        } else {
            // No user is signed in
        }
    }

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("$userEmail/IMAGES")

    @Volatile private var INSTANCE : UserImgRepo ?= null

    fun getInstance() : UserImgRepo{
        return INSTANCE ?: synchronized(this){

            val instance = UserImgRepo()
            INSTANCE = instance
            instance
        }


    }
    fun loadUsers(userList : MutableLiveData<List<UserId>>){

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val _userList : List<UserId> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(UserId::class.java)!!

                    }

                    userList.postValue(_userList)

                }catch (e : Exception){


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }

}