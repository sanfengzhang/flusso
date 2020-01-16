package com.hanl.datamgr.support.asm;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Opcodes;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
public class FlussoAnnotationVisitor extends AnnotationVisitor {

    private FlussoAsmAnnotation annotation;

    public FlussoAnnotationVisitor(AnnotationVisitor av) {
        super(Opcodes.ASM5, av);
    }

    public    FlussoAnnotationVisitor(AnnotationVisitor av, FlussoAsmAnnotation annotation) {
        super(Opcodes.ASM5, av);
        this.annotation = annotation;
    }

    /**
     * 读取注解的值
     */
    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        annotation.addAttr(name, value);
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
