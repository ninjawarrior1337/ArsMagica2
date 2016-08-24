package am2.lore;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import am2.api.event.GenerateCompendiumLoreEvent;
import am2.gui.GuiArcaneCompendium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("deprecation")
public abstract class CompendiumEntry implements Comparable<CompendiumEntry>{
	
	protected String id;
	protected CompendiumEntryType type;
	protected String[] related;
	protected boolean defaultUnlocked = false;
	
	public CompendiumEntry(CompendiumEntryType type, String id, String... relatedItems) {
		this.type = type;
		this.id = id;
		this.related = relatedItems;
	}
	
	public CompendiumEntry setUnlocked(boolean unlocked) {
		defaultUnlocked = unlocked;
		return this;
	}
	
	public boolean isDefaultUnlocked() {
		return defaultUnlocked;
	}
	
	public CompendiumEntryType getType() {
		return type;
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return I18n.translateToLocal("compendium." + id + ".name");
	}
	
	public String getDescription() {
		return I18n.translateToLocal("compendium." + id + ".desc");
	}
	
	public ArrayList<String> getPages() {
		try {
			FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
			String text = I18n.translateToLocal("compendium." + id + ".pages");//.replaceAll("!d", "\n\n").replace("!l", "\n");
			GenerateCompendiumLoreEvent event = new GenerateCompendiumLoreEvent(text, id);
			MinecraftForge.EVENT_BUS.post(event);
			return splitStringToLines(fontRendererObj, event.lore, GuiArcaneCompendium.lineWidth, GuiArcaneCompendium.maxLines);
		} catch (Throwable t) {
			return Lists.newArrayList("Shit Broke.", t.getMessage());
		}
	}
	
	public String[] getParents() {
		String[] string = id.split(".");
		String[] ret = new String[string.length - 1];
		for (int i = 0; i < ret.length; i++)
			ret[i] = string[i];
		return ret;
	}
	
	public String[] getRelatedItems() {
		return related;
	}
	
	public abstract GuiArcaneCompendium getCompendiumGui();
	
	public abstract ItemStack getRepresentStack();
	
//	protected CompendiumEntryType type;
//	protected String name;
//	protected String description;
//	protected String id;
//	protected CompendiumEntry parent;
//	protected boolean isLocked;
//	protected boolean isNew;
//
//	protected ArrayList<CompendiumEntry> subItems;
//	protected ArrayList<String> relatedItems;
//
//	public CompendiumEntry(CompendiumEntryType type, String id){
//		this.type = type;
//		subItems = new ArrayList<CompendiumEntry>();
//		relatedItems = new ArrayList<String>();
//		this.isLocked = true;
//		this.id = id;
//	}
//
//	protected CompendiumEntry setParent(CompendiumEntry parent){
//		this.parent = parent;
//		return this;
//	}
//
//	public final String getID(){
//		return id;
//	}
//
//	public final boolean isLocked(){
//		return isLocked;
//	}
//
//	public final boolean isNew(){
//		return isNew;
//	}
//
//	public final void setIsNew(boolean isNew){
//		this.isNew = isNew;
//	}
//
//	public final void setIsLocked(boolean locked){
//		this.isLocked = locked;
//	}
//
//	public final String getName(){
//		return name;
//	}
//
//	public final String getName(String subItemID){
//		for (CompendiumEntry entry : subItems){
//			if (entry.getID().equals(subItemID)){
//				return entry.getName();
//			}
//		}
//		return name;
//	}
//
//	public final String getDescription(){
//		if (this.description == null || this.description.equals("")){
//			if (this.parent != null){
//				return this.parent.getDescription();
//			}
//		}
//		return description;
//	}
//
//	public final String getDescription(String subItemID){
//		for (CompendiumEntry entry : subItems){
//			if (entry.getID().equals(subItemID)){
//				return description + " " + ArcaneCompendium.KEYWORD_NEWLINE + " " + ArcaneCompendium.KEYWORD_NEWLINE + " " + entry.getDescription();
//			}
//		}
//		return getDescription();
//	}
//
//	public final String getParentSection(){
//		return type.getCategoryName();
//	}
//
//	public final String getTagName(){
//		return type.getNodeName();
//	}
//
//	public final CompendiumEntry getParent(){
//		return this.parent;
//	}
//
//	protected abstract void parseEx(Node node);
//
//	public void parse(Node node){
//		Node idNode = node.getAttributes().getNamedItem("id");
//		this.id = idNode != null ? idNode.getNodeValue() : "unknown";
//		
////		Node orderNode = node.getAttributes().getNamedItem("order");
////		this.order = orderNode != null ? Integer.parseInt(orderNode.getNodeValue()) : -1;
//
//		Node lockableNode = node.getAttributes().getNamedItem("unlocked");
//		this.isLocked = lockableNode != null ? !Boolean.parseBoolean(lockableNode.getNodeValue()) : true;
//
//		Node newNode = node.getAttributes().getNamedItem("new");
//		this.isNew = newNode != null ? Boolean.parseBoolean(newNode.getNodeValue()) : true;
//
//		NodeList childNodes = node.getChildNodes();
//
//		for (int i = 0; i < childNodes.getLength(); ++i){
//			Node childNode = childNodes.item(i);
////			if (childNode.getNodeName() == "name"){
////				this.name = childNode.getTextContent();
////			}else if (childNode.getNodeName() == "desc"){
////				this.description = childNode.getTextContent();
////			}else 
//			if (childNode.getNodeName() == "relatedEntries"){
//				String[] relatedItems = childNode.getTextContent().split(",");
//				for (String s : relatedItems)
//					this.relatedItems.add(s.trim());
//			}else if (childNode.getNodeName().equals("subitem")){
//				CompendiumEntry subItem = null;
//				try{
//					subItem = this.getClass().getConstructor().newInstance();
//				}catch (Throwable t){
//					t.printStackTrace();
//					continue;
//				}
//				subItem.parse(childNode);
//				subItem.setParent(this);
//				this.subItems.add(subItem);
//				ArcaneCompendium.instance.addAlias(subItem.getID(), this.getID());
//			}
//		}
//		this.name = I18n.translateToLocal("entry." + id + ".name");
//		this.description = I18n.translateToLocal("entry." + id + ".desc");
//		//perform any child-specific parsing
//		parseEx(node);
//	}
//
//	public boolean hasSubItems(){
//		return subItems.size() > 0;
//	}
//
//	public CompendiumEntry[] getSubItems(){
//		return subItems.toArray(new CompendiumEntry[subItems.size()]);
//	}
//
//	public CompendiumEntry[] getRelatedItems(){
//		ArrayList<CompendiumEntry> relations = new ArrayList<CompendiumEntry>();
//		for (String s : this.relatedItems){
//			CompendiumEntry e = ArcaneCompendium.instance.getEntry(s);
//			if (e != null && e != this)
//				relations.add(e);
//		}
//
//		return relations.toArray(new CompendiumEntry[relations.size()]);
//	}
//
//	public GuiArcaneCompendium getCompendiumGui(String searchID){
//		int meta = -1;
//		if (searchID.indexOf('@') > -1){
//			String[] split = searchID.split("@");
//			searchID = split[0];
//			try{
//				meta = Integer.parseInt(split[1]);
//			}catch (Throwable t){
//				t.printStackTrace();
//			}
//		}
//
//		return getCompendiumGui(searchID, meta);
//	}
//
//	@SideOnly(Side.CLIENT)
//	protected
//	abstract GuiArcaneCompendium getCompendiumGui(String searchID, int meta);
//
//	public abstract ItemStack getRepresentItemStack(String searchID, int meta);
//
	
