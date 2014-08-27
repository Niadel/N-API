package co.uk.niadel.mpi.modhandler.loadhandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import co.uk.niadel.mpi.annotations.IAnnotationHandler;
import co.uk.niadel.mpi.annotations.MPIAnnotations.Library;
import co.uk.niadel.mpi.annotations.MPIAnnotations.UnstableLibrary;
import co.uk.niadel.mpi.util.NAPILogHelper;

/**
 * The annotation handler used by N-API.
 * @deprecated N-API Annotations are now preferably found via ASM.
 */
@Deprecated
public class AnnotationHandlerNAPI implements IAnnotationHandler
{
	@Override
	public void handleAnnotation(Annotation annotation, Object modRegister)
	{
		if (annotation.annotationType() == UnstableLibrary.class)
		{
			//Tell the user that the library is unstable and mods using it could break
			NAPILogHelper.log("[IMPORTANT] " + ((UnstableLibrary) annotation).specialMessage());
		}
	}

	@Override
	public void handleMethodAnnotations(Annotation[] annotations, Method theMethod, Object modRegister)
	{

	}
}
