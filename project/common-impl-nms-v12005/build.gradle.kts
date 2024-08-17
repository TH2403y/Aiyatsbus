repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("ink.ptms.core:v12005:12005:mapped")
    compileOnly(project(":project:common-impl-nms-17"))
    compileOnly("com.mojang:brigadier:1.2.9")
}

// 子模块
taboolib { subproject = true }