	public static ArrayList<String> splitStringToLines(FontRenderer fontRenderer, String string, int lineWidth, int maxLines){
		ArrayList<String> toReturn = new ArrayList<String>();
		int numLines = 0;
		int len = 0;
		int pageCount = 0;
		String[] words = string.replace("\n", " ").replace("\t", "").split(" ");
		StringBuilder sb = new StringBuilder();
		String curLine = "";
		for (String s : words){
			s = s.trim();
			int wordWidth = fontRenderer.getStringWidth(s + " ");
			if (s.equals("!p")){
				sb.append(curLine);
				curLine = "";
				len = 0;
				numLines++;
				toReturn.add(sb.toString());
				sb = new StringBuilder();
				pageCount++;
				numLines = 0;
				continue;
			}else if (s.equals("!l")){
				sb.append(curLine + "\n");
				curLine = "";
				len = 0;
				numLines++;
				if ((numLines == maxLines && pageCount > 0) || (pageCount == 0 && numLines == maxLines - 2)){
					toReturn.add(sb.toString());
					sb = new StringBuilder();
					pageCount++;
					numLines = 0;
				}
				continue;
			}else if (s.equals("!d")){
				sb.append(curLine + "\n");
				curLine = "";
				len = 0;
				numLines++;
				if ((numLines == maxLines && pageCount > 0) || (pageCount == 0 && numLines == maxLines - 2)){
					toReturn.add(sb.toString());
					sb = new StringBuilder();
					pageCount++;
					numLines = 0;
				}
				sb.append(curLine + "\n");
				curLine = "";
				len = 0;
				numLines++;
				if ((numLines == maxLines && pageCount > 0) || (pageCount == 0 && numLines == maxLines - 2)){
					toReturn.add(sb.toString());
					sb = new StringBuilder();
					pageCount++;
					numLines = 0;
				}
				continue;
			}else if (len + wordWidth > lineWidth){
				sb.append(curLine);
				curLine = "";
				len = 0;
				numLines++;
				if ((numLines == maxLines && pageCount > 0) || (pageCount == 0 && numLines == maxLines - 2)){
					toReturn.add(sb.toString());
					sb = new StringBuilder();
					pageCount++;
					numLines = 0;
				}
			}
			curLine = curLine + " " + s;
			len += wordWidth + 1;
		}

		sb.append(curLine);

		if (sb.length() > 0){
			toReturn.add(sb.toString());
		}

		return toReturn;
	}

	@Override
	public int compareTo(CompendiumEntry arg0){

		if (arg0 == null) return 1;
		if (this.id != null && arg0.id != null)
			return this.id.compareTo(arg0.id);

		return -1;
	}

	public ArrayList<CompendiumEntry> getSubItems() {
		ArrayList<CompendiumEntry> entries = new ArrayList<>();
		for (CompendiumEntry entry : ArcaneCompendium.getCompendium().values()) {
			if (entry.getID().startsWith(id + "."))
				entries.add(entry);
		}
		return entries;
	}

	public boolean hasSubItems() {
		for (CompendiumEntry entry : ArcaneCompendium.getCompendium().values()) {
			if (entry.getID().startsWith(id + "."))
				return true;
		}
		return false;
	}
}
