package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator
{
    /**
     * The Block ID that the generator is allowed to replace while generating the terrain.
     */
    private int replaceID;

    public WorldGenSpikes(int par1)
    {
        this.replaceID = par1;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        if (par1World.isAirBlock(par3, par4, par5) && par1World.getBlockId(par3, par4 - 1, par5) == this.replaceID)
        {
            int l = par2Random.nextInt(32) + 6;
            int i1 = par2Random.nextInt(4) + 1;
            int j1;
            int k1;
            int l1;
            int i2;

            for (j1 = par3 - i1; j1 <= par3 + i1; ++j1)
            {
                for (k1 = par5 - i1; k1 <= par5 + i1; ++k1)
                {
                    l1 = j1 - par3;
                    i2 = k1 - par5;

                    if (l1 * l1 + i2 * i2 <= i1 * i1 + 1 && par1World.getBlockId(j1, par4 - 1, k1) != this.replaceID)
                    {
                        return false;
                    }
                }
            }

            for (j1 = par4; j1 < par4 + l && j1 < 128; ++j1)
            {
                for (k1 = par3 - i1; k1 <= par3 + i1; ++k1)
                {
                    for (l1 = par5 - i1; l1 <= par5 + i1; ++l1)
                    {
                        i2 = k1 - par3;
                        int j2 = l1 - par5;

                        if (i2 * i2 + j2 * j2 <= i1 * i1 + 1)
                        {
                            par1World.setBlockAndMetadataWithNotify(k1, j1, l1, Block.obsidian.blockID, 0, 2);
                        }
                    }
                }
            }

            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(par1World);
            entityendercrystal.setLocationAndAngles((double)((float)par3 + 0.5F), (double)(par4 + l), (double)((float)par5 + 0.5F), par2Random.nextFloat() * 360.0F, 0.0F);
            par1World.spawnEntityInWorld(entityendercrystal);
            par1World.setBlockAndMetadataWithNotify(par3, par4 + l, par5, Block.bedrock.blockID, 0, 2);
            return true;
        }
        else
        {
            return false;
        }
    }
}
