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
     * Constructor privado para evitar instancias
     */
    private Util() {
        // Constructor privado para evitar instancias
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

    /**
     * Detecta el tipo de fichero a partir de un array de bytes
     *
     * @param bytes Array de bytes
     * @return Tipo de fichero
     */
    public static String detectFileType(byte[] bytes) {
        if (bytes.length >= 2 && bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) {
            return "jpeg";
        } else if (bytes.length >= 8 &&
                bytes[0] == (byte) 0x89 &&
                bytes[1] == (byte) 0x50 &&
                bytes[2] == (byte) 0x4E &&
                bytes[3] == (byte) 0x47 &&
                bytes[4] == (byte) 0x0D &&
                bytes[5] == (byte) 0x0A &&
                bytes[6] == (byte) 0x1A &&
                bytes[7] == (byte) 0x0A) {
            return "png";
        } else if (bytes.length >= 6 &&
                bytes[0] == (byte) 0x47 &&
                bytes[1] == (byte) 0x49 &&
                bytes[2] == (byte) 0x46 &&
                bytes[3] == (byte) 0x38) {
            return "gif";
        } else {
            return "application/octet-stream";
        }
    }
}
