package com.mongodb.app.presentation.tasks

import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.mongodb.app.data.SyncRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import aws.sdk.kotlin.services.lambda.LambdaClient
import aws.sdk.kotlin.services.lambda.model.InvokeRequest
import aws.sdk.kotlin.services.lambda.model.LogType
import aws.smithy.kotlin.runtime.http.engine.callContext
import com.google.android.material.snackbar.Snackbar
import com.mongodb.app.TemplateApp
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

sealed class ItemAddMessageEvent {
    class Info(val message: String) : ItemAddMessageEvent()
    class Error(val message: String, val throwable: Throwable) : ItemAddMessageEvent()
}

class ItemAddMessageViewModel(
    private val repository: SyncRepository
) : ViewModel() {

    private val _trxMessagePopupVisible: MutableState<Boolean> = mutableStateOf(false)
    val addItemPopupVisible: State<Boolean>
        get() = _trxMessagePopupVisible

    private val _trxMessage: MutableState<String> = mutableStateOf("Transaction Passed")
    val trxMessage: State<String>
        get() = _trxMessage

    fun openMessageDialog() {
        _trxMessagePopupVisible.value = true
    }

    fun updateTrxMessage(message: String) {
        _trxMessage.value = message
    }

    fun closeMessageDialog() {
        _trxMessagePopupVisible.value = false
    }

    companion object {
        fun factory(
            repository: SyncRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): AbstractSavedStateViewModelFactory {
            return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return AddItemViewModel(repository) as T
                }
            }
        }
    }
}
