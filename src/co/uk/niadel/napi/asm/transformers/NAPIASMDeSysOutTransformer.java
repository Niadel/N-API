package co.uk.niadel.napi.asm.transformers;

import co.uk.niadel.napi.asm.IASMTransformer;
import co.uk.niadel.napi.modhandler.NAPIModRegister;
import co.uk.niadel.napi.util.NAPILogHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Gets rid of calls to System.out.println and System.err.println to encourage the use of a logger, as loggers allow us to know what
 * mod is "saying" what. This makes debugging with multiple mods installed MUCH easier, assuming people actually use the logger.
 *
 * @author Niadel
 */
public class NAPIASMDeSysOutTransformer implements IASMTransformer, Opcodes
{
	private static boolean SHOULD_RUN = Boolean.valueOf(NAPIModRegister.napiConfig.getConfigValue("removeSysOut", "true"));

	@Override
	public byte[] manipulateBytecodes(String className, byte[] bytes)
	{
		if (SHOULD_RUN)
		{
			//Could be way more optimised, but I'll save that for later.
			ClassReader classReader = new ClassReader(bytes);
			ClassNode classNode = new ClassNode();
			classReader.accept(classNode, 0);

			//Yup. I'm allowing it to catch my own uses of System.out.println. It'll stop me from being a hypocrite
			//dev. Only in N-API though.
			if (!className.contains("net.minecraft.") && !className.startsWith("java") && !isClassExternalLibrary(className))
			{
				for (MethodNode method : classNode.methods)
				{
					String allExceptionsThrown = method.exceptions == null ? "---" : "";

					if (method.exceptions != null)
					{
						for (String exceptionThrown : method.exceptions)
						{
							allExceptionsThrown += ("," + exceptionThrown);
						}
					}

					for (int i = 0; i == method.instructions.size(); i++)
					{
						AbstractInsnNode instruction = method.instructions.get(i);

						if (instruction instanceof FieldInsnNode)
						{
							FieldInsnNode actInsn = (FieldInsnNode) instruction;

							if (actInsn.getType() == GETSTATIC)
							{
								if (actInsn.owner.contains("java/lang/System"))
								{
									if (actInsn.name == "out")
									{
										AbstractInsnNode nextInsn = method.instructions.get(i + 1);

										if (nextInsn instanceof LdcInsnNode)
										{
											AbstractInsnNode nextInsn2 = method.instructions.get(i + 2);

											if (nextInsn2 instanceof MethodInsnNode)
											{
												MethodInsnNode actInsn2 = (MethodInsnNode) nextInsn2;

												if (actInsn2.getType() == INVOKEVIRTUAL && actInsn2.owner == "java/io/PrintStream" && (actInsn2.name == "println" || actInsn2.name == "printf"))
												{
													//Remove GETSTATIC
													method.instructions.remove(instruction);
													//Remove LDC
													method.instructions.remove(nextInsn);
													//Remove actual method call.
													method.instructions.remove(nextInsn2);

													NAPILogHelper.instance.logWarn("Found call to System.out.println or System.out.printf! DO NOT DO THIS! Use a logger instead! The call has been removed.");
													NAPILogHelper.instance.logWarn("Offending caller is method " + method.name + " with description " + method.desc + " that throws exceptions " + allExceptionsThrown + " in class " + className);
												}
											}
										}
									}
									else if (actInsn.name == "err")
									{
										AbstractInsnNode nextInsn = method.instructions.get(i + 1);

										if (nextInsn instanceof LdcInsnNode)
										{
											AbstractInsnNode nextInsn2 = method.instructions.get(i + 2);

											if (nextInsn2 instanceof MethodInsnNode)
											{
												MethodInsnNode actInsn2 = (MethodInsnNode) nextInsn2;

												if (actInsn2.getType() == INVOKEVIRTUAL && actInsn2.owner == "java/io/PrintStream" && (actInsn2.name == "println" || actInsn2.name == "printf"))
												{
													//Remove GETSTATIC
													method.instructions.remove(instruction);
													//Remove LDC
													method.instructions.remove(nextInsn);
													//Remove actual method call.
													method.instructions.remove(nextInsn2);

													NAPILogHelper.instance.logWarn("Found call to System.err.println or System.err.printf! DO NOT DO THIS! Use a logger instead! The call has been removed.");
													NAPILogHelper.instance.logWarn("Offending caller is method " + method.name + " with description " + method.desc + " that throws exceptions " + allExceptionsThrown + " in class " + className);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			return new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES).toByteArray();

		}

		return null;
	}

	public static final void setEnabled()
	{
		SHOULD_RUN = true;
	}

	public static final boolean isClassExternalLibrary(String className)
	{
		ArrayList<String> externalLibs = new ArrayList<>(Arrays.asList(
				"com.mojang",
				"argo",
				"org.bouncycastle",
				"com.jcraft",
				"paulscode.sound",
				"org.apache",
				"com.google",
				"com.ibm.icu",
				"net.java.games",
				"net.minecraft.launchwrapper",
				"joptsimple",
				"org.lwjgl",
				"LZMA",
				"io.netty",
				"assets.realms",
				"gnu.trove",
				"tv.twitch",
				"javax.vecmath",
				"co.uk.niadel.commons",
				"org.objectweb.asm"
		));

		for (String externalLib : externalLibs)
		{
			if (className.startsWith(externalLib))
			{
				return true;
			}
		}

		return false;
	}
}
