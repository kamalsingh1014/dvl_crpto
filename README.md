# KList DSL - Custom List Component for Jetpack Compose

A fluent DSL for creating configurable lists in Jetpack Compose, inspired by the Modifier chaining pattern.

## Overview

KList provides a declarative, chainable API for building list-based UIs in Jetpack Compose. It allows you to configure lists using a fluent interface similar to how you would chain Modifier functions.

## Features

- ✅ Fluent API design inspired by Compose Modifier pattern
- ✅ Chainable configuration methods
- ✅ Support for headers and multiple sections
- ✅ Click handling for list items
- ✅ Customizable padding and dividers
- ✅ Reusable item components
- ✅ Animated item support (bonus feature)
- ✅ Builder pattern alternative (bonus feature)

## Basic Usage

### Simple List with Header

@Composable
fun HomeScreen() {
    val coinList = listOf(
        Coin("Bitcoin", "BTC", 45000.0, 5.2, "$850B"),
        Coin("Ethereum", "ETH", 3200.0, 8.1, "$380B")
    )
    
    KList.create()
        .padding(16.dp)
        .header("Coins")
        .items(coinList) { coin ->
            KListItem(coin)
        }
        .render()
}

@Composable
fun KListScreen() {
    KList.create()
        .padding(10.dp)
        .header("Top Gainers")
        .items(topGainers) { coin ->
            KListItem(coin)
        }
        .header("Top Losers")
        .items(topLosers) { coin ->
            KListItem(coin)
        }
        .clickable<Coin> { coin ->
            // Handle coin selection
            onCoinSelected(coin)
        }
        .withDividers()
        .render()
}

## API Reference

### Core Methods

| Method | Description | Example |
|--------|-------------|---------|
| `padding(dp: Dp)` | Adds padding around the entire list | `.padding(16.dp)` |
| `header(title: String)` | Adds a header section to the list | `.header("My List")` |
| `items(list, content)` | Adds items with their composable content | `.items(myList) { MyItem(it) }` |
| `clickable(handler)` | Adds click handling to list items | `.clickable { item -> onClick(item) }` |
| `withDividers()` | Adds dividers between list items | `.withDividers()` |
| `render()` | Renders the configured list as a Composable | `.render()` |

### Usage Patterns


#### 1. List with Header and Padding
KList.create()
    .header("Section 1")
    .items(list1) { item -> Item1(item) }
    .header("Section 2") 
    .items(list2) { item -> Item2(item) }
    .render()

## Components

### KList.kt
Core DSL class with fluent configuration methods and rendering logic.

### KListItem.kt
Sample reusable item components:
- `KListItem(coin)` - Styled card component for cryptocurrency data
- `SimpleKListItem(text, subtitle)` - Basic text-based list item


### KListExtensions.kt (Bonus)
 Features including:
- `AnimatedKList` - List with item animations
- `KListBuilder` - Alternative builder pattern implementation
- Extension functions for convenient access

##  Features

### 1. Animated Lists
AnimatedKList.create()
    .padding(16.dp)
    .header("Animated Items")
    .items(dynamicList) { item ->
        AnimatedItem(item)
    }
    .render()



## Architecture

The KList DSL follows these design principles:

1. **Immutability**: Each chained method returns a new KList instance
2. **Fluent Interface**: Method chaining for intuitive API usage  
3. **Separation of Concerns**: Configuration is separate from rendering
4. **Composable Integration**: Seamless integration with Jetpack Compose
5. **Type Safety**: Generic type parameters ensure type-safe click handling

## Sample Data

The demo uses a `Coin` data class representing cryptocurrency information:

```kotlin
data class Coin(
    val name: String,
    val symbol: String, 
    val price: Double,
    val changePercent: Double,
    val marketCap: String
)
```

## Requirements Met

- ✅ Core KList implementation with fluent API
- ✅ Chainable modifiers (padding, header, items)
- ✅ LazyColumn-based rendering
- ✅ Composable integration with .render() method
- ✅ Clickable support (bonus)
- ✅ Multiple headers/sections (bonus)
- ✅ Item animations (bonus)
- ✅ Divider support (bonus)
- ✅ Builder pattern implementation (bonus)

## Known Issues & Improvements

1. **Type Safety**: Click handlers use type erasure - could be improved with better generic constraints
2. **Performance**: Large lists might benefit from key-based optimization in LazyColumn
3. **Customization**: Header styling is currently fixed - could be made configurable
4. **State Management**: No built-in state management for dynamic list updates
5. **Accessibility**: Could benefit from additional accessibility modifiers

## Future Enhancements

- Custom header styling options
- Swipe-to-delete functionality
- Pull-to-refresh support
- Sticky headers
- Grid layout support
- Search/filtering capabilities
