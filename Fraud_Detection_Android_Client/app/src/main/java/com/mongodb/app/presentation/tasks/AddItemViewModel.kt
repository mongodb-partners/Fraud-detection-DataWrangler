package com.mongodb.app.presentation.tasks

import android.os.Bundle
import android.provider.Settings
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
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

sealed class AddItemEvent {
    class Info(val message: String) : AddItemEvent()
    class Error(val message: String, val throwable: Throwable) : AddItemEvent()
}

class AddItemViewModel(
    private val repository: SyncRepository
) : ViewModel() {

    private val _addItemPopupVisible: MutableState<Boolean> = mutableStateOf(false)
    val addItemPopupVisible: State<Boolean>
        get() = _addItemPopupVisible

    private val _cardNumber: MutableState<String> = mutableStateOf("")
    val cardNumber: State<String>
        get() = _cardNumber

    private val _cardType: MutableState<String> = mutableStateOf("")
    val cardType: State<String>
        get() = _cardType

    private val _emailDomain: MutableState<String> = mutableStateOf("")
    val emailDomain: State<String>
        get() = _emailDomain

    private val _phone: MutableState<String> = mutableStateOf("")
    val phone: State<String>
        get() = _phone

    private val _trxAmount: MutableState<String> = mutableStateOf("")
    val trxAmount: State<String>
        get() = _trxAmount

    private val _addItemEvent: MutableSharedFlow<AddItemEvent> = MutableSharedFlow()
    val addItemEvent: Flow<AddItemEvent>
        get() = _addItemEvent

    fun openAddTaskDialog() {
        _addItemPopupVisible.value = true
    }

    fun closeAddTaskDialog() {
        cleanUpAndClose()
    }

    fun updateCardNumber(cardNumber: String) {
        _cardNumber.value = cardNumber
    }

    fun updateAmount(cardType: String) {
        _trxAmount.value = cardType
    }

    fun updatePhone(cardType: String) {
        _phone.value = cardType
    }

    fun updateEmail(cardType: String) {
        _emailDomain.value = cardType
    }

    fun updateCardType(cardType: String) {
        _cardType.value = cardType
    }

    private fun generateId(): String {
        val uuid = UUID.randomUUID()
        val mostSignificantBits = uuid.mostSignificantBits and (-0x1L ushr 16)
        val leastSignificantBits = uuid.leastSignificantBits and (-0x1L ushr 16)
        return String.format("%08x-%04x-%04x-%04x-%012x", mostSignificantBits, leastSignificantBits.ushr(48), leastSignificantBits.ushr(32) and 0xFFFF, leastSignificantBits.ushr(16) and 0xFFFF, leastSignificantBits and 0xFFFF_FFFF_FFFFL)
    }

    fun getIPv4Address(): String? {
        val interfaces: List<NetworkInterface> = NetworkInterface.getNetworkInterfaces().toList()
        for (intf in interfaces) {
            val addrs = intf.inetAddresses.toList()
            for (addr in addrs) {
                if (!addr.isLinkLocalAddress && !addr.isLoopbackAddress && addr is InetAddress) {
                    val ip = addr.hostAddress
                    if (ip.contains(".") && !ip.contains(":")) {
                        return ip
                    }
                }
            }
        }
        return null
    }

    fun generateDeviceId(): String {
        val random = Random()
        val sb = StringBuilder()

        // Add the first digit
        sb.append(random.nextInt(8) + 1)
        // Add the remaining 11 digits
        repeat(11) {
            sb.append(random.nextInt(10))
        }
        return sb.toString()
    }


    private fun invokeFunction(): String {
        val id = generateId()
        val ipAddress = getIPv4Address()
        val deviceId = generateDeviceId()

        val input = "1,6,${id},${cardNumber.value},${cardType.value},${emailDomain.value},${ipAddress},${deviceId},${phone.value},45,${trxAmount.value}"
        println("Input values : $input")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://zigy2hzajqvpljq5rhwnz7nx4u0pzawj.lambda-url.us-east-1.on.aws/?input=$input")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body.string()
        println("Response : $responseBody")
        return responseBody
    }

    fun addTask() {
        CoroutineScope(Dispatchers.IO).launch {
            var fraud = invokeFunction()
            runCatching {
                repository.addCard(
                    cardNumber.value,
                    cardType.value,
                    phone.value,
                    emailDomain.value,
                    trxAmount.value,
                    fraud.toBoolean()
                )
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    if (fraud.toBoolean()) {
                        _addItemEvent.emit(AddItemEvent.Info("'${cardNumber.value}' added successfully, It's a fraud"))
                    }
                    else {
                        _addItemEvent.emit(AddItemEvent.Info("'${cardNumber.value}' added successfully, It's not a fraud"))
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    _addItemEvent.emit(AddItemEvent.Error("Fraud Detected", it))
                }
            }
            cleanUpAndClose()
        }
    }

    private fun cleanUpAndClose() {
        _cardNumber.value = ""
        _cardType.value = ""
        _phone.value = ""
        _trxAmount.value = ""
        _emailDomain.value = ""
        _addItemPopupVisible.value = false
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
