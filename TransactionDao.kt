package com.example.mobile_development_project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("""
    SELECT * FROM `transaction`
    WHERE strftime('%m', date / 1000, 'unixepoch') = :month
    AND strftime('%Y', date / 1000, 'unixepoch') = :year
""")
    fun getTransactionsByMonth(
        month: String,
        year: String
    ): Flow<List<Transaction>>
}
