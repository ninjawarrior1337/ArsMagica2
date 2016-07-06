package am2.power;

import java.util.ArrayList;

import net.minecraft.util.text.TextFormatting;

public class PowerTypes {
	
	private static int nextID = 1;
	private static ArrayList<PowerTypes> types = new ArrayList<>();
	public static final PowerTypes NONE = new PowerTypes("None");
	public static final PowerTypes LIGHT = new PowerTypes("Light", "\u00A7b", 0x00AAFF);
	public static final PowerTypes NEUTRAL = new PowerTypes("Neutral", "\u00A71", 0x0000AA);
	public static final PowerTypes DARK = new PowerTypes("Dark", "\u00A74", 0x770000);
	private int ID;
	private String name;
	private String chatColor;
	private int color;
	
	private PowerTypes(String name) {
		ID = 0;
		this.name = name;
		this.chatColor = TextFormatting.WHITE.toString();
		this.color = 0xffffff;
	}
	
	public PowerTypes(String name, String chatColor, int color) {
		ID = nextID;
		nextID *= 2;
		this.name = name;
		this.chatColor = chatColor;
		this.color = color;
		types.add(this);
	}
	
	public int ID() {
		return ID;
	}
	
	public String name() {
		return name.toLowerCase();
	}
	
	public static ArrayList<PowerTypes> all() {
		return types;
	}
	
	public int getColor() {
		return color;
	}
	
	public String getChatColor() {
		return chatColor;
	}

	public static PowerTypes getByID(int integer) {
		for (PowerTypes type : all()) {
			if (type.ID == integer)
				return type;
		}
		return NONE;
	}
}
