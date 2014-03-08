package com.erigir.oz.feature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method that accepts an OutputStream with this to receive a stream that you can write to, and others can
 * read current state of
 *
 *
 * Should look like:
 * @NamedOutput("excelFileOutput")
 * public void setExcelFileOutput(OutputStream os)
 *
 * Your process can then write to this stream, and the monitoring system can read from it - commonly used for
 * writing logs of ongoing status, or for writing known end-progress
 *
 *
 * User: chrweiss
 * Date: 3/7/14
 * Time: 11:27 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NamedOutput {
    String value();
}
