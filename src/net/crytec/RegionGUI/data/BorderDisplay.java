package net.crytec.RegionGUI.data;
/*
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import net.crytec.RegionGUI.RegionGUI;
import net.minecraft.server.v1_16_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R1.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_16_R1.WorldBorder;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import ru.komiss77.Managers.Cuboid;



public class BorderDisplay implements Runnable {
    
    
    //private final WrapperPlayServerWorldBorder resetcenterPacket;
    //private final WrapperPlayServerWorldBorder resetsizePacket;
    //private final WrapperPlayServerWorldBorder centerPacket;
    //private final WrapperPlayServerWorldBorder sizePacket;
    private final Player player;
    //private final int size;
    private final BukkitTask task;
    
    private final PacketPlayOutWorldBorder ppowbOld;
    private final PacketPlayOutWorldBorder ppowbNew;
    
    public BorderDisplay(final Player player, final Location minPoint, final Location maxPoint, final boolean tpToCenter) {
        this.player = player;
        
        final Cuboid cuboid = new Cuboid(minPoint, maxPoint);
        final Location center = cuboid.getCenter(player.getLocation());
        
        if (tpToCenter && !cuboid.contains(player.getLocation())) player.teleport(center);
        
        final int radius = Math.abs(cuboid.getHightesX() - cuboid.getLowerX());//вычислание размера привата по заготовке, т.к. квадрат, считаем по Х     //claim.getTemplate().getSize();
        //final Location center = WGutils.Get_region_center(player, region);
//System.out.println("center="+center+" radius="+radius);        
        
        final WorldBorder oldWb = ((CraftPlayer) player).getHandle().getWorld().getWorldBorder();
        ppowbOld = new PacketPlayOutWorldBorder(oldWb, EnumWorldBorderAction.INITIALIZE);   

        final WorldBorder newWb = new WorldBorder();
        newWb.world = ((CraftWorld) player.getWorld()).getHandle();
        newWb.setSize(radius);
        newWb.setDamageAmount(0);
        newWb.setCenter(center.getBlockX(),center.getBlockZ());
        ppowbNew = new PacketPlayOutWorldBorder(newWb, EnumWorldBorderAction.INITIALIZE);
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppowbNew);    
        
        /*
        //reset prepare
        this.resetcenterPacket = new WrapperPlayServerWorldBorder();
        (this.resetsizePacket = new WrapperPlayServerWorldBorder()).setRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setOldRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setSpeed(0L);
        this.resetsizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        
        this.resetcenterPacket.setCenterX(world.getWorldBorder().getCenter().getBlockX());
        this.resetcenterPacket.setCenterZ(world.getWorldBorder().getCenter().getBlockZ());
        this.resetcenterPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
        
        
        //set
        this.centerPacket = new WrapperPlayServerWorldBorder();
        (this.sizePacket = new WrapperPlayServerWorldBorder()).setRadius(this.size);
        this.sizePacket.setOldRadius(this.size);
        this.sizePacket.setSpeed(0L);
        this.sizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        
        this.centerPacket.setCenterX(center.getX());
        this.centerPacket.setCenterZ(center.getZ());
        this.centerPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
        this.centerPacket.sendPacket(this.player);
        Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> this.sizePacket.sendPacket(this.player), 1L);
        this.task = Bukkit.getScheduler().runTaskTimer(RegionGUI.getInstance(), this, 5L, 20L);
        
        task = Bukkit.getScheduler().runTaskTimer(RegionGUI.getInstance(), this, 5L, 20L);
    }
    
    public void reset() {
        //resetcenterPacket.sendPacket(this.player);
        //Bukkit.getScheduler().runTaskLater((Plugin)RegionGUI.getInstance(), () -> resetsizePacket.sendPacket(this.player), 1L);
        if (player!=null && player.isOnline()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket( ppowbOld );
        }
    }
    
    @Override
    public void run() {
        if ( !player.isOnline() || player.isSneaking() ) {
            task.cancel();
            reset();
            player.resetTitle();
            return;
        }
        player.sendTitle("", "§7Шифт - остановить показ", 0, 30, 0);
    }
}
*/