package com.mygame.android.json.templete.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)  
public @interface JsonField {

	public String name() default "";
	public Class type() default Void.class;
	
	public Class entity() default Void.class;
}
