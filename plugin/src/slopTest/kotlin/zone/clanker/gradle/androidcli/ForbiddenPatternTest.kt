package zone.clanker.gradle.androidcli

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.BehaviorSpec

class ForbiddenPatternTest :
    BehaviorSpec({
        given("main source set") {
            val scope = Konsist.scopeFromProduction(moduleName = "plugin")

            then("no println calls") {
                scope.files.assertFalse {
                    it.text.contains("println(")
                }
            }

            then("no System.err usage") {
                scope.files.assertFalse {
                    it.text.contains("System.err")
                }
            }

            then("no System.out usage") {
                scope.files.assertFalse {
                    it.text.contains("System.out")
                }
            }

            then("no wildcard imports") {
                scope.imports.assertFalse {
                    it.name.endsWith(".*")
                }
            }
        }
    })
