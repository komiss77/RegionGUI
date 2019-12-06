package net.crytec.RegionGUI.menus;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.RegionGUI.Language;
import net.crytec.phoenix.api.inventory.SmartInventory;
import org.bukkit.entity.Player;





public class MenuOpener {

    public static void openMain(final Player player, final ProtectedRegion region) {
        SmartInventory.builder().provider(new RegionMain(region)).size(4).title(Language.INTERFACE_MANAGE_TITLE.toString()).build().open(player);
    }
   
    
    
    
}
