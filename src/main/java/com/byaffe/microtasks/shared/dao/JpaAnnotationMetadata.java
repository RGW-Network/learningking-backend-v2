package com.byaffe.microtasks.shared.dao;


import com.googlecode.genericdao.search.Metadata;
import com.googlecode.genericdao.search.jpa.JPAAnnotationCollectionMetadata;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;


public class JpaAnnotationMetadata implements Metadata {
    Class<?> klass;
    static Map<Class<?>, JpaAnnotationMetadata> metadataCache = new HashMap();
    Map<String, Property> props;

    private JpaAnnotationMetadata(Class<?> klass) {
        this.klass = klass;
    }

    public static <T> Metadata getMetadata(Type type) {
        Class<?> klass = null;
        Type[] typeArguments = null;
        if (type instanceof ParameterizedType) {
            typeArguments = ((ParameterizedType)type).getActualTypeArguments();
            type = ((ParameterizedType)type).getRawType();
        }

        if (type instanceof Class) {
            klass = (Class)type;
            if (Collection.class.isAssignableFrom(klass)) {
                if (typeArguments != null && typeArguments.length == 1) {
                    if (!(typeArguments[0] instanceof Class)) {
                        throw new IllegalArgumentException("weirdness a third time.");
                    } else {
                        return new JPAAnnotationCollectionMetadata((Class)typeArguments[0], klass);
                    }
                } else {
                    throw new IllegalArgumentException("weirdness again.");
                }
            } else if (klass.isArray()) {
                return new JPAAnnotationCollectionMetadata(klass.getComponentType(), klass);
            } else {
                JpaAnnotationMetadata md = metadataCache.get(klass);
                if (md == null) {
                    md = new JpaAnnotationMetadata(klass);
                    metadataCache.put(klass, md);
                }

                return md;
            }
        } else {
            throw new IllegalArgumentException("weirdness");
        }
    }

    public Class<?> getCollectionClass() {
        return null;
    }

    public String getEntityName() {
        Entity annotation = (Entity)this.klass.getAnnotation(Entity.class);
        if (annotation == null) {
            throw new UnsupportedOperationException("Cannot get Entity Name of non-entity type.");
        } else {
            return annotation.name() != null && !annotation.name().isEmpty() ? annotation.name() : this.klass.getSimpleName();
        }
    }

    public String getIdProperty() {
        Iterator var1 = this.getProps().values().iterator();

        Property prop;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            prop = (Property)var1.next();
        } while(!prop.hasAnnotation(Id.class) && !prop.hasAnnotation(EmbeddedId.class));

