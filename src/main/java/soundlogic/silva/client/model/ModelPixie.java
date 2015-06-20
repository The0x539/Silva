package soundlogic.silva.client.model;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.Pixie;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.common.entity.EntityPixie;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelPixie extends ModelBase {
	
	ModelRenderer Body;
	ModelRenderer LeftWing;
	ModelRenderer RightWing;
	
	public ModelPixie() {
		textureWidth = 64;
		textureHeight = 32;

		Body = new ModelRenderer(this, 0, 0);
		Body.addBox(0F, 0F, 0F, 4, 4, 4);
		Body.setRotationPoint(-2F, 16F, -2F);
		Body.setTextureSize(64, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		LeftWing = new ModelRenderer(this, 32, 0);
		LeftWing.addBox(0F, 0F, -1F, 0, 4, 7);
		LeftWing.setRotationPoint(2F, 15F, 2F);
		LeftWing.setTextureSize(64, 32);
		LeftWing.mirror = true;
		setRotation(LeftWing, 0F, 0F, 0F);
		RightWing = new ModelRenderer(this, 50, 0);
		RightWing.addBox(0F, 0F, -1F, 0, 4, 7);
		RightWing.setRotationPoint(-2F, 15F, 2F);
		RightWing.setTextureSize(64, 32);
		RightWing.mirror = true;
		setRotation(RightWing, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		Body.render(f5);
		LeftWing.render(f5);
		RightWing.render(f5);
	}

	public void render(Pixie pixie, float pticks, float f5) {
		setRotationAngles(pixie, pticks);
        GL11.glDisable(GL11.GL_CULL_FACE);
		Body.render(f5);
		LeftWing.render(f5);
		RightWing.render(f5);
        GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(Pixie pixie, float pticks) {
		RightWing.rotateAngleY = -((MathHelper.cos((pixie.ticks+pticks) * 1.7F* .5F)+1F) / 2F * (float)Math.PI * 0.5F);
		LeftWing.rotateAngleY = (MathHelper.cos((pixie.ticks+pticks) * 1.7F * .5F)+1F) / 2F * (float)Math.PI * 0.5F;
	}
}