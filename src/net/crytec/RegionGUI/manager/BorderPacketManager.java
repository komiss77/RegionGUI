package net.crytec.RegionGUI.manager;

import com.comphenix.protocol.wrappers.EnumWrappers;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.utils.packets.WrapperPlayServerWorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;



public class BorderPacketManager
{
    private final RegionGUI plugin;
    private final Location center;
    private final int size;
    private final WrapperPlayServerWorldBorder centerPacket;
    private final WrapperPlayServerWorldBorder sizePacket;
    private final WrapperPlayServerWorldBorder resetcenterPacket;
    private final WrapperPlayServerWorldBorder resetsizePacket;
    
    public BorderPacketManager(final RegionGUI plugin, final Location center, final Template claim) {
        this.plugin = plugin;
        this.center = center.clone().add(0.5D, 0.0D, 0.5D);;
        this.size = claim.getSize() + 1;
        this.centerPacket = new WrapperPlayServerWorldBorder();
        this.sizePacket = new WrapperPlayServerWorldBorder();
        this.resetcenterPacket = new WrapperPlayServerWorldBorder();
        this.resetsizePacket = new WrapperPlayServerWorldBorder();
        this.buildPackets();
    }
    
    private void buildPackets() {
        final World world = this.center.getWorld();
        this.resetsizePacket.setRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setOldRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setSpeed(0L);
        this.resetsizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        this.resetcenterPacket.setCenterX(world.getWorldBorder().getCenter().getBlockX());
        this.resetcenterPacket.setCenterZ(world.getWorldBorder().getCenter().getBlockZ());
        this.resetcenterPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
        this.sizePacket.setRadius(this.size);
        this.sizePacket.setOldRadius(this.size);
        this.sizePacket.setSpeed(0L);
        this.sizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        this.centerPacket.setCenterX(this.center.getX());
        this.centerPacket.setCenterZ(this.center.getZ());
        this.centerPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
    }
    
    public void sendReset(final Player player) {
        this.resetcenterPacket.sendPacket(player);
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> this.resetsizePacket.sendPacket(player), 1L);
    }
    
    public void send(final Player player) {
        this.centerPacket.sendPacket(player);
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> this.sizePacket.sendPacket(player), 1L);
    }
}
