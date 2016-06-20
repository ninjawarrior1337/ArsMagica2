package am2.skill;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class Skill {
	
	private String ID;
	private int posX, posY;
	private SkillTree tree;
	private String[] parents;
	private ResourceLocation icon;
	private SkillPoint point;
	
	public Skill(String ID, ResourceLocation icon, SkillPoint point, int posX, int posY, SkillTree tree, String... string) {
		this.posX = posX;
		this.posY = posY;
		this.ID = ID.toLowerCase();
		this.tree = tree;
		this.parents = string;
		this.icon = icon;
		this.point = point;
	}
	
	public String getID() {
		return ID;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public ResourceLocation getIcon() {
		return icon;
	}
	
	public SkillTree getTree() {
		return tree;
	}
	
	public String[] getParents() {
		return parents;
	}
	
	public void writeToNBT (NBTTagCompound tag) {
		tag.setString("ID", ID);
	}
	
	public SkillPoint getPoint() {
		return point;
	}
	
	@Override
	public String toString() {
		return ID;
	}
	
	public String getName() {
		return I18n.translateToLocal("skill." + getID() + ".name");
	}
	
	public String getOcculusDesc() {
		return I18n.translateToLocal("skill." + getID() + ".occulusdesc");
	}
}
