package com.example.recipelist.data.sync

import android.util.Log
import com.example.recipelist.data.repository.FavoritesRepository
import com.example.recipelist.data.repository.OfflineFirstShoppingListRepository
import com.example.recipelist.data.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class SyncManager(
    private val settingsRepository: SettingsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val shoppingListRepository: OfflineFirstShoppingListRepository
) {
    suspend fun performInitialSync(newUid: String): Boolean {
        val lastUid = settingsRepository.lastLoggedInUid.first()
        Log.d("SyncManager", "A iniciar sincronização para o UID: $newUid. Último UID conhecido: $lastUid")

        return if (newUid != lastUid || lastUid == null) {
            Log.i("SyncManager", "Novo utilizador ou instalação nova detetada. Sincronizando: Firebase -> Local.")

            try {
                favoritesRepository.clearLocal()
                shoppingListRepository.clearLocal()

                Log.d("SyncManager", "A buscar favoritos remotos...")
                val remoteFavorites = favoritesRepository.fetchRemoteFavorites(newUid)
                Log.d("SyncManager", "Recebidos ${remoteFavorites.size} favoritos do Firebase.")

                Log.d("SyncManager", "A buscar lista de compras remota...")
                val remoteShoppingList = shoppingListRepository.fetchRemoteShoppingList(newUid)
                Log.d("SyncManager", "Recebidos ${remoteShoppingList.size} itens da lista de compras do Firebase.")

                favoritesRepository.overwriteLocal(remoteFavorites)
                shoppingListRepository.overwriteLocal(remoteShoppingList)

                settingsRepository.setLastLoggedInUid(newUid)
                Log.i("SyncManager", "Sincronização Firebase -> Local concluída com SUCESSO.")
                true
            } catch (e: Exception) {
                Log.e("SyncManager", "FALHA CRÍTICA durante a sincronização Firebase -> Local.", e)
                false
            }
        } else {
            Log.i("SyncManager", "Utilizador recorrente detetado. A sincronizar alterações pendentes...")
            favoritesRepository.syncAllPending()
            shoppingListRepository.syncAllPending()
            Log.i("SyncManager", "Sincronização de pendentes concluída.")
            true
        }
    }
}