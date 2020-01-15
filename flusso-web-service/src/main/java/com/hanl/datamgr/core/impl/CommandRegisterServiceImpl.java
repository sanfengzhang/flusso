package com.hanl.datamgr.core.impl;

import com.hanl.data.common.CommandField;
import com.hanl.datamgr.core.CommandRegisterService;
import com.hanl.datamgr.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.kitesdk.morphline.base.AbstractCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author: Hanl
 * @date :2020/1/15
 * @desc:
 * 主要是方便第三方开发完自己的包之后，可以通过上传Jar
 * 包就可以将命令信息持久化到数据库。
 * 用户只需要关注参数初始化就可以
 */
@Service
@Slf4j
public class CommandRegisterServiceImpl implements CommandRegisterService {

    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new FileSystemResourceLoader());

    private static final String SUPER_COMMAND_CLASS_NAME = AbstractCommand.class.getName();

    @Value("${app.upload.path}")
    private String uploadPath;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerCommands(String... commandJarFileNames) throws BusException {
        //使用Spring实现的类属性查找更快捷,内部使用ASM实现的
        for (String jarFilePath : commandJarFileNames) {
            try {
                //
                String classPath = uploadPath + "/" + jarFilePath + "/**/*.class";
                Resource[] source = resolver.getResources(classPath);
                //不需要缓存
                MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resolver);
                for (Resource resource : source) {
                    if (resource.isReadable()) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        String superClassName = reader.getClassMetadata().getSuperClassName();
                        if (SUPER_COMMAND_CLASS_NAME.equals(superClassName)) {
                            AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                            Set<MethodMetadata> set = annotationMetadata.getAnnotatedMethods(CommandField.class.getName());
                            for (MethodMetadata methodMetadata : set) {
                                Map<String, Object> fieldAnnotation = methodMetadata.getAnnotationAttributes(CommandField.class.getName());
                                String fieldName = fieldAnnotation.get(CommandField.FILED_NAME).toString();
                                String fieldType = fieldAnnotation.get(CommandField.FILED_TYPE).toString();
                                String fieldDesc = fieldAnnotation.get(CommandField.FILED_DESC).toString();
                                log.info("Register Field attr fieldName={},fieldType={},fieldDesc={}", fieldName, fieldType, fieldDesc);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压Jar包
     *
     * @param commandJarFilePath
     * @throws IOException
     */
    private void unzipJarFile(String... commandJarFilePath) throws IOException {
        for (String jarFilePath : commandJarFilePath) {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = jarEntryEnumeration.nextElement();

            }

        }
    }
}
