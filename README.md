# WaterOS Launcher

Ultra-lightweight, customizable, ad-free Android TV / Fire OS launcher built with Kotlin and Jetpack Compose for TV.

Designed as the standalone visual companion to [WaterStick](https://github.com/MedCy1/WaterStick).

---

## Highlights

- **Zero Ads & Zero Telemetry**: Clean, respectful home interface with no banner trailers, sponsored carousels, or commercial trackers.
- **Ultra-Lightweight Footprint**: Memory footprint of ~45 MB RAM vs 150+ MB for bloated third-party launchers, preserving resources for 4K streaming.
- **Multi-Theme Engine**:
  - **Water Neon (Default)**: Deep midnight slate with electric cyan highlights and teal glows.
  - **OLED Pure Black**: Absolute black (#000000) for zero emission on OLED displays.
  - **Nordic Frost**: Arctic slate-blue with crisp ice-blue accents.
  - **Cyber Amber**: Charcoal dark with cinema amber highlights.
- **Native D-Pad TV Focus**: Built specifically for remote controls using `androidx.tv:tv-foundation` with fluid scaling and spatial focus transitions.
- **Fire OS Watchdog Bypass**: Includes both direct home category intent priority (priority 1000) and an internal accessibility bridge service to intercept physical remote Home key presses.

---

## Technical Specifications

- **Target Device**: Amazon Fire TV Stick 4K Max 2nd Gen (Fire OS 8 / Android 11) & universal Android TV / Google TV devices (Android 9.0+ / API 28+).
- **Language**: Kotlin 2.0
- **UI Toolkit**: Jetpack Compose for TV (Material 3 TV)
- **Minimum SDK**: API 28 (Android 9.0 Pie)
- **Target SDK**: API 34 (Android 14)

---

## Installation via WaterStick

WaterStick automatically provisions WaterOS Launcher, grants necessary permissions, and overrides default home routing:

```bash
# Connect to Fire TV
waterstick connect 192.168.1.50:5555

# Sideload and activate WaterOS Launcher
waterstick sideload install wateros-launcher
```

---

## Manual Build

Requirements:
- Android SDK (API 34)
- JDK 17+

```bash
# Assemble release APK
./gradlew assembleRelease

# Install directly via ADB
adb install -r app/build/outputs/apk/release/app-release.apk
```

---

## License

PolyForm Noncommercial 1.0.0. Copyright (c) 2026 MedCy1 (Mederick Roge).
See `LICENSE` for details.
