package net.crytec.RegionGUI.manager;


import java.util.HashMap;
import net.crytec.RegionGUI.data.PreviewBlock;
import net.crytec.RegionGUI.data.Template;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;






public class PreviewBlockManager implements Listener {

    private static HashMap<String,PreviewBlock> on_wiev;

    
    
    public PreviewBlockManager() {
        on_wiev = new HashMap<>();
    }

    
    
    
        
    public static void startPreview(final Player player, final Template template) {
        stopPrewiev(player);
        on_wiev.put( player.getName(), new PreviewBlock(player, template) );
    }

    public static void stopPrewiev(final Player player) {
        if (on_wiev.containsKey(player.getName())) {
            on_wiev.get(player.getName()).stopPrewiev(false);
            on_wiev.remove(player.getName());
        }
    }

  //  public static boolean Is_wiev_mode(Player player, String region_type) {
        //return (on_wiev.containsKey(player.getName()) && on_wiev.get(player.getName()).region_type.equals(region_type));
  //      return (on_wiev.containsKey(player.getName()));
   // }
    









    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent e) {
        stopPrewiev(e.getEntity());
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent e) {
        stopPrewiev(e.getPlayer());
    }

    
    
    
    /*
    public static void sendOutline(final Player player, final List<Vector> list) {
 
            final RegionManager regionmanager = RegionUtils.getRegionManager(player.getWorld());
            final LocalPlayer localplayer = WorldGuardPlugin.inst().wrapPlayer(player);
            
            //BukkitAdapter.adapt(player.getLocation());
            
            RegionGUI.getInstance().getServer().getScheduler().runTask(RegionGUI.getInstance(), () -> {
                
                list.stream().map( (vector) -> player.getWorld().getHighestBlockAt(vector.getBlockX(), vector.getBlockZ()).getRelative(BlockFace.UP)).forEach((block) -> {
                        if (regionmanager.getApplicableRegions(block.getLocation()).size() == 0) {
                            player.sendBlockChange(block.getLocation(), Material.EMERALD_BLOCK, (byte) 0);
                        } else if (regionmanager.getApplicableRegions(block.getLocation()).isOwnerOfAll(localplayer)) {
                            player.sendBlockChange(block.getLocation(), Material.GOLD_BLOCK, (byte) 0);
                        } else {
                            player.sendBlockChange(block.getLocation(), Material.REDSTONE_BLOCK, (byte) 0);
                        }
                }); 
                
            });     
    }*/

    
    
    /*
    public static void removeOutline(final Player player, final List<Vector> list) {
        RegionGUI.getInstance().getServer().getScheduler().runTask(RegionGUI.getInstance(), () -> {    
            list.stream().forEach((vector) -> {
                Block block = player.getWorld().getHighestBlockAt(vector.getBlockX(), vector.getBlockZ()).getRelative(BlockFace.UP);
                player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
            }); 
         });     
    }*/

    /*
    
    
    public static List<Vector> getLocationsFromRegion(CuboidRegion cuboidregion) {
        BlockVector blockvector = cuboidregion.getMinimumPoint().toBlockVector();
        BlockVector blockvector1 = cuboidregion.getMaximumPoint().toBlockVector();
        ArrayList<Vector> arraylist = new ArrayList();
        ArrayList<Vector> arraylist1 = new ArrayList();

        arraylist1.add(new Vector(blockvector.getX(), blockvector.getY(), blockvector.getZ()));
        arraylist1.add(new Vector(blockvector1.getX(), blockvector.getY(), blockvector.getZ()));
        arraylist1.add(new Vector(blockvector1.getX(), blockvector.getY(), blockvector1.getZ()));
        arraylist1.add(new Vector(blockvector.getX(), blockvector.getY(), blockvector1.getZ()));

        for (int i = 0; i < arraylist1.size(); ++i) {
            Vector vector = arraylist1.get(i);
            Vector vector1;

            if (i + 1 < arraylist1.size()) {
                vector1 =  arraylist1.get(i + 1);
            } else {
                vector1 =  arraylist1.get(0);
            }

            arraylist.addAll(Border_line(vector, vector1));
        }

        return arraylist;
    }

    
    
    
    public static List<Vector> Border_line(Vector vector, Vector vector1) {
        ArrayList<Vector> arraylist = new ArrayList();
        int i = (int) (vector.distance(vector1) / 1.0D) + 1;
        double d0 = vector.distance(vector1);
        double d1 = d0 / (double) (i - 1);
        Vector vector2 = vector1.subtract(vector).normalize().multiply(d1);

        for (int j = 0; j < i; ++j) {
            Vector vector3 = vector.add(vector2.multiply(j));
            arraylist.add(vector3);
        }

        return arraylist;
    }
    */
    
    
    
    
}
