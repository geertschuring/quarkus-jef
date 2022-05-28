package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Qualifier
@Target({ FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
public @interface SPI {
    @Nonbinding
    String name();
}
