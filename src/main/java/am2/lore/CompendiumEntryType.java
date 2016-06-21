package am2.lore;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class CompendiumEntryType{

	private String categoryName;
	private String categoryLabel;
	private TextureAtlasSprite representItem;
	
	public CompendiumEntryType(String categoryName, String categoryLabel){
		this.categoryName = categoryName;
		this.categoryLabel = categoryLabel;
	}

	public void setRepresentIcon(TextureAtlasSprite IIcon){
		this.representItem = IIcon;
	}

	public String getCategoryName(){
		return this.categoryName;
	}

	public String getNodeName(){
		return I18n.translateToLocal("category." + categoryName + ".name");
	}

	public String getCategoryLabel(){
		return this.categoryLabel;
	}

	public TextureAtlasSprite getRepresentItem(){
		return representItem;
	}


}
