package net.crytec.RegionGUI.commands;


import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.menus.AdminTemplateList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.komiss77.utils.inventory.SmartInventory;




//@CommandPermission("region.admin")

public class LandAdmin implements CommandExecutor {
    
    public LandAdmin() {
    }
    
    
    
    @Override
    public boolean onCommand ( CommandSender se, Command comandd, String cmd, String[] a) {
        
        if ( !(se instanceof Player) ) { 
            se.sendMessage("§4Это не консольная команда!"); 
            return false; 
        }
        
        final Player player = (Player) se;
        
        if (!player.hasPermission("region.admin")) { 
            se.sendMessage("§4Нет прав!"); 
            return false; 
        }
        
        SmartInventory.builder().provider(new AdminTemplateList()).size(6).title("§8Редактор заготовок [" + player.getWorld().getName() + "]").build().open(player);
        
        return true;
    }

    
}
