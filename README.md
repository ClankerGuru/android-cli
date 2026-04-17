# android-cli

Gradle settings plugin wrapping Google's agent-first `android` CLI.

Plugin ID: `zone.clanker.gradle.android-cli`
Artifact: `zone.clanker:android-cli`

## Target CLI

This plugin targets Google's `android` CLI as documented at
<https://developer.android.com/tools/agents/android-cli>.

Verified against the following surface:

```
Usage: android [-hV] [--sdk=PARAM] [COMMAND]
  -h, --help        Show this help message and exit.
      --sdk=PARAM   Path to the Android SDK
  -V, --version     Print version information and exit.
Commands:
  create    Create a new Android project
  describe  Analyzes an Android project to generate descriptive metadata.
  docs      Android documentation commands
  emulator  Emulator commands
  help      Shows the help of all commands
  info      Print environment information (SDK Location, etc.)
  init      Initializes the environment (eg. skills) for Android CLI.
  layout    Returns the layout tree of an application
  run       Deploy an Android Application
  screen    Commands to view the device
  sdk       Download and list SDK packages
  skills    Manage skills
  update    Update the Android CLI
```

The plugin wraps a subset of these subcommands as Gradle tasks (see below).
Anything the plugin does not model directly is reachable via `android-run`
with `-Pzone.clanker.android-cli.args="..."`.

## Usage

```kotlin
// settings.gradle.kts
plugins {
    id("zone.clanker.gradle.android-cli") version "<version>"
}

androidCli {
    // binary = "android"               // override if not on PATH
    // extraArgs = listOf("--verbose")  // appended to every invocation
}
```

## Tasks

| Task | What it runs |
|------|--------------|
| `android-version` | `android version` |
| `android-describe` | `android describe` (`-Pzone.clanker.android-cli.json=true` for JSON) |
| `android-screen-capture` | `android screen capture` |
| `android-skills-list` | `android skills list` |
| `android-skills-find` | `android skills find [query]` (`-Pzone.clanker.android-cli.query=<term>`) |
| `android-skills-add` | `android skills add <name>` (`-Pzone.clanker.android-cli.skill=<name>`) |
| `android-skills-remove` | `android skills remove <name>` |
| `android-run` | `android <args>` (`-Pzone.clanker.android-cli.args="a b c"`) |

Each task extends `Exec` and pre-checks that the `android` binary is on `PATH`.
