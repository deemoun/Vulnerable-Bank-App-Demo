package com.training.vulnerablebank

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.training.vulnerablebank.ui.theme.VulnerableBankAppTheme
import com.training.vulnerablebank.ui.components.ScreenHeader
import com.training.vulnerablebank.utils.PreferencesManager
import java.time.LocalDate

class TransferActivity : BaseLocalizedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("VulnBankLab", "Transfer activity opened without authentication checks")
        val preferencesManager = PreferencesManager(this)

        setContent {
            VulnerableBankAppTheme {
                TransferScreen(
                    onBackToDashboard = {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    },
                    onSubmit = { recipient, amount ->
                        Log.d("VulnBankLab", "Transfer requested to=$recipient amount=$amount")
                        submitTransfer(preferencesManager, recipient, amount)
                    }
                )
            }
        }
    }

    private fun submitTransfer(preferencesManager: PreferencesManager, recipient: String, amount: String): String {
        val parsedAmount = amount.toDoubleOrNull()
        val sender = preferencesManager.getStoredUsername().ifBlank { "admin" }

        val resultMessage = if (parsedAmount == null) {
            getString(R.string.recipient_not_found_message)
        } else if (!preferencesManager.userExists(recipient)) {
            getString(R.string.recipient_not_found_message)
        } else if (preferencesManager.getBalanceForUser(sender) < parsedAmount) {
            getString(R.string.insufficient_balance_message)
        } else {
            val transferResult = preferencesManager.transferBetweenAccounts(sender, recipient, parsedAmount)
            if (transferResult == null) {
                getString(R.string.recipient_not_found_message)
            } else {
                preferencesManager.addTransactionEntry(
                    title = "Transfer from $sender to $recipient",
                    date = LocalDate.now().toString(),
                    amount = "-$${"%.2f".format(parsedAmount)}"
                )
                getString(R.string.transfer_completed_message)
            }
        }

        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show()
        return resultMessage
    }
}

@Composable
private fun TransferScreen(
    onBackToDashboard: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var recipient by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var transferStatusMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = stringResource(R.string.transfer_heading),
            showBackButton = true,
            onBackClick = onBackToDashboard,
            titleContentDescription = "transfer_heading"
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.transfer_deeplink_notice),
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = recipient,
                    onValueChange = { recipient = it },
                    label = { Text(stringResource(R.string.recipient_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "recipientField"
                        }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.amount_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "amountField"
                        }
                )
                Text(
                    text = stringResource(R.string.transfer_validation_warning),
                    style = MaterialTheme.typography.bodySmall
                )
                Button(
                    onClick = { transferStatusMessage = onSubmit(recipient, amount) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "submit_transfer_button" }
                ) {
                    Text(text = stringResource(R.string.submit_transfer_button))
                }
                if (transferStatusMessage.isNotBlank()) {
                    Text(
                        text = transferStatusMessage,
                        modifier = Modifier.semantics { contentDescription = "transfer_status_message" },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
