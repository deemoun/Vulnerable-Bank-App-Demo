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

        // Isolate tests: always start from seeded data and a clean session.
        preferencesManager.resetToInitialState()
        preferencesManager.clearSavedLogin()
    }

    @Test
    fun initialStateSeedsDefaultUsersAndAdminBalance() {
        // Verify seeded users exist and admin balance is initialized correctly.
        assertTrue(preferencesManager.userExists("admin"))
        assertTrue(preferencesManager.userExists("nick"))
        assertTrue(preferencesManager.userExists("lisa"))
        assertEquals(1000.0, preferencesManager.getBalanceForUser("admin"), 0.0)
    }

    @Test
    fun savingUserPersistsUsernameAndClearRemovesIt() {
        val user = User("nick", "unsafe-pass", "dev-token")
        preferencesManager.saveUser(user)
        assertEquals("nick", preferencesManager.getStoredUsername())

        preferencesManager.clearSavedLogin()
        assertEquals("", preferencesManager.getStoredUsername())
    }

    @Test
    fun validTransferUpdatesSenderAndRecipientBalances() {
        val result = preferencesManager.transferBetweenAccounts("admin", "nick", 250.0)

        // A successful transfer returns a non-null result and updates both balances.
        assertNotNull(result)
        assertEquals(750.0, preferencesManager.getBalanceForUser("admin"), 0.0)
        assertEquals(250.0, preferencesManager.getBalanceForUser("nick"), 0.0)
    }

    @Test
    fun invalidTransferKeepsBalancesUnchangedAndReturnsNull() {
        val result = preferencesManager.transferBetweenAccounts("admin", "nick", -10.0)

        assertNull(result)
        assertEquals(1000.0, preferencesManager.getBalanceForUser("admin"), 0.0)
        assertEquals(0.0, preferencesManager.getBalanceForUser("nick"), 0.0)
    }

    @Test
    fun transactionsListKeepsNewestEntriesFirst() {
        preferencesManager.addTransactionEntry("A", "2026-01-01", "-10")
        preferencesManager.addTransactionEntry("B", "2026-01-02", "+10")

        // New entries should be inserted at the start of the list.
        assertEquals(listOf("B|2026-01-02|+10", "A|2026-01-01|-10"), preferencesManager.getTransactions())
    }

    @Test
    fun selectedLanguageTagRoundTripsThroughPreferences() {
        preferencesManager.setSelectedLanguageTag("ru")

        assertEquals("ru", preferencesManager.getSelectedLanguageTag())
    }

    @Test
    fun localeWrapperAppliesRequestedLanguageToContext() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val wrapped = LocaleUtils.wrapContextWithLocale(context, "ru")

        assertEquals("ru", wrapped.resources.configuration.locales[0].language)
    }

    @Test
    fun securityVulnerabilityListAndHardcodedConstantsArePresent() {
        // Smoke-check: vulnerability list and key constants are not empty.
        assertTrue(SecurityVulnerabilities.vulnerabilityList.isNotEmpty())
        assertFalse(SecurityVulnerabilities.HARDCODED_USERNAME.isBlank())
        assertFalse(SecurityVulnerabilities.HARDCODED_PASSWORD.isBlank())
    }
}
