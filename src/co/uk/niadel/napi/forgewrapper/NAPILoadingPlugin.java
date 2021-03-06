package co.uk.niadel.napi.forgewrapper;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.*;
import co.uk.niadel.napi.common.NAPIData;

/**
 * Used for loading special stuff for Forge.
 * @author Niadel
 *
 */
@Name(value = "N-API Forge Wrapper Access Transformer")
@MCVersion(value = NAPIData.MC_VERSION)
public final class NAPILoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] {};
	}

	@Override
	public String getModContainerClass()
	{
		return "co.uk.niadel.api.forgewrapper.NAPIFMLModContainer";
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		
	}

	@Override
	public String getAccessTransformerClass()
	{
		return "co.uk.niadel.api.forgewrapper.NAPIAccessTransformer";
	}
}
