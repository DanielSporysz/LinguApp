package pl.ourdomain.tlumaczenia.controllers

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pl.ourdomain.tlumaczenia.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Define the settings file to use by this settings fragment
        preferenceManager.sharedPreferencesName = getString(R.string.file_name_preferences)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference_screen)
    }
}