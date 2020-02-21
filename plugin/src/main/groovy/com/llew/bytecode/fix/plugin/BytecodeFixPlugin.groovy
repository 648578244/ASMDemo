package com.llew.bytecode.fix.plugin

import com.android.build.gradle.AppExtension
import com.llew.bytecode.fix.transform.AsmInjectTrans
import org.gradle.api.Plugin;
import org.gradle.api.Project;
class BytecodeFixPlugin implements Plugin<Project> {
    @Override
   void apply(Project project){
        println( "初始化插件")
        if(project.hasProperty("android")){
            project.android.registerTransform(new AsmInjectTrans())
        }
   }
}


