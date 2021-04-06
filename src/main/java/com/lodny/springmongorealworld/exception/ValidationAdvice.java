package com.lodny.springmongorealworld.exception;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@Aspect
// @RestControllerAdvice
public class ValidationAdvice {

  @Around("execution(* com.lodny.springmongorealworld..*Controller.*(..))")
  public Object validCheck(ProceedingJoinPoint joinPoint) throws Throwable {
    // String type = joinPoint.getSignature().getDeclaringTypeName();
    // String method = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    // System.out.println("type : " + type);
    // System.out.println("method : " + method);

    for (Object arg : args) {

      if (!(arg instanceof BindingResult) || !((BindingResult) arg).hasErrors()) {
        continue;
      }

      System.out.println("> ValidationAdvice : arg : " + arg);
      // HttpServletRequest request
      //   = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

      // System.out.println("ValidationAdvice : arg : " + arg);

      ErrorDto errors = new ErrorDto();
      ((BindingResult) arg).getFieldErrors().forEach(err -> {
        System.out.println("> ValidationAdvice : err : " + err.getField() + ", msg : " + err.getDefaultMessage());
        errors.add(err.getField(), err.getDefaultMessage());
      });

      // System.out.println(">>> ValidationAdvice : validCheck() : Error ~~~" + errors);
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // System.out.println("> ValidationAdvice : validCheck() : NO Error");

    return joinPoint.proceed();
  }
}
