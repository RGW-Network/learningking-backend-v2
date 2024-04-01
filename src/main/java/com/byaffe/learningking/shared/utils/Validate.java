package com.byaffe.learningking.shared.utils;

import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

import static java.lang.String.format;

/**
 * Adapted from Spring Assert class but throws BadRequest exceptions
 */
public abstract class Validate {

    /**
     * Assert a boolean expression, throwing an {@code ValidationFailedException} if
     * the expression evaluates to {@code false}.
     * <p>
     * Call {@link #isTrue} if you wish to throw an
     * {@code ValidationFailedException} on an assertion failure.
     *
     * <pre class="code">
     * Assert.state(id == null, &quot;The id property must not already be initialized&quot;);
     * </pre>
     *
     * @param expression a boolean expression
     * @param message    the exception message to use if the assertion fails
     * @throws ValidationFailedException if {@code expression} is {@code false}
     */
    public static void check(Boolean expression, String message, Object... params) throws ValidationFailedException {
        isTrue(expression, format(message, params));
    }

    /**
     * Assert a boolean expression, throwing an {@code ValidationFailedException} if
     * the expression evaluates to {@code false}.
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
     * </pre>
     *
     * @param expression a boolean expression
     * @param message    the exception message to use if the assertion fails
     * @throws ValidationFailedException if {@code expression} is {@code false}
     */
    public static void isTrue(Boolean expression, String message, Object... params) throws ValidationFailedException {
        if (!Boolean.TRUE.equals(expression)) {
            throw new ValidationFailedException(format(message, params));
        }
    }

    /**
     * Assert that an object is {@code null}.
     *
     * <pre class="code">
     * Assert.isNull(value, &quot;The value must be null&quot;);
     * </pre>
     *
     * @param object  the object to check
     * @param message the exception message to use if the assertion fails
     * @throws ValidationFailedException if the object is not {@code null}
     */
    public static void isNull(Object object, String message, Object... args) throws ValidationFailedException {
        if (object != null) {
            throw new ValidationFailedException(format(message, args));
        }
        if (object instanceof String && StringUtils.isBlank(object.toString())) {
            throw new ValidationFailedException(format(message, args));
        }
    }

    /**
     * Assert that an object is not {@code null}.
     *
     * <pre class="code">
     * Assert.notNull(clazz, &quot;The class must not be null&quot;);
     * </pre>
     *
     * @param object  the object to check
     * @param message the exception message to use if the assertion fails
     * @throws ValidationFailedException
     * @throws ValidationFailedException if the object is {@code null}
     */
    public static void notNull(Object object, String message, Object... args) throws ValidationFailedException {
        if (object == null) {
            throw new ValidationFailedException(format(message, args));
        }
        if (object instanceof String && StringUtils.isBlank(object.toString())) {
            throw new ValidationFailedException(format(message, args));
        }
    }

    /**
     * Assert that the given String is not empty; that is, it must not be
     * {@code null} and not the empty String.
     *
     * <pre class="code">
     * Assert.hasLength(name, &quot;Name must not be empty&quot;);
     * </pre>
     *
     * @param text    the String to check
     * @param message the exception message to use if the assertion fails
     * @see StringUtils#isNoneEmpty(CharSequence...)
     * @throws ValidationFailedException if the text is empty
     */
    public static void hasLength(String text, String message) throws ValidationFailedException {
        if (!StringUtils.isNotEmpty(text)) {
            throw new ValidationFailedException(message);
        }
    }

    /**
     * Assert that the given String contains valid text content; that is, it must
     * not be {@code null} and must contain at least one non-whitespace character.
     *
     * <pre class="code">
     * Assert.hasText(name, &quot;'name' must not be empty&quot;);
     * </pre>
     *
     * @param text    the String to check
     * @param message the exception message to use if the assertion fails
     * @see StringUtils#isNoneEmpty(CharSequence...)
     * @throws ValidationFailedException if the text does not contain valid text
     *                                   content
     */
    public static void hasText(String text, String message) throws ValidationFailedException {
        if (!StringUtils.isNotBlank(text)) {
            throw new ValidationFailedException(message);
        }
    }

    /**
     * Assert that the given text does not contain the given substring.
     *
     * <pre class="code">
     * Assert.doesNotContain(name, &quot;rod&quot;, &quot;Name must not contain 'rod'&quot;);
     * </pre>
     *
     * @param textToSearch the text to search
     * @param substring    the substring to find within the text
     * @param message      the exception message to use if the assertion fails
     * @throws ValidationFailedException if the text contains the substring
     */
    public static void doesNotContain(String textToSearch, String substring, String message)
            throws ValidationFailedException {
        if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring)
                && textToSearch.contains(substring)) {
            throw new ValidationFailedException(message);
        }
    }

    /**
     * Assert that an array contains elements; that is, it must not be {@code null}
     * and must contain at least one element.
     *
     * <pre class="code">
     * Assert.notEmpty(array, &quot;The array must contain elements&quot;);
     * </pre>
     *
     * @param array   the array to check
     * @param message the exception message to use if the assertion fails
     * @throws ValidationFailedException if the object array is {@code null} or
     *                                   contains no elements
     */
    public static void notEmpty(Object[] array, String message) throws ValidationFailedException {
        if (ArrayUtils.isEmpty(array)) {
            throw new ValidationFailedException(message);
        }
    }

    /**
     * Assert that an array contains no {@code null} elements.
     * <p>
     * Note: Does not complain if the array is empty!
     *
     * <pre class="code">
     * Assert.noNullElements(array, &quot;The array must contain non-null elements&quot;);
     * </pre>
     *
     * @param array   the array to check
     * @param message the exception message to use if the assertion fails
     * @throws ValidationFailedException if the object array contains a {@code null}
     *                                   element
     */
    public static void noNullElements(Object[] array, String message) throws ValidationFailedException {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ValidationFailedException(message);
                }
            }
        }
    }

    /**
     * Assert that a collection contains elements; that is, it must not be
     * {@code null} and must contain at least one element.
     *
     * <pre class="code">
     * Assert.notEmpty(collection, &quot;Collection must contain elements&quot;);
     * </pre>
     *
     * @param collection the collection to check
     * @param message    the exception message to use if the assertion fails
     * @throws ValidationFailedException if the collection is {@code null} or
     *                                   contains no elements
     */
    public static void notEmpty(Collection<?> collection, String message) throws ValidationFailedException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ValidationFailedException(message);
        }
    }


}
