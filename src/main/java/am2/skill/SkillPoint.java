package am2.skill;

import net.minecraft.util.text.TextFormatting;

public class SkillPoint {
	
	private final int color, minEarnLevel, levelsForPoint;
	private final String name;
	private final TextFormatting chatColor;
	
	private boolean render = true;
	
	public SkillPoint(String name, TextFormatting chatColor, int color, int minEarnLevel, int levelsForPoint) {
		this.color = color;
		this.name = name;
		this.minEarnLevel = minEarnLevel;
		this.levelsForPoint = levelsForPoint;
		this.chatColor = chatColor;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getLevelsForPoint() {
		return levelsForPoint;
	}
	
	public int getMinEarnLevel() {
		return minEarnLevel;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public TextFormatting getChatColor() {
		return chatColor;
	}
	
	public boolean canRender() {
		return render;
	}
	
	public SkillPoint disableRender() {
		render = false;
		return this;
	}
}
