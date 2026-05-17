package com.training.vulnerablebank.utils

import android.os.Build
import java.io.File
import java.util.Locale

data class VulnerabilityItem(
    val title: String,
    val description: String,
)

object SecurityVulnerabilities {
    const val HARDCODED_USERNAME = "admin"
    const val HARDCODED_PASSWORD = "password123"
    const val DEFAULT_AUTH_TOKEN = "dev-session-token-abc123"
    const val HARDCODED_API_URL = "https://api.vulnbanklab.training/internal"
    const val HARDCODED_API_KEY = "sk_test_vulnbanklab_unsafe_key"

    val vulnerabilityList = listOf(
        VulnerabilityItem(
            title = "Exported Activities",
            description = "Every activity is exported so other apps can invoke internal screens directly."
        ),
        VulnerabilityItem(
            title = "Debuggable Build",
            description = "The manifest enables debugging to simplify reverse engineering and runtime inspection."
        ),
        VulnerabilityItem(
            title = "Hardcoded Credentials",
            description = "The app ships with admin/password123 plus static API secrets in code."
        ),
        VulnerabilityItem(
            title = "Plaintext SharedPreferences",
            description = "Passwords and tokens are stored without encryption in SharedPreferences."
        ),
        VulnerabilityItem(
            title = "Sensitive Logging",
            description = "Credentials and tokens are written to Logcat using Log.d calls."
        ),
        VulnerabilityItem(
            title = "Deep Link Auth Bypass",
            description = "The transfer screen is reachable through vuln://transfer without checking login state."
        ),
        VulnerabilityItem(
            title = "Missing Input Validation",
            description = "Transfers accept empty recipients and negative amounts without any validation."
        ),
        VulnerabilityItem(
            title = "Weak Root Detection",
            description = "Root detection only checks a few hardcoded su paths and is easy to bypass."
        )
    )

    private val commonRootPaths = listOf(
        "/system/bin/su",
        "/system/xbin/su",
        "/sbin/su",
        "/su/bin/su",
        "/system/app/Superuser.apk",
        "/system/app/SuperSU.apk",
        "/system/xbin/daemonsu",
        "/system/etc/init.d/99SuperSUDaemon",
        "/system/bin/.ext/.su",
        "/system/usr/we-need-root/su.backup",
        "/data/local/su",
        "/data/local/bin/su",
        "/data/local/xbin/su",
        "/data/adb/magisk",
        "/cache/su",
        "/dev/com.koushikdutta.superuser.daemon/"
    )

    private val commonEmulatorIndicators = listOf(
        "generic",
        "sdk_gphone",
        "emulator",
        "goldfish",
        "ranchu",
        "genymotion",
        "simulator"
    )

    private fun hasRootBinaries(): Boolean = commonRootPaths.any { File(it).exists() }

    private fun isRunningOnEmulator(): Boolean {
        val buildStrings = listOfNotNull(
            Build.FINGERPRINT,
            Build.MODEL,
            Build.MANUFACTURER,
            Build.BRAND,
            Build.DEVICE,
            Build.PRODUCT,
            Build.HARDWARE,
            Build.BOARD
        ).joinToString(" ").lowercase(Locale.ROOT)

        val indicatorMatches = commonEmulatorIndicators.count { indicator ->
            buildStrings.contains(indicator)
        }

        // Keep this intentionally weak for demo purposes:
        // treat emulator as detected only when multiple noisy indicators match.
        return indicatorMatches >= 2
    }

    fun checkIfRooted(): Boolean = hasRootBinaries() || isRunningOnEmulator()
}
