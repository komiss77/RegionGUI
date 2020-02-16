package net.crytec.RegionGUI.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.List;
import net.crytec.RegionGUI.Language;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.RegionUtils;
import net.crytec.RegionGUI.menus.LandBuyMenu;
import net.crytec.RegionGUI.menus.LandHomeMenu;
import net.crytec.RegionGUI.menus.MenuOpener;
import net.crytec.RegionGUI.menus.RegionSelectMenu;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.SmartInventory;



//@CommandAlias("land")
public class LandCommand implements CommandExecutor
{
    
    public LandCommand() {
    }
    
    
   @Override
    public boolean onCommand ( CommandSender se, Command comandd, String cmd, String[] a) {
        
        if ( !(se instanceof Player) ) { 
            se.sendMessage("§4Это не консольная команда!"); 
            return false; 
        }
        
        final Player player = (Player) se;
        
        if (!RegionGUI.getInstance().getConfig().getStringList("enabled_worlds").contains(player.getWorld().getName())) {
            player.sendMessage(Language.ERROR_WORLD_DISABLED.toChatString());
            return false;
        }
        
        if (a.length>=1 && a[0].equalsIgnoreCase("home")) {
            SmartInventory.builder().id("home-" + player.getName()).provider((InventoryProvider)new LandHomeMenu()).size(5, 9).title(Language.INTERFACE_HOME_TITLE.toString()).build().open(player);
            return true;
        }
        
        if (a.length>=2 && a[0].equalsIgnoreCase("list")) {
            if (player.getName().equalsIgnoreCase(a[1])) {
                player.performCommand("rg list");
            } else {
                player.performCommand("rg list -p "+a[1]);
            }
            return true;
        }
        
        checkForRegions(player);
        
        return true;
    }
    
    
    
   /*  @Default
    public void openLandInterface(final Player issuer) {
        if (!this.plugin.getConfig().getStringList("enabled_worlds").contains(issuer.getWorld().getName())) {
            issuer.sendMessage(Language.ERROR_WORLD_DISABLED.toChatString());
            return;
        }
        this.checkForRegions(issuer);
    }
    
    @Subcommand("help")
    public void sendCommandHelp(final CommandIssuer issuer, final CommandHelp help) {
        help.showHelp(issuer);
    }
    
    @Subcommand("home")
    public void openHomeGUI(final Player issuer) {
        SmartInventory.builder().id("home-" + issuer.getName()).provider((InventoryProvider)new LandHomeMenu()).size(5, 9).title(Language.INTERFACE_HOME_TITLE.toString()).build().open(issuer);
    }
    
    @Subcommand("list")
    //@CommandPermission("region.list")
    public void displayLandList(final Player issuer, @Optional final OnlinePlayer op) {
        if (op == null || op.getPlayer()==null || issuer.getName().equals(op.getPlayer().getName())) {
            issuer.performCommand("rg list");
        } else {
            issuer.performCommand("rg list -p "+op.getPlayer().getName());
        }
        
        //issuer.sendMessage("LandCommand displayLandList не доделано");
       if (op == null) {
            for (final ClaimEntry claimEntry : RegionGUI.getInstance().getPlayerManager().getPlayerClaims(issuer.getUniqueId())) {
                issuer.sendMessage(Language.COMMAND_LIST_ENTRY.toChatString().replace("%region%", claimEntry.getRegionID()).replace("%template%", claimEntry.getTemplate().getDisplayname()));
            }
            return;
        }
        for (final ClaimEntry claimEntry2 : RegionGUI.getInstance().getPlayerManager().getPlayerClaims(issuer.getUniqueId())) {
            issuer.sendMessage(Language.COMMAND_LIST_ENTRY.toChatString().replace("%region%", claimEntry2.getRegionID()).replace("%template%", claimEntry2.getTemplate().getDisplayname()));
        }
    }
    */
    
    
    
    private void checkForRegions(Player player) {
        //RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt((org.bukkit.World)player.getWorld()));
        
        LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionManager rm = RegionUtils.getRegionManager(player.getWorld());
        ApplicableRegionSet applicableRegionSet = rm.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
        
        
        
        
        
        if (applicableRegionSet.size() == 0) { //нет приватов в точке нахождения, открываем меню покупки - добавить тп в свои регионы
            
            
            //if (!player.hasPermission("region.claim")) {
            //    player.sendMessage(Language.ERROR_NO_PERMISSION.toChatString());
            //    return;
            //}
            SmartInventory.builder().id("regiongui.claim"). provider(new LandBuyMenu(RegionGUI.getInstance())). size(5, 9). title(Language.INTERFACE_BUY_TITLE.toString()). build().open(player);

            
            
            
        } else if (applicableRegionSet.size() == 1) { //только один приват в точке нахождения, открываем меню управления им
            
            
            ProtectedRegion rg = applicableRegionSet.getRegions().iterator().next();
            
            if ( rg.isOwner(lp) ) { //если владелец, меню управления
                
                MenuOpener.openMain(player, rg);
                
            } else { //стоим в чужом привате!  //посылаем гулять или предложить тп в свои регионы
                
                //if (player.hasPermission("region.mod")) { //модератор - управление чужим приватом
                //    SmartInventory.builder(). provider(new RegionManagerInterface(rg)) .size(3) .title(Language.INTERFACE_MANAGE_TITLE.toString()) .build().open(player);
               // } else {
                    if (rg.isMember(lp)) {
                        player.sendMessage("§5В этом регионе у вас права пользователя.");
                    } else {
                        player.sendMessage("§cВы в чужом регионе.");
                    }

                        if (RegionUtils.getPlayerRegions(player).isEmpty()) {

                            TextComponent msg = new TextComponent( "§fНажмите сюда для ТП в свободное место!" );
                            msg.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT,  new ComponentBuilder("§aКлик - случайный телепорт в свободное место").create()));
                            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpr" ) );
                            player.spigot().sendMessage( msg);

                        } else {

                            TextComponent msg = new TextComponent( "§fНажмите сюда для перехода в свой регион!" );
                            msg.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT,  new ComponentBuilder("§aКлик-открыть меню ТП в свой регион").create()));
                            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/land home" ) );
                            player.spigot().sendMessage( msg);
                            //p.spigot().sendMessage(ChatMessageType.CHAT, msg);
                        }
               // }
            }
                  
            
            
            

        } else  if (applicableRegionSet.size() > 1) {   //несколько приватов в точке нахождения, открываем меню выбора каким управлять
            
            //находим приваты игрока в данной точке
            final List <ProtectedRegion> playerRegions = RegionUtils.getPlayerRegions(player);
            
            //если его приватов тут нет, посылаем гулять или предложить тп в свои регионы
            if (playerRegions.isEmpty()) {
                
                player.sendMessage("Вы в чужом регионе! Отойдите за его границы.");
                
            } else {
                
                SmartInventory.builder().id("regiongui.regionselect").provider( new RegionSelectMenu(playerRegions)).size(3).title(Language.INTERFACE_SELECT_TITLE.toString()).build().open(player);
            
            }
            

        }
        
        
        
    }

    
    
        
}
