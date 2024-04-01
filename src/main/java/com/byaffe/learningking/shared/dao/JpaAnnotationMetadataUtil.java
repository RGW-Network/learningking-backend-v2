package com.byaffe.learningking.shared.dao;


import com.googlecode.genericdao.search.Metadata;
import com.googlecode.genericdao.search.MetadataUtil;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.io.Serializable;

@Component
public class JpaAnnotationMetadataUtil implements MetadataUtil {

    public JpaAnnotationMetadataUtil() {
    }

    public Metadata get(Class<?> klass) throws IllegalArgumentException {
        return JpaAnnotationMetadata.getMetadata(klass);
    }

    public Metadata get(Class<?> rootEntityClass, String propertyPath) throws IllegalArgumentException {
        Metadata md = this.get(rootEntityClass);
        if (propertyPath != null && !propertyPath.equals("")) {
            String[] var4 = propertyPath.split("\\.");
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String prop = var4[var6];
                if ("id".equals(prop)) {
                    md = md.getIdType();
                } else {
                    md = md.getPropertyType(prop);
                }

                if (md == null) {
                    throw new IllegalArgumentException("Property path '" + propertyPath + "' invalid for type " + rootEntityClass.getName());
                }
            }

            return md;
        } else {
            return md;
        }
    }

    public Serializable getId(Object object) {
        Metadata md = this.get(object.getClass());
        return md.getIdValue(object);
    }

    public boolean isId(Class<?> rootClass, String propertyPath) {
        if (propertyPath != null && !"".equals(propertyPath)) {
            if (propertyPath.equals("id") || propertyPath.endsWith(".id") && this.get(rootClass, propertyPath.substring(0, propertyPath.length() - 3)).isEntity()) {
                return true;
            } else {
                int pos = propertyPath.lastIndexOf(".");
                if (pos != -1) {
                    Metadata parentType = this.get(rootClass, propertyPath.substring(0, pos));
                    return !parentType.isEntity() ? false : propertyPath.substring(pos + 1).equals(parentType.getIdProperty());
                } else {
                    return propertyPath.equals(this.get(rootClass).getIdProperty());
                }
            }
        } else {
            return false;
        }
    }

    public <T> Class<T> getUnproxiedClass(Class<?> klass) {
        while (true) {
            if (klass.getAnnotation(Entity.class) == null) {
                klass = klass.getSuperclass();
                if (!Object.class.equals(klass)) {
                    continue;
                }

                return null;
            }
            return (Class<T>) klass;
        }
    }

    public <T> Class<T> getUnproxiedClass(Object entity) {
        return this.getUnproxiedClass(entity.getClass());
    }
}
