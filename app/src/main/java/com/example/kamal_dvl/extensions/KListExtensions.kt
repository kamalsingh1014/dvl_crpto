package com.example.klist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
 * Extension functions and helper utilities for KList
 */

/**
 * Animated KList variant that supports item animations
 */
class AnimatedKList private constructor(
    private val padding: Dp = 0.dp,
    private val headers: List<String> = emptyList(),
    private val sections: List<AnimatedKListSection<*>> = emptyList(),
    private val clickHandler: ((Any) -> Unit)? = null,
    private val hasDividers: Boolean = false
) {

    companion object {
        fun create(): AnimatedKList = AnimatedKList()
    }

    fun padding(dp: Dp): AnimatedKList = AnimatedKList(
        padding = dp,
        headers = headers,
        sections = sections,
        clickHandler = clickHandler,
        hasDividers = hasDividers
    )

    fun header(title: String): AnimatedKList = AnimatedKList(
        padding = padding,
        headers = headers + title,
        sections = sections,
        clickHandler = clickHandler,
        hasDividers = hasDividers
    )

    fun <T> items(
        list: List<T>,
        itemContent: @Composable (T) -> Unit
    ): AnimatedKList = AnimatedKList(
        padding = padding,
        headers = headers,
        sections = sections + AnimatedKListSection(list, itemContent),
        clickHandler = clickHandler,
        hasDividers = hasDividers
    )

    fun <T> clickable(handler: (T) -> Unit): AnimatedKList = AnimatedKList(
        padding = padding,
        headers = headers,
        sections = sections,
        clickHandler = { item ->
            @Suppress("UNCHECKED_CAST")
            handler(item as T)
        },
        hasDividers = hasDividers
    )

    fun withDividers(): AnimatedKList = AnimatedKList(
        padding = padding,
        headers = headers,
        sections = sections,
        clickHandler = clickHandler,
        hasDividers = true
    )

    @Composable
    fun render() {
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            headers.forEach { headerTitle ->
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = headerTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            sections.forEach { section ->
                when (section) {
                    is AnimatedKListSection<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        val typedSection = section as AnimatedKListSection<Any>
                        items(typedSection.items, key = { it.hashCode() }) { item ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column {
                                    typedSection.content(item)

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
        }
    }
}

private data class AnimatedKListSection<T>(
    val items: List<T>,
    val content: @Composable (T) -> Unit
)

/**
 * Builder pattern implementation of KList for alternative API style
 */
class KListBuilder {
    private var padding: Dp = 0.dp
    private val headers = mutableListOf<String>()
    private val sections = mutableListOf<KListBuilderSection<*>>()
    private var clickHandler: ((Any) -> Unit)? = null
    private var hasDividers: Boolean = false

    fun padding(dp: Dp) = apply { padding = dp }

    fun header(title: String) = apply { headers.add(title) }

    fun <T> items(list: List<T>, content: @Composable (T) -> Unit) = apply {
        sections.add(KListBuilderSection(list, content))
    }

    fun <T> clickable(handler: (T) -> Unit) = apply {
        clickHandler = { item ->
            @Suppress("UNCHECKED_CAST")
            handler(item as T)
        }
    }

    fun withDividers() = apply { hasDividers = true }

    @Composable
    fun build() {
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            headers.forEach { headerTitle ->
                item {
                    Text(
                        text = headerTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            sections.forEach { section ->
                when (section) {
                    is AnimatedKListSection<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        val typedSection = section as AnimatedKListSection<Any>
                        items(typedSection.items, key = { it.hashCode() }) { item ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column {
                                    typedSection.content(item)

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
        }
    }
}

private data class KListBuilderSection<T>(
    val items: List<T>,
    val content: @Composable (T) -> Unit
)

/**
 * DSL function for builder pattern usage
 */
@Composable
fun KList(block: KListBuilder.() -> Unit) {
    KListBuilder().apply(block).build()
}

/**
 * Extension functions for convenient access
 */

// Alternative entry point using extension
fun KList.Companion.new(): KList = KList.create()

// Utility function to create a KList with common configurations
fun KList.Companion.withDefaults(
    padding: Dp = 16.dp,
    hasDividers: Boolean = false
): KList = create().padding(padding).let {
    if (hasDividers) it.withDividers() else it
}