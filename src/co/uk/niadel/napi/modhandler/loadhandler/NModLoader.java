package co.uk.niadel.napi.modhandler.loadhandler;

import co.uk.niadel.napi.asm.ASMRegistry;
import co.uk.niadel.napi.asm.NAPIASMModLocatingTransformer;
import co.uk.niadel.napi.init.Launch;
import co.uk.niadel.napi.modhandler.*;
import co.uk.niadel.napi.util.ModList;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import co.uk.niadel.napi.annotations.AnnotationHandlerRegistry;
import co.uk.niadel.napi.annotations.IAnnotationHandler;
import co.uk.niadel.napi.annotations.VersionMarkingAnnotations.TestFeature;
import co.uk.niadel.napi.annotations.MPIAnnotations.*;
import co.uk.niadel.napi.client.resources.ResourcesRegistry;
import co.uk.niadel.napi.potions.PotionRegistry;
import co.uk.niadel.napi.util.NAPILogHelper;

/**
 * This isn't actually as flexible as FML, but it does most of the same stuff. Flexibility may be improved in a later version of N-API.
 * It is worth noting that this is actually a LOT simpler than FML's loader, which spans a LOT of files, but this one is almost entirely
 * in one file. Well, the main loading code.
 * 
 * @author Niadel
 *
 */
@TestFeature(stable = false, firstAppearance = "1.0")
public class NModLoader extends URLClassLoader
{
	/**
	 * The NModLoader INSTANCE.
	 */
	public static final @Internal NModLoader INSTANCE = new NModLoader(new URL[0], getSystemClassLoader());

	public static final @Internal NAPIASMModLocatingTransformer modLocatingTransformer = new NAPIASMModLocatingTransformer();

	/**
	 * The Minecraft object.
	 */
	public static final Minecraft theMinecraft = Minecraft.getMinecraft();
	
	/**
	 * A list of modids belonging to Forge mods, added so N-API mods can test for Forge mods.
	 */
	public static final Map<String, String> forgeModids = new HashMap<>();

	/**
	 * List of mods that have been found and are scheduled to be loaded/have been loaded.
	 */
	public static final ModList mods = new ModList();

	/**
	 * The main Minecraft directory.
	 */
	public static final File mcMainDir = new File(theMinecraft.mcDataDir.getAbsolutePath().substring(0, theMinecraft.mcDataDir.getAbsolutePath().length() - 1));
	
	/**
	 * The directory for mods to be put in, the same folder Forge uses for convenience.
	 */
	public static final File mcModsDir = new File(theMinecraft.mcDataDir + "/mods/");

	/**
	 * Methods to execute on preInit.
	 */
	public static final Map<String, Method> preInitMethods = new HashMap<>();
	
	/**
	 * Methods to execute on init.
	 */
	public static final Map<String, Method> initMethods = new HashMap<>();
	
	/**
	 * Methods to execute on postInit.
	 */
	public static final Map<String, Method> postInitMethods = new HashMap<>();

	/**
	 * Whether or not N-API should initialise - False if the user's Java version is not 7.
	 */
	public static boolean shouldInit = true;

	public NModLoader(URL[] urls, ClassLoader parent)
	{
		super(urls, parent);
	}

	/**
	 * Looks for whether or not the mod with the specified modId exists.
	 * @param modId The modId to check for.
	 * @return Whether the mod exists.
	 */
	public static final boolean doesModExist(String modId)
	{
		return mods.doesModExist(modId);
	}
	
	/**
	 * Looks for whether or not the library with the specified modId exists.
	 * @param modId The modId to search for.
	 * @return Whether the library exists.
	 */
	public static final boolean doesLibraryExist(String modId)
	{
		return mods.doesListContainLibrary(modId);
	}

	public final Package[] getPackages()
	{
		return super.getPackages();
	}

	public static final void defineClass(String className, byte[] bytes)
	{
		INSTANCE.defineClass(className, bytes, 0, bytes.length);
	}

	/**
	 * Loads a URL into the class path.
	 * @param url The url to add.
	 */
	public static final void loadUrl(URL url)
	{
		INSTANCE.addURL(url);
	}

