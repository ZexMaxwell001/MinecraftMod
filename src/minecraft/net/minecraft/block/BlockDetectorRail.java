package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDetectorRail extends BlockRailBase
{
    @SideOnly(Side.CLIENT)
    private Icon[] field_100010_b;

    public BlockDetectorRail(int par1)
    {
        super(par1, true);
        this.setTickRandomly(true);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 20;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (!par1World.isRemote)
        {
            int l = par1World.getBlockMetadata(par2, par3, par4);

            if ((l & 8) == 0)
            {
                this.setStateIfMinecartInteractsWithRail(par1World, par2, par3, par4, l);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            int l = par1World.getBlockMetadata(par2, par3, par4);

            if ((l & 8) != 0)
            {
                this.setStateIfMinecartInteractsWithRail(par1World, par2, par3, par4, l);
            }
        }
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) != 0 ? 15 : 0;
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) == 0 ? 0 : (par5 == 1 ? 15 : 0);
    }

    /**
     * Update the detector rail power state if a minecart enter, stays or leave the block.
     */
    private void setStateIfMinecartInteractsWithRail(World par1World, int par2, int par3, int par4, int par5)
    {
        boolean flag = (par5 & 8) != 0;
        boolean flag1 = false;
        float f = 0.125F;
        List list = par1World.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getAABBPool().getAABB((double)((float)par2 + f), (double)par3, (double)((float)par4 + f), (double)((float)(par2 + 1) - f), (double)((float)(par3 + 1) - f), (double)((float)(par4 + 1) - f)));

        if (!list.isEmpty())
        {
            flag1 = true;
        }

        if (flag1 && !flag)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par5 | 8, 3);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
        }

        if (!flag1 && flag)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par5 & 7, 3);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
        }

        if (flag1)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        }

        par1World.func_96440_m(par2, par3, par4, this.blockID);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setStateIfMinecartInteractsWithRail(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4));
    }

    public boolean func_96468_q_()
    {
        return true;
    }

    public int func_94328_b_(World par1World, int par2, int par3, int par4, int par5)
    {
        if ((par1World.getBlockMetadata(par2, par3, par4) & 8) > 0)
        {
            float f = 0.125F;
            List list = par1World.selectEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getAABBPool().getAABB((double)((float)par2 + f), (double)par3, (double)((float)par4 + f), (double)((float)(par2 + 1) - f), (double)((float)(par3 + 1) - f), (double)((float)(par4 + 1) - f)), IEntitySelector.field_96566_b);

            if (list.size() > 0)
            {
                return Container.func_94526_b((IInventory)list.get(0));
            }
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.field_100010_b = new Icon[2];
        this.field_100010_b[0] = par1IconRegister.func_94245_a("detectorRail");
        this.field_100010_b[1] = par1IconRegister.func_94245_a("detectorRail_on");
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return (par2 & 8) != 0 ? this.field_100010_b[1] : this.field_100010_b[0];
    }
}
