plugins { id("clkx-conventions") }

gradlePlugin {
    plugins {
        register("android-cli") {
            id = "zone.clanker.gradle.android-cli"
            implementationClass = "zone.clanker.gradle.androidcli.AndroidCli\$SettingsPlugin"
            displayName = "Android CLI Plugin"
            description = "Gradle tasks wrapping Google's agent-first `android` CLI"
        }
    }
}
