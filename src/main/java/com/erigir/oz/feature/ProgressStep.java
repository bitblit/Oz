package com.erigir.oz.feature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a public method with this if it can return a double value between 0 and 1 to display the progress
 *
 * Should look like:
 * @ProgressMonitor
 * public double myProgress()
 *
 * User: chrweiss
 * Date: 3/7/14
 * Time: 11:27 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProgressStep {
}
