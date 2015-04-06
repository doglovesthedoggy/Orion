package com.rsorion.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Manifest {
	String name();

	String author() default "Matt";

	String description();

	String image();
}
