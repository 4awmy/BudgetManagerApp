package com.example.mobile_development_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val dao: TransactionDao
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance


    fun loadAllTransactions() {
        viewModelScope.launch {
            dao.getAllTransactions().collectLatest { list ->
                _transactions.value = list
                updateCalculation(list)
            }
        }
    }

    fun loadMonthlyTransaction(month: String, year: String) {
        viewModelScope.launch {
            dao.getTransactionsByMonth(month, year).collectLatest { list ->
                _transactions.value = list
                updateCalculation(list)
            }
        }
    }

    fun addTransaction(
        title: String,
        amount: Double,
        type: String,
        date: Long
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                title = title,
                amount = amount,
                type = type,
                date = date
            )
            dao.insertTransaction(transaction)
        }
    }


    private fun updateCalculation(transactions: List<Transaction>) {
        val income = transactions.filter { it.type == "Income" }.sumOf { it.amount }
        val expense = transactions.filter { it.type == "Expense" }.sumOf { it.amount }

        _totalIncome.value = income
        _totalExpense.value = expense
        _balance.value = income - expense
    }

    private fun calculateTotalIncome(transactions: List<Transaction>): Double {
        return transactions
            .filter { it.type == "EXPENSE" }
            .sumOf { it.amount }
    }

    private fun calculateTotalExpense(transactions: List<Transaction>): Double {
        return transactions
            .filter { it.type == "INCOME" }
            .sumOf { it.amount }

    }
}