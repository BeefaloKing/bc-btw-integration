// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package buildcraft.energy;

import buildcraft.api.*;
import buildcraft.core.ILiquidContainer;
import buildcraft.core.TileBuildCraft;
import net.minecraft.src.*;

// Referenced classes of package buildcraft.energy:
//            Engine, EngineStone, EngineIron, EngineWood,
//            PneumaticPowerProvider

public class TileEngine extends TileBuildCraft
    implements IPowerReceptor, IInventory, ILiquidContainer
{

    public TileEngine()
    {
        progressPart = 0;
        burnTime = 0;
        serverPistonSpeed = 0.0F;
        lastPower = false;
        totalBurnTime = 0;
        scaledBurnTime = 0;
        provider = BuildCraftCore.powerFramework.createPowerProvider();
    }

    public void initialize()
    {
        if(!APIProxy.isClient(worldObj))
        {
            if(engine == null)
            {
                createEngineIfNeeded();
            }
            engine.orientation = Orientations.values()[orientation];
            provider.configure(0, 1, engine.maxEnergyReceived(), 1, engine.maxEnergy);
        }
    }

    public void updateEntity()
    {
        super.updateEntity();
        if(engine == null)
        {
            return;
        }
        if(APIProxy.isClient(worldObj))
        {
            if(progressPart != 0)
            {
                engine.progress += serverPistonSpeed;
                if(engine.progress > 1.0F)
                {
                    progressPart = 0;
                }
            }
            return;
        }
        engine.update();
        boolean flag;
        if(engine instanceof EngineWood)
        {
            flag = engine.isBurning();
        }
        else
        {
            flag = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
        }
        if(progressPart != 0)
        {
            engine.progress += engine.getPistonSpeed();
            if((double)engine.progress > 0.5D && progressPart == 1)
            {
                progressPart = 2;
                Position position = new Position(xCoord, yCoord, zCoord, engine.orientation);
                position.moveForwards(1.0D);
                TileEntity tileentity = worldObj.getBlockTileEntity((int)position.x, (int)position.y, (int)position.z);
                if(isPoweredTile(tileentity))
                {
                    IPowerReceptor ipowerreceptor = (IPowerReceptor)tileentity;
                    int j = engine.extractEnergy(ipowerreceptor.getPowerProvider().minEnergyReceived, ipowerreceptor.getPowerProvider().maxEnergyReceived, true);
                    if(j > 0)
                    {
                        ipowerreceptor.getPowerProvider().receiveEnergy(j);
                    }
                }
            } else
            if(engine.progress >= 1.0F)
            {
                engine.progress = 0.0F;
                progressPart = 0;
            }
        } else
        if(flag)
        {
            Position position1 = new Position(xCoord, yCoord, zCoord, engine.orientation);
            position1.moveForwards(1.0D);
            TileEntity tileentity1 = worldObj.getBlockTileEntity((int)position1.x, (int)position1.y, (int)position1.z);
            if(isPoweredTile(tileentity1))
            {
                IPowerReceptor ipowerreceptor1 = (IPowerReceptor)tileentity1;
                if(engine.extractEnergy(ipowerreceptor1.getPowerProvider().minEnergyReceived, ipowerreceptor1.getPowerProvider().maxEnergyReceived, false) > 0)
                {
                    progressPart = 1;
                    sendNetworkUpdate();
                }
            }
        } else
        if((worldObj.getWorldTime() % 20L) * 10L == 0L)
        {
            sendNetworkUpdate();
        }
        if(engine instanceof EngineStone)
        {
            if(burnTime > 0)
            {
                burnTime--;
                engine.addEnergy(1);
            }
            if(burnTime == 0 && flag)
            {
                burnTime = totalBurnTime = getItemBurnTime(itemInInventory);
                if(burnTime > 0)
                {
                    decrStackSize(1, 1);
                }
            }
        } else
        if(engine instanceof EngineIron)
        {
            if(flag && burnTime > 0)
            {
                burnTime--;
                engine.addEnergy(2);
            }
            if(itemInInventory != null && itemInInventory.itemID == BuildCraftEnergy.bucketOil.shiftedIndex)
            {
                totalBurnTime = OIL_BUCKET_TIME * 10;
                int i = OIL_BUCKET_TIME;
                if(burnTime + i <= totalBurnTime)
                {
                    itemInInventory = new ItemStack(Item.bucketEmpty, 1);
                    burnTime = burnTime + i;
                }
            }
        }
        if(totalBurnTime != 0)
        {
            scaledBurnTime = (short)((burnTime * 1000) / totalBurnTime);
        } else
        {
            scaledBurnTime = 0;
        }
    }

    private void createEngineIfNeeded()
    {
        if(engine == null)
        {
            int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            if(i == 0)
            {
                engine = new EngineWood(this);
            } else
            if(i == 1)
            {
                engine = new EngineStone(this);
            } else
            if(i == 2)
            {
                engine = new EngineIron(this);
            }
            engine.orientation = Orientations.values()[orientation];
        }
    }

    public void switchOrientation()
    {
        int i = orientation + 1;
        do
        {
            if(i > orientation + 6)
            {
                break;
            }
            Orientations orientations = Orientations.values()[i % 6];
            Position position = new Position(xCoord, yCoord, zCoord, orientations);
            position.moveForwards(1.0D);
            TileEntity tileentity = worldObj.getBlockTileEntity((int)position.x, (int)position.y, (int)position.z);
            if(isPoweredTile(tileentity))
            {
                if(engine != null)
                {
                    engine.orientation = orientations;
                }
                orientation = orientations.ordinal();
                worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
                break;
            }
            i++;
        } while(true);
    }

    public void delete()
    {
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        int i = nbttagcompound.getInteger("kind");
        if(i == 0)
        {
            engine = new EngineWood(this);
        } else
        if(i == 1)
        {
            engine = new EngineStone(this);
        } else
        if(i == 2)
        {
            engine = new EngineIron(this);
        }
        orientation = nbttagcompound.getInteger("orientation");
        engine.progress = nbttagcompound.getFloat("progress");
        engine.energy = nbttagcompound.getInteger("energy");
        engine.orientation = Orientations.values()[orientation];
        totalBurnTime = nbttagcompound.getInteger("totalBurnTime");
        burnTime = nbttagcompound.getInteger("burnTime");
        if(nbttagcompound.hasKey("itemInInventory"))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("itemInInventory");
            itemInInventory = new ItemStack(nbttagcompound1);
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("kind", worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
        nbttagcompound.setInteger("orientation", orientation);
        nbttagcompound.setFloat("progress", engine.progress);
        nbttagcompound.setInteger("energy", engine.energy);
        nbttagcompound.setInteger("totalBurnTime", totalBurnTime);
        nbttagcompound.setInteger("burnTime", burnTime);
        if(itemInInventory != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            itemInInventory.writeToNBT(nbttagcompound1);
            nbttagcompound.setTag("itemInInventory", nbttagcompound1);
        }
    }

    public int getSizeInventory()
    {
        return 1;
    }

    public ItemStack getStackInSlot(int i)
    {
        return itemInInventory;
    }

    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack itemstack = itemInInventory.splitStack(j);
        if(itemInInventory.stackSize == 0)
        {
            itemInInventory = null;
        }
        return itemstack;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        itemInInventory = itemstack;
    }

    public String getInvName()
    {
        return "Engine";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    private int getItemBurnTime(ItemStack itemstack)
    {
        if(itemstack == null)
        {
            return 0;
        }
        int i = itemstack.getItem().shiftedIndex;
        if(i < 256 && Block.blocksList[i].blockMaterial == Material.wood)
        {
            return 300;
        }
        if(i == Item.stick.shiftedIndex)
        {
            return 100;
        }
        if(i == Item.coal.shiftedIndex)
        {
            return 1600;
        }
        if(i == Item.bucketLava.shiftedIndex)
        {
            return 20000;
        } else
        {
            return i != Block.sapling.blockID ? ModLoader.AddAllFuel(i) : 100;
        }
    }

    public boolean isBurning()
    {
        return engine != null && engine.isBurning();
    }

    public int getBurnTimeRemainingScaled(int i)
    {
        return (scaledBurnTime * i) / 1000;
    }

    public Packet getDescriptionPacket()
    {
        createEngineIfNeeded();
        return super.getDescriptionPacket();
    }

    public Packet230ModLoader getUpdatePacket()
    {
        serverPistonSpeed = engine.getPistonSpeed();
        return super.getUpdatePacket();
    }

    public void handleDescriptionPacket(Packet230ModLoader packet230modloader)
    {
        createEngineIfNeeded();
        super.handleDescriptionPacket(packet230modloader);
    }

    public void handleUpdatePacket(Packet230ModLoader packet230modloader)
    {
        createEngineIfNeeded();
        super.handleUpdatePacket(packet230modloader);
    }

    public void setPowerProvider(PowerProvider powerprovider)
    {
        provider = powerprovider;
    }

    public PowerProvider getPowerProvider()
    {
        return provider;
    }

    public void doWork()
    {
        if(APIProxy.isClient(worldObj))
        {
            return;
        } else
        {
            engine.addEnergy((int)((float)provider.useEnergy(1, engine.maxEnergyReceived(), true) * 0.95F));
            return;
        }
    }

    public boolean isPoweredTile(TileEntity tileentity)
    {
        if(tileentity instanceof IPowerReceptor)
        {
            IPowerReceptor ipowerreceptor = (IPowerReceptor)tileentity;
            PowerProvider powerprovider = ipowerreceptor.getPowerProvider();
            return powerprovider != null && powerprovider.getClass().equals(buildcraft.energy.PneumaticPowerProvider.class);
        } else
        {
            return false;
        }
    }

    public int fill(Orientations orientations, int i)
    {
        if(engine instanceof EngineIron)
        {
            totalBurnTime = OIL_BUCKET_TIME * 10;
            int j = (int)(((float)i * (float)OIL_BUCKET_TIME) / 1000F);
            if(j + burnTime <= OIL_BUCKET_TIME * 10)
            {
                burnTime = burnTime + j;
                return i;
            } else
            {
                int k = OIL_BUCKET_TIME * 10 - burnTime;
                int l = (int)(((float)k * 1000F) / (float)OIL_BUCKET_TIME);
                burnTime += (int)(((float)l * (float)OIL_BUCKET_TIME) / 1000F);
                return l;
            }
        } else
        {
            return 0;
        }
    }

    public int empty(int i, boolean flag)
    {
        return 0;
    }

    public int getLiquidQuantity()
    {
        return 0;
    }

    public int getCapacity()
    {
        return 10000;
    }

    public Engine engine;
    public int progressPart;
    public int burnTime;
    public float serverPistonSpeed;
    boolean lastPower;
    public int orientation;
    private ItemStack itemInInventory;
    public int totalBurnTime;
    public short scaledBurnTime;
    PowerProvider provider;
    public static int OIL_BUCKET_TIME = 10000;

}