	/**
	 * Gets the mod container corresponding to the specified mod id.
	 * @param modId The modId to get the mod register of.
	 * @return The mod container with the id of modId.
	 */
	public static final IModContainer getModContainerByModId(String modId)
	{
		return mods.getModContainerById(modId);
	}
	
	/**
	 * The entry point for the loader.
	 */
	public static final void loadModsFromDir()
	{
		if (Launch.checkJavaVersion())
		{
			try
			{
				if (!mcModsDir.exists())
				{
					mcModsDir.mkdir();
					NAPILogHelper.log("Created mods folder at " + mcModsDir.toPath().toString() + "!");
				}

				loadUrl(mcModsDir.toURI().toURL());

				initNAPIRegister();

				if (mcModsDir.listFiles() != null)
				{
					for (File currFile : mcModsDir.listFiles())
					{
						if (currFile.isDirectory())
						{
							loadUrl(currFile.toURI().toURL());
							loadClasses(currFile);
						}
						else if (currFile.toPath().toString().endsWith(".jar") || currFile.toPath().toString().endsWith(".zip"))
						{
							loadUrl(currFile.toURI().toURL());
						}
					}

					registerTransformers();
				}
			} catch (IOException | SecurityException | IllegalArgumentException e)
			{
				e.printStackTrace();
				NAPILogHelper.logError(e);
			}
		}
		else
		{
			shouldInit = false;
		}
	}
	
	/**
	 * Calls all mod register's registerTransformers() method.
	 */
	public static final void registerTransformers()
	{
	}
	
	/**
	 * Initialises the N-API mod register.
	 */
	private static final void initNAPIRegister()
	{
		modLocatingTransformer.manipulateBytecodes("co.uk.niadel.napi.modhandler.NAPIModRegister");

			/*if (theClass == NAPIModRegister.class)
			{
				NAPIModRegister register = theClass.newInstance();

				register.registerEventHandlers();
				register.registerTransformers();
				processAnnotations(new Mod(register.getModId(), register.getVersion(), register));
				register.preModInit();
				register.modInit();
				register.postModInit();
			}*/
	}
	
