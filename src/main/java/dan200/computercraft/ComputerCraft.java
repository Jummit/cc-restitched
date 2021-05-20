/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2021. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft;

import static dan200.computercraft.shared.ComputerCraftRegistry.ModBlocks;
import static dan200.computercraft.shared.ComputerCraftRegistry.init;

import dan200.computercraft.core.asm.GenericSource;
import dan200.computercraft.shared.common.ColourableRecipe;
import dan200.computercraft.shared.computer.core.ClientComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.computer.recipe.ComputerUpgradeRecipe;
import dan200.computercraft.shared.data.BlockNamedEntityLootCondition;
import dan200.computercraft.shared.data.HasComputerIdLootCondition;
import dan200.computercraft.shared.data.PlayerCreativeLootCondition;
import dan200.computercraft.shared.media.recipes.DiskRecipe;
import dan200.computercraft.shared.media.recipes.PrintoutRecipe;
import dan200.computercraft.shared.pocket.recipes.PocketComputerUpgradeRecipe;
import dan200.computercraft.shared.proxy.ComputerCraftProxyCommon;
import dan200.computercraft.shared.turtle.recipes.TurtleRecipe;
import dan200.computercraft.shared.turtle.recipes.TurtleUpgradeRecipe;
import dan200.computercraft.shared.util.ImpostorRecipe;
import dan200.computercraft.shared.util.ImpostorShapelessRecipe;
import dan200.computercraft.shared.util.ServiceUtil;
import dan200.computercraft.shared.util.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

public final class ComputerCraft implements ModInitializer {
    public static final String MOD_ID = "computercraft";
    public static final int terminalWidth_computer = 51;
    public static final int terminalHeight_computer = 19;
    public static final int terminalWidth_turtle = 39;
    public static final int terminalHeight_turtle = 13;
    public static final int terminalWidth_pocketComputer = 26;
    public static final int terminalHeight_pocketComputer = 20;
    // Registries
    public static final ClientComputerRegistry clientComputerRegistry = new ClientComputerRegistry();
    public static final ServerComputerRegistry serverComputerRegistry = new ServerComputerRegistry();
    // Logging
    public static final Logger log = LogManager.getLogger(MOD_ID);
    public static ItemGroup MAIN_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "main"), () -> new ItemStack(ModBlocks.COMPUTER_NORMAL));
    public static ModConfig config = null;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        ComputerCraftProxyCommon.init();
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "colour"), ColourableRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "computer_upgrade"), ComputerUpgradeRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER,
                          new Identifier(ComputerCraft.MOD_ID, "pocket_computer_upgrade"),
                          PocketComputerUpgradeRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "disk"), DiskRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "printout"), PrintoutRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "turtle"), TurtleRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "turtle_upgrade"), TurtleUpgradeRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "impostor_shaped"), ImpostorRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ComputerCraft.MOD_ID, "impostor_shapeless"), ImpostorShapelessRecipe.SERIALIZER);
        Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(ComputerCraft.MOD_ID, "block_named"), BlockNamedEntityLootCondition.TYPE);
        Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(ComputerCraft.MOD_ID, "player_creative"), PlayerCreativeLootCondition.TYPE);
        Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(ComputerCraft.MOD_ID, "has_id"), HasComputerIdLootCondition.TYPE);
        init();
        GenericSource.setup( () -> ServiceUtil.loadServices( GenericSource.class ));
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "classic"), modContainer, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "overhaul"), modContainer, ResourcePackActivationType.NORMAL);
        });
    }

    public static ModConfig getConfig() {
        if (config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }
        return config;
    }

}
