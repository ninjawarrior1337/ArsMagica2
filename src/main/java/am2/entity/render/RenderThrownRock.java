package am2.entity.render;

import am2.entity.EntityThrownRock;
import am2.entity.model.ModelThrownRock;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderThrownRock extends RenderLiving<EntityThrownRock>{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", "textures/mobs/bosses/earth_guardian.png");

	public RenderThrownRock(RenderManager manager){
		super(manager, new ModelThrownRock(), 0.5f);
	}

	@Override
	public void doRender(EntityThrownRock par1Entity, double par2, double par4, double par6, float par8, float par9){
		if (!par1Entity.getIsShootingStar())
			super.doRender(par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityThrownRock entity){
		return rLoc;
	}

}
