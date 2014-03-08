package com.erigir.oz.feature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method that, if called, will cause this process to abort cleanly.
 *
 * Should look like:
 * @Abort
 * public void abort()
 *
 * TODO: Will oz close and flush streams as soon as this returns?
 *
 *
 * User: chrweiss
 * Date: 3/7/14
 * Time: 11:27 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Abort {
}
