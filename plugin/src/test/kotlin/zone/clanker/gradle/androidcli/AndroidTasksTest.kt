package zone.clanker.gradle.androidcli

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import zone.clanker.gradle.androidcli.task.AndroidDescribeTask
import zone.clanker.gradle.androidcli.task.AndroidRunTask
import zone.clanker.gradle.androidcli.task.AndroidVersionTask
import zone.clanker.gradle.androidcli.task.screen.AndroidScreenCaptureTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsAddTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsFindTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsListTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsRemoveTask

private fun newProject(): Pair<Project, AndroidCli.SettingsExtension> {
    val project = ProjectBuilder.builder().build()
    val ext =
        project.extensions.create(
            AndroidCli.EXTENSION_NAME,
            AndroidCli.SettingsExtension::class.java,
        )
    AndroidCli.registerTasks(project, ext)
    return project to ext
}

class AndroidTasksTest :
    BehaviorSpec({
        given("AndroidVersionTask") {
            val (project, _) = newProject()
            val task = project.tasks.getByName(AndroidCli.TASK_VERSION) as AndroidVersionTask

            then("buildArgs returns [version]") {
                task.buildArgs() shouldBe listOf("version")
            }
        }

        given("AndroidDescribeTask") {
            `when`("PROP_JSON not set") {
                val (project, _) = newProject()
                val task = project.tasks.getByName(AndroidCli.TASK_DESCRIBE) as AndroidDescribeTask

                then("buildArgs is [describe]") {
                    task.buildArgs() shouldBe listOf("describe")
                }
            }
            `when`("PROP_JSON=true") {
                val (project, _) = newProject()
                project.extensions.extraProperties.set(AndroidCli.PROP_JSON, "true")
                val task = project.tasks.getByName(AndroidCli.TASK_DESCRIBE) as AndroidDescribeTask

                then("buildArgs appends --json") {
                    task.buildArgs() shouldBe listOf("describe", "--json")
                }
            }
            `when`("PROP_JSON=false explicitly") {
                val (project, _) = newProject()
                project.extensions.extraProperties.set(AndroidCli.PROP_JSON, "false")
                val task = project.tasks.getByName(AndroidCli.TASK_DESCRIBE) as AndroidDescribeTask

                then("buildArgs omits --json") {
                    task.buildArgs() shouldBe listOf("describe")
                }
            }
            `when`("extraArgs is set") {
                val (project, ext) = newProject()
                ext.extraArgs = listOf("--verbose")
                val task = project.tasks.getByName(AndroidCli.TASK_DESCRIBE) as AndroidDescribeTask

                then("buildArgs appends extraArgs") {
                    task.buildArgs() shouldBe listOf("describe", "--verbose")
                }
            }
        }

        given("AndroidScreenCaptureTask") {
            val (project, ext) = newProject()
            ext.extraArgs = listOf("--output", "/tmp/s.png")
            val task = project.tasks.getByName(AndroidCli.TASK_SCREEN_CAPTURE) as AndroidScreenCaptureTask

            then("buildArgs is [screen, capture, ...extraArgs]") {
                task.buildArgs() shouldBe listOf("screen", "capture", "--output", "/tmp/s.png")
            }
        }

        given("AndroidSkillsListTask") {
            val (project, _) = newProject()
            val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_LIST) as AndroidSkillsListTask

            then("buildArgs is [skills, list]") {
                task.buildArgs() shouldBe listOf("skills", "list")
            }
        }

        given("AndroidSkillsFindTask") {
            `when`("PROP_QUERY not set") {
                val (project, _) = newProject()
                val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_FIND) as AndroidSkillsFindTask

                then("buildArgs is [skills, find]") {
                    task.buildArgs() shouldBe listOf("skills", "find")
                }
            }
            `when`("PROP_QUERY set to blank") {
                val (project, _) = newProject()
                project.extensions.extraProperties.set(AndroidCli.PROP_QUERY, "   ")
                val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_FIND) as AndroidSkillsFindTask

                then("blank query is ignored") {
                    task.buildArgs() shouldBe listOf("skills", "find")
                }
            }
            `when`("PROP_QUERY set to a value") {
                val (project, _) = newProject()
                project.extensions.extraProperties.set(AndroidCli.PROP_QUERY, "compose")
                val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_FIND) as AndroidSkillsFindTask

                then("buildArgs appends query") {
                    task.buildArgs() shouldBe listOf("skills", "find", "compose")
                }
            }
        }

        given("AndroidSkillsAddTask") {
            `when`("PROP_SKILL not set") {
                val (project, _) = newProject()
                val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_ADD) as AndroidSkillsAddTask

                then("buildArgs throws with helpful message") {
                    val ex = shouldThrow<IllegalStateException> { task.buildArgs() }
                    ex.message!! shouldContain AndroidCli.PROP_SKILL
                }
            }
            `when`("PROP_SKILL set") {
                val (project, _) = newProject()
                project.extensions.extraProperties.set(AndroidCli.PROP_SKILL, "navigation")
                val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_ADD) as AndroidSkillsAddTask

                then("buildArgs is [skills, add, <skill>]") {
                    task.buildArgs() shouldBe listOf("skills", "add", "navigation")
                }
            }
        }

        given("AndroidSkillsRemoveTask") {
            `when`("PROP_SKILL not set") {
                val (project, _) = newProject()
                val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_REMOVE) as AndroidSkillsRemoveTask

                then("buildArgs throws") {
                    val ex = shouldThrow<IllegalStateException> { task.buildArgs() }
                    ex.message!! shouldContain AndroidCli.PROP_SKILL
                }
            }
            `when`("PROP_SKILL set") {
                val (project, _) = newProject()
                project.extensions.extraProperties.set(AndroidCli.PROP_SKILL, "navigation")
                val task = project.tasks.getByName(AndroidCli.TASK_SKILLS_REMOVE) as AndroidSkillsRemoveTask

                then("buildArgs is [skills, remove, <skill>]") {
                    task.buildArgs() shouldBe listOf("skills", "remove", "navigation")
                }
            }
        }

        given("AndroidRunTask") {
            `when`("PROP_ARGS not set and no extraArgs") {
                val (project, _) = newProject()
                val task = project.tasks.getByName(AndroidCli.TASK_RUN) as AndroidRunTask

                then("buildArgs is empty") {
                    task.buildArgs() shouldBe emptyList()
                }
            }
            `when`("PROP_ARGS set with multiple tokens") {
                val (project, _) = newProject()
                project.extensions.extraProperties.set(AndroidCli.PROP_ARGS, "skills add  compose")
                val task = project.tasks.getByName(AndroidCli.TASK_RUN) as AndroidRunTask

                then("buildArgs splits on spaces ignoring blanks") {
                    task.buildArgs() shouldBe listOf("skills", "add", "compose")
                }
            }
            `when`("extraArgs is set") {
                val (project, ext) = newProject()
                ext.extraArgs = listOf("--verbose")
                project.extensions.extraProperties.set(AndroidCli.PROP_ARGS, "version")
                val task = project.tasks.getByName(AndroidCli.TASK_RUN) as AndroidRunTask

                then("extraArgs is appended") {
                    task.buildArgs() shouldContain "--verbose"
                    task.buildArgs() shouldBe listOf("version", "--verbose")
                }
            }
        }

        given("AndroidExecTask.checkBinaryInstalled") {
            `when`("binary exists on PATH") {
                val (project, ext) = newProject()
                ext.binary = "which"
                val task = project.tasks.getByName(AndroidCli.TASK_VERSION) as AndroidVersionTask

                then("it does not throw") {
                    task.checkBinaryInstalled()
                }
            }
            `when`("binary missing") {
                val (project, ext) = newProject()
                ext.binary = "zz-definitely-not-installed-zz"
                val task = project.tasks.getByName(AndroidCli.TASK_VERSION) as AndroidVersionTask

                then("it throws with install hint") {
                    val ex = shouldThrow<IllegalStateException> { task.checkBinaryInstalled() }
                    ex.message!! shouldContain "not found on PATH"
                    ex.message!! shouldContain "zz-definitely-not-installed-zz"
                }
            }
        }
    })
