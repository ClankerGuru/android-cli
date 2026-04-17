package zone.clanker.gradle.androidcli.task

import org.gradle.api.tasks.UntrackedTask
import zone.clanker.gradle.androidcli.AndroidCli

@UntrackedTask(because = "Executes external CLI")
abstract class AndroidDescribeTask : AndroidExecTask() {
    init {
        doFirst { commandLine(listOf(extension.binary) + buildArgs()) }
    }

    internal fun buildArgs(): List<String> {
        val json = project.findProperty(AndroidCli.PROP_JSON)?.toString()?.toBoolean() ?: false
        return buildList {
            add("describe")
            if (json) add("--json")
            addAll(extension.extraArgs)
        }
    }
}
