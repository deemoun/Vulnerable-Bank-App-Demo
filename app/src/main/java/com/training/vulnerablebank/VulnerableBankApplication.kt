package com.training.vulnerablebank

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.training.vulnerablebank.utils.PreferencesManager

class VulnerableBankApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val languageTag = PreferencesManager(this).getSelectedLanguageTag()
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
    }
}
