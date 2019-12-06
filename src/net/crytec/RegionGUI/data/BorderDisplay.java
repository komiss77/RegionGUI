package net.crytec.RegionGUI.data;

import net.crytec.RegionGUI.Language;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import net.crytec.RegionGUI.RegionGUI;
import org.bukkit.Bukkit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.Player;
import net.crytec.RegionGUI.utils.packets.WrapperPlayServerWorldBorder;



public class BorderDisplay implements Runnable
{
    private final WrapperPlayServerWorldBorder resetcenterPacket;
    private final WrapperPlayServerWorldBorder resetsizePacket;
    private final WrapperPlayServerWorldBorder centerPacket;
    private final WrapperPlayServerWorldBorder sizePacket;
    private final Player player;
    private final int size;
    private final BukkitTask task;
    
    public BorderDisplay(final Player player, final ProtectedRegion region) {
        this.player = player;
        final World world = player.getWorld();
        this.size = Math.abs(region.getMaximumPoint().getBlockX() - region.getMinimumPoint().getBlockX());//вычислание размера привата по заготовке, т.к. квадрат, считаем по Х     //claim.getTemplate().getSize();
        this.resetcenterPacket = new WrapperPlayServerWorldBorder();
        (this.resetsizePacket = new WrapperPlayServerWorldBorder()).setRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setOldRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setSpeed(0L);
        this.resetsizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        final CuboidRegion cuboidRegion = new CuboidRegion(region.getMinimumPoint(), region.getMaximumPoint());
        this.resetcenterPacket.setCenterX(world.getWorldBorder().getCenter().getBlockX());
        this.resetcenterPacket.setCenterZ(world.getWorldBorder().getCenter().getBlockZ());
        this.resetcenterPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
        this.centerPacket = new WrapperPlayServerWorldBorder();
        (this.sizePacket = new WrapperPlayServerWorldBorder()).setRadius(this.size);
        this.sizePacket.setOldRadius(this.size);
        this.sizePacket.setSpeed(0L);
        this.sizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        final Location add = BukkitAdapter.adapt(player.getWorld(), cuboidRegion.getCenter()).clone().add(0.5, 0.0, 0.5);
        this.centerPacket.setCenterX(add.getX());
        this.centerPacket.setCenterZ(add.getZ());
        this.centerPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
        this.centerPacket.sendPacket(this.player);
        Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> this.sizePacket.sendPacket(this.player), 1L);
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)RegionGUI.getInstance(), (Runnable)this, 5L, 20L);
    }
    
    public void reset() {
        this.resetcenterPacket.sendPacket(this.player);
        Bukkit.getScheduler().runTaskLater((Plugin)RegionGUI.getInstance(), () -> this.resetsizePacket.sendPacket(this.player), 1L);
    }
    
    @Override
    public void run() {
        if ( !player.isOnline() || player.isSneaking() ) {
            task.cancel();
            reset();
            player.resetTitle();
            return;
        }
        player.sendTitle("", Language.PREVIEW_CANCEL.toString(), 0, 30, 0);
    }
}
