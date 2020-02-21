package com.llew.bytecode.fix.transform

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.llew.bytecode.fix.Classvisitor.AsmClassVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

class AsmInjectTrans extends  Transform{

    private static final String TAG = "BytecodeFixTransform"

    @Override
    String getName() {
        return TAG
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        transformInvocation.inputs.each {
            TransformInput input ->
                input.directoryInputs.each {
                    DirectoryInput directoryInput ->
                        // 下面是一些判断条件和处理，暂时可以不看
                        if (directoryInput.file.isDirectory()) {
                            directoryInput.file.eachFileRecurse { File file ->
                                def name = file.name
                                if (name.endsWith(".class")
                                        && !name.endsWith("R.class")
                                        && !name.endsWith("BuildConfig.class")
                                        && !name.contains("R\$")

                                ) {
                                    println("==== directoryInput file name == " + file.getAbsolutePath())
                                    // 获取ClassReader，参数是文件的字节数组
                                    ClassReader classReader = new ClassReader(file.bytes)
                                    // 获取ClassWriter，参数1是reader，参数2用于修改类的默认行为，一般传入ClassWriter.COMPUTE_MAXS
                                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                                    //自定义ClassVisitor
                                    AsmClassVisitor classVisitor = new AsmClassVisitor(Opcodes.ASM5, classWriter)
                                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                                    byte[] bytes = classWriter.toByteArray()
                                    File destFile = new File(file.parentFile.absoluteFile, name)
                                    FileOutputStream fileOutputStream = new FileOutputStream(destFile);
                                    fileOutputStream.write(bytes)
                                    fileOutputStream.close()
                                }
                            }
                        }

                        def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                        FileUtils.copyDirectory(directoryInput.file, dest)
                }
                input.jarInputs.each { JarInput jarInput ->
                    def jarName = jarInput.name
                    def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                    if (jarName.endsWith(".jar")) {
                        jarName = jarName.substring(0, jarName.length() - 4)
                    }

                    def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                    FileUtils.copyFile(jarInput.file, dest)
                }
        }
    }
}