package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public interface Hopper extends IInventory
{
    /**
     * Returns the worldObj for this tileEntity.
     */
    World getWorldObj();

    double func_96107_aA();

    double func_96109_aB();

    double func_96108_aC();
}
