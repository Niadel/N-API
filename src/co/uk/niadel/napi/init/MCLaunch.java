package co.uk.niadel.napi.init;

import co.uk.niadel.napi.annotations.Internal;
import co.uk.niadel.napi.asm.ASMRegistry;
import co.uk.niadel.napi.asm.transformers.NAPIASMDeSysOutTransformer;
import net.minecraft.client.main.Main;

/**
 * Patches then launches Minecraft.
 */
public class MCLaunch
{
	@Internal
	private static final MCGamePatcher mcGamePatcher = new MCGamePatcher();

	public static void main(String[] args)
	{
		assert DevLaunch.checkJavaVersion();
		NAPIASMDeSysOutTransformer.setEnabled();
		ASMRegistry.callASMTransformerForClass(mcGamePatcher, ObfedClassNames.BOOTSTRAP);
		Main.main(args);
	}
}
