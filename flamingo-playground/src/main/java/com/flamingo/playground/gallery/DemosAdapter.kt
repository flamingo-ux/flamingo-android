package com.flamingo.playground.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flamingo.demoapi.FlamingoComponentDemoType
import com.flamingo.playground.R
import com.flamingo.playground.databinding.DemoListItemBinding

class DemosAdapter(
    private val demos: List<Demo>,
    private val onClick: (Demo) -> Unit
) : RecyclerView.Adapter<DemosAdapter.DemoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.demo_list_item, parent, false)
        return DemoViewHolder(view, onClick)
    }

    override fun getItemCount() = demos.size

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        holder.bind(demos[position])
    }

    class DemoViewHolder(
        itemView: View,
        private val onClick: (Demo) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val b = DemoListItemBinding.bind(itemView)
        fun bind(item: Demo) {
            b.demoTitle.text = item.title
            b.demoSubtitle.isVisible = item.subtitle != null
            b.demoSubtitle.text = item.subtitle
            b.root.setOnClickListener { onClick(item) }
        }
    }
}

data class Demo(
    val clazz: Class<out Fragment>,
    val title: String,
    val subtitle: String?
) {
    fun instantiateFragment(): Fragment = clazz.getConstructor().newInstance()
        ?: error("Demo class \"${clazz.name}\" is not a Fragment")
}

internal fun renderDemoTypeName(clazz: Class<out Fragment>): String {
    return clazz.annotations.mapNotNull {
        it.annotationClass.java.getAnnotation(FlamingoComponentDemoType::class.java)?.typeName
    }.joinToString(separator = "; ")
}
