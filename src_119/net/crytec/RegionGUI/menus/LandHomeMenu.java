package net.crytec.RegionGUI.menus;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.List;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.Pagination;
import ru.komiss77.utils.inventory.SlotIterator;
import ru.komiss77.utils.inventory.SlotPos;
import net.crytec.RegionGUI.Language;
import net.crytec.RegionGUI.data.RegionUtils;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.TemplateManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.ApiOstrov;
import ru.komiss77.modules.world.Cuboid;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.ItemUtils;



public class LandHomeMenu implements InventoryProvider
{
    private static final ItemStack fill;
    
    static {
        fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    }
    

    
    
    
    
    
    @Override
        public void init(Player player, InventoryContent inventoryContents) {
            
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        
        inventoryContents.fillBorders(ClickableItem.empty((ItemStack)fill));
        Pagination pagination = inventoryContents.pagination();
        ArrayList<ClickableItem> arrayList = new ArrayList<>();
        
        //final List <ProtectedRegion> playerRegions = new ArrayList<>();
        
        
        //boolean found = false;
        final List<ProtectedRegion> rgList = new ArrayList<>();//RegionUtils.getPlayerOwnedRegions(player, world);
        
        for (World world : Bukkit.getWorlds()) {
            //playerRegions.addAll(RegionUtils.getPlayerRegions(player, world));
            
            int number = 1;
            
            for (final ProtectedRegion region : RegionUtils.getPlayerOwnedRegions(player, world)) {
                rgList.add(region);
            
                final Template template = TemplateManager.getByName( RegionUtils.getTemplateName(region) );
                final String createTime = RegionUtils.getCreateTime(region);

                //ArrayList<String> regionButton = new ArrayList<>(Language.INTERFACE_HOME_ENTRYBUTTON_DESCRIPTION.getDescriptionArray());
                //regionButton.replaceAll(string -> string.replace("%world%", world.getName()));
                
                arrayList.add(ClickableItem.of(new ItemBuilder(Material.GRAY_BED)
                        .name("§7Регион §6"+number)
                        .lore("§fВы владелец.")
                        .lore("")
                        .lore("§7Тип региона: "+(template==null ? "не определён" : template.getDisplayname()))
                        .lore("§7Создан: §6"+(createTime.isEmpty()?"§8нет данных":createTime))
                        .lore ("§7Пользователей"+(region.getMembers().getPlayerDomain().size()==0 ? " нет" : ": "+region.getMembers().getPlayerDomain().size()))
                        .lore("")
                        .lore("§7Примерная локация региона:")
                        .lore("§6"+region.getMaximumPoint().getBlockX()+", "+region.getMaximumPoint().getBlockY()+", "+region.getMaximumPoint().getBlockZ())
                        .lore("")
                        .lore("ЛКМ - телепорт в регион")
                        .lore("")
                        //.lore(regionButton)
                        .build(), e -> {
                            
                    if (region.getFlag((Flag)Flags.TELE_LOC) != null) {
                        
                        com.sk89q.worldedit.util.Location location = (com.sk89q.worldedit.util.Location)region.getFlag((Flag)Flags.TELE_LOC);
                        org.bukkit.Location location2 = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)location);
                        player.teleport(location2);
                        
                    } else {
                        Location loc1 = BukkitAdapter.adapt(world, region.getMinimumPoint());
                        Location loc2 = BukkitAdapter.adapt(world, region.getMaximumPoint());
                        Cuboid cuboid = new Cuboid (loc1, loc2);
                        ApiOstrov.teleportSave(player, cuboid.getCenter(loc1));
                        //player.sendMessage(Language.ERROR_NO_HOME_SET.toChatString());
                        
                    }
                    
                    player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
                }));
                
                number++;
                //found = true;
            }

            
            
