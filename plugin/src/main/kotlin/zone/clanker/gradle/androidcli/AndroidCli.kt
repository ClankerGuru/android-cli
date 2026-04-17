package zone.clanker.gradle.androidcli

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import zone.clanker.gradle.androidcli.task.AndroidDescribeTask
import zone.clanker.gradle.androidcli.task.AndroidRunTask
import zone.clanker.gradle.androidcli.task.AndroidVersionTask
import zone.clanker.gradle.androidcli.task.screen.AndroidScreenCaptureTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsAddTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsFindTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsListTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsRemoveTask

data object AndroidCli {
    const val BINARY = "android"
    const val GROUP = "android-cli"
    const val EXTENSION_NAME = "androidCli"

    const val TASK_VERSION = "android-version"
    const val TASK_DESCRIBE = "android-describe"
    const val TASK_SCREEN_CAPTURE = "android-screen-capture"
    const val TASK_SKILLS_LIST = "android-skills-list"
    const val TASK_SKILLS_FIND = "android-skills-find"
    const val TASK_SKILLS_ADD = "android-skills-add"
    const val TASK_SKILLS_REMOVE = "android-skills-remove"
    const val TASK_RUN = "android-run"

    const val PROP_SKILL = "zone.clanker.android-cli.skill"
    const val PROP_QUERY = "zone.clanker.android-cli.query"
    const val PROP_ARGS = "zone.clanker.android-cli.args"
    const val PROP_JSON = "zone.clanker.android-cli.json"

    open class SettingsExtension {
        var binary: String = BINARY
        var extraArgs: List<String> = emptyList()
    }

    class SettingsPlugin : Plugin<Settings> {
        override fun apply(settings: Settings) {
            val extension = settings.extensions.create(EXTENSION_NAME, SettingsExtension::class.java)
            settings.gradle.rootProject(
                Action { rootProject ->
                    registerTasks(rootProject, extension)
                },
            )
        }
    }

    internal fun registerTasks(
        rootProject: Project,
        extension: SettingsExtension,
    ) {
        rootProject.tasks.register(TASK_VERSION, AndroidVersionTask::class.java) {
            it.group = GROUP
            it.description = "Show `android` CLI version"
            it.extension = extension
        }
        rootProject.tasks.register(TASK_DESCRIBE, AndroidDescribeTask::class.java) {
            it.group = GROUP
            it.description = "Describe the Android project context (pass -P$PROP_JSON=true for JSON)"
            it.extension = extension
        }
        rootProject.tasks.register(TASK_SCREEN_CAPTURE, AndroidScreenCaptureTask::class.java) {
            it.group = GROUP
            it.description = "Capture a screenshot of the connected device"
            it.extension = extension
        }
        rootProject.tasks.register(TASK_SKILLS_LIST, AndroidSkillsListTask::class.java) {
            it.group = GROUP
            it.description = "List installed Android CLI skills"
            it.extension = extension
        }
        rootProject.tasks.register(TASK_SKILLS_FIND, AndroidSkillsFindTask::class.java) {
            it.group = GROUP
            it.description = "Find available skills (pass -P$PROP_QUERY=<term>)"
            it.extension = extension
        }
        rootProject.tasks.register(TASK_SKILLS_ADD, AndroidSkillsAddTask::class.java) {
            it.group = GROUP
            it.description = "Install a skill (pass -P$PROP_SKILL=<name>)"
            it.extension = extension
        }
        rootProject.tasks.register(TASK_SKILLS_REMOVE, AndroidSkillsRemoveTask::class.java) {
            it.group = GROUP
            it.description = "Uninstall a skill (pass -P$PROP_SKILL=<name>)"
            it.extension = extension
        }
        rootProject.tasks.register(TASK_RUN, AndroidRunTask::class.java) {
            it.group = GROUP
            it.description = "Run `android` with arbitrary args (pass -P$PROP_ARGS=\"a b c\")"
            it.extension = extension
        }
    }
}
