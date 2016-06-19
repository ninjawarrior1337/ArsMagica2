package am2.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import scala.util.Random;

public class RenderUtils {
	
	private static Random rand = new Random();
	
	public static void drawBox (float minX, float minZ, float maxX, float maxZ, float zLevel, float minU, float minV, float maxU, float maxV) {
		Tessellator t = Tessellator.getInstance();
		VertexBuffer wr = t.getBuffer();
		wr.begin(7, DefaultVertexFormats.POSITION_TEX);
		wr.pos(minX, minZ + maxZ, zLevel).tex(minU, maxV).endVertex();;
		wr.pos(minX + maxX, minZ + maxZ, zLevel).tex(maxU, maxV).endVertex();
		wr.pos(minX + maxX, minZ, zLevel).tex(maxU, minV).endVertex();
		wr.pos(minX, minZ, zLevel).tex(minU, minV).endVertex();
		t.draw();
	}
	
	public static float getRed (int color) {
		return ((color & 0xFF0000) >> 16) / 255.0f;
	}
	
	public static float getGreen (int color) {
		return ((color & 0x00FF00) >> 8) / 255.0f;
	}
	
	public static float getBlue (int color) {
		return (color & 0x0000FF) / 255.0f;
	}
	
	public static void color(int color) {
		
	}
	
	public static void line2d (float xStart, float yStart, float xEnd, float yEnd, float zLevel, int color) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.enableDepth();
		GL11.glLineWidth(1f);
		GL11.glColor3d(((color & 0xFF0000) >> 16) / 255.0d, ((color & 0x00FF00) >> 8) / 255.0f, (color & 0x0000FF) / 255.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(xStart, yStart, zLevel);
		GL11.glVertex3f(xEnd, yEnd, zLevel);
		GL11.glEnd();
		GL11.glColor3d(1.0f, 1.0f, 1.0f);
		GlStateManager.disableDepth();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void lineThick2d (float xStart, float yStart, float xEnd, float yEnd, float zLevel, int color) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.enableDepth();
		GL11.glLineWidth(4f);
		GL11.glColor3d(((color & 0xFF0000) >> 16) / 255.0d, ((color & 0x00FF00) >> 8) / 255.0f, (color & 0x0000FF) / 255.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(xStart, yStart, zLevel);
		GL11.glVertex3f(xEnd, yEnd, zLevel);
		GL11.glEnd();
		GL11.glColor3d(1.0f, 1.0f, 1.0f);
		GlStateManager.disableDepth();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	public static void fractalLine2df(float xStart, float yStart, float xEnd, float yEnd, float zLevel, int color, float displace, float fractalDetail) {
		if (displace < fractalDetail){
			line2d(xStart, yStart, xEnd, yEnd, zLevel, color);
		}else{
			int mid_x = (int) ((xEnd + xStart) / 2);
			int mid_y = (int) ((yEnd + yStart) / 2);
			mid_x += (rand.nextFloat() - 0.5) * displace;
			mid_y += (rand.nextFloat() - 0.5) * displace;
			fractalLine2df(xStart, yStart, mid_x, mid_y, zLevel, color, displace / 2f, fractalDetail);
			fractalLine2df(xEnd, yEnd, mid_x, mid_y, zLevel, color, displace / 2f, fractalDetail);
		}
	}

	public static void fractalLine2dd(double xStart, double yStart, double xEnd, double yEnd, float zLevel, int color, float displace, float fractalDetail) {
		fractalLine2df((float)xStart, (float)yStart, (float)xEnd, (float)yEnd, (float)zLevel, color, displace, fractalDetail);
	}

}
