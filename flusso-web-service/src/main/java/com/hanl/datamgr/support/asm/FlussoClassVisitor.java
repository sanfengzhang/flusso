package com.hanl.datamgr.support.asm;

import org.springframework.asm.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
public class FlussoClassVisitor extends ClassVisitor {

    private List<FlussoAsmAnnotation> fieldAnnotations = new ArrayList<>();

    private List<FlussoAsmAnnotation> clazzAnnotations = new ArrayList<>();

    public FlussoClassVisitor() {

        super(Opcodes.ASM5);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {

        return new FlussoFieldVisitor(super.visitField(access, name, desc, signature, value), name, fieldAnnotations);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        String annotationClassName = Type.getType(desc).getClassName();
        FlussoAsmAnnotation flussoAsmAnnotation = new FlussoAsmAnnotation(annotationClassName);
        clazzAnnotations.add(flussoAsmAnnotation);
        return new FlussoAnnotationVisitor(super.visitAnnotation(desc, visible), flussoAsmAnnotation);
    }

    public List<FlussoAsmAnnotation> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public List<FlussoAsmAnnotation> getClazzAnnotations() {
        return clazzAnnotations;
    }
}