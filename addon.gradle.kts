import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

tasks.withType<Test>().configureEach { useJUnit() }
extensions.configure<PublishingExtension> {
    publications.withType<MavenPublication>().configureEach {
        groupId = "industries.angelic.psychedelicraft"
        pom {
            name.set("Psychedelicraft Legacy")
            description.set("Farming, brewing and psychedelic effects for Minecraft 1.7.10.")
            url.set("https://github.com/AngelicIndustries/Psychedelicraft-Legacy")
            licenses { license { name.set("MIT"); url.set("https://opensource.org/license/mit") } }
        }
    }
}

tasks.withType<JavaExec>().configureEach {
    if (name.startsWith("runServer")) {
        doFirst("stripClientOnlyMods") {
            val clientOnlyMods = listOf(
                "angelica", "ModularUI2", "catalogue", "AnExtraTouch", "BlurLegacy",
                "VintageResourcify", "vrap", "euphoria-patches"
            )
            classpath = classpath.filter { file ->
                clientOnlyMods.none { file.name.contains(it, ignoreCase = true) }
            }
        }
    }
}
