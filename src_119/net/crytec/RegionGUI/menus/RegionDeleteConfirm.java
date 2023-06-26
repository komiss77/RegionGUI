package net.crytec.RegionGUI.menus;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.RegionGUI.Language;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.RegionUtils;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.TemplateManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.ApiOstrov;
import ru.komiss77.Ostrov;
import ru.komiss77.modules.world.Schematic;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;



public class RegionDeleteConfirm implements InventoryProvider {
    
    private static final ItemStack fill;
    private final ProtectedRegion region;
    
    static {
        fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    }
    
    public RegionDeleteConfirm(final ProtectedRegion region) {
        this.region = region;
    }
    
    @Override
    public void init(Player player, InventoryContent inventoryContents) {
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        inventoryContents.fill(ClickableItem.empty(fill));
        //if (!this.claim.getProtectedRegion().isPresent()) {
        //    System.out.println("Claim not present");
        //    player.closeInventory();
        //    return;
        //}
       // ProtectedRegion protectedRegion = this.claim.getProtectedRegion().get();
        
        inventoryContents.set( 0, 2, ClickableItem.of( new ItemBuilder(Material.REDSTONE).name(Language.INTERFACE_DELETE_ABORT_BUTTON.toString()).build(), 
                e -> MenuOpener.openMain(player, region) ));
       
        inventoryContents.set( 0, 6, ClickableItem.of( new ItemBuilder(Material.EMERALD).name(Language.INTERFACE_DELETE_CONFIRM_BUTTON.toString()).build(),
                e -> {
            player.closeInventory();

            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);

            //RegionUtils.getRegionManager(player.getWorld()).removeRegion(this.region.getId());
            //RegionUtils.saveRegions(player.getWorld());

            if (RegionGUI.getInstance().getConfig().getBoolean("regenOnDelete", true)) {
                
                final Schematic sch = ApiOstrov.getWorldEditor().getSchematic(player, region.getId(), true);
                final Location pos1 = BukkitAdapter.adapt(player.getWorld(), region.getMinimumPoint());
//Ostrov.log("--paste pos1="+pos1);
                ApiOstrov.getWorldEditor().paste (player, pos1, sch, true);
               /* ApiOstrov.getWorldEditor().paste(
                    player,
                    BukkitAdapter.adapt( player.getWorld(), region.getMinimumPoint() ),
                    RegionGUI.getInstance().getDataFolder() + "/schematics",
                    region.getId(),
                    true,
                    true,
                    () -> {
                        //RegionUtils.getRegionManager(player.getWorld()).removeRegion(this.region.getId().toLowerCase());
                        //RegionUtils.saveRegions(player.getWorld());
                    },
                    WE.scipOnPasteDefault
                );*/
            

                
            }// else {
                
                RegionUtils.getRegionManager(player.getWorld()).removeRegion(this.region.getId());
                RegionUtils.saveRegions(player.getWorld());
                
          //  }
            
            
            //возврат денег
            final Template template = TemplateManager.getByName( RegionUtils.getTemplateName(region) );
            if (template!=null && template.getRefund()>0) {
                ApiOstrov.moneyChange(player, template.getRefund(), "Возврат денег за регион");
                
            }
            player.sendMessage(Language.REGION_REMOVED.toChatString());
            
        }));
        
        
    }
    

    
}




/*
рабочая версия вставки
            
                try{
                    
                    final World faweWorld = FaweAPI.getWorld(player.getWorld().getName());
                    //Region toRegen = new CuboidRegion(region.getMinimumPoint(), region.getMaximumPoint());

                    final File file = new File("plugins/RegionGUI/Schematics/"+region.getId().toLowerCase()+".schem");
                    
                    if (file.exists()) {
                        final ClipboardFormat cf = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

                        boolean allowUndo = false;
                        boolean noAir = false;

                        cf.load(file).paste(faweWorld, region.getMinimumPoint(), allowUndo, noAir, (Transform) null); ;

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if ( !file.delete() ) {
                                    RegionGUI.log_err("файл не удалился");
                                }
                            }
                        }.runTaskLaterAsynchronously(RegionGUI.getInstance(), 100);
                        
                        //file.delete();
                    } else {
                    RegionGUI.log_err("Вставка соханённой копии региона "+region.getId()+" : файл не найден!1");
                }
                    
                } catch (IOException ex) {
                    RegionGUI.log_err("Вставка соханённой копии региона "+region.getId()+" : "+ex.getMessage());
                    //ex.printStackTrace();
                }
*/