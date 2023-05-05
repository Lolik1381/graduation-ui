package ru.stankin.compose.core.util

enum class Route(val path: String, val scaffoldVisible: Boolean, var role: Role = Role.ALL, val defaultRoute: Boolean = false) {
    AUTH("auth", false),
    CHANGE_PASSWORD("change-password", false, Role.ALL),
    TASK_INFO("task-info", true, Role.USER, true),
    TASK("task/{taskId}", true, Role.USER),
    USER_PROFILE("user-profile", true, Role.USER),
    ADMIN_PROFILE("admin-profile", true, Role.ADMIN),

//    TASK_TEMPLATE_UPDATE("task-template-update/{taskTemplateId}", true, Role.ADMIN),
    CREATE_PAGE("create-page", true, Role.ADMIN),
    TASK_CREATE("task-create/{taskTemplateId}", true, Role.ADMIN),
    TASK_SCHEDULER_CREATE("task-scheduler-create/{taskTemplateId}", true, Role.ADMIN),

    /**
     * Шаблоны заданий
     */
    TASK_TEMPLATE_LIST("taskTemplateList", true, Role.ADMIN, true),
    TASK_TEMPLATE("taskTemplate?taskTemplateId={taskTemplateId}", true, Role.ADMIN),

    CREATE_USAGE("createUsage", false, Role.ADMIN),
    CHANGE_USAGE("changeUsage", false, Role.ADMIN),
    CREATE_GROUP("createGroup", false, Role.ADMIN),
    CHANGE_GROUP("changeGroup", false, Role.ADMIN),
    CREATE_EQUIPMENT("createEquipment", false, Role.ADMIN),
    CHANGE_EQUIPMENT("changeEquipment", false, Role.ADMIN)
}

fun Route.replacePathVariable(value: String?): String {
    return this.path.replace(Regex("\\{[\\w\\d]+\\}"), value.orEmpty())
}