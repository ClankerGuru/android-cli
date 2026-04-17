package zone.clanker.gradle.androidcli

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.gradle.testfixtures.ProjectBuilder
import zone.clanker.gradle.androidcli.task.AndroidDescribeTask
import zone.clanker.gradle.androidcli.task.AndroidRunTask
import zone.clanker.gradle.androidcli.task.AndroidVersionTask
import zone.clanker.gradle.androidcli.task.screen.AndroidScreenCaptureTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsAddTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsFindTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsListTask
import zone.clanker.gradle.androidcli.task.skills.AndroidSkillsRemoveTask

class AndroidCliPluginTest :
    BehaviorSpec({
        given("AndroidCli constants") {
            then("BINARY is android") {
                AndroidCli.BINARY shouldBe "android"
            }
            then("GROUP is android-cli") {
                AndroidCli.GROUP shouldBe "android-cli"
            }
            then("EXTENSION_NAME is androidCli") {
                AndroidCli.EXTENSION_NAME shouldBe "androidCli"
            }
            then("task names are correct") {
                AndroidCli.TASK_VERSION shouldBe "android-version"
                AndroidCli.TASK_DESCRIBE shouldBe "android-describe"
                AndroidCli.TASK_SCREEN_CAPTURE shouldBe "android-screen-capture"
                AndroidCli.TASK_SKILLS_LIST shouldBe "android-skills-list"
                AndroidCli.TASK_SKILLS_FIND shouldBe "android-skills-find"
                AndroidCli.TASK_SKILLS_ADD shouldBe "android-skills-add"
                AndroidCli.TASK_SKILLS_REMOVE shouldBe "android-skills-remove"
                AndroidCli.TASK_RUN shouldBe "android-run"
            }
            then("property keys are namespaced") {
                AndroidCli.PROP_SKILL shouldBe "zone.clanker.android-cli.skill"
                AndroidCli.PROP_QUERY shouldBe "zone.clanker.android-cli.query"
                AndroidCli.PROP_ARGS shouldBe "zone.clanker.android-cli.args"
                AndroidCli.PROP_JSON shouldBe "zone.clanker.android-cli.json"
            }
        }

        given("AndroidCli.SettingsExtension") {
            val ext = AndroidCli.SettingsExtension()

            `when`("created with defaults") {
                then("binary defaults to android") {
                    ext.binary shouldBe "android"
                }
                then("extraArgs defaults to empty") {
                    ext.extraArgs shouldBe emptyList()
                }
            }

            `when`("properties are set") {
                then("binary is mutable") {
                    ext.binary = "/opt/android/bin/android"
                    ext.binary shouldBe "/opt/android/bin/android"
                }
                then("extraArgs is mutable") {
                    ext.extraArgs = listOf("--verbose")
                    ext.extraArgs shouldBe listOf("--verbose")
                }
            }
        }

        given("AndroidCli.registerTasks") {
            val project = ProjectBuilder.builder().build()
            val ext =
                project.extensions.create(
                    AndroidCli.EXTENSION_NAME,
                    AndroidCli.SettingsExtension::class.java,
                )
            AndroidCli.registerTasks(project, ext)

            `when`("tasks are registered") {
                then("all tasks exist") {
                    project.tasks.findByName(AndroidCli.TASK_VERSION).shouldNotBeNull()
                    project.tasks.findByName(AndroidCli.TASK_DESCRIBE).shouldNotBeNull()
                    project.tasks.findByName(AndroidCli.TASK_SCREEN_CAPTURE).shouldNotBeNull()
                    project.tasks.findByName(AndroidCli.TASK_SKILLS_LIST).shouldNotBeNull()
                    project.tasks.findByName(AndroidCli.TASK_SKILLS_FIND).shouldNotBeNull()
                    project.tasks.findByName(AndroidCli.TASK_SKILLS_ADD).shouldNotBeNull()
                    project.tasks.findByName(AndroidCli.TASK_SKILLS_REMOVE).shouldNotBeNull()
                    project.tasks.findByName(AndroidCli.TASK_RUN).shouldNotBeNull()
                }

                then("tasks have correct group") {
                    project.tasks.findByName(AndroidCli.TASK_VERSION)!!.group shouldBe AndroidCli.GROUP
                    project.tasks.findByName(AndroidCli.TASK_DESCRIBE)!!.group shouldBe AndroidCli.GROUP
                    project.tasks.findByName(AndroidCli.TASK_SKILLS_ADD)!!.group shouldBe AndroidCli.GROUP
                }

                then("tasks have correct types") {
                    project.tasks
                        .findByName(AndroidCli.TASK_VERSION)!!
                        .shouldBeInstanceOf<AndroidVersionTask>()
                    project.tasks
                        .findByName(AndroidCli.TASK_DESCRIBE)!!
                        .shouldBeInstanceOf<AndroidDescribeTask>()
                    project.tasks
                        .findByName(AndroidCli.TASK_SCREEN_CAPTURE)!!
                        .shouldBeInstanceOf<AndroidScreenCaptureTask>()
                    project.tasks
                        .findByName(AndroidCli.TASK_SKILLS_LIST)!!
                        .shouldBeInstanceOf<AndroidSkillsListTask>()
                    project.tasks
                        .findByName(AndroidCli.TASK_SKILLS_FIND)!!
                        .shouldBeInstanceOf<AndroidSkillsFindTask>()
                    project.tasks
                        .findByName(AndroidCli.TASK_SKILLS_ADD)!!
                        .shouldBeInstanceOf<AndroidSkillsAddTask>()
                    project.tasks
                        .findByName(AndroidCli.TASK_SKILLS_REMOVE)!!
                        .shouldBeInstanceOf<AndroidSkillsRemoveTask>()
                    project.tasks
                        .findByName(AndroidCli.TASK_RUN)!!
                        .shouldBeInstanceOf<AndroidRunTask>()
                }

                then("tasks have extension wired") {
                    val versionTask = project.tasks.findByName(AndroidCli.TASK_VERSION) as AndroidVersionTask
                    versionTask.extension shouldBe ext
                    val addTask = project.tasks.findByName(AndroidCli.TASK_SKILLS_ADD) as AndroidSkillsAddTask
                    addTask.extension shouldBe ext
                }

                then("extension is registered") {
                    project.extensions
                        .findByType(AndroidCli.SettingsExtension::class.java)
                        .shouldNotBeNull()
                }
            }
        }

        given("AndroidCli.SettingsPlugin") {
            `when`("instantiated") {
                then("it is not null") {
                    AndroidCli.SettingsPlugin().shouldNotBeNull()
                }
            }
        }

        given("AndroidCli architecture") {
            then("AndroidCli is a data object") {
                AndroidCli::class.isData shouldBe true
                AndroidCli::class.objectInstance.shouldNotBeNull()
            }
            then("SettingsPlugin is inside AndroidCli") {
                AndroidCli.SettingsPlugin::class.java.enclosingClass shouldBe AndroidCli::class.java
            }
            then("SettingsExtension is inside AndroidCli") {
                AndroidCli.SettingsExtension::class.java.enclosingClass shouldBe AndroidCli::class.java
            }
        }
    })
