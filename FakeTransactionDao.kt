package com.example.mobile_development_project

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class FakeTransactionDao : TransactionDao {

    private val transactions = mutableListOf<Transaction>()
    private val transactionsFlow = MutableStateFlow<List<Transaction>>(emptyList())

    override suspend fun insertTransaction(transaction: Transaction) {
        transactions.add(transaction)
        transactionsFlow.value = transactions.sortedByDescending { it.date }
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionsFlow
    }

    override fun getTransactionsByMonth(month: String, year: String): Flow<List<Transaction>> {
        return transactionsFlow.map { transactionList ->
            transactionList.filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date
                val transactionMonth = (cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
                val transactionYear = cal.get(Calendar.YEAR).toString()
                transactionMonth == month && transactionYear == year
            }
        }
    }
}
