package com.training.vulnerablebank

import android.content.Context
import androidx.activity.ComponentActivity
import com.training.vulnerablebank.utils.LocaleUtils
import com.training.vulnerablebank.utils.PreferencesManager

open class BaseLocalizedActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val languageTag = PreferencesManager(newBase).getSelectedLanguageTag()
        val localizedContext = LocaleUtils.wrapContextWithLocale(newBase, languageTag)
        super.attachBaseContext(localizedContext)
    }
}
