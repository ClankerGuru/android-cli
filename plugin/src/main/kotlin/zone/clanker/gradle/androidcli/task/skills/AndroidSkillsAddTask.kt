package zone.clanker.gradle.androidcli.task.skills

import org.gradle.api.tasks.UntrackedTask
import zone.clanker.gradle.androidcli.AndroidCli
import zone.clanker.gradle.androidcli.task.AndroidExecTask

@UntrackedTask(because = "Executes external CLI")
abstract class AndroidSkillsAddTask : AndroidExecTask() {
    init {
        doFirst { commandLine(listOf(extension.binary) + buildArgs()) }
    }

    internal fun buildArgs(): List<String> {
        val skill =
            project.findProperty(AndroidCli.PROP_SKILL)?.toString()
                ?: error(
                    "Required property '${AndroidCli.PROP_SKILL}' not set. " +
                        "Use -P${AndroidCli.PROP_SKILL}=<name>",
                )
        return listOf("skills", "add", skill)
    }
}
