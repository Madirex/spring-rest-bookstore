package com.nullers.restbookstore.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase Util
 */
public class Util {
    /**
     * Constructor privado para evitar instanciación
     */
    private Util() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Obtiene los nombres de las propiedades nulas de un objeto
     *
     * @param source Objeto del que obtener las propiedades nulas
     * @return Array de String con los nombres de las propiedades nulas
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        var pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (var pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
