pluginManagement {
    includeBuild("build-logic") { name = "android-cli-build-logic" }
}

plugins {
    id("clkx-settings")
}

rootProject.name = "android-cli"

include(":plugin")
