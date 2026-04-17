import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
    id("clkx-module")
    id("clkx-toolchain")
    id("clkx-testing")
    id("clkx-cover")
    id("clkx-konsist")
    id("clkx-detekt")
    id("clkx-ktlint")
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    configure(JavaLibrary(javadocJar = JavadocJar.Empty()))
}
