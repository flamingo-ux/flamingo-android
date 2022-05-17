package com.flamingo.playground.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flamingo.R
import com.flamingo.playground.context
import com.flamingo.playground.databinding.ItemGradientBinding
import com.flamingo.playground.R as DemoR

class GradientsDemoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = RecyclerView(requireContext()).apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = GradientsAdapter(loadGradientsList())
    }

    private fun loadGradientsList(): List<Int> {
        return R.drawable::class.java.fields.mapNotNull {
            if (it.name.startsWith("ds_gradient_")) it.getInt(null) else null
        }
    }

    private class GradientsAdapter(
        private val gradientsResIds: List<Int>,
    ) : RecyclerView.Adapter<GradientsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(DemoR.layout.item_gradient, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = gradientsResIds.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(gradientsResIds[position])
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val b = ItemGradientBinding.bind(itemView)
            fun bind(resId: Int) {
                val gradientName = itemView.resources.getResourceEntryName(resId)
                b.name.text = "${absoluteAdapterPosition + 1}. R.drawable.$gradientName"
                b.gradient.background = getDrawable(context, resId)
            }
        }
    }
}
