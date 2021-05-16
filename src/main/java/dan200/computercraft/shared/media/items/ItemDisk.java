/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2021. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.shared.media.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.shared.ComputerCraftRegistry;
import dan200.computercraft.shared.MediaProviders;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.peripheral.diskdrive.BlockDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.DiskDriveState;
import dan200.computercraft.shared.util.Colour;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDisk extends Item implements IMedia, IColouredItem {
    private static final String NBT_ID = "DiskId";

    public ItemDisk(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<Text> list, TooltipContext options) {
        if (options.isAdvanced()) {
            int id = getDiskID(stack);
            if (id >= 0) {
                list.add(new TranslatableText("gui.computercraft.tooltip.disk_id", id).formatted(Formatting.GRAY));
            }
        }
    }

    // This logic is in `TileDiskDrive` in the forge version, but because shift
    // right click doesn't fire the `TileDiskDrive.onActivate` callback it is
    // implemented like Minecraft's Jukebox.
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (context.getPlayer().isInSneakingPose() && blockState.isOf(ComputerCraftRegistry.ModBlocks.DISK_DRIVE) && blockState.get(BlockDiskDrive.STATE) == DiskDriveState.EMPTY) {
            // Try to put a disk into the drive
            ItemStack itemStack = context.getPlayer().getStackInHand(context.getHand());
            if (!world.isClient) {
                ComputerCraftRegistry.ModBlocks.DISK_DRIVE.setDisk(world, blockPos, blockState, itemStack);
                itemStack.decrement(1);
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public void appendStacks(@Nonnull ItemGroup tabs, @Nonnull DefaultedList<ItemStack> list) {
        if (!this.isIn(tabs)) {
            return;
        }
        for (int colour = 0; colour < 16; colour++) {
            list.add(createFromIDAndColour(-1, null, Colour.VALUES[colour].getHex()));
        }
    }

    @Nonnull
    public static ItemStack createFromIDAndColour(int id, String label, int colour) {
        ItemStack stack = new ItemStack(ComputerCraftRegistry.ModItems.DISK);
        setDiskID(stack, id);
        ComputerCraftRegistry.ModItems.DISK.setLabel(stack, label);
        IColouredItem.setColourBasic(stack, colour);
        return stack;
    }

    private static void setDiskID(@Nonnull ItemStack stack, int id) {
        if (id >= 0) {
            stack.getOrCreateTag()
                 .putInt(NBT_ID, id);
        }
    }

    public static int getDiskID(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains(NBT_ID) ? nbt.getInt(NBT_ID) : -1;
    }

    @Override
    public String getLabel(@Nonnull ItemStack stack) {
        return stack.hasCustomName() ? stack.getName()
                                            .getString() : null;
    }

    @Override
    public boolean setLabel(@Nonnull ItemStack stack, String label) {
        if (label != null) {
            stack.setCustomName(new LiteralText(label));
        } else {
            stack.removeCustomName();
        }
        return true;
    }

    @Override
    public IMount createDataMount(@Nonnull ItemStack stack, @Nonnull World world) {
        int diskID = getDiskID(stack);
        if (diskID < 0) {
            diskID = ComputerCraftAPI.createUniqueNumberedSaveDir(world, "disk");
            setDiskID(stack, diskID);
        }
        return ComputerCraftAPI.createSaveDirMount(world, "disk/" + diskID, ComputerCraft.floppySpaceLimit);
    }

    @Override
    public int getColour(@Nonnull ItemStack stack) {
        int colour = IColouredItem.getColourBasic(stack);
        return colour == -1 ? Colour.WHITE.getHex() : colour;
    }
}
