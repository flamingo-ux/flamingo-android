package com.flamingo.playground

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.demoapi.tintIcons
import com.flamingo.playground.overlay.disableDebugOverlay
import com.flamingo.playground.overlay.enableDebugOverlay
import com.flamingo.roboto.dsTextStyle
import com.flamingo.view.components.AlertMessage
import com.flamingo.view.components.Avatar
import com.flamingo.view.components.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DesignDemosFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.design_demos_preferences, rootKey)
        preferenceScreen.tintIcons { key != "compose_sandbox" }
        setupDesignerToolsDownloadButton()
        setupDebugOverlay()
        setupVersion()
        Flamingo.isStagingBuild = true
        initFlamingoFonts()
    }

    private fun initFlamingoFonts() {
        // for Button
        Button.fontSetter = Button.Companion.FontSetter { textView, isLarge ->
            textView.dsTextStyle(com.flamingo.roboto.R.style.TextStyle_Flamingo_Button)
        }
        // for AlertMessage
        AlertMessage.fontSetter = AlertMessage.Companion.FontSetter { textView ->
            textView.dsTextStyle(com.flamingo.roboto.R.style.TextStyle_Flamingo_Body1)
        }
        // for Avatar
        Avatar.fontSetter = Avatar.Companion.FontSetter { textView ->
            textView.dsTextStyle(com.flamingo.roboto.R.style.TextStyle_Flamingo_Body1)
        }
    }

    private fun setupDebugOverlay() {
        setupDebugOverlayToggle()
        setupDebugOverlayTileTutorial()
    }

    private fun setupDebugOverlayTileTutorial() {
        findPreference("debug_overlay_tile_tutorial").setOnPreferenceClickListener {
            requireContext().openTileTutorial()
            true
        }
    }

    private fun setupVersion() {
        findPreference("version").apply {
            summary = Flamingo.versionName
            setOnPreferenceClickListener {
                val urlString = getString(R.string.flamingo_github_release, Flamingo.versionName)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                ContextCompat.startActivity(requireContext(), browserIntent, null)
                true
            }
        }
    }

    private fun Context.openTileTutorial() {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.add_tile_tutorial_url)))
        ContextCompat.startActivity(this, browserIntent, null)
    }

    private fun setupDebugOverlayToggle() {
        val pref = findPreference("debug_overlay_toggle") as SwitchPreferenceCompat
        // todo sync with [Flamingo.debugOverlay] when this fragment is @Composable
        pref.isChecked = Flamingo.debugOverlay != null
        pref.setOnPreferenceChangeListener { _, checked ->
            if (checked as Boolean) {
                Flamingo.enableDebugOverlay()
            } else {
                Flamingo.disableDebugOverlay()
            }
            true
        }
    }

    private fun setupDesignerToolsDownloadButton() {
        findPreference("design_demo_tools_download_designer_tools").setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(
                    getString(R.string.download_designer_tools_explanation).filterNot { it == '\n' }
                )
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    context?.openGooglePlayApp("com.scheffsblend.designertools")
                }
                .show()
            true
        }
    }

    /**
     * Opens application page on the Google Play app, or on the Google Play website in the browser,
     * if Google Play app is not installed.
     *
     * @param appId - package name of the app to open, e.g. "com.instagram.android"
     */
    private fun Context.openGooglePlayApp(appId: String) {
        val appUrl = "https://play.google.com/store/apps/details?id=$appId"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(appUrl)
            // this makes sure only the Google Play app is allowed to intercept the intent
            setPackage("com.android.vending")
            // make sure it does NOT open in the stack of your activity
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // task reparenting if needed
            addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            // if the Google Play was already open in a search result
            // this makes sure it will still go to the app page
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // if GP not present on device, open web browser
            openUrl(appUrl)
        }
    }
}
