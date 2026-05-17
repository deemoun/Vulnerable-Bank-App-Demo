package com.training.vulnerablebank

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.training.vulnerablebank.data.User
import com.training.vulnerablebank.utils.LocaleUtils
import com.training.vulnerablebank.utils.PreferencesManager
import com.training.vulnerablebank.utils.SecurityVulnerabilities
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferencesManagerInstrumentedTest {

    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        preferencesManager = PreferencesManager(context)
        preferencesManager.resetToInitialState()
        preferencesManager.clearSavedLogin()
    }

    @Test
    fun ensureInitialState_createsSeedAccounts() {
        assertTrue(preferencesManager.userExists("admin"))
        assertTrue(preferencesManager.userExists("nick"))
        assertTrue(preferencesManager.userExists("lisa"))
        assertEquals(1000.0, preferencesManager.getBalanceForUser("admin"), 0.0)
    }

    @Test
    fun saveUser_andClearSavedLogin_workAsExpected() {
        val user = User("nick", "unsafe-pass", "dev-token")
        preferencesManager.saveUser(user)

        assertEquals("nick", preferencesManager.getStoredUsername())

        preferencesManager.clearSavedLogin()
        assertEquals("", preferencesManager.getStoredUsername())
    }

    @Test
    fun transferBetweenAccounts_validTransfer_updatesBothBalances() {
        val result = preferencesManager.transferBetweenAccounts("admin", "nick", 250.0)

        assertNotNull(result)
        assertEquals(750.0, preferencesManager.getBalanceForUser("admin"), 0.0)
        assertEquals(250.0, preferencesManager.getBalanceForUser("nick"), 0.0)
    }

    @Test
    fun transferBetweenAccounts_invalidTransfer_returnsNullAndPreservesBalances() {
        val result = preferencesManager.transferBetweenAccounts("admin", "nick", -10.0)

        assertNull(result)
        assertEquals(1000.0, preferencesManager.getBalanceForUser("admin"), 0.0)
        assertEquals(0.0, preferencesManager.getBalanceForUser("nick"), 0.0)
    }

    @Test
    fun transactions_arePrependedInReverseChronologicalOrder() {
        preferencesManager.addTransactionEntry("A", "2026-01-01", "-10")
        preferencesManager.addTransactionEntry("B", "2026-01-02", "+10")

        assertEquals(listOf("B|2026-01-02|+10", "A|2026-01-01|-10"), preferencesManager.getTransactions())
    }

    @Test
    fun selectedLanguageTag_roundTrips() {
        preferencesManager.setSelectedLanguageTag("ru")

        assertEquals("ru", preferencesManager.getSelectedLanguageTag())
    }

    @Test
    fun wrapContextWithLocale_appliesGivenLocale() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val wrapped = LocaleUtils.wrapContextWithLocale(context, "ru")

        assertEquals("ru", wrapped.resources.configuration.locales[0].language)
    }

    @Test
    fun vulnerabilityList_containsExpectedEntries() {
        assertTrue(SecurityVulnerabilities.vulnerabilityList.isNotEmpty())
        assertFalse(SecurityVulnerabilities.HARDCODED_USERNAME.isBlank())
        assertFalse(SecurityVulnerabilities.HARDCODED_PASSWORD.isBlank())
    }
}