        return prop.name;
    }

    public Metadata getIdType() {
        String idProp = this.getIdProperty();
        return idProp != null ? this.getPropertyType(idProp) : null;
    }

    public Serializable getIdValue(Object object) {
        String idProp = this.getIdProperty();
        return idProp != null ? (Serializable)this.getPropertyValue(object, idProp) : null;
    }

    public Class<?> getJavaClass() {
        return this.klass;
    }

    public String[] getProperties() {
        String[] array = new String[this.getProps().size()];
        int i = 0;

        String prop;
        for(Iterator var3 = this.getProps().keySet().iterator(); var3.hasNext(); array[i++] = prop) {
            prop = (String)var3.next();
        }

        return array;
    }

    public Metadata getPropertyType(String property) {
        Property prop = this.getProps().get(property);
        return prop == null ? null : getMetadata(prop.getType());
    }

    public Object getPropertyValue(Object object, String property) {
        Property prop = this.getProps().get(property);
        return prop == null ? null : prop.getValue(object);
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isEmeddable() {
        return null != this.klass.getAnnotation(Embeddable.class);
    }

    public boolean isEntity() {
        return null != this.klass.getAnnotation(Entity.class);
    }

    public boolean isNumeric() {
        return Number.class.isAssignableFrom(this.klass);
    }

    public boolean isString() {
        return String.class.equals(this.klass);
    }

    public synchronized Map<String, Property> getProps() {
        if (this.props != null) {
            return this.props;
        } else {
            this.props = new TreeMap();
            if (!this.isEntity() && !this.isEmeddable()) {
                return this.props;
            } else {
                Field[] var1 = this.klass.getFields();
                int var2 = var1.length;

                int var3;
                for(var3 = 0; var3 < var2; ++var3) {
                    Field field = var1[var3];
                    if (null != field.getDeclaringClass().getAnnotation(Entity.class) || null != field.getDeclaringClass().getAnnotation(Embeddable.class) || null != field.getDeclaringClass().getAnnotation(MappedSuperclass.class)) {
                        this.props.put(field.getName(), new Property(field));
                    }
                }

                Method[] var12 = this.klass.getMethods();
                var2 = var12.length;

                for(var3 = 0; var3 < var2; ++var3) {
                    Method method = var12[var3];
                    String[] name = getterName(method);
                    if (name != null && (null != method.getDeclaringClass().getAnnotation(Entity.class) || null != method.getDeclaringClass().getAnnotation(Embeddable.class) || null != method.getDeclaringClass().getAnnotation(MappedSuperclass.class))) {
                        Property property = this.props.get(name[0]);
                        if (property == null) {
                            property = new Property(name[0]);
                            this.props.put(name[0], property);
                        }

                        property.getter = method;
                        if (property.field == null) {
                            try {
                                property.field = this.klass.getDeclaredField(name[0]);
                                if (!property.field.getGenericType().equals(property.getter.getGenericReturnType())) {
                                    property.field = null;
                                }
                            } catch (SecurityException var10) {
                            } catch (NoSuchFieldException var11) {
                            }
                        }

                        try {
                            property.setter = this.klass.getMethod("set" + name[1], method.getReturnType());
                        } catch (SecurityException var8) {
                        } catch (NoSuchMethodException var9) {
                        }
                    }
                }

                Iterator iterator = this.props.entrySet().iterator();

                while(iterator.hasNext()) {
                    Property property = (Property)((Entry)iterator.next()).getValue();
                    if (property.hasAnnotation(Transient.class)) {
                        iterator.remove();
                    }
                }

                return this.props;
            }
        }
    }

    public static String[] getterName(Method method) {
        if (method.getParameterTypes().length == 0 && method.getReturnType() != null) {
            String name = method.getName();
            if (name.length() > 3 && name.startsWith("get")) {
                name = name.substring(3);
            } else {
                if (name.length() <= 2 || !method.getReturnType().equals(Boolean.class) && !method.getReturnType().equals(Boolean.TYPE) || !name.startsWith("is")) {
                    return null;
                }

                name = name.substring(2);
            }

            return Character.isUpperCase(name.charAt(0)) ? new String[]{name.substring(0, 1).toLowerCase() + name.substring(1), name} : null;
        } else {
            return null;
        }
    }

    private static class Property {
        String name;
        Field field;
        Method getter;
        Method setter;

        public Property(String name) {
            this.name = name;
        }

        public Property(Field field) {
            this.name = field.getName();
            this.field = field;
        }

        public Type getType() {
            if (this.getter != null) {
                return this.getter.getGenericReturnType();
            } else if (this.field != null) {
                return this.field.getGenericType();
            } else {
                return this.setter != null ? this.setter.getGenericParameterTypes()[0] : null;
            }
        }

        public Object getValue(Object o) {
            try {
                if (this.getter != null) {
                    return this.getter.invoke(o);
                } else {
                    return this.field != null ? this.field.get(o) : null;
                }
            } catch (IllegalArgumentException var3) {
                throw new RuntimeException("Unexpected error getting value of property");
            } catch (IllegalAccessException var4) {
                throw new RuntimeException("Unexpected error getting value of property");
            } catch (InvocationTargetException var5) {
                throw new RuntimeException("Unexpected error getting value of property");
            }
        }

        public <T extends Annotation> boolean hasAnnotation(Class<T> annotationClass) {
            return this.getAnnotation(annotationClass) != null;
        }

        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            T val = null;
            if (this.getter != null) {
                val = this.getter.getAnnotation(annotationClass);
            }

            if (val == null && this.field != null) {
                val = this.field.getAnnotation(annotationClass);
            }

            if (val == null && this.setter != null) {
                val = this.setter.getAnnotation(annotationClass);
            }

            return val;
        }
    }
}
