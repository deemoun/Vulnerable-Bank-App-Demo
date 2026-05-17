package com.training.vulnerablebank

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.training.vulnerablebank.ui.theme.VulnerableBankAppTheme
import com.training.vulnerablebank.ui.components.ScreenHeader
import com.training.vulnerablebank.utils.PreferencesManager
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class DashboardActivity : BaseLocalizedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferencesManager = PreferencesManager(this)
        enableEdgeToEdge()
        setContent {
            VulnerableBankAppTheme {
                DashboardScreen(
                    username = preferencesManager.getStoredUsername().ifBlank { "guest" },
                    balance = preferencesManager.getStoredBalance(),
                    onViewTransactions = {
                        startActivity(Intent(this, TransactionsActivity::class.java))
                    },
                    onMakeTransfer = {
                        startActivity(Intent(this, TransferActivity::class.java))
                    },
                    onOpenSettings = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
private fun DashboardScreen(
    username: String,
    balance: Double,
    onViewTransactions: () -> Unit,
    onMakeTransfer: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()
    val currencyCode = if (locale.language == "ru") "RUB" else "USD"
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance(currencyCode)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.dashboard_heading),
                showBackButton = false,
                onBackClick = {},
                titleContentDescription = "dashboard_heading"
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.welcome_user, username),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(R.string.balance_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = currencyFormatter.format(balance),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(onClick = onViewTransactions, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "view_transactions_button" }) { Text(text = stringResource(R.string.view_transactions_button)) }
            Button(onClick = onMakeTransfer, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "make_transfer_button" }) { Text(text = stringResource(R.string.make_transfer_button)) }
        }

        FloatingActionButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .semantics { contentDescription = "settings_button" }
        ) {
            Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings_button))
        }
    }
}
