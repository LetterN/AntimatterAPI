package muramasa.gregtech.common.blocks;

import muramasa.gregtech.api.enums.StoneType;
import muramasa.gregtech.api.materials.Material;
import muramasa.gregtech.api.properties.GTProperties;
import muramasa.gregtech.client.render.StateMapperRedirect;
import muramasa.gregtech.common.utils.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;

public class BlockOre extends Block {

    private static LinkedHashMap<String, BlockOre> BLOCK_LOOKUP = new LinkedHashMap<>();
    private static StateMapperRedirect stateMapRedirect = new StateMapperRedirect(new ResourceLocation(Ref.MODID, "block_ore"));

    private Material material;
    private StoneType type;

    public BlockOre(StoneType type, Material material) {
        super(net.minecraft.block.material.Material.ROCK);
        setUnlocalizedName("ore_" + type.getName() + "_" + material.getName());
        setRegistryName("ore_" + type.getName() + "_" + material.getName());
        setCreativeTab(Ref.TAB_BLOCKS);
        this.material = material;
        this.type = type;
        BLOCK_LOOKUP.put(type.getName() + "_" + material.getName(), this);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(GTProperties.SET, GTProperties.STONE).build();
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState exState = (IExtendedBlockState) state;
        return exState.withProperty(GTProperties.STONE, type.getInternalId()).withProperty(GTProperties.SET, material.getSet().ordinal());
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (type == StoneType.STONE) {
            items.add(new ItemStack(this));
        }
    }

    //TODO
    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return 1.0f + (getHarvestLevel(blockState) * 1.0f);
    }

    //TODO
    @Override
    public int getHarvestLevel(IBlockState state) {
        return 1;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(material.getChunk(1));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Ref.MODID + ":block_ore", "inventory"));
        ModelLoader.setCustomStateMapper(this, stateMapRedirect);
    }

    public Material getMaterial() {
        return material;
    }

    public String getMapString() {
        return type.getName() + material.getSet().getName();
    }

    public static BlockOre get(String material) {
        return BLOCK_LOOKUP.get(material);
    }

    public static Collection<BlockOre> getAll() {
        return BLOCK_LOOKUP.values();
    }

    public static class ColorHandler implements IBlockColor {
        @Override
        public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            if (tintIndex == 1) {
                BlockOre block = (BlockOre) state.getBlock();
                return block.getMaterial().getRGB();
            }
            return -1;
        }
    }
}
