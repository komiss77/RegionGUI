package net.crytec.RegionGUI.menus;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.File;
import java.io.IOException;
import net.crytec.RegionGUI.Language;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.RegionUtils;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.TemplateManager;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.phoenix.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.komiss77.ApiOstrov;



public class RegionDeleteConfirm implements InventoryProvider
{
    private static final ItemStack fill;
    private final ProtectedRegion region;
    
    static {
        fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    }
    
    public RegionDeleteConfirm(final ProtectedRegion region) {
        this.region = region;
    }
    
    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        inventoryContents.fill(ClickableItem.empty(fill));
        //if (!this.claim.getProtectedRegion().isPresent()) {
        //    System.out.println("Claim not present");
        //    player.closeInventory();
        //    return;
        //}
       // ProtectedRegion protectedRegion = this.claim.getProtectedRegion().get();
        
        inventoryContents.set( 0, 2, ClickableItem.of( new ItemBuilder(Material.REDSTONE).name(Language.INTERFACE_DELETE_ABORT_BUTTON.toString()).build(), 
                inventoryClickEvent -> MenuOpener.openMain(player, region) ));
       
        inventoryContents.set( 0, 6, ClickableItem.of( new ItemBuilder(Material.EMERALD).name(Language.INTERFACE_DELETE_CONFIRM_BUTTON.toString()).build(),
                inventoryClickEvent -> {
            player.closeInventory();

            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);

            RegionUtils.getRegionManager(player.getWorld()).removeRegion(this.region.getId());
            RegionUtils.saveRegions(player.getWorld());
            
            
            
            
            
            
            
            
            
            
            
            
            if (RegionGUI.getInstance().getConfig().getBoolean("regenOnDelete", true)) {
            
                    final World faweWorld = FaweAPI.getWorld(player.getWorld().getName());

                     
                try{
         
                    final File file = new File("plugins/RegionGUI/schematics/land/"+region.getId().toLowerCase()+".schem");
                    
                    if (file.exists()) {
                        final ClipboardFormat cf = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

                        final boolean allowUndo = false;
                        final boolean withAir = true;

                        cf.load(file).paste(faweWorld, region.getMinimumPoint(), allowUndo, withAir, (Transform) null); ;

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if ( !file.delete() ) {
                                    RegionGUI.log_err("файл не удалился");
                                }
                            }
                        }.runTaskLaterAsynchronously(RegionGUI.getInstance(), 100);
                        
                    } else {
                    RegionGUI.log_err("Вставка соханённой копии региона "+region.getId()+" : файл не найден!1");
                }
                    
                } catch (IOException ex) {
                    RegionGUI.log_err("Вставка соханённой копии региона "+region.getId()+" : "+ex.getMessage());
                    //ex.printStackTrace();
                }
                
            }
            
            
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