package com.dangerfield.libraries.storage.datastore

interface Transaction {

    suspend fun <T> invoke(block: suspend () -> T): T
}