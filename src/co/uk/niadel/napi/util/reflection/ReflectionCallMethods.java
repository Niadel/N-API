package co.uk.niadel.napi.util.reflection;

import co.uk.niadel.napi.util.NAPILogHelper;

import java.lang.reflect.*;


/**
 * Allows one to call methods that are usually uncallable. Mainly here to remove all of the
 * boilerplate code that users of Reflection face. This is all actually Reflection code,
 * however.
 * 
 * @author Niadel
 *
 */
public final class ReflectionCallMethods
{
	/**
	 * Creates a class from a string and calls the method specified by methodName.
	 * @param className
	 * @param methodName
	 * @param args
	 */
	public static final void callMethod(String className, String methodName, Object ... args) 
	{
		try
		{
			Class<?> theClass = Class.forName(className);
			Method method = theClass.getDeclaredMethod("methodName", new Class[] {});
			method.setAccessible(true);
			method.invoke(theClass, args);
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			NAPILogHelper.logError(e);
		}
	}
	
	/**
	 * Calls a method with a provided class.
	 * @param classToCallMethod
	 * @param methodName
	 * @param args
	 */
	public static final <X> void callMethod(Class<? extends X> classToCallMethod, String methodName, Object ... args)
	{
		try
		{
			Method method = classToCallMethod.getDeclaredMethod(methodName, new Class[] {});
			method.setAccessible(true);
			method.invoke(classToCallMethod, args);
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			NAPILogHelper.logError(e);
		}
	}

	public static final <X> void callArglessMethod(Class<? extends X> classToCallMethod, String methodName)
	{
		callMethod(classToCallMethod, methodName, new Object[] {});
	}
}
