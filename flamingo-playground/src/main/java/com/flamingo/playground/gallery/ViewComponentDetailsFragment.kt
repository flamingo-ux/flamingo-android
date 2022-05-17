package com.flamingo.playground.gallery

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.playground.R
import com.flamingo.playground.StagingFragmentContainer
import com.flamingo.playground.databinding.FragmentDscDetailsBinding
import com.flamingo.playground.setForceDarkOnWebView
import com.flamingo.playground.utils.viewBinding

/**
 * Fragment that allows to view a design system component preview, name, demos, docs, etc.
 * This info is gathered from [FlamingoComponent] annotation, which is required on every design
 * system component.
 */
class ViewComponentDetailsFragment : Fragment() {
    private val b by viewBinding(FragmentDscDetailsBinding::bind)
    private lateinit var dsc: FlamingoComponent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentDscDetailsBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dsc = requireArguments().toFlamingoComponent()

        b.displayName.text = dsc.displayName
        setupPreview()
        setupDemos()
        setupFigmaUrl()
        setupDocs()
    }

    private fun setupPreview() {
        val previewFragment = Class.forName(dsc.preview)
            .getConstructor()
            .newInstance() as Fragment
        childFragmentManager.beginTransaction()
            .replace(R.id.component_preview, previewFragment)
            .commit()
    }

    private fun setupDemos() {
        val demoClasses = dsc.theDemos.map { Class.forName(it) as Class<out Fragment> }
        if (demoClasses.isEmpty()) {
            b.noDemosTextView.isGone = false
            b.demosRecycler.isGone = true
            return
        }
        val demos = demoClasses.mapIndexed { index, clazz ->
            val optionalName = clazz.getAnnotation(FlamingoComponentDemoName::class.java)?.name
            val typeName = renderDemoTypeName(clazz)
            Demo(
                clazz = clazz,
                title = optionalName
                    ?: requireContext().getString(R.string.component_details_demo_item, index + 1),
                subtitle = typeName.ifEmpty { null },
            )
        }

        b.demosRecycler.adapter = DemosAdapter(demos) { demo ->
            val fragment = demo.clazz.getConstructor().newInstance()
                ?: error("Demo class \"${demo.clazz.name}\" is not a Fragment")
            (requireActivity() as StagingFragmentContainer).openFragment(fragment, demo.clazz.name)
        }
    }

    private fun setupFigmaUrl() {
        val figmaSpannedString = buildSpannedString {
            inSpans(URLSpan(dsc.figmaUrl)) { append(dsc.figmaUrl) }
        }
        b.figmaTextView.setText(figmaSpannedString, TextView.BufferType.SPANNABLE)
        b.figmaTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupDocs() = with(b.docsWebView) {
        settings.loadsImagesAutomatically = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.defaultTextEncodingName = "UTF-8"
        requireContext().setForceDarkOnWebView(this)
        loadData(dsc.docs, "text/html; charset=utf-8", "UTF-8")
    }

    companion object {
        @JvmStatic
        fun newInstance(component: FlamingoComponent) = ViewComponentDetailsFragment().apply {
            arguments = component.toBundle()
        }
    }
}
