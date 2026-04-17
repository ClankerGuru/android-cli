package zone.clanker.gradle.androidcli.task

import org.gradle.api.tasks.UntrackedTask

@UntrackedTask(because = "Executes external CLI")
abstract class AndroidVersionTask : AndroidExecTask() {
    init {
        doFirst { commandLine(listOf(extension.binary) + buildArgs()) }
    }

    internal fun buildArgs(): List<String> = listOf("version")
}
