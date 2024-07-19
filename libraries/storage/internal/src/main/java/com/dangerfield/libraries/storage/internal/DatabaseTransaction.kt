package com.dangerfield.libraries.storage.internal

import androidx.room.withTransaction
import com.dangerfield.libraries.storage.datastore.Transaction
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
class DatabaseTransaction @Inject constructor(
    private val db: AppDatabase
): Transaction {

    override suspend fun <T> invoke(block: suspend () -> T): T {
        return db.withTransaction(block)
    }
}