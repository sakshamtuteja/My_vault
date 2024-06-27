package com.aryan.vault.Modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aryan.vault.Repositories.UserPdfRepo

class UserViewModel: ViewModel() {

    private val repository : UserPdfRepo
    private val _allUsers = MutableLiveData<List<UserId>>()
    val allUsers : LiveData<List<UserId>> = _allUsers


    init {

        repository = UserPdfRepo().getInstance()
        repository.loadUsers(_allUsers)

    }
}