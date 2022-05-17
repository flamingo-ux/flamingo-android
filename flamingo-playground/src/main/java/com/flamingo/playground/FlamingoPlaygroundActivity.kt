@file:Suppress("NoUnusedImports")

package com.flamingo.playground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.flamingo.Flamingo
import com.flamingo.roboto.initRobotoTypography
import com.flamingo.utils.UnitConversions
import com.flamingo.utils.resolveColorAttr
import timber.log.Timber
import androidx.appcompat.R as AppCompatR
import com.flamingo.R as FlamingoR

public class FlamingoPlaygroundActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, StagingFragmentContainer {

    override fun openFragment(fragment: Fragment, tag: String?) {
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.content, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        runCatching { UnitConversions.init(this) }
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        Flamingo.initRobotoTypography()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flamingo_playground_activity)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.navigationIcon =
            ContextCompat.getDrawable(this, FlamingoR.drawable.ds_ic_arrow_left)!!
                .apply { setTint(resolveColorAttr(AppCompatR.attr.colorPrimary)) }

        if (savedInstanceState == null) {
            val tag = DesignDemosFragment::class.java.name
            var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)
            if (fragment == null) fragment = DesignDemosFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, fragment, tag)
                .commit()
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference,
    ): Boolean {
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment
        )
        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.content, fragment, pref.key)
            .addToBackStack(pref.key)
            .commit()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
