package com.nullers.restbookstore;

import com.nullers.restbookstore.util.Util;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.springframework.test.util.AssertionErrors.assertTrue;

class UtilTest {

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<Util> constructor = Util.class.getDeclaredConstructor();
        assertTrue("El constructor no es privado", Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}