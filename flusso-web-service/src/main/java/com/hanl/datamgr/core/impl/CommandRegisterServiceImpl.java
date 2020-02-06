package com.hanl.datamgr.core.impl;

import org.kitesdk.morphline.api.CommandDescription;
import org.kitesdk.morphline.api.CommandParam;
import com.hanl.datamgr.core.CommandRegisterService;
import com.hanl.datamgr.entity.CommandEntity;
import com.hanl.datamgr.entity.CommandParamEntity;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.repository.CommandRepository;
import com.hanl.datamgr.support.asm.FlussoAsmAnnotation;
import com.hanl.datamgr.support.asm.FlussoClassVisitor;
import lombok.extern.slf4j.Slf4j;
import org.kitesdk.morphline.base.AbstractCommand;
import org.springframework.asm.ClassReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
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
public class CommandRegisterServiceImpl implements CommandRegisterService, InitializingBean {

    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new FileSystemResourceLoader());

    private static final String COMMAND_SUPER_CLASS_NAME = AbstractCommand.class.getName();

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private ResourceLoader resourceLoader;


    @Value("${app.upload.path}")
    private String uploadPath;

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources("classpath*:com/hanl/data/transform/command/**/*.class");
        registerCommand(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerCommands(String... commandJarFileNames) throws BusException {
        //使用Spring实现的类属性查找更快捷,内部使用ASM实现的
        for (String jarFilePath : commandJarFileNames) {
            try {
                //
                String classPath = uploadPath + "/" + jarFilePath + "/**/*.class";
                Resource[] source = resolver.getResources(classPath);
                registerCommand(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerCommand(Resource[] source) throws IOException {
        List<CommandEntity> commandEntityList = new ArrayList<>();
        for (Resource resource : source) {
            if (resource.isReadable()) {
                ClassReader classReader = new ClassReader(resource.getInputStream());
                FlussoClassVisitor flussoClassVisitor = new FlussoClassVisitor();
                classReader.accept(flussoClassVisitor, ClassReader.EXPAND_FRAMES);
                String superClassName = classReader.getSuperName().replaceAll("/", ".");
                if (COMMAND_SUPER_CLASS_NAME.equals(superClassName)) {
                    String clazzName = classReader.getClassName();
                    try {
                        clazzName = clazzName.replaceAll("/", ".").substring(0, clazzName.lastIndexOf("$"));
                    } catch (Exception e) {
                        continue;
                    }
                    CommandEntity commandEntity = new CommandEntity();
                    commandEntity.setCommandClazz(clazzName);
                    Iterator<FlussoAsmAnnotation> classAnnotations = flussoClassVisitor.getClazzAnnotations().iterator();
                    while (classAnnotations.hasNext()) {
                        FlussoAsmAnnotation flussoAsmAnnotation = classAnnotations.next();
                        if (CommandDescription.class.getName().equals(flussoAsmAnnotation.getAnnotationClassName())) {
                            commandEntity.setCommandName(flussoAsmAnnotation.getAttr(CommandDescription.NAME).toString());
                            commandEntity.setCommandMorphName(flussoAsmAnnotation.getAttr(CommandDescription.MORPH_NAME).toString());
                            commandEntity.setCommandType(flussoAsmAnnotation.getAttr(CommandDescription.CMD_TYPE).toString());
                            break;
                        }
                    }

                    Set<CommandParamEntity> commandParamEntityList = new HashSet<>();
                    Iterator<FlussoAsmAnnotation> fieldAnnotations = flussoClassVisitor.getFieldAnnotations().iterator();
                    while (fieldAnnotations.hasNext()) {
                        FlussoAsmAnnotation flussoAsmAnnotation = fieldAnnotations.next();
                        if (CommandParam.class.getName().equals(flussoAsmAnnotation.getAnnotationClassName())) {
                            CommandParamEntity commandParamEntity = new CommandParamEntity();
                            commandParamEntity.setParamName(flussoAsmAnnotation.getAttr(CommandParam.PARAM_NAME).toString());
                            commandParamEntity.setParamType(flussoAsmAnnotation.getAttr(CommandParam.PARAM_TYPE).toString());
                            commandParamEntity.setCmdParamAlias(flussoAsmAnnotation.getAttr(CommandParam.PARAM_DISPLAY_NAME).toString());
                            commandEntity.addCommandParamEntity(commandParamEntity);
                        }
                    }
                    commandEntityList.add(commandEntity);
                }

            }
            if (!CollectionUtils.isEmpty(commandEntityList)) {
                commandRepository.saveAll(commandEntityList);
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
