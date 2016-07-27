package am2.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

import java.util.Iterator;
import java.util.ListIterator;

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

import am2.ArsMagica2;
import net.minecraft.launchwrapper.IClassTransformer;

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
						list.add(new TypeInsnNode(NEW, "am2/api/event/EventPotionAdded"));
						list.add(new InsnNode(DUP));
						list.add(new VarInsnNode(ALOAD, 1));
						list.add(new MethodInsnNode(INVOKESPECIAL, "am2/api/event/EventPotionAdded", "<init>", "(L" + className + ")V", false));
						list.add(new VarInsnNode(ASTORE, 2));
						list.add(new LabelNode());
						list.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
						list.add(new VarInsnNode(ALOAD, 2));
						list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
						list.add(new InsnNode(POP));
						list.add(new LabelNode());
						list.add(new VarInsnNode(ALOAD, 2));
						list.add(new MethodInsnNode(INVOKEVIRTUAL, "am2/api/event/EventPotionAdded", "getEffect", "()L" + className, false));
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
		} else if (transformedName.equalsIgnoreCase("net.minecraft.client.renderer.EntityRenderer"))
			return patchEntityRenderer(basicClass);
		return basicClass;
	}
	
	private byte[] patchEntityRenderer(byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		for (MethodNode mn : cn.methods){
			if (mn.name.equals("setupCameraTransform") && mn.desc.equals("(FI)V")){ // setupCameraTransform
				AbstractInsnNode orientCameraNode = null;
				AbstractInsnNode gluPerspectiveNode = null;
				ArsMagica2.LOGGER.info("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
				while (instructions.hasNext()){
					AbstractInsnNode node = instructions.next();
					if (node instanceof MethodInsnNode){
						MethodInsnNode method = (MethodInsnNode)node;
						if (orientCameraNode == null && method.name.equals("orientCamera") && method.desc.equals("(F)V")){ //orientCamera
							ArsMagica2.LOGGER.info("Core: Located target method insn node: " + method.name + method.desc);
							orientCameraNode = node;
							continue;
						}else if (gluPerspectiveNode == null && method.name.equals("gluPerspective") && method.desc.equals("(FFFF)V")){
							ArsMagica2.LOGGER.info("Core: Located target method insn node: " + method.name + method.desc);
							gluPerspectiveNode = node;
							continue;
						}
					}

					if (orientCameraNode != null && gluPerspectiveNode != null){
						//found all nodes we're looking for
						break;
					}
				}
				if (orientCameraNode != null){
					VarInsnNode floatset = new VarInsnNode(FLOAD, 1);
					MethodInsnNode callout = new MethodInsnNode(INVOKESTATIC, "am2/gui/AMGuiHelper", "shiftView", "(F)V", false);
					mn.instructions.insert(orientCameraNode, callout);
					mn.instructions.insert(orientCameraNode, floatset);
					ArsMagica2.LOGGER.info("Core: Success!  Inserted callout function op (shift)!");
				}
				if (gluPerspectiveNode != null){
					VarInsnNode floatset = new VarInsnNode(FLOAD, 1);
					MethodInsnNode callout = new MethodInsnNode(INVOKESTATIC, "am2/gui/AMGuiHelper", "flipView", "(F)V", false);
					mn.instructions.insert(gluPerspectiveNode, callout);
					mn.instructions.insert(gluPerspectiveNode, floatset);
					ArsMagica2.LOGGER.info("Core: Success!  Inserted callout function op (flip)!");
				}

			}
		}
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}

}
