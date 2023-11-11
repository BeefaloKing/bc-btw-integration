// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.src;

import buildcraft.core.*;
import buildcraft.energy.*;
import forge.MinecraftForge;
import forge.Property;
import java.util.Random;
import java.util.TreeMap;

// Referenced classes of package net.minecraft.src:
//            BuildCraftCore, CraftingManager, ItemStack, Block,
//            Item, ModLoader, Material, World

public class BuildCraftEnergy
{

    public BuildCraftEnergy()
    {
    }

    public static void ModsLoaded()
    {
        BuildCraftCore.initialize();
        Property property = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("engine.id", DefaultProps.ENGINE_ID);
        Property property1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilMoving.id", DefaultProps.OIL_MOVING_ID);
        Property property2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilStill.id", DefaultProps.OIL_STILL_ID);
        Property property3 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("bucketOil.id", 2, DefaultProps.BUCKET_OIL_ID);
        BuildCraftCore.mainConfiguration.save();
        CraftingManager craftingmanager = CraftingManager.getInstance();
        engineBlock = new BlockEngine(Integer.parseInt(property.value));
        craftingmanager.addRecipe(new ItemStack(engineBlock, 1, 0), new Object[] {
            "www", " g ", "GpG", Character.valueOf('w'), Block.planks, Character.valueOf('g'), Block.glass, Character.valueOf('G'), BuildCraftCore.woodenGearItem, Character.valueOf('p'),
            Block.pistonBase
        });
        craftingmanager.addRecipe(new ItemStack(engineBlock, 1, 1), new Object[] {
            "www", " g ", "GpG", Character.valueOf('w'), Block.cobblestone, Character.valueOf('g'), Block.glass, Character.valueOf('G'), BuildCraftCore.stoneGearItem, Character.valueOf('p'),
            Block.pistonBase
        });
        craftingmanager.addRecipe(new ItemStack(engineBlock, 1, 2), new Object[] {
            "www", " g ", "GpG", Character.valueOf('w'), Item.ingotIron, Character.valueOf('g'), Block.glass, Character.valueOf('G'), BuildCraftCore.ironGearItem, Character.valueOf('p'),
            Block.pistonBase
        });
        ModLoader.RegisterBlock(engineBlock);
        Item.itemsList[engineBlock.blockID] = null;
        Item.itemsList[engineBlock.blockID] = new ItemEngine(engineBlock.blockID - 256);
        CoreProxy.addName(new ItemStack(engineBlock, 1, 0), "Wooden Engine");
        CoreProxy.addName(new ItemStack(engineBlock, 1, 1), "Steam Engine");
        CoreProxy.addName(new ItemStack(engineBlock, 1, 2), "Combustion Engine");
        oilMoving = (new BlockOilFlowing(Integer.parseInt(property1.value), Material.water)).setHardness(100F).setLightOpacity(3).setBlockName("oil");
        CoreProxy.addName(oilMoving.setBlockName("oilMoving"), "Oil");
        ModLoader.RegisterBlock(oilMoving);
        oilStill = (new BlockOilStill(Integer.parseInt(property2.value), Material.water)).setHardness(100F).setLightOpacity(3).setBlockName("oil");
        CoreProxy.addName(oilStill.setBlockName("oilStill"), "Oil");
        ModLoader.RegisterBlock(oilStill);
        if(oilMoving.blockID + 1 != oilStill.blockID)
        {
            throw new RuntimeException("Oil Still id must be Oil Moving id + 1");
        } else
        {
            MinecraftForge.registerCustomBucketHandler(new OilBucketHandler());
            bucketOil = (new ItemBucketOil(Integer.parseInt(property3.value))).setItemName("bucketOil").setContainerItem(Item.bucketEmpty);
            CoreProxy.addName(bucketOil, "Oil Bucket");
            return;
        }
    }

    public static void generateSurface(World world, Random random, int i, int j)
    {
        OilPopulate.doPopulate(world, i, j);
    }

    public static final int ENERGY_REMOVE_BLOCK = 25;
    public static final int ENERGY_EXTRACT_ITEM = 2;
    public static BlockEngine engineBlock;
    public static Block oilMoving;
    public static Block oilStill;
    public static Item bucketOil;
    public static TreeMap saturationStored = new TreeMap();

}
