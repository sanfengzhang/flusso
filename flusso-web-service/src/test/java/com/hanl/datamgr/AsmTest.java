package com.hanl.datamgr;

import org.kitesdk.morphline.api.CommandParam;
import com.hanl.datamgr.support.asm.FlussoClassVisitor;
import org.junit.Test;
import org.springframework.asm.ClassReader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.CollectionUtils;


import javax.persistence.Column;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
public class AsmTest {

    @Test
    public void testAsm() throws Exception {
        ClassReader classReader = new ClassReader(new FileInputStream(new File("D:/eclipse-workspace/flusso/flusso/flusso-web-service/target/test-classes/com/hanl/datamgr/UserPo$User.class")));
        FlussoClassVisitor flussoClassVisitor = new FlussoClassVisitor();
        classReader.accept(flussoClassVisitor, ClassReader.EXPAND_FRAMES);
        System.out.println(flussoClassVisitor.getClazzAnnotations().toString());
        System.out.println(flussoClassVisitor.getFieldAnnotations().toString());
        System.out.println(classReader.getSuperName());
    }

    @Test
    public void testSpringAsm() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new FileSystemResourceLoader());
        String classPath = "D:/eclipse-workspace/flusso/flusso/flusso-web-service/target" + "/**/*Po.class";
        Resource[] source = resolver.getResources(classPath);
        //不需要缓存
        MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resolver);
        for (Resource resource : source) {
            if (resource.isReadable()) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                Set<MethodMetadata> set = annotationMetadata.getAnnotatedMethods(Column.class.getName());
                System.out.println(annotationMetadata.getAllAnnotationAttributes(CommandParam.class.getName()));
                for (MethodMetadata methodMetadata : set) {
                    Map<String, Object> fieldAnnotation = methodMetadata.getAnnotationAttributes(Column.class.getName());
                    if (!CollectionUtils.isEmpty(fieldAnnotation)) {
                        System.out.println(fieldAnnotation.toString());
                    }
                }

            }
        }

    }
}
