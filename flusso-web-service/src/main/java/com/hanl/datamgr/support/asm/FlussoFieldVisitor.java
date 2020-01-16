package com.hanl.datamgr.support.asm;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;

import java.util.List;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
public class FlussoFieldVisitor extends FieldVisitor {

    private String name;

    private List<FlussoAsmAnnotation> annotations;


    public FlussoFieldVisitor(FieldVisitor fieldVisitor) {
        super(Opcodes.ASM5, fieldVisitor);
    }

    public FlussoFieldVisitor(FieldVisitor fieldVisitor, String name, List<FlussoAsmAnnotation> annotations) {
        super(Opcodes.ASM5, fieldVisitor);
        this.name = name;
        this.annotations = annotations;
    }


    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc != null) {
            String annotationClassName = Type.getType(desc).getClassName();
            FlussoAsmAnnotation flussoAsmAnnotation = new FlussoAsmAnnotation(annotationClassName);
            this.annotations.add(flussoAsmAnnotation);
            return new FlussoAnnotationVisitor(super.visitAnnotation(desc, visible), flussoAsmAnnotation);
        }
        return null;

    }

}
