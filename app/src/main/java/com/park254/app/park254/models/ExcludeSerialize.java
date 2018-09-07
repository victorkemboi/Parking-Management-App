package com.park254.app.park254.models;

/**
 * Annotation for decorating fields to be ignored during login
 */
public @interface ExcludeSerialize {
    String info() default "";
}
