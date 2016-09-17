package am2.asm;

public class Mappings {
	
	public static final Mapping POTION_EFFECT_TYPE = new Mapping("Lnet/minecraft/potion/PotionEffect;", "Lrl;");
	public static final Mapping NBT_TAG_COMPOUND_TYPE = new Mapping("Lnet/minecraft/nbt/NBTTagCompound;", "Ldq;");	
	
	public static class Mapping {
		private String deobf, obf;
		
		public Mapping(String deobf, String obf) {
			this.deobf =  deobf;
			this.obf = obf;
		}
		
		public String getValue(boolean isObf) {
			return isObf ? obf : deobf;
		}
	}
}
