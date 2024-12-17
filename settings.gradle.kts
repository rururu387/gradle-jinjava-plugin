rootProject.name = "gradle-jinjava-plugin"

pluginManagement {
    val pluginPublishVersion: String by settings
    val shadowPluginVersion: String by settings
    plugins {
        id("com.gradle.plugin-publish") version pluginPublishVersion
    }
}