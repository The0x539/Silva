package soundlogic.silva.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelManaPotato extends ModelBase {

	ModelRenderer potato;

	public ModelManaPotato() {
        this.textureWidth = 64;
        this.textureHeight = 32;

		potato = new ModelRenderer(this, 0, 0);
		potato.addBox(0F, 0F, 0F, 4, 6, 4);
		potato.setRotationPoint(-2F, 18F, -2F);
		potato.setTextureSize(64, 32);
	}

	public void render() {
		potato.render(1F / 16F);
	}

}
