package com.example.coffeeshop.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Data.Entity.Order
import com.example.coffeeshop.Repository.MainRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MainRepository(application)
    private val compositeDisposable = CompositeDisposable()

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    fun loadOrders(userId: Int) {
        compositeDisposable.add(
            repository.loadOrders(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) {
                        _orders.postValue(response.result ?: emptyList())
                    } else {
                        Log.w("OrderViewModel", "loadOrders was not successful: ${response.message}")
                        _orders.postValue(emptyList())
                    }
                }, { error ->
                    Log.e("OrderViewModel", "loadOrders error: ${error.message}")
                    _orders.postValue(emptyList())
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}


