package com.training.vulnerablebank

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.training.vulnerablebank.ui.theme.VulnerableBankAppTheme
import com.training.vulnerablebank.ui.components.ScreenHeader
import com.training.vulnerablebank.utils.PreferencesManager

class TransactionsActivity : BaseLocalizedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferencesManager = PreferencesManager(this)
        setContent {
            VulnerableBankAppTheme {
                TransactionsScreen(
                    preferencesManager = preferencesManager,
                    onBackToDashboard = {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

private data class TransactionEntry(
    val title: String,
    val date: String,
    val amount: String,
)

@Composable
private fun TransactionsScreen(
    preferencesManager: PreferencesManager,
    onBackToDashboard: () -> Unit
) {
    val transactions = preferencesManager.getTransactions().mapNotNull { entry ->
        val values = entry.split("|", limit = 3)
        if (values.size == 3) {
            TransactionEntry(values[0], values[1], values[2])
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp)
            .semantics { contentDescription = "transactions_list" },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = stringResource(R.string.transactions_heading),
            showBackButton = true,
            onBackClick = onBackToDashboard,
            titleContentDescription = "transactions_heading"
        )
        if (transactions.isEmpty()) {
            Text(
                text = stringResource(R.string.transactions_empty_message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transactions.size) { index ->
                    val transaction = transactions[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "transaction_item_$index" },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = transaction.title,
                                modifier = Modifier.semantics {
                                    contentDescription = "transaction_item_${index}_title"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = transaction.date,
                                modifier = Modifier.semantics {
                                    contentDescription = "transaction_item_${index}_date"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = transaction.amount,
                                modifier = Modifier.semantics {
                                    contentDescription = "transaction_item_${index}_amount"
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
