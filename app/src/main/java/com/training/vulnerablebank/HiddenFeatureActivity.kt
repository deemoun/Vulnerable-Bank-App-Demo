package com.training.vulnerablebank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.training.vulnerablebank.ui.theme.VulnerableBankAppTheme
import com.training.vulnerablebank.ui.components.ScreenHeader

class HiddenFeatureActivity : BaseLocalizedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VulnerableBankAppTheme {
                HiddenFeatureScreen(
                    onBackToDashboard = {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    },
                    onGoToDashboard = {
                        Log.e("VulnBankLab", "Wrong argument for MainActivity")
                        startActivity(Intent(this, DashboardActivity::class.java).putExtra("main_arg", 1337))
                        throw IllegalArgumentException("Wrong argument for MainActivity")
                    }
                )
            }
        }
    }
}

@Composable
private fun HiddenFeatureScreen(
    onBackToDashboard: () -> Unit,
    onGoToDashboard: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = stringResource(R.string.hidden_feature_message),
            showBackButton = true,
            onBackClick = onBackToDashboard
        )
        Button(
            onClick = onGoToDashboard,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "hidden_feature_button" }
        ) {
            Text(text = stringResource(R.string.hidden_feature_button))
        }
    }
}
