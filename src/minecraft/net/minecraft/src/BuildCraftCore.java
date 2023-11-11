// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.src;

import buildcraft.api.PowerFramework;
import buildcraft.core.*;
import forge.Property;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.TreeMap;

// Referenced classes of package net.minecraft.src:
//            CraftingManager, Item, ItemStack, Block,
//            ModLoader, BaseMod

public class BuildCraftCore
{

    public BuildCraftCore()
    {
    }

    public static void initialize()
    {
        if(initialized)
        {
            return;
        }
        initialized = true;
        mainConfiguration = new BuildCraftConfiguration(new File(CoreProxy.getBuildCraftBase(), "config/buildcraft.cfg"), true);
        mainConfiguration.load();
        redLaserTexture = 2;
        blueLaserTexture = 1;
        stripesLaserTexture = 3;
        transparentTexture = 0;
        Property property = mainConfiguration.getOrCreateBooleanProperty("current.continuous", 0, DefaultProps.CURRENT_CONTINUOUS);
        property.comment = "set to true for allowing machines to be driven by continuous current";
        continuousCurrentModel = Boolean.parseBoolean(property.value);
        Property property1 = mainConfiguration.getOrCreateProperty("power.framework", 0, "buildcraft.energy.PneumaticPowerFramework");
        try
        {
            powerFramework = (PowerFramework)Class.forName(property1.value).getConstructor(null).newInstance(null);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
            powerFramework = new RedstonePowerFramework();
        }
        Property property2 = mainConfiguration.getOrCreateIntProperty("wrench.id", 2, DefaultProps.WRENCH_ID);
        mainConfiguration.save();
        initializeGears();
        CraftingManager craftingmanager = CraftingManager.getInstance();
        wrenchItem = (new BuildCraftItem(Integer.parseInt(property2.value))).setIconIndex(2).setItemName("wrenchItem");
        craftingmanager.addRecipe(new ItemStack(wrenchItem), new Object[] {
            "I I", " G ", " I ", Character.valueOf('I'), Item.ingotIron, Character.valueOf('G'), stoneGearItem
        });
        CoreProxy.addName(wrenchItem, "Wrench");
        mainConfiguration.save();
    }

    public static void initializeGears()
    {
        if(gearsInitialized)
        {
            return;
        } else
        {
            // Property property = mainConfiguration.getOrCreateIntProperty("woodenGearItem.id", 2, DefaultProps.WOODEN_GEAR_ID);
            Property property1 = mainConfiguration.getOrCreateIntProperty("stoneGearItem.id", 2, DefaultProps.STONE_GEAR_ID);
            Property property2 = mainConfiguration.getOrCreateIntProperty("ironGearItem.id", 2, DefaultProps.IRON_GEAR_ID);
            Property property3 = mainConfiguration.getOrCreateIntProperty("goldenGearItem.id", 2, DefaultProps.GOLDEN_GEAR_ID);
            Property property4 = mainConfiguration.getOrCreateIntProperty("diamondGearItem.id", 2, DefaultProps.DIAMOND_GEAR_ID);
            mainConfiguration.save();
            gearsInitialized = true;
            CraftingManager craftingmanager = CraftingManager.getInstance();
            // woodenGearItem = (new BuildCraftItem(Integer.parseInt(property.value))).setIconIndex(16).setItemName("woodenGearItem");
            // craftingmanager.addRecipe(new ItemStack(woodenGearItem), new Object[] {
            //     " S ", "S S", " S ", Character.valueOf('S'), Item.stick
            // });
            // Replace buildcraft gear with BTW gear.
            woodenGearItem = mod_FCBetterThanWolves.fcGear;
            CoreProxy.addName(woodenGearItem, "Wooden Gear");
            stoneGearItem = (new BuildCraftItem(Integer.parseInt(property1.value))).setIconIndex(17).setItemName("stoneGearItem");
            craftingmanager.addRecipe(new ItemStack(stoneGearItem), new Object[] {
                " I ", "IGI", " I ", Character.valueOf('I'), Block.cobblestone, Character.valueOf('G'), woodenGearItem
            });
            CoreProxy.addName(stoneGearItem, "Stone Gear");
            // Replace iron gear crafting recipie with steel.
            ironGearItem = (new BuildCraftItem(Integer.parseInt(property2.value))).setIconIndex(18).setItemName("ironGearItem");
            craftingmanager.addRecipe(new ItemStack(ironGearItem), new Object[] {
                " I ", "IGI", " I ", Character.valueOf('I'), mod_FCBetterThanWolves.fcSteel, Character.valueOf('G'), stoneGearItem
            });
            CoreProxy.addName(ironGearItem, "Steel Gear");
            goldGearItem = (new BuildCraftItem(Integer.parseInt(property3.value))).setIconIndex(19).setItemName("goldGearItem");
            craftingmanager.addRecipe(new ItemStack(goldGearItem), new Object[] {
                " I ", "IGI", " I ", Character.valueOf('I'), Item.ingotGold, Character.valueOf('G'), ironGearItem
            });
            CoreProxy.addName(goldGearItem, "Gold Gear");
            diamondGearItem = (new BuildCraftItem(Integer.parseInt(property4.value))).setIconIndex(20).setItemName("diamondGearItem");
            craftingmanager.addRecipe(new ItemStack(diamondGearItem), new Object[] {
                " I ", "IGI", " I ", Character.valueOf('I'), Item.diamond, Character.valueOf('G'), goldGearItem
            });
            CoreProxy.addName(diamondGearItem, "Diamond Gear");
            mainConfiguration.save();
            return;
        }
    }

    public static void initializeModel(BaseMod basemod)
    {
        blockByEntityModel = ModLoader.getUniqueBlockModelID(basemod, true);
        pipeModel = ModLoader.getUniqueBlockModelID(basemod, true);
        markerModel = ModLoader.getUniqueBlockModelID(basemod, false);
        oilModel = ModLoader.getUniqueBlockModelID(basemod, false);
    }

    public static BuildCraftConfiguration mainConfiguration;
    public static TreeMap bufferedDescriptions = new TreeMap();
    public static final int trackedPassiveEntityId = 156;
    public static boolean continuousCurrentModel;
    private static boolean initialized = false;
    private static boolean gearsInitialized = false;
    public static Item woodenGearItem;
    public static Item stoneGearItem;
    public static Item ironGearItem;
    public static Item goldGearItem;
    public static Item diamondGearItem;
    public static Item wrenchItem;
    public static int redLaserTexture;
    public static int blueLaserTexture;
    public static int stripesLaserTexture;
    public static int transparentTexture;
    public static int blockByEntityModel;
    public static int pipeModel;
    public static int markerModel;
    public static int oilModel;
    public static String customBuildCraftTexture = "/net/minecraft/src/buildcraft/core/gui/block_textures.png";
    public static String customBuildCraftSprites = "/net/minecraft/src/buildcraft/core/gui/item_textures.png";
    public static PowerFramework powerFramework;
    public static final int OIL_BUCKET_QUANTITY = 1000;

}
