package com.github.nekomeowww.customdrones.handlers;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.event.entity.*;
import com.github.nekomeowww.customdrones.AchievementPageDrone;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.block.BlockCrafter;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.drone.module.ModuleDe;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneBit;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;
import com.github.nekomeowww.customdrones.item.ItemDronePainter;
import com.github.nekomeowww.customdrones.item.ItemDronePart;
import com.github.nekomeowww.customdrones.item.ItemDroneScrew;
import com.github.nekomeowww.customdrones.item.ItemDroneSpawn;
import com.github.nekomeowww.customdrones.item.ItemPlasmaGun;

public class EventHandler
{
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void explosion(ExplosionEvent evn)
    {
        Explosion ex = evn.getExplosion();
        World world = evn.getWorld();
        BlockPos bp = new BlockPos(ex.getPosition());
        AxisAlignedBB aabb = new AxisAlignedBB(bp).expandXyz(ModuleDe.getMaxPossibleRange());
        List<EntityDrone> drones = world.getEntitiesWithinAABB(EntityDrone.class, aabb);
        for (EntityDrone d : drones) {
            if (d.hasEnabled(Module.deexplosion))
            {
                double range = ((ModuleDe)Module.deexplosion).getRange(d);
                if (d.getEntityBoundingBox().expandXyz(range).isVecInside(ex.getPosition()))
                {
                    evn.setCanceled(true);
                    d.droneInfo.reduceBattery(20.0D * d.droneInfo.getBatteryConsumptionRate(d));
                    world.playSound(null, bp, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 2.5F, 1.0F);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void itemPickup(PlayerEvent.ItemPickupEvent evn)
    {
        itemReceived(evn.player, evn.pickedUp.getEntityItem());
    }

    @SubscribeEvent
    public void itemCraft(PlayerEvent.ItemCraftedEvent evn)
    {
        itemReceived(evn.player, evn.crafting);
    }

    @SubscribeEvent
    public void itemSmelt(PlayerEvent.ItemSmeltedEvent evn)
    {
        itemReceived(evn.player, evn.smelting);
    }

    public void itemReceived(EntityPlayer player, ItemStack is)
    {
        Item i = is.getItem();
        if ((i instanceof ItemDroneBit))
        {
            player.addStat(AchievementPageDrone.droneBit);
        }
        else if ((i instanceof ItemDronePart))
        {
            player.addStat(AchievementPageDrone.dronePart);
        }
        else if ((i instanceof ItemDroneSpawn))
        {
            player.addStat(AchievementPageDrone.droneSpawn);
            DroneInfo di = CustomDrones.droneSpawn.getDroneInfo(is);
            if ((di.casing == 4) && (di.chip == 4) && (di.core == 4) && (di.engine == 4)) {
                player.addStat(AchievementPageDrone.droneSpawnBest);
            }
        }
        else if ((i instanceof ItemDroneFlyer))
        {
            player.addStat(AchievementPageDrone.droneFlyer);
        }
        else if ((i instanceof ItemDronePainter))
        {
            player.addStat(AchievementPageDrone.dronePaint);
        }
        else if ((i instanceof ItemDroneScrew))
        {
            player.addStat(AchievementPageDrone.droneScrew);
        }
        else if ((i instanceof ItemPlasmaGun))
        {
            player.addStat(AchievementPageDrone.dronePlasmaGun);
        }
        else if ((i instanceof ItemBlock))
        {
            Block b = ((ItemBlock)i).getBlock();
            if ((b instanceof BlockCrafter)) {
                player.addStat(AchievementPageDrone.droneCrafter);
            }
        }
    }
}
