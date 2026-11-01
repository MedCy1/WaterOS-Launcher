# Compose rules
-keepclassmembers class * extends androidx.compose.runtime.State { *; }

# Coil image loading
-keep class coil.** { *; }
-dontwarn coil.**
