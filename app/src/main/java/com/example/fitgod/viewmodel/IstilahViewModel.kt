package com.example.fitgod.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitgod.data.remote.model.IstilahDto
import com.example.fitgod.data.repository.IstilahGymRepository
import com.example.fitgod.util.SessionManager
import com.example.fitgod.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IstilahViewModel(
    private val repository: IstilahGymRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // List istilah dari API
    private val _istilahList = MutableStateFlow<List<IstilahDto>>(emptyList())
    val istilahList: StateFlow<List<IstilahDto>> = _istilahList

    // Status operasi (tambah / edit / hapus / load)
    private val _operationState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val operationState: StateFlow<UiState<Unit>> = _operationState

    /**
     * Load data istilah dari API.
     * Dipanggil pertama kali di HomeScreen (LaunchedEffect),
     * dan juga setelah tambah/edit/hapus.
     */
    fun loadIstilah(search: String? = null) {
        val userId = sessionManager.getUserId()
        if (userId == -1) {
            _operationState.value = UiState.Error("User belum login")
            _istilahList.value = emptyList()
            return
        }

        viewModelScope.launch {
            _operationState.value = UiState.Loading
            val result = repository.getIstilahGym(userId, search)

            result
                .onSuccess { list ->
                    _istilahList.value = list
                    _operationState.value = UiState.Success(Unit)
                }
                .onFailure { e ->
                    _istilahList.value = emptyList()
                    _operationState.value =
                        UiState.Error(e.message ?: "Gagal memuat istilah")
                }
        }
    }

    fun addIstilah(nama: String, kategori: String, deskripsi: String) {
        val userId = sessionManager.getUserId()
        if (userId == -1) {
            _operationState.value = UiState.Error("User belum login")
            return
        }

        viewModelScope.launch {
            _operationState.value = UiState.Loading
            val result = repository.tambahIstilah(userId, nama, kategori, deskripsi)

            result
                .onSuccess {
                    // setelah tambah, reload list
                    loadIstilah()
                }
                .onFailure { e ->
                    _operationState.value =
                        UiState.Error(e.message ?: "Gagal menambah istilah")
                }
        }
    }

    fun updateIstilah(idIstilah: Int, nama: String, kategori: String, deskripsi: String) {
        viewModelScope.launch {
            _operationState.value = UiState.Loading
            val result = repository.updateIstilah(idIstilah, nama, kategori, deskripsi)

            result
                .onSuccess {
                    // setelah update, reload list
                    loadIstilah()
                }
                .onFailure { e ->
                    _operationState.value =
                        UiState.Error(e.message ?: "Gagal mengubah istilah")
                }
        }
    }

    fun deleteIstilah(idIstilah: Int) {
        viewModelScope.launch {
            _operationState.value = UiState.Loading
            val result = repository.hapusIstilah(idIstilah)

            result
                .onSuccess {
                    // setelah hapus, reload list
                    loadIstilah()
                }
                .onFailure { e ->
                    _operationState.value =
                        UiState.Error(e.message ?: "Gagal menghapus istilah")
                }
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(IstilahViewModel::class.java)) {
                        val repo = IstilahGymRepository()
                        val session = SessionManager(context)
                        @Suppress("UNCHECKED_CAST")
                        return IstilahViewModel(repo, session) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
