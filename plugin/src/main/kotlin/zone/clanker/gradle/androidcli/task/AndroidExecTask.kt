package zone.clanker.gradle.androidcli.task

import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.UntrackedTask
import zone.clanker.gradle.androidcli.AndroidCli

@UntrackedTask(because = "Executes external CLI")
abstract class AndroidExecTask : Exec() {
    @Internal
    lateinit var extension: AndroidCli.SettingsExtension

    init {
        doFirst { checkBinaryInstalled() }
    }

    internal fun checkBinaryInstalled() {
        val check = ProcessBuilder("which", extension.binary).redirectErrorStream(true).start()
        check.waitFor()
        if (check.exitValue() != 0) {
            error("'${extension.binary}' not found on PATH. Install Google's Android CLI first.")
        }
    }
}
