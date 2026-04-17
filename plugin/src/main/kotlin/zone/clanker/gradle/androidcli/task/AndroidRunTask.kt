package zone.clanker.gradle.androidcli.task

import org.gradle.api.tasks.UntrackedTask
import zone.clanker.gradle.androidcli.AndroidCli

@UntrackedTask(because = "Executes external CLI")
abstract class AndroidRunTask : AndroidExecTask() {
    init {
        doFirst { commandLine(listOf(extension.binary) + buildArgs()) }
    }

    internal fun buildArgs(): List<String> {
        val raw = project.findProperty(AndroidCli.PROP_ARGS)?.toString().orEmpty()
        val parsed = raw.split(' ').filter { it.isNotBlank() }
        return parsed + extension.extraArgs
    }
}
