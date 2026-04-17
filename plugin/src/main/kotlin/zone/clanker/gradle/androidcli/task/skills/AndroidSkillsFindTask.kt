package zone.clanker.gradle.androidcli.task.skills

import org.gradle.api.tasks.UntrackedTask
import zone.clanker.gradle.androidcli.AndroidCli
import zone.clanker.gradle.androidcli.task.AndroidExecTask

@UntrackedTask(because = "Executes external CLI")
abstract class AndroidSkillsFindTask : AndroidExecTask() {
    init {
        doFirst { commandLine(listOf(extension.binary) + buildArgs()) }
    }

    internal fun buildArgs(): List<String> {
        val query = project.findProperty(AndroidCli.PROP_QUERY)?.toString()
        return buildList {
            add("skills")
            add("find")
            if (!query.isNullOrBlank()) add(query)
        }
    }
}
