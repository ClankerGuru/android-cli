package zone.clanker.gradle.androidcli.task.skills

import org.gradle.api.tasks.UntrackedTask
import zone.clanker.gradle.androidcli.task.AndroidExecTask

@UntrackedTask(because = "Executes external CLI")
abstract class AndroidSkillsListTask : AndroidExecTask() {
    init {
        doFirst { commandLine(listOf(extension.binary) + buildArgs()) }
    }

    internal fun buildArgs(): List<String> = listOf("skills", "list")
}
