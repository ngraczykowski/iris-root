package com.silenteight.sep.base.aspects.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.annotation.Validated;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@SuppressWarnings("squid:S1200")
@Aspect
public class ValidationAspect {

  // NOTE(ahaczewski): Defines arguments for a pointcut, where the class a method is declared
  //  in has Spring's @Validated annotation or is REST controller, as these classes get their
  //  methods validated using Spring AOP, and we do not want to validate it twice. Note, that
  //  Spring AOP is unable to advice on private or final methods, therefore those will
  //  get treated by this aspect instead.
  private static final String SPRING_VALIDATED_METHOD_POINTCUT_ARGS =
      "(!private !final * @("
          + " org.springframework.validation.annotation.Validated"
          + " || org.springframework.stereotype.Controller"
          + " || org.springframework.web.bind.annotation.RestController"
          + ") *.*(..))";
  private static final String CALL_TO_SPRING_VALIDATED_METHOD_POINTCUT =
      "call" + SPRING_VALIDATED_METHOD_POINTCUT_ARGS;
  private static final String EXECUTION_OF_SPRING_VALIDATED_METHOD_POINTCUT =
      "execution" + SPRING_VALIDATED_METHOD_POINTCUT_ARGS;

  private ExecutableValidator validator;

  public ValidationAspect() {
    setValidator(null);
  }

  public final void setValidator(@Nullable Validator validator) {
    if (validator == null)
      this.validator = Validation.buildDefaultValidatorFactory().getValidator().forExecutables();
    else
      this.validator = validator.forExecutables();
  }

  @Pointcut("execution(* *.*(..,"
      + " @(javax.validation.Valid"
      + " || javax.validation.constraints..*"
      + " || org.hibernate.validator.constraints..*) (*), "
      + "..))")
  void executionWithValidatedArguments() {
    // Pointcut is empty.
  }

  @Pointcut(EXECUTION_OF_SPRING_VALIDATED_METHOD_POINTCUT)
  void executionOfSpringValidatedMethod() {
    // Pointcut is empty.
  }

  @Before("executionWithValidatedArguments() && !executionOfSpringValidatedMethod()")
  public void validateArguments(JoinPoint joinPoint) {
    MethodSignature methodSignature = getAndCheckMethodSignature(joinPoint);
    Object target = joinPoint.getTarget();

    Class<?>[] groups = determineValidationGroups(methodSignature.getMethod(), target.getClass());

    Method methodToValidate = methodSignature.getMethod();
    Set<ConstraintViolation<Object>> result =
        validator.validateParameters(target, methodToValidate, joinPoint.getArgs(), groups);

    if (!result.isEmpty())
      throw new ConstraintViolationException(result);
  }

  @Nonnull
  private static MethodSignature getAndCheckMethodSignature(JoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    if (Modifier.isStatic(methodSignature.getModifiers()))
      throw new UnsupportedStaticMethodException(methodSignature);

    return methodSignature;
  }

  @Pointcut("@annotation(javax.validation.Valid)")
  void withValidAnnotation() {
    // Pointcut is empty.
  }

  @Pointcut("withValidAnnotation() && call(!void *.*(..)) ")
  void callToValidatedMethod() {
    // Pointcut is empty.
  }

  @Pointcut(CALL_TO_SPRING_VALIDATED_METHOD_POINTCUT)
  void callToSpringValidatedMethod() {
    // Pointcut is empty.
  }

  @AfterReturning(
      pointcut = "callToValidatedMethod() && !callToSpringValidatedMethod()",
      returning = "returnValue",
      argNames = "returnValue, joinPoint")
  public void validateReturnValue(Object returnValue, JoinPoint joinPoint) {
    MethodSignature methodSignature = getAndCheckMethodSignature(joinPoint);
    Object target = joinPoint.getTarget();

    Class<?>[] groups = determineValidationGroups(methodSignature.getMethod(), target.getClass());

    Set<ConstraintViolation<Object>> result =
        validator.validateReturnValue(target, methodSignature.getMethod(), returnValue, groups);

    if (!result.isEmpty())
      throw new ConstraintViolationException(result);
  }

  private static Class<?>[] determineValidationGroups(Method method, Class<?> targetClass) {
    Validated validatedAnn = findAnnotation(method, Validated.class);
    if (validatedAnn == null)
      validatedAnn = findAnnotation(targetClass, Validated.class);

    return (validatedAnn != null ? validatedAnn.value() : new Class<?>[0]);
  }

  @Pointcut("withValidAnnotation() && call(*.new(..))")
  void callToValidatedConstructor() {
    // Pointcut is empty.
  }

  @Around("callToValidatedConstructor() && !callToSpringValidatedMethod()")
  public Object validateConstructorCall(ProceedingJoinPoint pjp) throws Throwable {
    ConstructorSignature ctorSignature = (ConstructorSignature) pjp.getSignature();
    Constructor<?> constructor = ctorSignature.getConstructor();

    Class<?>[] groups = determineValidationGroups(constructor, constructor.getDeclaringClass());

    Set<ConstraintViolation<Object>> result;

    result = validator.validateConstructorParameters(constructor, pjp.getArgs(), groups);
    if (!result.isEmpty())
      throw new ConstraintViolationException(result);

    Object createdObject = pjp.proceed();

    result = validator.validateConstructorReturnValue(constructor, createdObject, groups);
    if (!result.isEmpty())
      throw new ConstraintViolationException(result);

    return createdObject;
  }

  // NOTE(ahaczewski): The "duplication" of determineValidationGroups is to use
  //  cache in AnnotationUtils.findAnnotation(Method, ...), as the
  //  findAnnotation(AnnotatedElement, ...) method does not cache results.
  private static Class<?>[] determineValidationGroups(
      AnnotatedElement annotatedElement, Class<?> targetClass) {

    Validated validatedAnn = findAnnotation(annotatedElement, Validated.class);
    if (validatedAnn == null)
      validatedAnn = findAnnotation(targetClass, Validated.class);

    return (validatedAnn != null ? validatedAnn.value() : new Class<?>[0]);
  }
}
