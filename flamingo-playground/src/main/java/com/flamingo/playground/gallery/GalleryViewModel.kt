package com.flamingo.playground.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.crab.FlamingoComponentRecord
import kotlin.reflect.KProperty

internal class GalleryViewModel : ViewModel() {
    private val repository = ComponentsRepository()

    private val _filter = MutableStateFlow(Filter())
    private var filterValue by _filter
    val filter: StateFlow<Filter> = _filter

    private val _viewComponents: MutableStateFlow<List<FlamingoComponent>?> = MutableStateFlow(null)
    val viewComponents: StateFlow<List<FlamingoComponent>?> = _viewComponents

    val composeComponents: List<FlamingoComponentRecord> = repository.composeComponents

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _viewComponents.value = repository.loadViewComponents()
        }
    }

    fun changeComposeComponentsFilter() {
        filterValue = filterValue.copy(composeComponents = !filterValue.composeComponents)
    }

    fun changeShowPreviewsFilter() {
        val filterValue = filterValue
        // previews  are available only for compose components
        if (!filterValue.composeComponents) return
        this.filterValue = filterValue.copy(showPreviews = !filterValue.showPreviews)
    }

    private operator fun <T> MutableStateFlow<T>.setValue(thisObj: Any?, p: KProperty<*>, any: T) {
        value = any
    }

    private operator fun <T> StateFlow<T>.getValue(thisObj: Any?, property: KProperty<*>): T = value
}

internal data class Filter(
    val composeComponents: Boolean = true,
    val showPreviews: Boolean = false,
)
