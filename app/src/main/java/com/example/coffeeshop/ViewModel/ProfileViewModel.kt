package com.example.coffeeshop.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Data.Entity.UserDetail
import com.example.coffeeshop.Repository.MainRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MainRepository(application)
    private val compositeDisposable = CompositeDisposable()

    private val _userDetail = MutableLiveData<UserDetail?>()
    val userDetail: LiveData<UserDetail?> = _userDetail

    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

    fun loadUserDetail(userId: Int) {
        compositeDisposable.add(
            repository.loadUserDetail(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.success) {
                        _userDetail.postValue(it.result?.firstOrNull())
                    } else {
                        _userDetail.postValue(null)
                    }
                }, {
                    Log.e("ProfileViewModel", "loadUserDetail error", it)
                    _userDetail.postValue(null)
                })
        )
    }

    fun updateProfile(userId: Int, username: String, phone: String) {
        compositeDisposable.add(
            repository.updateProfile(userId, username, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Log.d("ProfileViewModel", "updateProfile response: success=${response.success}, message=${response.message}")
                    _updateResult.postValue(response.success)
                    if(response.success){
                        // After a successful update, reload the user details to get fresh data
                        loadUserDetail(userId)
                    }
                }, { error ->
                    Log.e("ProfileViewModel", "updateProfile error: ${error.message}", error)
                    _updateResult.postValue(false)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
