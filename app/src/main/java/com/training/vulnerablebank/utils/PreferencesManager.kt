package com.training.vulnerablebank.utils

import android.content.Context
import android.util.Log
import com.training.vulnerablebank.data.User
import org.json.JSONArray
import org.json.JSONObject

class PreferencesManager(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        Log.d(TAG, "Saving plaintext credentials username=${user.username} password=${user.password}")
        Log.d(TAG, "Saving auth token=${user.authToken}")
        preferences.edit()
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_PASSWORD, user.password)
            .putString(KEY_AUTH_TOKEN, user.authToken)
            .apply()
    }

    fun ensureInitialState() {
        if (preferences.contains(KEY_USER_BALANCES)) return
        val balances = JSONObject()
            .put("admin", 1000.0)
            .put("nick", 0.0)
            .put("lisa", 0.0)
            .put("john", 500.0)
        preferences.edit()
            .putString(KEY_USER_BALANCES, balances.toString())
            .putString(KEY_TRANSACTIONS, JSONArray().toString())
            .apply()
    }

    fun resetToInitialState() {
        preferences.edit().clear().apply()
        ensureInitialState()
    }

    fun getStoredUsername(): String = preferences.getString(KEY_USERNAME, "") ?: ""

    fun getStoredBalance(): Double = getBalanceForUser(getStoredUsername().ifBlank { "admin" })

    fun getBalanceForUser(username: String): Double = getBalances().optDouble(username, 0.0)

    fun userExists(username: String): Boolean = getBalances().has(username)

    fun clearSavedLogin() {
        preferences.edit()
            .remove(KEY_USERNAME)
            .remove(KEY_PASSWORD)
            .remove(KEY_AUTH_TOKEN)
            .apply()
    }

    fun getSelectedLanguageTag(): String = preferences.getString(KEY_SELECTED_LANGUAGE_TAG, DEFAULT_LANGUAGE_TAG) ?: DEFAULT_LANGUAGE_TAG

    fun setSelectedLanguageTag(languageTag: String) {
        preferences.edit().putString(KEY_SELECTED_LANGUAGE_TAG, languageTag).apply()
    }

    fun transferBetweenAccounts(sender: String, recipient: String, amount: Double): Pair<Double, Double>? {
        if (amount <= 0) return null
        val balances = getBalances()
        if (!balances.has(sender) || !balances.has(recipient)) return null
        val senderBalance = balances.optDouble(sender, 0.0)
        if (senderBalance < amount) return null

        balances.put(sender, senderBalance - amount)
        balances.put(recipient, balances.optDouble(recipient, 0.0) + amount)
        preferences.edit().putString(KEY_USER_BALANCES, balances.toString()).apply()
        return Pair(balances.optDouble(sender, 0.0), balances.optDouble(recipient, 0.0))
    }

    fun addTransactionEntry(title: String, date: String, amount: String) {
        val currentTransactions = getTransactions().toMutableList()
        currentTransactions.add(0, "$title|$date|$amount")
        preferences.edit()
            .putString(KEY_TRANSACTIONS, JSONArray(currentTransactions).toString())
            .apply()
    }

    fun getTransactions(): List<String> {
        val stored = preferences.getString(KEY_TRANSACTIONS, null) ?: return emptyList()
        return runCatching {
            val jsonArray = JSONArray(stored)
            List(jsonArray.length()) { index -> jsonArray.getString(index) }
        }.getOrDefault(emptyList())
    }

    private fun getBalances(): JSONObject {
        val stored = preferences.getString(KEY_USER_BALANCES, null)
        return runCatching { JSONObject(stored ?: "") }.getOrElse { JSONObject() }
    }

    companion object {
        private const val TAG = "PreferencesManager"
        private const val PREFS_NAME = "vuln_bank_prefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_BALANCES = "user_balances"
        private const val KEY_TRANSACTIONS = "transactions"
        private const val KEY_SELECTED_LANGUAGE_TAG = "selected_language_tag"
        private const val DEFAULT_LANGUAGE_TAG = "en"
    }
}
