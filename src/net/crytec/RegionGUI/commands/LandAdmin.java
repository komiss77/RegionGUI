package net.crytec.RegionGUI.commands;

import net.crytec.acf.CommandHelp;
import net.crytec.acf.CommandIssuer;
import net.crytec.acf.annotation.Description;
import net.crytec.acf.annotation.Subcommand;
import net.crytec.acf.annotation.Default;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.RegionGUI.menus.AdminTemplateList;
import net.crytec.phoenix.api.inventory.SmartInventory;
import org.bukkit.entity.Player;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.acf.annotation.CommandPermission;
import net.crytec.acf.annotation.CommandAlias;
import net.crytec.acf.BaseCommand;



@CommandAlias("landadmin")
@CommandPermission("region.admin")

public class LandAdmin extends BaseCommand
{
    //private final RegionGUI plugin;
    
    public LandAdmin(final RegionGUI plugin) {
        //this.plugin = plugin;
    }
    
    @Default
    public void openEditor(final Player player) {
        SmartInventory.builder().provider(new AdminTemplateList()).size(6).title("§8Редактор заготовок [" + player.getWorld().getName() + "]").build().open(player);
    }
    
    @Subcommand("editor")
    @Description("Opens the land template editor")
    public void landAdminCommand(final Player player) {
        SmartInventory.builder().provider(new AdminTemplateList()).size(6).title("§8Редактор заготовок [" + player.getWorld().getName() + "]").build().open(player);
    }
    /*
    @Subcommand("addregion")
    @Description("Adds a defined worldguard region to a player with a given template, regardless of size or checks. This is a forced action.")
    public void forceAdd(final Player player, final OfflinePlayer owner, final ProtectedRegion region, final RegionClaimTemplate claim) {
        this.plugin.getPlayerManager().addClaimToPlayer(owner.getUniqueId(), region, claim);
        player.sendMessage("§7Assigned region to player. Please note this is a forced action and no size/permission check apply.");
    }*/
    
    @Subcommand("help")
    public void sendCommandHelp(final CommandIssuer issuer, final CommandHelp help) {
        help.showHelp(issuer);
    }
}
