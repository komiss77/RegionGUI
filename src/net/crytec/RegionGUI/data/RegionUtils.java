package net.crytec.RegionGUI.data;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.komiss77.ApiOstrov;


public class RegionUtils
{
    private static WorldGuardPlatform platform;
    
    static {
        RegionUtils.platform = WorldGuard.getInstance().getPlatform();
    }
    
    //public static boolean validateMaterial(final String material) {
   //     return EnumUtils.isValidEnum((Class)Material.class, material);
   // }
    
    public static boolean saveRegions(final World world) {
        try {
            RegionUtils.platform.getRegionContainer().get(BukkitAdapter.adapt(world)).saveChanges();
            return true;
        }
        catch (StorageException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static RegionManager getRegionManager(final World world) {
        return RegionUtils.platform.getRegionContainer().get(BukkitAdapter.adapt(world));
    }
    
    public static List<Vector> getLocationsFromRegion(final ProtectedRegion region) {
        final BlockVector3 minimumPoint = region.getMinimumPoint();
        final BlockVector3 maximumPoint = region.getMaximumPoint();
        final int n = region.getMaximumPoint().getY() - region.getMinimumPoint().getY();
        final ArrayList<Vector> list = new ArrayList<>();
        final ArrayList<Vector> list2 = new ArrayList<>();
        list2.add(new Vector(minimumPoint.getX(), minimumPoint.getY(), minimumPoint.getZ()));
        list2.add(new Vector(maximumPoint.getX(), minimumPoint.getY(), minimumPoint.getZ()));
        list2.add(new Vector(maximumPoint.getX(), minimumPoint.getY(), maximumPoint.getZ()));
        list2.add(new Vector(minimumPoint.getX(), minimumPoint.getY(), maximumPoint.getZ()));
        for (int i = 0; i < list2.size(); ++i) {
            final Vector vector = list2.get(i);
            Vector p2;
            if (i + 1 < list2.size()) {
                p2 = list2.get(i + 1);
            }
            else {
                p2 = list2.get(0);
            }
            final Vector add = vector.add(new Vector(0, n, 0));
            final Vector add2 = p2.add(new Vector(0, n, 0));
            list.addAll(regionLine(vector, p2));
            list.addAll(regionLine(add, add2));
            list.addAll(regionLine(vector, add));
            for (double n2 = 2.0; n2 < n; n2 += 2.0) {
                list.addAll(regionLine(vector.add(new Vector(0.0, n2, 0.0)), p2.add(new Vector(0.0, n2, 0.0))));
            }
        }
        return (List<Vector>)list;
    }
    
    public static List<Vector> regionLine(final Vector p1, final Vector p2) {
        final ArrayList<Vector> list = new ArrayList<>();
        final int n = (int)(p1.distance(p2) / 1.0) + 1;
        final Vector multiply = p2.subtract(p1).normalize().multiply(p1.distance(p2) / (n - 1));
        for (int i = 0; i < n; ++i) {
            list.add(p1.add(multiply.multiply(i)));
        }
        return list;
    }
    
    
    
    
    
    public static List <ProtectedRegion> getPlayerOwnedRegions (final Player bukkitplayer) {
        final List <ProtectedRegion> playerRegions = new ArrayList<>();
        for (final World world : Bukkit.getWorlds()) {
            playerRegions.addAll(getPlayerOwnedRegions(bukkitplayer, world));
        }
        return playerRegions;
    }

    public static List <ProtectedRegion> getPlayerOwnedRegions (final Player bukkitplayer, final World world) {
        final LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(bukkitplayer);
        final List <ProtectedRegion> playerRegions = new ArrayList<>();
        for (final ProtectedRegion rg : getRegionManager(world).getRegions().values()) {
            if (rg.isOwner(lp) && rg.getId().startsWith(lp.getName()+"-rgui-")) {
                playerRegions.add(rg);
            }
        }
        return playerRegions;
    }
    
    public static List <ProtectedRegion> getPlayerUserRegions (final Player bukkitplayer) {
        final List <ProtectedRegion> playerRegions = new ArrayList<>();
        for (final World world : Bukkit.getWorlds()) {
            playerRegions.addAll(getPlayerUserRegions(bukkitplayer, world));
        }
        return playerRegions;
    }
    
    public static List <ProtectedRegion> getPlayerUserRegions (final Player bukkitplayer, final World world) {
        final LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(bukkitplayer);
        final List <ProtectedRegion> playerRegions = new ArrayList<>();
        for (final ProtectedRegion rg : getRegionManager(world).getRegions().values()) {
            if (rg.isMember(lp) && rg.getId().startsWith(lp.getName()+"-rgui-")) {
                playerRegions.add(rg);
            }
        }
        return playerRegions;
    }
    
    public static String getTemplateName (final ProtectedRegion region) {
        if (isValidRegionId(region.getId()))  {
 //System.out.println("getTemplateName ="+region.getId().split("-")[2]);
           return region.getId().split("-")[2];
        } else {
            return "";
        }
    }

    public static String getCreateTime (final ProtectedRegion region) {
        if (isValidRegionId(region.getId()))  {
            return ApiOstrov.dateFromStamp( Long.valueOf(region.getId().split("-")[3]));
        } else {
            return "";
        }
    }    
    
    // komiss77-rgui-template-timestamp
    public static boolean isValidRegionId(final String regionId) {
//System.out.println("isValidRegionId 1");
        if (regionId.contains("-")) {
            String[] split = regionId.split("-");
//System.out.println("isValidRegionId "+split[0]+" "+split[1]+" "+split[2]+" "+split[3]+ "integer?"+ApiOstrov.isInteger(split[3]));
            if ( split.length==4 && split[1].equals("rgui") && ApiOstrov.isInteger(split[3])) {
                return true;
            }
        } 
        return false;
    }
    
    
    
    
    
    
    
    
    
}
