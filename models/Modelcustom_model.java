// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports

public static class Modelcustom_model extends EntityModel<Entity> {
	private final ModelRenderer OreFish;
	private final ModelRenderer leftfin;
	private final ModelRenderer rightfin;

	public Modelcustom_model() {
		texWidth = 32;
		texHeight = 32;

		OreFish = new ModelRenderer(this);
		OreFish.setPos(0.0F, 24.0F, 0.0F);
		OreFish.texOffs(0, 0).addBox(-7.0F, -4.0F, -1.0F, 11.0F, 3.0F, 2.0F, 0.0F, false);
		OreFish.texOffs(0, 5).addBox(-6.0F, -1.0F, -1.0F, 10.0F, 1.0F, 2.0F, 0.0F, false);
		OreFish.texOffs(10, 8).addBox(-4.0F, -5.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);
		OreFish.texOffs(0, 8).addBox(-1.0F, -5.0F, 0.0F, 3.0F, 1.0F, 0.0F, 0.0F, false);
		OreFish.texOffs(6, 8).addBox(4.0F, -3.0F, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, false);
		OreFish.texOffs(8, 10).addBox(5.0F, -4.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);
		OreFish.texOffs(4, 10).addBox(5.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);

		leftfin = new ModelRenderer(this);
		leftfin.setPos(-3.0F, -1.0F, -1.0F);
		OreFish.addChild(leftfin);
		setRotationAngle(leftfin, -0.7854F, 0.0F, 0.0F);
		leftfin.texOffs(0, 10).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);
		leftfin.texOffs(0, 1).addBox(-1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		rightfin = new ModelRenderer(this);
		rightfin.setPos(-3.0F, -1.0F, 1.0F);
		OreFish.addChild(rightfin);
		setRotationAngle(rightfin, 0.7854F, 0.0F, 0.0F);
		rightfin.texOffs(0, 9).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);
		rightfin.texOffs(0, 0).addBox(-1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		OreFish.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}