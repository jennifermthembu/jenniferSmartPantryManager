package com.richfield.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.richfield.smartpantry.R;

/**
 * Settings / profile screen (brief Section 2.2). Uses the AndroidX Preference
 * library so toggles persist automatically in SharedPreferences. Provides:
 *  - an "expiring soon" alert toggle,
 *  - a preferred-units choice, and
 *  - the "Almost There" bonus list toggle consumed by SuggestedRecipesFragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_EXPIRY_ALERTS = "pref_expiry_alerts";
    public static final String KEY_PREFERRED_UNITS = "pref_preferred_units";
    public static final String KEY_ALMOST_THERE = "pref_almost_there";
    private static final String KEY_ACCOUNT_EMAIL = "pref_account_email";
    private static final String KEY_SIGN_OUT = "pref_sign_out";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference signOut = findPreference(KEY_SIGN_OUT);
        if (signOut != null) {
            signOut.setOnPreferenceClickListener(preference -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(requireContext(), R.string.settings_signed_out,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
                return true;
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Preference account = findPreference(KEY_ACCOUNT_EMAIL);
        if (account != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user == null ? "" : user.getEmail();
            account.setSummary(email == null ? "" : getString(R.string.settings_signed_in_as, email));
        }
    }
}
