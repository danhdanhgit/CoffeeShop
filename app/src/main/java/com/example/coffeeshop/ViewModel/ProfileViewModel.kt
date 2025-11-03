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

    fun loadUserDetail(userId: Int) {
        compositeDisposable.add(
            repository.loadUserDetail(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success && response.result != null) {
                        _userDetail.postValue(response.result)
                    } else {
                        Log.w("ProfileViewModel", "loadUserDetail was not successful")
                        _userDetail.postValue(null)
                    }
                }, { error ->
                    Log.e("ProfileViewModel", "loadUserDetail error: ${error.message}")
                    _userDetail.postValue(null)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

