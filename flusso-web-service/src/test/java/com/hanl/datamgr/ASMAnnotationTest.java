package com.hanl.datamgr;


import org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
public class ASMAnnotationTest {
    public static class MyClassVisitor extends ClassVisitor {
        public MyClassVisitor() {
            super(Opcodes.ASM5);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {

            return new MyFieldVisitor(super.visitField(access, name, desc, signature, value));
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return super.visitAnnotation(desc, visible);
        }
    }

    public static class MyFieldVisitor extends FieldVisitor {
        public MyFieldVisitor(FieldVisitor fieldVisitor) {
            super(Opcodes.ASM5, fieldVisitor);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

            return new MyAnnotationVisitor(super.visitAnnotation(desc, visible));
        }
    }

    public static class MyAnnotationVisitor extends AnnotationVisitor {

        public MyAnnotationVisitor(AnnotationVisitor av) {
            super(Opcodes.ASM5, av);
        }

        /**
         * 读取注解的值
         */
        @Override
        public void visit(String name, Object value) {
            super.visit(name, value);
            System.out.println(name + " = " + value);
        }

        /*
         * 注解枚举的类型的值
         */
        @Override
        public void visitEnum(String name, String desc, String value) {
            super.visitEnum(name, desc, value);
            System.out.println("name =" + name + ", desc=" + desc + " , value=" + value);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String name, String desc) {
            return super.visitAnnotation(name, desc);
        }

    }


    public static void main(String[] args) throws Exception {
        ClassReader classReader = new ClassReader(new FileInputStream(new File("D:/eclipse-workspace/flusso/flusso/flusso-web-service/target/test-classes/com/hanl/datamgr/CommandPo.class")));
        classReader.accept(new MyClassVisitor(), ClassReader.EXPAND_FRAMES);
    }
}
