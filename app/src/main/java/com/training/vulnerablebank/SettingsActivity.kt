package com.training.vulnerablebank

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.training.vulnerablebank.ui.theme.VulnerableBankAppTheme
import com.training.vulnerablebank.ui.components.ScreenHeader
import com.training.vulnerablebank.utils.PreferencesManager
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean

class SettingsActivity : BaseLocalizedActivity() {
    private val networkTestInProgress = AtomicBoolean(false)
    private val networkTestUrl = "https://yarygintech.com/labs/ping.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferencesManager = PreferencesManager(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VulnerableBankAppTheme {
                SettingsScreen(
                    onBackToDashboard = {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    },
                    onClearAllData = {
                        preferencesManager.resetToInitialState()
                        Toast.makeText(this, getString(R.string.clear_data_done), Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finishAffinity()
                    },
                    onNetworkConnectionTest = { runNetworkConnectionTest() },
                    onLogout = {
                        preferencesManager.clearSavedLogin()
                        Toast.makeText(this, getString(R.string.logged_out_message), Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finishAffinity()
                    },
                    selectedLanguageTag = preferencesManager.getSelectedLanguageTag(),
                    onLanguageSelected = { languageTag ->
                        preferencesManager.setSelectedLanguageTag(languageTag)
                        recreate()
                    }
                )
            }
        }
    }

    private fun runNetworkConnectionTest() { /* same as dashboard */
        if (!networkTestInProgress.compareAndSet(false, true)) {
            Toast.makeText(this, R.string.network_test_in_progress_message, Toast.LENGTH_SHORT).show(); return
        }
        Thread {
            val startTime = System.currentTimeMillis()
            try {
                val resultMessage = runCatching {
                    val connection = (URL(networkTestUrl).openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"; connectTimeout = 5000; readTimeout = 5000; setRequestProperty("Connection", "close")
                    }
                    try {
                        val responseCode = connection.responseCode
                        val stream = if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) connection.errorStream else connection.inputStream
                        stream?.use { val buffer = ByteArray(1024); while (it.read(buffer) != -1) {} }
                        val elapsed = System.currentTimeMillis() - startTime
                        if (responseCode in 200..299) getString(R.string.network_test_success_message, responseCode, elapsed)
                        else getString(R.string.network_test_failure_http_message, responseCode)
                    } finally { connection.disconnect() }
                }.getOrElse { ex -> getString(R.string.network_test_failure_message, ex.localizedMessage ?: ex.javaClass.simpleName) }
                runOnUiThread { Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show() }
            } finally { networkTestInProgress.set(false) }
        }.start()
    }
}

@Composable
private fun SettingsScreen(onBackToDashboard: () -> Unit, onClearAllData: () -> Unit, onNetworkConnectionTest: () -> Unit, onLogout: () -> Unit, selectedLanguageTag: String, onLanguageSelected: (String) -> Unit) {
    var activeLanguageTag by remember(selectedLanguageTag) { mutableStateOf(selectedLanguageTag) }
    Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ScreenHeader(
            title = stringResource(R.string.settings_button),
            showBackButton = true,
            onBackClick = onBackToDashboard,
            titleContentDescription = "settings_heading"
        )
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onClearAllData, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "clear_all_data_button" }) { Text(stringResource(R.string.clear_all_data_button)) }
                Button(onClick = onNetworkConnectionTest, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "network_connection_test_button" }) { Text(stringResource(R.string.network_connection_test_button)) }
                Button(onClick = onLogout, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "logout_button" }) { Text(stringResource(R.string.logout_button)) }
            }
        }
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(R.string.language_section_title), style = MaterialTheme.typography.titleMedium)
                Button(onClick = {
                    activeLanguageTag = "ru"
                    onLanguageSelected("ru")
                }, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "language_russian_button" }) { Text(stringResource(R.string.language_russian)) }
                Button(onClick = {
                    activeLanguageTag = "en"
                    onLanguageSelected("en")
                }, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "language_english_button" }) { Text(stringResource(R.string.language_english_default)) }
                Text(text = stringResource(R.string.language_current, activeLanguageTag.uppercase()))
            }
        }
    }
}
