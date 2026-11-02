plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

// Required to exercise Angelica's SDL-GPU backend in runClient25.
minecraft {
    lwjgl3Bindings.addAll("shaderc", "spvc")
}

apply(from = "https://raw.githubusercontent.com/AngelicIndustries/67minecraft-gradle-publish/${property("publish67ScriptTag")}/67minecraft-publish.gradle.kts")
