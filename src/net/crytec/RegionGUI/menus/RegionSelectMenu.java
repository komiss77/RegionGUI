package net.crytec.RegionGUI.menus;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.List;
import net.crytec.RegionGUI.data.RegionUtils;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.TemplateManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.Pagination;
import ru.komiss77.utils.inventory.SlotIterator;
import ru.komiss77.utils.inventory.SlotPos;



public class RegionSelectMenu implements InventoryProvider
{
    private final List<ProtectedRegion> regions;
    
    public RegionSelectMenu(final List<ProtectedRegion> regions) {
        this.regions = regions;
    }
    
    //если стоять в перекрывающихся регионах
    @Override
    public void init(final Player player, InventoryContent inventoryContents) {
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        
        Pagination pagination = inventoryContents.pagination();
        ArrayList<ClickableItem> menuEntrys = new ArrayList<>();
        
        int number = 1;
        for (ProtectedRegion region : this.regions) {
            
            final Template template = TemplateManager.getByName( RegionUtils.getTemplateName(region) );
            final String createTime = RegionUtils.getCreateTime(region);
            
            //String string = ChatColor.GREEN + region.getId();
            //String string2 = Language.INTERFACE_SELECT_DESCRIPTION.toString().replace("%region%", string);
            
            ItemStack itemStack = new ItemBuilder(Material.BOOK)
                    .name("§7Регион §6"+number)
                    .lore("§7Тип региона: "+(template==null ? "не определён" : template.getDisplayname()))
                    .lore("§7Создан: §6"+(createTime.isEmpty()?"§8нет данных":createTime))
                    .lore ("§7Пользователей"+(region.getMembers().getPlayerDomain().size()==0 ? " нет" : ": "+region.getMembers().getPlayerDomain().size()))
                    .lore("ЛКМ - перейти к управлению")
                    .build();
            number++;
            
            menuEntrys.add(ClickableItem.of( itemStack, inventoryClickEvent -> 
                    MenuOpener.openMain(player, region) ));
        }
        
        ClickableItem[] arrclickableItem = new ClickableItem[menuEntrys.size()];
        ClickableItem[] citems = menuEntrys.toArray(arrclickableItem);
        
        pagination.setItems(citems);
        pagination.setItemsPerPage(18);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 0)));
        
    }
    /*
    PROCYON
    public void init(final Player player, final InventoryContents contents) {
        final Pagination pagination = contents.pagination();
        final ArrayList<ClickableItem> list = new ArrayList<ClickableItem>();
        final Iterator<ClaimEntry> iterator = this.claims.iterator();
        while (iterator.hasNext()) {
            final String string = ChatColor.GREEN + iterator.next().getRegionID();
            final ClaimEntry claim;
            list.add(ClickableItem.of(new ItemBuilder(Material.BOOK).name(string).lore(Language.INTERFACE_SELECT_DESCRIPTION.toString().replace("%region%", string)).build(), p2 -> SmartInventory.builder().provider((InventoryProvider)new RegionManageInterface(claim)).size(3).title(Language.INTERFACE_MANAGE_TITLE.toString()).build().open(player)));
        }
        pagination.setItems((ClickableItem[])list.toArray(new ClickableItem[list.size()]));
        pagination.setItemsPerPage(18);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));
    }*/
}

/* FF
   public void init(Player player, InventoryContents contents) {
      Pagination var3 = var2.pagination();
      ArrayList var4 = new ArrayList();
      Iterator var6 = this.claims.iterator();

      while(var6.hasNext()) {
         ClaimEntry var5 = (ClaimEntry)var6.next();
         String var7 = ChatColor.GREEN + var5.getRegionID();
         String var8 = Language.INTERFACE_SELECT_DESCRIPTION.toString().replace("%region%", var7);
         ItemStack var9 = (new ItemBuilder(Material.BOOK)).name(var7).lore(var8).build();
         var4.add(ClickableItem.of(var9, (var2x) -> {
            SmartInventory.builder().provider(new RegionManageInterface(var5)).size(3).title(Language.INTERFACE_MANAGE_TITLE.toString()).build().open(var1);
         }));
      }

      ClickableItem[] var10 = new ClickableItem[var4.size()];
      var10 = (ClickableItem[])var4.toArray(var10);
      var3.setItems(var10);
      var3.setItemsPerPage(18);
      var3.addToIterator(var2.newIterator(Type.HORIZONTAL, 1, 0));
   }
*/
