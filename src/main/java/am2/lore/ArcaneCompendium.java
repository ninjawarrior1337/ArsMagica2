package am2.lore;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.IMultiblockStructureController;
import am2.api.SpellRegistry;
import am2.api.event.CompendiumEntryRegistrationEvent;
import am2.api.extensions.IArcaneCompendium;
import am2.api.skill.Skill;
import am2.extensions.DataDefinitions;
import am2.rituals.IRitualInteraction;
import am2.spell.AbstractSpellPart;
import am2.spell.SpellComponent;
import am2.spell.SpellModifier;
import am2.spell.SpellModifiers;
import am2.spell.SpellShape;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ArcaneCompendium implements IArcaneCompendium, ICapabilityProvider, ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(IArcaneCompendium.class)
	public static Capability<IArcaneCompendium> INSTANCE = null;
	
	/**
	 * Allowed class are : <BR>
	 * {@link Item}, {@link ItemStack}, {@link Block}, {@link IBlockState}, {@link Class}<{@link Entity}>, {@link IRitualInteraction}, {@link AbstractSpellPart}
	 * 
	 * @param entryItem
	 * @param id : format : parent1.parent2.name, usually there wont be parents...
	 * @param mods : Only for spell component/shape registration
	 * @param allowReplace
	 * @param relatedKeys
	 */
	public static void AddCompendiumEntry(Object entryItem, String id, EnumSet<SpellModifiers> mods, boolean allowReplace, String... relatedKeys) {
		AddCompendiumEntry(entryItem, id, mods, allowReplace, false, relatedKeys);
	}
	
	@SuppressWarnings("unchecked")
	public static void AddCompendiumEntry(Object entryItem, String id, EnumSet<SpellModifiers> mods, boolean allowReplace, boolean defaultUnlock, String... relatedKeys) {		
		CompendiumEntry entry = null;
		CompendiumEntryRegistrationEvent event = new CompendiumEntryRegistrationEvent(entryItem, id, mods, relatedKeys);
		try {
			MinecraftForge.EVENT_BUS.post(event);
			entry = event.getEntry();
			if (entry == null) {
				if (entryItem instanceof Item)
					entry = new CompendiumEntryItem(id, (Item) entryItem, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof ItemStack)
					entry = new CompendiumEntryItem(id, ((ItemStack)entryItem).getItem(), ((ItemStack)entryItem).getItemDamage(), relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof Block)
					entry = new CompendiumEntryBlock(id, ((Block)entryItem).getDefaultState(), relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof IBlockState)
					entry = new CompendiumEntryBlock(id, (IBlockState) entryItem, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof Skill)
					entry = new CompendiumEntryTalent(id, (Skill) entryItem, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof SpellComponent)
					entry = new CompendiumEntrySpellComponent(id, SpellRegistry.getSkillFromPart((SpellComponent) entryItem), mods, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof SpellShape)
					entry = new CompendiumEntrySpellShape(id, SpellRegistry.getSkillFromPart((SpellShape) entryItem), mods, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof SpellModifier)
					entry = new CompendiumEntrySpellModifier(id, SpellRegistry.getSkillFromPart((SpellModifier) entryItem), ((SpellModifier) entryItem).getAspectsModified(), relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof Class && IRitualInteraction.class.isAssignableFrom((Class<?>) entryItem))
					entry = new CompendiumEntryRitual(id, (Class<? extends IRitualInteraction>) entryItem, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof Class && IMultiblockStructureController.class.isAssignableFrom(((Class<?>)entryItem)) && TileEntity.class.isAssignableFrom(((Class<?>)entryItem)))
					entry = new CompendiumEntryStructure(id, ((Class<? extends TileEntity>)entryItem), relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof Class && Entity.class.isAssignableFrom(((Class<?>)entryItem)))
					entry = new CompendiumEntryMob(id, (Class<? extends Entity>) entryItem, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof Entity)
					entry = new CompendiumEntryMob(id, (Class<? extends Entity>) entryItem.getClass(), relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof String && "guide".equalsIgnoreCase((String) entryItem))
					entry = new CompendiumEntryGuide(id, relatedKeys).setUnlocked(defaultUnlock);
				else if (entryItem instanceof String && "mechanic".equalsIgnoreCase((String) entryItem))
					entry = new CompendiumEntryMechanic(id, relatedKeys).setUnlocked(defaultUnlock);
			}
		} catch (Throwable t) {
			ArsMagica2.LOGGER.error("Exception caught during " + id + " initialization...");
			t.printStackTrace();
		}
				
		if (entry != null) {
			if (allowReplace) compendiumEntries.put(id, entry);
			else if (!compendiumEntries.containsKey(id)) compendiumEntries.put(id, entry);
			else ArsMagica2.LOGGER.info("Trying to register entry with id " + id + " but it already exists, skipping...");
		}
	}
	
	private static final Map<String, CompendiumEntry> compendiumEntries = new HashMap<>();
	
	private EntityPlayer player;
	
	public static ImmutableMap<String, CompendiumEntry> getCompendium() {
		return ImmutableMap.copyOf(compendiumEntries);
	}
	public ArcaneCompendium() {}
	
	public void unlockEntry(String name) {
		ArrayList<String> categories = player.getDataManager().get(DataDefinitions.COMPENDIUM);
		categories.add(name);
		player.getDataManager().set(DataDefinitions.COMPENDIUM, categories);
	}
	
	public boolean isUnlocked(String name)  {
		if (getCompendium().get(name) != null && getCompendium().get(name).isDefaultUnlocked()) return true;
		for (String str : player.getDataManager().get(DataDefinitions.COMPENDIUM)) {
			if (name.equals(str))
				return true;
		}
		for (String str : player.getDataManager().get(DataDefinitions.CATEGORIES)) {
			if (name.equals(str))
				return true;
		}

		return false;
	}
	
	public void init(EntityPlayer player) {
		this.player = player;
		player.getDataManager().register(DataDefinitions.COMPENDIUM, new ArrayList<String>());
		player.getDataManager().register(DataDefinitions.CATEGORIES, new ArrayList<String>());
	}

	public static IArcaneCompendium For(EntityPlayer entityPlayer) {
		return entityPlayer.getCapability(INSTANCE, null);
	}

	@Override
	public void unlockCategory(String string) {
		ArrayList<String> categories = player.getDataManager().get(DataDefinitions.CATEGORIES);
		categories.add(string);
		player.getDataManager().set(DataDefinitions.CATEGORIES, categories);
	}

	@Override
	public void unlockRelatedItems(ItemStack crafting) {
		if (crafting == null)
			return;
		if (crafting.getItem() == null)
			return;
		CompendiumEntry compendiumEntry = null;
		for (Entry<String, CompendiumEntry> entry : getCompendium().entrySet()) {
			if (crafting.getItem() instanceof ItemBlock && entry.getValue() instanceof CompendiumEntryBlock) {
				compendiumEntry = entry.getValue();
			} else if (entry.getValue() instanceof CompendiumEntryItem){
				compendiumEntry = entry.getValue();
			}
		}
		if (compendiumEntry == null)
			return;
		for (String related : compendiumEntry.getRelatedItems()) {
			this.unlockEntry(related);
		}
	}

	public static LinkedHashSet<CompendiumEntryType> getCategories() {
		return Sets.newLinkedHashSet(Lists.newArrayList(CompendiumEntryTypes.categoryList()));
	}

	@Override
	public ArrayList<CompendiumEntry> getEntriesForCategory(String categoryName) {
		ArrayList<CompendiumEntry> array = new ArrayList<>();
		for (CompendiumEntry entry : getCompendium().values()) {
			if (entry.getType().getCategoryName().equals(categoryName))
				array.add(entry);
		}
		return array;
	}

	@Override
	public boolean isNew(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == INSTANCE)
			return (T) this;
		return null;
	}

	@Override
	public NBTBase serializeNBT() {
		return new IArcaneCompendium.Storage().writeNBT(INSTANCE, this, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		System.out.println("Deserialize");
		new IArcaneCompendium.Storage().readNBT(INSTANCE, this, null, nbt);
	}
	
	
//	public static final ArcaneCompendium instance = new ArcaneCompendium();
//
//	public static final String KEYWORD_NEWPAGE = "!p";
//	public static final String KEYWORD_NEWLINE = "!l";
//	public static final String KEYWORD_DOUBLENEWLINE = "!d";
//
//	public static Achievement compendiumData = (new Achievement("am2_ach_data", "compendiumData", 0, 0, ItemDefs.arcaneCompendium, null));
//	public static Achievement componentUnlock = (new Achievement("am2_ach_unlock", "componentUnlock", 0, 0, ItemDefs.spellParchment, null));
//
//	private final TreeMap<String, CompendiumEntry> compendium;
//	private final TreeMap<String, String> aliases;
//	private final TreeMap<String, String> zeroItemTexts;
//
//	private String saveFileLocation;
//	private String updatesFolderLocation;
//	private boolean hasLoaded = false;
//	private boolean forcePackagedCompendium = false;
//
//	private String languageCode = "";
//	private String MCVersion = "";
//	private String modVersion = "";
//	private String compendiumVersion = "";
//
//	private String latestModVersion = "";
//	private String latestDownloadLink = "";
//	private String latestPatchNotesLink = "";
//	private boolean modUpdateAvailable = false;
//
//	private ArcaneCompendium(){
//		compendium = new TreeMap<String, CompendiumEntry>();
//		aliases = new TreeMap<String, String>();
//		zeroItemTexts = new TreeMap<String, String>();
//	}
//
//	public void setSaveLocation(String saveFileLocation){
//		this.saveFileLocation = saveFileLocation + File.separatorChar + "compendiumunlocks";
//		this.updatesFolderLocation = saveFileLocation + File.separatorChar + "compendiumUpdates";
//		try{
//			File file = new File(this.saveFileLocation);
//			if (!file.exists())
//				file.mkdirs();
//
//			file = new File(updatesFolderLocation);
//			if (!file.exists())
//				file.mkdirs();
//		}catch (Throwable t){
//			ArsMagica2.LOGGER.error("Could not create save location!");
//			t.printStackTrace();
//		}
//	}
//
//	private String getWorldName(){
//		if (ClientTickHandler.worldName == null)
//			return null;
//		return ClientTickHandler.worldName.replaceAll("[^\\x00-\\x7F]", "");
//	}
//
//	public void saveUnlockData(){
//		if (!hasLoaded)
//			return;
//
//		if (saveFileLocation != null && getWorldName() != null){
//			try{
//				File file = new File(saveFileLocation + File.separatorChar + getWorldName() + ".txt");
//				if (!file.exists())
//					file.createNewFile();
//				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//
//				for (CompendiumEntry entry : compendium.values()){
//					String id = entry.getID() + "|U";
//					if (entry.isLocked)
//						id += "L";
//					if (entry.isNew)
//						id += "N";
//					writer.write(id + "\n");
//				}
//
//				writer.close();
//			}catch (IOException e){
//				ArsMagica2.LOGGER.error("Compendium unlock state failed to save!");
//				e.printStackTrace();
//			}
//
//		}
//	}
//
//	public void loadUnlockData(){
//
//		if (!ArsMagica2.config.stagedCompendium()){
//			for (CompendiumEntry entry : compendium.values()){
//				entry.isLocked = false;
//				entry.isNew = false;
//			}
//			return;
//		}
//
//		if (saveFileLocation != null && getWorldName() != null){
//			try{
//				hasLoaded = true;
//
//				File file = new File(saveFileLocation + File.separatorChar + getWorldName() + ".txt");
//				if (!file.exists()){
//					ArsMagica2.LOGGER.info("Compendium unlock state not found to load.  Assuming it hasn't been created yet.");
//					return;
//				}
//				BufferedReader reader = new BufferedReader(new FileReader(file));
//
//				String s;
//				while ((s = reader.readLine()) != null){
//					String[] split = s.trim().replace("\n", "").replace("\r", "").split("\\|");
//					if (split.length != 2)
//						continue;
//					CompendiumEntry entry = this.getEntryAbsolute(split[0]);
//					if (entry == null) continue;
//
//					entry.isLocked = split[1].contains("L");
//					entry.isNew = split[1].contains("N");
//				}
//
//				reader.close();
//			}catch (IOException e){
//				ArsMagica2.LOGGER.error("Compendium unlock state failed to load!");
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public void addAlias(String alias, String baseItem){
//		aliases.put(alias, baseItem);
//	}
//
//	public void init(Language lang){
//		if (lang == null){
//			ArsMagica2.LOGGER.error("Got a current language of NULL from Minecraft?!?  The compendium cannot load!");
//			return;
//		}
//		MCVersion = (String)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110447_Z", "launchedVersion");
//		languageCode = lang.getLanguageCode().trim();
//		modVersion = ArsMagica2.instance.getVersion();
//
//		compendium.clear();
//		aliases.clear();
//		zeroItemTexts.clear();
//
//		//check for mod updates
//		if (ArsMagica2.config.allowVersionChecks())
//			checkForModUpdates();
//		else
//			ArsMagica2.LOGGER.info("Skipping version check due to config");
//
//		//load the version of the compendium only
//		loadDocumentVersion(lang);
//
//		//check for compendium updates
//		if (ArsMagica2.config.allowCompendiumUpdates())
//			updateCompendium(lang);
//		else
//			ArsMagica2.LOGGER.info("Skipping Compendium auto-update due to config");
//
//		//get the compendium stream again, either from an updated version or the default packaged one
//		InputStream stream = getCompendium(lang);
//		if (stream == null){
//			ArsMagica2.LOGGER.error("Unable to load the Arcane Compendium!");
//			return;
//		}
//		//load the entire document
//		loadDocument(stream);
//		//cleanup
//		try{
//			stream.close();
//		}catch (IOException e){
//			e.printStackTrace();
//		}
//	}
//
//	private boolean updateCompendium(Language lang){
//		if (!ArsMagica2.config.allowCompendiumUpdates())
//			return false;
//
//		try{
//			String txt = WebRequestUtils.sendPost("http://arcanacraft.qorconcept.com/mc/CompendiumVersioning.txt", new HashMap<String, String>());
//			String[] lines = txt.replace("\r\n", "\n").split("\n");
//			for (String s : lines){
//				if (s.startsWith("#"))
//					continue;
//				String[] sections = s.split("\\|");
//
//				//check MC version and language
//				if (!sections[1].trim().equalsIgnoreCase(languageCode) || !MCVersion.startsWith(sections[0].trim()))
//					continue;
//				if (versionCompare(sections[2], this.compendiumVersion) > 0){
//					if (modVersion.startsWith(sections[3])){
//						//if we're here, an update is needed!
//						String compendiumFileName = "ArcaneCompendium_" + lang.getLanguageCode() + ".xml";
//						String compendiumData = WebRequestUtils.sendPost("http://arcanacraft.qorconcept.com/mc/" + MCVersion + "/" + compendiumFileName, new HashMap<String, String>());
//
//						//we have the data, save it to the local repository
//						saveCompendiumData(compendiumData, compendiumFileName);
//						ArsMagica2.LOGGER.info("Updated Compendium");
//						return true;
//					}
//				}
//			}
//		}catch (Throwable t){
//			ArsMagica2.LOGGER.warn("Unable to update the compendium!");
//			t.printStackTrace();
//		}
//		return false;
//	}
//
//	private void checkForModUpdates(){
//		try{
//			ArsMagica2.LOGGER.info("Checking Version.  MC Version: %s", MCVersion);
//			this.latestModVersion = this.modVersion;
//			String txt = WebRequestUtils.sendPost("http://arcanacraft.qorconcept.com/mc/AM2Versioning.txt", new HashMap<String, String>());
//			String[] lines = txt.replace("\r\n", "\n").split("\n");
//			for (String s : lines){
//				if (s.startsWith("#"))
//					continue;
//				String[] sections = s.split("\\|");
//				if (!MCVersion.startsWith(sections[0].trim()))
//					continue;
//				if (versionCompare(sections[1], this.latestModVersion) > 0){
//					ArsMagica2.LOGGER.info("An update is available.  Version %s is released, detected local version of %s.", this.modVersion, sections[1]);
//					this.latestModVersion = sections[1];
//					this.latestDownloadLink = sections.length >= 3 ? sections[2] : "";
//					this.latestPatchNotesLink = sections.length >= 4 ? sections[3] : "";
//					modUpdateAvailable = true;
//				}else{
//					ArsMagica2.LOGGER.info("You are running the latest version of AM2.  Latest Released Version: %s.  Your Version: %s.", this.latestModVersion, this.modVersion);
//				}
//			}
//
//		}catch (Throwable t){
//			t.printStackTrace();
//		}
//	}
//
//	private void saveCompendiumData(String compendium, String fileName) throws Exception{
//		if (compendium == null){
//			throw new Exception("No compendium data received");
//		}
//		String path = updatesFolderLocation + File.separatorChar + MCVersion + File.separatorChar + modVersion;
//		File dirPath = new File(path);
//		if (!dirPath.exists())
//			dirPath.mkdirs();
//		File f = new File(path + File.separatorChar + fileName);
//		FileUtils.writeStringToFile(f, compendium);
//	}
//
//	/**
//	 * Gets the entry, with alias checks.
//	 */
//	public CompendiumEntry getEntry(String key){
//		if (aliases.containsKey(key)){
//			CompendiumEntry aliased = compendium.get(aliases.get(key));
//			if (aliased.hasSubItems()){
//				for (CompendiumEntry sub : aliased.getSubItems()){
//					if (sub.getID().equals(key)){
//						return sub;
//					}
//				}
//			}
//			return aliased;
//		}
//		return compendium.get(key);
//	}
//
//	/**
//	 * Gets the entry without alias checks.
//	 */
//	public CompendiumEntry getEntryAbsolute(String key){
//		return compendium.get(key);
//	}
//
//	public String getZeroItemText(String category){
//		String s = zeroItemTexts.get(category);
//		return s != null ? s : "";
//	}
//
//	private void loadDocument(InputStream stream){
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		try{
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			Document document = db.parse(stream);
//
//			Node parentNode = document.getChildNodes().item(0);
//
//			NodeList categories = parentNode.getChildNodes();
//
//			for (int i = 0; i < categories.getLength(); ++i){
//				Node category = categories.item(i);
//
//				if (category.getNodeName().equals("version")){
//					this.compendiumVersion = category.getTextContent();
//					continue;
//				}
//
//				if (category.getAttributes() != null){
//					Node zeroItemText = category.getAttributes().getNamedItem("zeroItemText");
//					if (zeroItemText != null){
//						zeroItemTexts.put(category.getNodeName(), zeroItemText.getTextContent());
//					}
//				}
//
//				NodeList entries = category.getChildNodes();
//				for (int j = 0; j < entries.getLength(); ++j){
//					Node entry = entries.item(j);
//					CompendiumEntryType type = CompendiumEntryTypes.getForSection(category.getNodeName(), entry.getNodeName());
//					if (type == null) continue;
//					CompendiumEntry ce = type.createCompendiumEntry(entry);
//					if (ce == null) continue;
//					this.compendium.put(ce.getID(), ce);
//				}
//			}
//
//		}catch (Throwable t){
//			t.printStackTrace();
//		}
//	}
//
//	private void loadDocumentVersion(Language lang){
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		try{
//			//get the packaged compendium version
//			InputStream stream = getPackagedCompendium(lang);
//			//standard XML parsing stuff
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			Document document = db.parse(stream);
//
//			Node parentNode = document.getChildNodes().item(0);
//
//			NodeList categories = parentNode.getChildNodes();
//
//			//look for the version node
//			for (int i = 0; i < categories.getLength(); ++i){
//				Node category = categories.item(i);
//
//				//got it!
//				if (category.getNodeName().equals("version")){
//					this.compendiumVersion = category.getTextContent();
//					break;
//				}
//			}
//			stream.close();
//
//			//get the downloaded version (or re-get the packaged version if there is no downloaded one)
//			stream = getCompendium(lang);
//			//standard parsing
//			db = dbf.newDocumentBuilder();
//			document = db.parse(stream);
//
//			parentNode = document.getChildNodes().item(0);
//
//			categories = parentNode.getChildNodes();
//
//			//find version node
//			for (int i = 0; i < categories.getLength(); ++i){
//				Node category = categories.item(i);
//
//				//got it!
//				if (category.getNodeName().equals("version")){
//					//get this one's version
//					String altVersion = category.getTextContent();
//					//compare versions - is the packaged compendium actually newer?
//					if (versionCompare(altVersion, this.compendiumVersion) > 0)
//						this.compendiumVersion = altVersion; //nope
//					else
//						this.forcePackagedCompendium = true; //yep
//					break;
//				}
//			}
//			stream.close();
//		}catch (Throwable t){
//			t.printStackTrace();
//		}
//	}
//
//	public Integer versionCompare(String str1, String str2){
//		String[] vals1 = str1.split("\\.");
//		String[] vals2 = str2.split("\\.");
//		int i = 0;
//		// set index to first non-equal ordinal or length of shortest version string
//		while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])){
//			i++;
//		}
//		// compare first non-equal ordinal number
//		if (i < vals1.length && i < vals2.length){
//			int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
//			return Integer.signum(diff);
//		}
//		// the strings are equal or one string is a substring of the other
//		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
//		else{
//			return Integer.signum(vals1.length - vals2.length);
//		}
//	}
//
//	private String getUpdatedPath(){
//		String compendiumFileName = "ArcaneCompendium_" + languageCode + ".xml";
//		return updatesFolderLocation + File.separatorChar + MCVersion + File.separatorChar + ArsMagica2.instance.getVersion() + File.separatorChar + compendiumFileName;
//	}
//
//	private InputStream getCompendium(Language lang){
//
//		if (!forcePackagedCompendium){
//			String path = getUpdatedPath();
//
//			try{
//				File f = new File(path);
//				if (f.exists())
//					return new FileInputStream(f);
//			}catch (Throwable t){
//				ArsMagica2.LOGGER.trace("An error occurred when trying to create an inputstream from the updated compendium.  Reverting to default.");
//			}
//			ArsMagica2.LOGGER.info("No updated compendium found.  Using packaged compendium.");
//		}
//		return getPackagedCompendium(lang);
//	}
//
//	private InputStream getPackagedCompendium(Language lang){
//		ResourceLocation rLoc = new ResourceLocation("arsmagica2", String.format("docs/ArcaneCompendium_%s.xml", lang.getLanguageCode()));
//		IResource resource = null;
//		try{
//			resource = Minecraft.getMinecraft().getResourceManager().getResource(rLoc);
//		}catch (IOException e){
//		}finally{
//			if (resource == null){
//				ArsMagica2.LOGGER.info("Unable to find localized compendium.  Defaulting to en_US");
//				rLoc = new ResourceLocation("arsmagica2", "docs/ArcaneCompendium_en_US.xml");
//				try{
//					resource = Minecraft.getMinecraft().getResourceManager().getResource(rLoc);
//				}catch (IOException e){
//				}
//			}
//		}
//
//		if (resource != null)
//			return resource.getInputStream();
//
//		throw new MissingResourceException("No packaged version of the compendium was found.  You may have a corrupted download.", "compendium", "Arcane Compendium");
//	}
//
//	public LinkedHashSet<CompendiumEntryType> getCategories(){
//		LinkedHashSet<CompendiumEntryType> toReturn = new LinkedHashSet<>();
//		for (CompendiumEntryType type : CompendiumEntryTypes.categoryList()){
//			toReturn.add(type);
//		}
//		return toReturn;
//	}
//
//	public ArrayList<CompendiumEntry> getEntriesForCategory(String category){
//		ArrayList<CompendiumEntry> toReturn = new ArrayList<CompendiumEntry>();
//
//		for (CompendiumEntry entry : compendium.values()){
//			if (entry.type.getCategoryName().equals(category)){
//				toReturn.add(entry);
//			}
//		}
//
//		Collections.sort(toReturn);
//
//		return toReturn;
//	}
//
//	private void unlockEntry(CompendiumEntry entry, boolean unlockRelated){
//		if (unlockRelated){
//			for (CompendiumEntry e : entry.getRelatedItems()){
//				unlockEntry(e, false);
//			}
//
//			for (CompendiumEntry e : entry.getSubItems()){
//				unlockEntry(e, false);
//			}
//		}
//		if (entry.isLocked()){
//			entry.setIsLocked(false);
//			if (EntityExtension.For(Minecraft.getMinecraft().thePlayer).getCurrentLevel() > 0)
//				Minecraft.getMinecraft().guiAchievement.displayAchievement(compendiumData);
//
//			saveUnlockData();
//		}
//	}
//	
//	/**
//	 * Unlocks a Compendium Entry and all of it's related entries
//	 *
//	 * @param key The key used to identify the entry to unlock
//	 */
//	public void unlockEntry(String key){
//		boolean verbose = key.startsWith("cmd::");
//		key = key.replace("cmd::", "");
//
//		if (verbose && key.equals("all")){
//			for (CompendiumEntry entry : compendium.values()){
//				entry.setIsLocked(false);
//			}
//			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.unlockSuccess")));
//			return;
//		}
//
//		CompendiumEntry entry = getEntry(key);
//		if (entry == null){
//			if (key.contains("@")){
//				entry = getEntry(key.split("@")[0]);
//			}
//			if (entry == null){
//				ArsMagica2.LOGGER.warn("Attempted to unlock a compendium entry for a non-existant key: " + key);
//				if (verbose){
//					String message = String.format(I18n.translateToLocal("am2.tooltip.compEntryNotFound"), key);
//					Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(message));
//				}
//				return;
//			}
//		}
//		while (entry.getParent() != null)
//			entry = entry.getParent();
//		if (entry.isLocked){
//			unlockEntry(entry, true);
//			if (verbose){
//				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.unlockSuccess")));
//			}
//		}
//	}
//
//	public void unlockCategory(String category){
//		for (CompendiumEntry entry : compendium.values()){
//			if (entry.type.getCategoryName().equals(category)){
//				unlockEntry(entry, false);
//			}
//		}
//	}
//
//	public void unlockRelatedItems(ItemStack stack){
//		//initial error checking
//		if (stack == null)
//			return;
//
//		//initialize variables (definitely don't miss the C days...)
//		//initialize not at the top of the function.  Take that, old school!
//		CompendiumEntry entry = null;
//		String itemID = "";
//
//		//ensure the unlocalized name has been set
//		if (stack.getItem().getUnlocalizedName() == null)
//			return;
//
//		//construct a search ID based on what is passed in
//		if (stack.getItem() instanceof Item){
//			itemID = stack.getItem().getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
//		}else if (stack.getItem() instanceof ItemBlock){
//			itemID = stack.getItem().getUnlocalizedName().replace("arsmagica2:", "").replace("tile.", "");
//		}
//
//		//append meta specific search if needed
//		if (stack.getItemDamage() > -1)
//			itemID += "@" + stack.getItemDamage();
//
//		//search based on our constructed ID
//		entry = getEntry(itemID);
//
//		//did we find something?
//		if (entry != null){
//			//great!  ensure it's unlocked in the compendium
//			unlockEntry(entry, true);
//		}
//	}
//
//	public void setLockedState(boolean b){
//		for (CompendiumEntry entry : this.compendium.values())
//			entry.setIsLocked(b);
//	}
//
//	public boolean isCategory(String id){
//		for (CompendiumEntryType type : this.getCategories())
//			if (type.getCategoryName().equals(id))
//				return true;
//		return false;
//	}
//
//	public boolean isModUpdateAvailable(){
//		return this.modUpdateAvailable;
//	}
//
//	public String getModDownloadLink(){
//		return this.latestDownloadLink;
//	}
//
//	public String getPatchNotesLink(){
//		return this.latestPatchNotesLink;
//	}
//
//
//	@Override
//	public void AddCompenidumEntry(Object entryItem, String entryKey, String entryName, String entryDesc, String parent, boolean allowReplace, String... relatedKeys){
//		if (entryItem == null){
//			ArsMagica2.LOGGER.warn("Null entry item passed.  Cannot add Compendium Entry with key %s.", entryKey);
//			return;
//		}
//
//		CompendiumEntry existingEntry = getEntry(entryKey);
//		if (existingEntry != null && !allowReplace){
//			ArsMagica2.LOGGER.warn("Compendium entry with key %s exists, and allowReplace is false.  The entry was not added.", entryKey);
//			return;
//		}
//
//		CompendiumEntry parentEntry = parent == null ? null : getEntry(parent);
//		if (parent != null && parentEntry == null){
//			ArsMagica2.LOGGER.warn("The parent ID %s was not found.  Entry %s will be added with no parent.", parent, entryKey);
//		}
//
//		CompendiumEntry newEntry = null;
//
//		if (entryItem instanceof Item){
//			newEntry = new CompendiumEntryItem();
//		}else if (entryItem instanceof Block){
//			newEntry = new CompendiumEntryBlock();
//		}else if (entryItem instanceof IShape){
//			newEntry = new CompendiumEntrySpellShape();
//		}else if (entryItem instanceof IComponent){
//			newEntry = new CompendiumEntrySpellComponent();
//		}else if (entryItem instanceof IModifier){
//			newEntry = new CompendiumEntrySpellModifier();
//		}else if (entryItem instanceof Entity){
//			newEntry = new CompendiumEntryMob();
//		}
//
//		newEntry.id = entryKey;
//		newEntry.name = entryName;
//		newEntry.description = entryDesc;
//		newEntry.isLocked = true;
//		newEntry.isNew = true;
//		newEntry.parent = parentEntry;
//		for (String s : relatedKeys)
//			newEntry.relatedItems.add(s);
//
//		if (parentEntry != null){
//			parentEntry.subItems.add(newEntry);
//			addAlias(newEntry.getID(), parentEntry.getID());
//		}
//
//		this.compendium.put(entryKey, newEntry);
//		ArsMagica2.LOGGER.debug("Successfully added compendium entry %s", entryKey);
//	}
//	
}
