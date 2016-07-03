package am2.asm;

import static org.objectweb.asm.Opcodes.*;

import java.util.ListIterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Transformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("net.minecraft.entity.EntityLivingBase")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);
			
			for (MethodNode mn : cn.methods) {
				if (mn.name.equals("c") || mn.name.equals("addPotionEffect")) {
					if (mn.desc.equals("(Lpf;)V") || mn.desc.equals("(Lnet/minecraft/potion/PotionEffect;)V")) {
						System.out.println("Patching addPotionEffect");
						String className = mn.desc.equals("(Lpf;)V") ? "pf;" : "net/minecraft/potion/PotionEffect;";
						InsnList list = new InsnList();
						list.add(new TypeInsnNode(NEW, "am2/event/EventPotionAdded"));
						list.add(new InsnNode(DUP));
						list.add(new VarInsnNode(ALOAD, 1));
						list.add(new MethodInsnNode(INVOKESPECIAL, "am2/event/EventPotionAdded", "<init>", "(L" + className + ")V", false));
						list.add(new VarInsnNode(ASTORE, 2));
						list.add(new LabelNode());
						list.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
						list.add(new VarInsnNode(ALOAD, 2));
						list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
						list.add(new InsnNode(POP));
						list.add(new LabelNode());
						list.add(new VarInsnNode(ALOAD, 2));
						list.add(new MethodInsnNode(INVOKEVIRTUAL, "am2/event/EventPotionAdded", "getEffect", "()L" + className, false));
						list.add(new VarInsnNode(ASTORE, 1));
						ListIterator<AbstractInsnNode> insns = mn.instructions.iterator();
						while (insns.hasNext()) {
							AbstractInsnNode insn = insns.next();
							if (insn instanceof LabelNode) {
								mn.instructions.insertBefore(insn, list);	
								break;
							}
						}
					}
				}
			}
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cn.accept(cw);
			return cw.toByteArray();
		} else if (transformedName.equalsIgnoreCase("net.minecraft.client.renderer.block.model.BlockPart$Deserializer")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);
			InsnList newInsn = new InsnList();
			newInsn.add(new VarInsnNode(ALOAD, 1));
			newInsn.add(new LdcInsnNode("angle"));
			newInsn.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/util/JsonUtils", "getFloat", "(Lcom/google/gson/JsonObject;Ljava/lang/String;)F", false));
			newInsn.add(new InsnNode(FRETURN));
			for (MethodNode mn : cn.methods) {
				if (mn.name.equals("parseAngle")) {
					System.out.println("Removing Model Rotation Limit...");
					mn.instructions = newInsn;
				}
			}
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cn.accept(cw);
			return cw.toByteArray();
		}
		return basicClass;
	}
	
	

}
