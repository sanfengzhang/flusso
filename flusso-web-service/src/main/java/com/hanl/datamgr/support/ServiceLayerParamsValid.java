package com.hanl.datamgr.support;

import com.google.common.collect.Lists;
import com.hanl.datamgr.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
//@Aspect
//@Component
@Slf4j
public class ServiceLayerParamsValid {

    private Validator validator;


    public ServiceLayerParamsValid() {
        //手动指定校验提示资源（默认在resource目录下ValidationMessages.properties）
        validator = Validation.byDefaultProvider().configure().messageInterpolator(new ResourceBundleMessageInterpolator(
                new PlatformResourceBundleLocator("validationMessages"))).buildValidatorFactory().getValidator();
    }

    // 定义接口参数校验切入点
    @Pointcut("@annotation(org.springframework.validation.annotation.Validated))")
    private void validateMethod() {
    }

    @Before("validateMethod()")
    public void before(JoinPoint joinPoint) throws BusException {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 执行方法参数的校验
        Set<ConstraintViolation<Object>> constraintViolations = validator.forExecutables().validateParameters(joinPoint.getThis(), signature.getMethod(), args);
        List<String> messages = Lists.newArrayList();
        for (ConstraintViolation<Object> error : constraintViolations) {
            messages.add(error.getMessage());
        }
        if (!messages.isEmpty()) {
            throw new BusException(messages.toString());
        }
    }
}
