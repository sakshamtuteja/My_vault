package com.aryan.vault.Modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aryan.vault.Repositories.UserImgRepo

class UserViewModelImage: ViewModel() {

    private val repository : UserImgRepo
    private val _allUsers = MutableLiveData<List<UserId>>()
    val allUsers : LiveData<List<UserId>> = _allUsers


    init {

        repository = UserImgRepo().getInstance()
        repository.loadUsers(_allUsers)

    }
}