            for (final ProtectedRegion region : RegionUtils.getPlayerUserRegions(player, world)) {
                
                if (rgList.contains(region)) continue;
                rgList.add(region); //нужно ниже
            
                final Template template = TemplateManager.getByName( RegionUtils.getTemplateName(region) );
                final String createTime = RegionUtils.getCreateTime(region);

                //ArrayList<String> regionButton = new ArrayList<>(Language.INTERFACE_HOME_ENTRYBUTTON_DESCRIPTION.getDescriptionArray());
                //regionButton.replaceAll(string -> string.replace("%world%", world.getName()));
                
                arrayList.add(ClickableItem.of(new ItemBuilder(Material.GRAY_BED)
                        .name("§7Регион §6"+number)
                        .lore("§fВы пользователь.")
                        .lore("")
                        .lore("§7Тип региона: "+(template==null ? "не определён" : template.getDisplayname()))
                        .lore("§7Создан: §6"+(createTime.isEmpty()?"§8нет данных":createTime))
                        .lore ("§7Пользователей"+(region.getMembers().getPlayerDomain().size()==0 ? " нет" : ": "+region.getMembers().getPlayerDomain().size()))
                        .lore("")
                        .lore(region.getFlag((Flag)Flags.TELE_LOC) != null ? "ЛКМ - телепорт в регион" : "")
                        .lore("")
                        .lore("§7Примерная локация региона:")
                        .lore(region.getMaximumPoint().getBlockX()+","+region.getMaximumPoint().getBlockY()+","+region.getMaximumPoint().getBlockZ())
                        //.lore(regionButton)
                        .build(), e -> {
                            
                    if (region.getFlag((Flag)Flags.TELE_LOC) != null) {
                        
                        com.sk89q.worldedit.util.Location location = (com.sk89q.worldedit.util.Location)region.getFlag((Flag)Flags.TELE_LOC);
                        org.bukkit.Location location2 = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)location);
                        player.teleport(location2);
                        
                    } else {
                        
                        player.sendMessage(Language.ERROR_NO_HOME_SET.toChatString());
                        
                    }
                    
                    player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
                }));
                
                number++;
            }

            
        }
        
        if (rgList.isEmpty()) {
            inventoryContents.add( ClickableItem.empty( new ItemBuilder(Material.BARRIER).name("§4Нет регионов!").lore( ItemUtils.Gen_lore(null, "Не найдено ни одного вашего региона в каком-либо мире!", "§c") ).build() ) );
            //player.sendMessage("§cНе найдено ни одного вашего региона в каком-либо мире!");
            //return;
        }
        

        

        ClickableItem[] arrclickableItem = new ClickableItem[arrayList.size()];
        ClickableItem[] arrclickableItem2 = arrayList.toArray(arrclickableItem);
        pagination.setItems(arrclickableItem2);
        pagination.setItemsPerPage(27);
        
        if (!pagination.isLast()) {
            inventoryContents.set( 4, 6, ClickableItem.of( new ItemBuilder(Material.MAP).name(Language.INTERFACE_NEXT_PAGE.toString()).build(), inventoryClickEvent 
                    -> inventoryContents.getHost().open(player, pagination.next().getPage())));
        }
        if (!pagination.isFirst()) {
            inventoryContents.set( 4, 2, ClickableItem.of( new ItemBuilder(Material.MAP).name(Language.INTERFACE_PREVIOUS_PAGE.toString()).build(), inventoryClickEvent 
                    -> inventoryContents.getHost().open(player, pagination.previous().getPage())));
        }
        
        //SlotIterator slotIterator = inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1);
        SlotIterator slotIterator = inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL,new SlotPos(1, 1));
        slotIterator = slotIterator.allowOverride(false);
        pagination.addToIterator(slotIterator);
    }
    
    
    
        
        
        
        
        
        
   /*     
    Procyon
    public void init(final Player player, final InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(LandHomeMenu.fill));
        final Pagination pagination = contents.pagination();
        final ArrayList<ClickableItem> list = new ArrayList<ClickableItem>();
        for (final ClaimEntry claimEntry : RegionGUI.getInstance().getPlayerManager().getPlayerClaims(player.getUniqueId())) {
            final World world = claimEntry.getTemplate().getWorld().get();
            if (RegionUtils.getRegionManager(world).getRegion(claimEntry.getRegionID()) == null) {
                Bukkit.getLogger().severe("WorldGuard region is missing [" + claimEntry.getRegionID() + "]. This region is still assigned to the player but missing in WorldGuard!");
                Bukkit.getLogger().severe("world: " + world.getName() + " template id: " + claimEntry.getTemplate());
            }
            else {
                final ArrayList <String>list2 = new ArrayList(Language.INTERFACE_HOME_ENTRYBUTTON_DESCRIPTION.getDescriptionArray());
                list2.replaceAll(s -> s.replace("%world%", world.getName()));
                
                
                
                final ProtectedRegion protectedRegion;
                list.add(ClickableItem.of(new ItemBuilder(Material.GRAY_BED).name(claimEntry.getRegionID()).lore((List)list2).build(), p2 -> {
                    if (protectedRegion.getFlag((Flag)Flags.TELE_LOC) != null) {
                        player.teleport(BukkitAdapter.adapt((Location)protectedRegion.getFlag((Flag)Flags.TELE_LOC)));
                    }
                    else {
                        player.sendMessage(Language.ERROR_NO_HOME_SET.toChatString());
                    }
                    return;
                }));
                
                
            }
        }
        
        pagination.setItems((ClickableItem[])list.toArray(new ClickableItem[list.size()]));
        pagination.setItemsPerPage(27);
        if (!pagination.isLast()) {
            contents.set(4, 6, ClickableItem.of(new ItemBuilder(Material.MAP).name(Language.INTERFACE_NEXT_PAGE.toString()).build(), p3 -> contents.inventory().open(player, pagination.next().getPage())));
        }
        if (!pagination.isFirst()) {
            contents.set(4, 2, ClickableItem.of(new ItemBuilder(Material.MAP).name(Language.INTERFACE_PREVIOUS_PAGE.toString()).build(), p3 -> contents.inventory().open(player, pagination.previous().getPage())));
        }
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1).allowOverride(false));
    }*/
}
