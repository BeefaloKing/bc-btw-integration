// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package buildcraft.energy;

import net.minecraft.src.FCBlockAxle;
import net.minecraft.src.FCBlockPos;
import net.minecraft.src.FCMechanicalDevice;
import net.minecraft.src.mod_FCBetterThanWolves;
import net.minecraft.src.World;

// Referenced classes of package buildcraft.energy:
//            Engine, TileEngine

public class EngineWood extends Engine implements FCMechanicalDevice
{
    public EngineWood(TileEngine tileengine)
    {
        super(tileengine);
        maxEnergy = 1000;
    }

    public String getTextureFile()
    {
        return "/net/minecraft/src/buildcraft/energy/gui/base_wood.png";
    }

    public int explosionRange()
    {
        return 1;
    }

    public int maxEnergyReceived()
    {
        return 50;
    }

    public float getPistonSpeed()
    {
        switch(getEnergyStage())
        {
        case Blue: // '\001'
            return 0.01F;

        case Green: // '\002'
            return 0.02F;

        case Yellow: // '\003'
            return 0.04F;

        case Red: // '\004'
            return 0.08F;
        }
        return 0.0F;
    }

    public void update()
    {
        // This is supposed to handle energy loss over time when not powered.
        // We instead handle ourselves due to the BTW mechanical power integration.
        //super.update();

        if(isBurning())
        {
            if(tile.worldObj.getWorldTime() % 20L == 0L)
            {
                energy++;
            }
        }
        else if(energy > 1) // Not inputting mechanical power.
        {
            energy--;
        }
    }

    public boolean isBurning()
    {
        return IsInputtingMechanicalPower(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public boolean CanOutputMechanicalPower()
    {
        return false;
    }

    public boolean CanInputMechanicalPower()
    {
        return false;
    }

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k)
    {
        // TODO: Move this somewhere common.
        // Translate BC orientation enum to BTW facing int.
        int l = 0;
        switch(tile.engine.orientation.reverse())
        {
        case XPos:
            l = 5;
            break;
        case XNeg:
            l = 4;
            break;
        case YPos:
            l = 1;
            break;
        case YNeg:
            l = 0;
            break;
        case ZPos:
            l = 3;
            break;
        case ZNeg:
            l = 2;
            break;
        }

        FCBlockPos blockPos = new FCBlockPos(i, j, k);
        blockPos.AddFacingAsOffset(l);
        int behind = world.getBlockId(blockPos.i, blockPos.j, blockPos.k);
        if(behind != mod_FCBetterThanWolves.fcAxleBlock.blockID)
        {
            return false;
        }

        FCBlockAxle blockAxle = (FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock;
        return blockAxle.IsAxleOrientedTowardsFacing(world, blockPos.i, blockPos.j, blockPos.k, l)
            && blockAxle.GetPowerLevel(world, blockPos.i, blockPos.j, blockPos.k) > 0;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int k)
    {
        return false;
    }
}
