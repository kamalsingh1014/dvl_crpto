package com.example.klist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A fluent DSL for creating configurable lists in Jetpack Compose.
 * Inspired by the Modifier chaining pattern.
 *
 * Usage:
 * ```
 * KList
 *     .padding(16.dp)
 *     .header("My List")
 *     .items(myItems) { item ->
 *         MyItemComposable(item)
 *     }
 *     .render()
 * ```
 */
class KList private constructor(
    private val padding: Dp = 0.dp,
    private val headers: List<String> = emptyList(),
    private val sections: List<KListSection<*>> = emptyList(),
    private val clickHandler: ((Any) -> Unit)? = null,
    private val hasDividers: Boolean = false
) {

    companion object {
        /**
         * Entry point for creating a new KList
         */
        fun create(): KList = KList()
    }

    /**
     * Adds padding to the entire list
     */
    fun padding(dp: Dp): KList = KList(
        padding = dp,
        headers = headers,
        sections = sections,
        clickHandler = clickHandler,
        hasDividers = hasDividers
    )

    /**
     * Adds a header to the list
     */
    fun header(title: String): KList = KList(
        padding = padding,
        headers = headers + title,
        sections = sections,
        clickHandler = clickHandler,
        hasDividers = hasDividers
    )

    /**
     * Adds a list of items with their corresponding composable content
     */
    fun <T> items(
        list: List<T>,
        itemContent: @Composable (T) -> Unit
    ): KList = KList(
        padding = padding,
        headers = headers,
        sections = sections + KListSection(list, itemContent),
        clickHandler = clickHandler,
        hasDividers = hasDividers
    )

    /**
     * Adds click handling to list items
     */
    fun <T> clickable(handler: (T) -> Unit): KList = KList(
        padding = padding,
        headers = headers,
        sections = sections,
        clickHandler = { item ->
            @Suppress("UNCHECKED_CAST")
            handler(item as T)
        },
        hasDividers = hasDividers
    )

    /**
     * Adds dividers between list items
     */
    fun withDividers(): KList = KList(
        padding = padding,
        headers = headers,
        sections = sections,
        clickHandler = clickHandler,
        hasDividers = true
    )

    /**
     * Renders the configured list as a Composable
     */
    @Composable
    fun render() {
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Render headers
            headers.forEach { headerTitle ->
                item {
                    KListHeader(title = headerTitle)
                }
            }

            // Render sections
            sections.forEach { section ->
                @Suppress("UNCHECKED_CAST")
                val typedSection = section as KListSection<Any>

                items(typedSection.items) { item ->
                    Column {
                        Box(
                            modifier = if (clickHandler != null) {
                                Modifier.clickable { clickHandler.invoke(item) }
                            } else {
                                Modifier
                            }
                        ) {
                            typedSection.content(item)
                        }

                        if (hasDividers && item != typedSection.items.lastOrNull()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                thickness = DividerDefaults.Thickness,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Internal data class to hold section information
 */
private data class KListSection<T>(
    val items: List<T>,
    val content: @Composable (T) -> Unit
)

/**
 * Default header composable used by KList
 */
@Composable
private fun KListHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/**
 * Extension property to create a new KList instance
 * This provides the fluent entry point: KList.padding(...)
 */
val KList.Companion.instance: KList
    get() = create()