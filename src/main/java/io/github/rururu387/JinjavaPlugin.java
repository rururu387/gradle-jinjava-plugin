package io.github.rururu387;

import jakarta.annotation.Nonnull;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class JinjavaPlugin implements Plugin<Project> {
    @Override
    public void apply(@Nonnull Project project) {
        project.getExtensions().create("jinjava", JinjavaExtension.class);
        var tasks = project.getTasks().withType(JinjavaTask.class);
        tasks.whenTaskAdded((JinjavaTask task) -> {
            task.setDescription("Template jinja2 files with given parameters and write them");
            task.setGroup("Jinjava");
        });
    }
}