	/**
	 * Loads the classes in the directory.
	 * @param dir The directory to load the classes of.
	 */
	public static final void loadClasses(File dir)
	{
		try
		{
			loadUrl(dir.toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			NAPILogHelper.logError("Unable to load the classes for jar file " + dir.getName() + "!");
			NAPILogHelper.logError(e);
		}
	}
	
	/**
	 * Processes annotations, doing special things depending on annotations.
	 * @param mod The mod container to process the annotations of.
	 */
	public static final void processAnnotations(IModContainer mod)
	{
		Object modRegister = mod.getMainClass();
		Annotation[] registerAnnotations = mod.getClassAnnotations();
		Map<Method, Annotation[]> registerMethods = mod.getMethodAnnotations();

		Iterator<Method> methodIterator = registerMethods.keySet().iterator();
		Iterator<Entry<Method, Annotation[]>> annotationIterator = registerMethods.entrySet().iterator();

		while (methodIterator.hasNext())
		{
			AnnotationHandlerRegistry.callAllMethodHandlers(annotationIterator.next().getValue(), methodIterator.next(), modRegister);
		}

		if (registerAnnotations != null)
		{
			//Makes it easier to add annotations.
			for (IAnnotationHandler currHandler : AnnotationHandlerRegistry.getAnnotationHandlers())
			{
				for (Annotation annotation : registerAnnotations)
				{
					currHandler.handleAnnotation(annotation, modRegister);
				}

				//This check is because AnnotationHandlerNAPI has to manually load the class in a different
				//way due to the fact the annotation handler has to put load libraries as well as mods.
				if (currHandler.getClass().getName() != AnnotationHandlerNAPI.class.getName())
				{
					loadMod(mod);
				}
			}
		}

		NAPILogHelper.log("Finished processing annotations for the mod " + mods.getContainerFromRegister(mod) + "!");
	}
	
	/**
	 * Converts an IModContainer into a Mod object and puts that Mod object into mods.
	 * @param mod The mod to load.
	 */
	public static final void loadMod(IModContainer mod)
	{
		mods.addMod(mod);
		NAPILogHelper.log("Loaded mod " + mod.getModId() + "!");
	}
	
	/**
	 * Adds the mod to mods.
	 * @param mod The mod to load the library of.
	 * @deprecated Use loadMod and pass it a ModContainer object, instead of the old Mod or Library.
	 */
	@Deprecated
	public static final void loadLibrary(IModContainer mod)
	{
		mods.addMod(mod, true);
		NAPILogHelper.log("Loaded library " + mod.getModId() + "!");
	}
	
	/**
	 * This invokes all of the ASM transformers, checks dependencies and libraries before calling a mod's modPreInit method.
	 */
	public static final void callAllPreInits()
	{
		if (shouldInit)
		{
			ASMRegistry.invokeAllTransformers();

			Iterator<IModContainer> modsIterator = mods.iterator();
			Iterator<Entry<String, Method>> methodsIterator = preInitMethods.entrySet().iterator();

			while (methodsIterator.hasNext() && modsIterator.hasNext())
			{
				Entry<String, Method> currMethod = methodsIterator.next();
				Object nextMod = modsIterator.next().getMainClass();

				if (checkDependencies(nextMod))
				{
					try
					{
						currMethod.getValue().invoke(currMethod.getKey(), new Object[]{});
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						NAPILogHelper.logError("There was an error invoking " + currMethod.getValue().getName() + "! If it required parameters, please remove them!");
						e.printStackTrace();
					}
				}
				else
				{
					//Remove the mod from loading.
					mods.getMods().remove(currMethod.getKey());
				}
			}

			ResourcesRegistry.addAllResourceDomains();
			NAPILogHelper.log("Called all mod's modPreInit methods!");
		}
	}
	
	/**
	 * Checks a register's dependencies. Returns true if checks were all successful.
	 * @param register The register to check the dependencies of.
	 * @return Whether or not the register passed the checks.
	 */
	public static final boolean checkDependencies(Object register)
	{
		return DependenciesRegistry.checkDependencies(register);
	}
	
	/**
	 * Calls all register's modInit method.
	 */
	public static final void callAllInits()
	{
		if (shouldInit)
		{
			try
			{
				Iterator<Entry<String, Method>> methodsIterator = initMethods.entrySet().iterator();

				while (methodsIterator.hasNext())
				{
					Entry<String, Method> nextMethod = methodsIterator.next();
					nextMethod.getValue().invoke(Class.forName(nextMethod.getKey()).newInstance(), new Object[]{});
				}

				NAPILogHelper.log("Called all mod's modInit methods!");
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | InstantiationException e1)
			{
				NAPILogHelper.logError(e1);
			}
		}
	}
	
	/**
	 * Calls all register's postModInit methods.
	 */
	public static final void callAllPostInits()
	{
		if (shouldInit)
		{
			try
			{
				Iterator<Entry<String, Method>> methodsIterator = postInitMethods.entrySet().iterator();
				Iterator<String> objsIterator = postInitMethods.keySet().iterator();

				while (methodsIterator.hasNext())
				{
					Entry<String, Method> nextMethod = methodsIterator.next();
					nextMethod.getValue().invoke(Class.forName(nextMethod.getKey()).newInstance(), new Object[]{});
				}

				NAPILogHelper.log("Called all mod's postInit methods!");
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | InstantiationException e1)
			{
				NAPILogHelper.logError(e1);
			}

			//Does the work of adding the potions to the Potion.potionTypes array. You know, just in case.
			PotionRegistry.addAllPotions();
		}
	}
	
	/**
	 * Loads an ASM class.
	 * @param theClass The class that was transformed to load.
	 * @param bytes The bytes of theClass.
	 */
	@TestFeature(firstAppearance = "1.0")
	public static final void loadTransformedClass(Class theClass, byte[] bytes)
	{
		NModLoader.defineClass(theClass.getName(), bytes);
		NAPILogHelper.log("Loaded ASM transformed class " + theClass.getName());
	}
}