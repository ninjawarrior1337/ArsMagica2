package thehippomaster.AnimationExample.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thehippomaster.AnimationExample.EntityTest;

@SideOnly(Side.CLIENT)
public class RenderTest extends RenderLiving<EntityTest> {
	
	public RenderTest(RenderManager manager) {
		super(manager, new ModelTest(), 0.25F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityTest entity) {
		return texture;
	}
	
	private static final ResourceLocation texture = new ResourceLocation("none");
}
