package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Geometric Balance Theme Colors
val GeoBackground = Color(0xFFFDFBFF) // Clean light violet/blue off-white
val GeoPrimaryDark = Color(0xFF001B3D) // Navy Blue heading text/accent
val GeoTextPrimary = Color(0xFF1A1C1E) // Charcoal black
val GeoTextSecondary = Color(0xFF43474E) // Soft grey for subtitles/info
val GeoBorder = Color(0xFFCAC6D0) // Thin neutral borders

// Interactive Elements / Bottom Nav / Cards
val GeoSurface = Color(0xFFFFFFFF)
val GeoSurfaceVariant = Color(0xFFF3EDF7) // Light violet-gray surface
val GeoNavIndicator = Color(0xFFE8DEF8) // Rounded selected nav pill

// Pastel Geometric Highlights
val GeoProgressBg = Color(0xFFEADDFF) // Progress card container
val GeoProgressBorder = Color(0xFFD0BCFF)
val GeoProgressIndicator = Color(0xFF6750A4)
val GeoProgressText = Color(0xFF21005D)

val GeoEventBg = Color(0xFFD6E2FF) // Soft Blue
val GeoEventText = Color(0xFF001B3D)

val GeoInternshipBg = Color(0xFFF2B8B5) // Soft Coral
val GeoInternshipText = Color(0xFF601410)

val GeoSkillChipBg = Color(0xFFF3EDF7)
val GeoSkillChipBorder = Color(0xFFE7E0EC)
val GeoSkillChipText = Color(0xFF1D1B20)

// Backward compatibility or mapped names for existing references
val SlateDarkBackground = GeoBackground
val SlateSurface = GeoSurface
val SlateSurfaceVariant = GeoSurfaceVariant
val AIBluePrimary = GeoProgressIndicator  // Maps to primary M3 Purple #6750A4
val AIBlueSecondary = Color(0xFF4A617C)     // Medium Slate Blue for subtler secondary action
val AccentOrange = Color(0xFFD14900)      // Keep a beautiful warm orange for warnings/bookmarks
val NeonPurple = Color(0xFF8B50E3)        // Radiant M3 Purple
val TextPrimary = GeoTextPrimary
val TextSecondary = GeoTextSecondary

val DarkPrimary = GeoProgressIndicator
val DarkSecondary = GeoPrimaryDark
val DarkTertiary = NeonPurple

val LightPrimary = GeoProgressIndicator
val LightSecondary = GeoPrimaryDark
val LightTertiary = NeonPurple

