package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CreateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@ExtendWith(CreateUserExtension.class)
public @interface DbUser {

  boolean runnable() default true;

  String username() default "";

  String password() default "";
}
