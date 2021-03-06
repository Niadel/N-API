package co.uk.niadel.napi.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Where you add annotations that are used by N-API or libraries to add Annotation
 * functionality without the complexity of ASM. More performance heavy, however, and you only have read access to the annotation.
 * For this reason I recommend using ASM. All of N-API's annotation based code was moved to ASM a LONG time ago.
 * @author Niadel
 *
 */
public final class AnnotationHandlerRegistry
{
	/**
	 * A Set that contains the Annotation Handlers.
	 */
	private static Set<IAnnotationHandler> annotationHandlers = new HashSet<>();
	
	/**
	 * Handlers that handle method annotations in Mod Registers.
	 */
	private static Set<IAnnotationHandler> methodAnnotationHandlers = new HashSet<>();
	
	/**
	 * Registers an annotation handler.
	 * @param handler The handler to register.
	 */
	public static final void addAnnotationHandler(IAnnotationHandler handler)
	{
		annotationHandlers.add(handler);
	}
	
	/**
	 * Adds a handler that handles method annotations in Mod Registers.
	 * @param handler The handler to register.
	 */
	public static final void addMethodAnnotationHandler(IAnnotationHandler handler)
	{
		methodAnnotationHandlers.add(handler);
	}
	
	/**
	 * Calls all method annotation handlers.
	 * @param annotations The annotations of method.
	 * @param method The method that has the annotations of annotations.
	 * @param register The register that owns method.
	 */
	@Internal
	public static final void callAllMethodHandlers(Annotation[] annotations, Method method, Object register)
	{
		for (IAnnotationHandler handler : methodAnnotationHandlers)
		{
			handler.handleMethodAnnotations(annotations, method, register);
		}
	}
	
	/**
	 * Returns the annotation handlers in a Set.
	 * @return annotationHandlers
	 */
	@Internal
	public static final Set<IAnnotationHandler> getAnnotationHandlers()
	{
		return annotationHandlers;
	}

	/**
	 * Gets the method annotation handlers.
	 * @return methodAnnotationHandlers
	 */
	public static final Set<IAnnotationHandler> getMethodAnnotationHandlers()
	{
		return methodAnnotationHandlers;
	}
}
