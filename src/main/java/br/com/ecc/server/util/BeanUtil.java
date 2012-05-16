package br.com.ecc.server.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class BeanUtil {

	/**
	 * Copia do src para dest todas as propriedades que tenham getters e setters caso
	 * não estejam anotadas com @Inject.
	 * Indicado para quando você não quer que o obj de destino seja criado com reflection.
	 * Ex: voce vai passar um object criado via Guice.
	 * @param src
	 * @param dest
	 * @return src populado 
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T copyProperties(T src, T dest) {
		Class cmdClass = src.getClass();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(cmdClass, Object.class);
			for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
				Annotation injectAnnotation = null;
				try {
					Field field = cmdClass.getDeclaredField(propertyDescriptor.getName());
					injectAnnotation = field.getAnnotation(Inject.class);
				} catch (SecurityException e) {
				} catch (NoSuchFieldException e) {
				}
				if (injectAnnotation == null) {
					Method readMethod = propertyDescriptor.getReadMethod();
					Method writeMethod = propertyDescriptor.getWriteMethod();
					if (readMethod != null && writeMethod != null) {
						try {
							Object returnValue = readMethod.invoke(src);
							writeMethod.invoke(dest, returnValue);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		
		return dest;
	}
	
	/**
	 * Cria um objeto de retorno via Guice e copia do src todas as propriedades que tenham 
	 * getters e setters caso não estejam anotadas com @Inject.
	 * @param <T>
	 * @param src populado 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(Injector injector, T src) {
		T dest = (T) injector.getInstance(src.getClass());
		return copyProperties(src, dest);
	}

	/**
	 * Cria um objeto de retorno via reflection e copia do src todas as propriedades que tenham 
	 * getters e setters caso não estejam anotadas com @Inject.
	 * @param <T>
	 * @param src populado 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(T src) {
		T dest =  null;
		try {
			dest = (T) src.getClass().newInstance();
			dest = copyProperties(src, dest);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return dest;
	}
}
