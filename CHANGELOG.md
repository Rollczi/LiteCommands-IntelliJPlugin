# Changelog

## Unreleased

## 3.11.0

### Added

- Support for IntelliJ IDEA 2026.1 and 2026.2 (build `261.*` and `262.*`).

### Changed

- The plugin is now compiled against the unified IntelliJ IDEA distribution, starting with `2026.1`.
- The plugin bytecode target is Java 21, which is required by IntelliJ IDEA `2026.1` and newer.

### Fixed

- The Kotlin annotation quick fixes no longer depend on `org.jetbrains.kotlin.idea.quickfix.crossLanguage.KotlinElementActionsFactory`, which was removed in IntelliJ IDEA `2026.2`. They now use the K2 implementation `K2ElementActionsFactory` instead.
- The `RunAnything` icon is used instead of the `Run_anything` icon, which was removed in IntelliJ IDEA `2026.2`.
