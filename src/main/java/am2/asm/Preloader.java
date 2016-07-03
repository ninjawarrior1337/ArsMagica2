package am2.asm;

import java.util.Map;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

@SortingIndex(1001)
public class Preloader extends DummyModContainer implements IFMLLoadingPlugin {
	
	private final ModMetadata md = new ModMetadata();

	public Preloader() {
		md.modId = getModId();
		md.version = getVersion();
		md.name = getName();
		md.parent = "arsmagica2";
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"am2.asm.Transformer"};
	}

	@Override
	public String getModContainerClass() {
		return "am2.asm.Preloader";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
	@Override
	public String getModId(){
		return "AM2-Preloader";
	}

	@Override
	public String getName(){
		return "AMCore";
	}
	
	@Override
	public ModMetadata getMetadata() {
		return null;
	}

	@Override
	public String getVersion(){
		return "0.0.3";
	}

}
