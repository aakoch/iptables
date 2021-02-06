package com.adamkoch.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that specifies that an element is not yet stable and may change without notice.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Unstable {
}
