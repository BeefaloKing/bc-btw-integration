// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.src;

import buildcraft.core.*;
import buildcraft.factory.*;
import forge.Property;

// Referenced classes of package net.minecraft.src:
//            mod_BuildCraftCore, BuildCraftCore, CraftingManager, ModLoader,
//            ItemStack, Item, Block

public class BuildCraftFactory
{

    public BuildCraftFactory()
    {
    }

    public static void initialize()
    {
        mod_BuildCraftCore.initialize();
        BuildCraftCore.initializeGears();
        CraftingManager craftingmanager = CraftingManager.getInstance();
        boolean flag = Boolean.parseBoolean(BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("mining.enabled", 0, true).value);
        Property property = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("miningWell.id", DefaultProps.MINING_WELL_ID);
        Property property1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("drill.id", DefaultProps.DRILL_ID);
        Property property2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("autoWorkbench.id", DefaultProps.AUTO_WORKBENCH_ID);
        Property property3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("frame.id", DefaultProps.FRAME_ID);
        Property property4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("quarry.id", DefaultProps.QUARRY_ID);
        Property property5 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("pump.id", DefaultProps.PUMP_ID);
        Property property6 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("tank.id", DefaultProps.TANK_ID);
        BuildCraftCore.mainConfiguration.save();
        miningWellBlock = new BlockMiningWell(Integer.parseInt(property.value));
        ModLoader.RegisterBlock(miningWellBlock);
        CoreProxy.addName(miningWellBlock.setBlockName("miningWellBlock"), "Mining Well");
        if(flag)
        {
            craftingmanager.addRecipe(new ItemStack(miningWellBlock, 1), new Object[] {
                "ipi", "igi", "iPi", Character.valueOf('p'), Item.redstone, Character.valueOf('i'), Item.ingotIron, Character.valueOf('g'), BuildCraftCore.ironGearItem, Character.valueOf('P'),
                Item.pickaxeSteel
            });
        }
        plainPipeBlock = new BlockPlainPipe(Integer.parseInt(property1.value));
        ModLoader.RegisterBlock(plainPipeBlock);
        CoreProxy.addName(plainPipeBlock.setBlockName("plainPipeBlock"), "Mining Pipe");
        autoWorkbenchBlock = new BlockAutoWorkbench(Integer.parseInt(property2.value));
        ModLoader.RegisterBlock(autoWorkbenchBlock);
        craftingmanager.addRecipe(new ItemStack(autoWorkbenchBlock), new Object[] {
            " s ", "gwg", " g ", Character.valueOf('w'), Block.workbench, Character.valueOf('g'), BuildCraftCore.woodenGearItem, Character.valueOf('s'), BuildCraftCore.ironGearItem
        });
        CoreProxy.addName(autoWorkbenchBlock.setBlockName("autoWorkbenchBlock"), "Automatic Crafting Table");
        frameBlock = new BlockFrame(Integer.parseInt(property3.value));
        ModLoader.RegisterBlock(frameBlock);
        CoreProxy.addName(frameBlock.setBlockName("frameBlock"), "Frame");
        quarryBlock = new BlockQuarry(Integer.parseInt(property4.value));
        ModLoader.RegisterBlock(quarryBlock);
        if(flag)
        {
            craftingmanager.addRecipe(new ItemStack(quarryBlock), new Object[] {
                "ipi", "gig", "dDd", Character.valueOf('i'), BuildCraftCore.ironGearItem, Character.valueOf('p'), Item.redstone, Character.valueOf('g'), BuildCraftCore.goldGearItem, Character.valueOf('d'),
                BuildCraftCore.diamondGearItem, Character.valueOf('D'), Item.pickaxeDiamond
            });
        }
        CoreProxy.addName(quarryBlock.setBlockName("machineBlock"), "Quarry");
        tankBlock = new BlockTank(Integer.parseInt(property6.value));
        craftingmanager.addRecipe(new ItemStack(tankBlock), new Object[] {
            "ggg", "g g", "ggg", Character.valueOf('g'), Block.glass
        });
        CoreProxy.addName(tankBlock.setBlockName("tankBlock"), "Tank");
        ModLoader.RegisterBlock(tankBlock);
        pumpBlock = new BlockPump(Integer.parseInt(property5.value));
        craftingmanager.addRecipe(new ItemStack(pumpBlock), new Object[] {
            "T ", "W ", Character.valueOf('T'), tankBlock, Character.valueOf('W'), miningWellBlock
        });
        CoreProxy.addName(pumpBlock.setBlockName("pumpBlock"), "Pump");
        ModLoader.RegisterBlock(pumpBlock);
        ModLoader.RegisterTileEntity(buildcraft.factory.TileQuarry.class, "Machine");
        ModLoader.RegisterTileEntity(buildcraft.factory.TileMiningWell.class, "MiningWell");
        ModLoader.RegisterTileEntity(buildcraft.factory.TileAutoWorkbench.class, "AutoWorkbench");
        ModLoader.RegisterTileEntity(buildcraft.factory.TilePump.class, "net.minecraft.src.buildcraft.factory.TilePump");
        ModLoader.RegisterTileEntity(buildcraft.factory.TileTank.class, "net.minecraft.src.buildcraft.factory.TileTank");
        drillTexture = 33;
        BuildCraftCore.mainConfiguration.save();
    }

    public static BlockQuarry quarryBlock;
    public static BlockMiningWell miningWellBlock;
    public static BlockAutoWorkbench autoWorkbenchBlock;
    public static BlockFrame frameBlock;
    public static BlockPlainPipe plainPipeBlock;
    public static BlockPump pumpBlock;
    public static BlockTank tankBlock;
    public static int drillTexture;
}
