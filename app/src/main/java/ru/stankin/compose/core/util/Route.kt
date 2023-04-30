package ru.stankin.compose.core.util

enum class Route(val path: String, val scaffoldVisible: Boolean, var role: Role = Role.ALL, val defaultRoute: Boolean = false) {
    AUTH("auth", false),
    TASK_INFO("task-info", true, Role.USER, true),
    TASK("task/{taskId}", true, Role.USER),
    USER_PROFILE("user-profile", true, Role.USER),
    ADMIN_PROFILE("admin-profile", true, Role.ADMIN),
    TASK_TEMPLATE("task-template", true, Role.ADMIN, true),
    TASK_TEMPLATE_CREATE("task-template-create", true, Role.ADMIN),
    TASK_TEMPLATE_UPDATE("task-template-update/{taskTemplateId}", true, Role.ADMIN),
    CREATE_PAGE("create-page", true, Role.ADMIN),
    TASK_CREATE("task-create/{taskTemplateId}", true, Role.ADMIN),
    TASK_SCHEDULER_CREATE("task-scheduler-create/{taskTemplateId}", true, Role.ADMIN)
}

fun Route.replacePathVariable(value: String?): String {
    return this.path.replace(Regex("\\{[\\w\\d]+\\}"), value.orEmpty())
}