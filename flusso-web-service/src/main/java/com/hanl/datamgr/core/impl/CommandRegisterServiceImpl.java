package com.hanl.datamgr.core.impl;

import com.hanl.data.common.CommandDescription;
import com.hanl.data.common.CommandField;
import com.hanl.datamgr.core.CommandRegisterService;
import com.hanl.datamgr.entity.CommandEntity;
import com.hanl.datamgr.entity.CommandParamEntity;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.support.asm.FlussoAsmAnnotation;
import com.hanl.datamgr.support.asm.FlussoClassVisitor;
import lombok.extern.slf4j.Slf4j;
import org.kitesdk.morphline.base.AbstractCommand;
import org.springframework.asm.ClassReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author: Hanl
 * @date :2020/1/15
 * @desc: 主要是方便第三方开发完自己的包之后，可以通过上传Jar
 * 包就可以将命令信息持久化到数据库。
 * 用户只需要关注参数初始化就可以
 */
@Service
@Slf4j
public class CommandRegisterServiceImpl implements CommandRegisterService {

    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new FileSystemResourceLoader());

    private static final String COMMAND_SUPER_CLASS_NAME = AbstractCommand.class.getName();


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
                for (Resource resource : source) {
                    if (resource.isReadable()) {
                        CommandEntity commandEntity = new CommandEntity();
                        ClassReader classReader = new ClassReader(resource.getInputStream());
                        FlussoClassVisitor flussoClassVisitor = new FlussoClassVisitor();
                        classReader.accept(flussoClassVisitor, ClassReader.EXPAND_FRAMES);
                        String superClassName = classReader.getSuperName().replaceAll("/", ".");
                        if (COMMAND_SUPER_CLASS_NAME.equals(superClassName)) {
                            String clazzName = classReader.getClassName();
                            clazzName = clazzName.replaceAll("/", ".").substring(0, clazzName.lastIndexOf("$")) + "Builder";
                            commandEntity.setCommandClazz(clazzName);
                            Iterator<FlussoAsmAnnotation> classAnnotations = flussoClassVisitor.getClazzAnnotations().iterator();
                            while (classAnnotations.hasNext()) {
                                FlussoAsmAnnotation flussoAsmAnnotation = classAnnotations.next();
                                if (CommandDescription.class.getName().equals(flussoAsmAnnotation.getAnnotationClassName())) {
                                    commandEntity.setCommandName(flussoAsmAnnotation.getAttr(CommandDescription.NAME).toString());
                                    commandEntity.setCommandMorphName(flussoAsmAnnotation.getAttr(CommandDescription.MORPH_NAME).toString());
                                    break;
                                }
                            }

                            Set<CommandParamEntity> commandParamEntityList = new HashSet<>();
                            Iterator<FlussoAsmAnnotation> fieldAnnotations = flussoClassVisitor.getFieldAnnotations().iterator();
                            while (fieldAnnotations.hasNext()) {
                                FlussoAsmAnnotation flussoAsmAnnotation = fieldAnnotations.next();
                                if (CommandField.class.getName().equals(flussoAsmAnnotation.getAnnotationClassName())) {
                                    CommandParamEntity commandParamEntity = new CommandParamEntity();
                                    commandParamEntity.setFieldName(flussoAsmAnnotation.getAttr(CommandField.FILED_NAME).toString());
                                    commandParamEntity.setFieldType(flussoAsmAnnotation.getAttr(CommandField.FILED_TYPE).toString());
                                    commandParamEntityList.add(commandParamEntity);
                                }
                            }
                            commandEntity.setCmdParams(commandParamEntityList);
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
