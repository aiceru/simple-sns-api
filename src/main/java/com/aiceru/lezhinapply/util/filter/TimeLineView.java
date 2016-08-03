package com.aiceru.lezhinapply.util.filter;

import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.jersey.message.filtering.EntityFiltering;

import java.lang.annotation.*;

/**
 * Created by iceru on 2016. 8. 3..
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EntityFiltering
public @interface TimeLineView {
  public static class Factory extends AnnotationLiteral<TimeLineView> implements TimeLineView {
    private Factory() {
    }

    public static TimeLineView get() {
      return new Factory();
    }
  }
}
