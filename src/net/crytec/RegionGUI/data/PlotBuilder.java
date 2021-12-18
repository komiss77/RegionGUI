package net.crytec.RegionGUI.data;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.domains.PlayerDomain;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import java.util.Iterator;
import net.crytec.RegionGUI.Language;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.manager.PreviewBlockManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.komiss77.ApiOstrov;


public class PlotBuilder
{
    private final RegionGUI plugin;
    private final Player player;
    private final Template claimTemplate;
    private final Location loc;
    private final RegionManager manager;
    private final LocalPlayer localPlayer;
    
    
    
    public PlotBuilder(final Player player, final Template claimTemplate) {
        this.plugin = (RegionGUI)JavaPlugin.getPlugin((Class)RegionGUI.class);
        this.player = player;
        this.claimTemplate = claimTemplate;
        this.loc = player.getLocation();
        this.manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        this.localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
    }
    
    public void build() {
        PreviewBlockManager.stopPrewiev(player);
        //final int blockX = this.loc.getBlockX();
        //final int blockZ = this.loc.getBlockZ();
        int blockY = this.loc.getBlockY();
        
        if (blockY>30) blockY = 30; 
        
        final int size = this.claimTemplate.getSize();
//System.out.println("claimTemplate size="+size);        
        if (this.claimTemplate.getPermission() != null && !this.claimTemplate.getPermission().isEmpty() && !this.player.hasPermission(this.claimTemplate.getPermission())) {
            this.player.sendMessage(Language.ERROR_NO_PERMISSION.toChatString().replaceAll("%permission%", this.claimTemplate.getPermission()));
            return;
        }
        
        //final int halfSize = (int)Math.round(size / 2.0);
        final Vector top = claimTemplate.getMaximumPoint(loc).toVector();//new Vector(down.getBlockX() + size, blockY + claimTemplate.getHeight(), down.getBlockZ() + size); //на 1 меньше, т.к. включая
        final Vector down = claimTemplate.getMinimumPoint(loc).toVector();//new Vector(blockX - halfSize, blockY - claimTemplate.getDepth(), blockZ - halfSize); //находим нижний угол
        //final Vector top = new Vector(blockX + n, blockY + claimTemplate.getHeight(), blockZ + n);
        
        final String regName = this.player.getName()+"-rgui-"+claimTemplate.getName()+"-"+ApiOstrov.currentTimeSec();
        
        //final ProtectedCuboidRegion region = new ProtectedCuboidRegion( this.plugin.getConfig().getString("region-identifier").replace("%player%", this.player.getName()).replace("%displayname%", this.regionName), BlockVector3.at(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()), BlockVector3.at(vector2.getBlockX(), vector2.getBlockY(), vector2.getBlockZ()));
        final ProtectedCuboidRegion region = new ProtectedCuboidRegion( regName, 
            BlockVector3.at(top.getBlockX(), top.getBlockY(), top.getBlockZ()),
            BlockVector3.at(down.getBlockX(), down.getBlockY(), down.getBlockZ())
        );
        
        if (this.manager.overlapsUnownedRegion(region, this.localPlayer) && this.plugin.getConfig().getBoolean("preventRegionOverlap", true)) {
            this.player.sendMessage(Language.ERROR_OVERLAP.toString());
            return;
        }
        
        //добавление владельца
        final DefaultDomain owners = region.getOwners();
        final PlayerDomain playerDomain = owners.getPlayerDomain();
        //WorldGuard.getInstance().getProfileCache().put(new Profile(this.player.getUniqueId(), this.player.getName()));
        //playerDomain.addPlayer(this.player.getUniqueId());
        playerDomain.addPlayer(this.player.getName());
        owners.setPlayerDomain(playerDomain);
        region.setOwners(owners);
        region.setFlag((Flag)Flags.TELE_LOC, (Object)BukkitAdapter.adapt(this.player.getLocation()));
        
//System.out.println("claimTemplate size="+size+" region "+region.getMinimumPoint()+"  "+region.getMaximumPoint());        
        
        final int price = this.claimTemplate.getPrice();
        //if (!RegionGUI.econ.withdrawPlayer((OfflinePlayer)this.player.getPlayer(), (double)price).transactionSuccess()) {
        if ( ApiOstrov.moneyGetBalance(player.getName()) < price) {
            this.player.sendMessage(Language.ERROR_NO_MONEY.toChatString());
            return;
        }
        ApiOstrov.moneyChange(player.getName(), -price, "Покупка региона");
        //this.player.sendMessage(Language.REGION_CREATE_MONEY.toChatString().replace("%money%", new StringBuilder().append(price).toString()).replace("%currencyname%", RegionGUI.econ.currencyNameSingular()));
        region.setDirty(true);
        this.manager.addRegion(region);
        this.player.sendMessage(Language.REGION_CREATE_SUCCESS.toChatString());
        
        
        
        
        
        if (RegionGUI.getInstance().getConfig().getBoolean("regenOnDelete", true)) {
            //ApiOstrov.getWorldEditor().save(player, BukkitAdapter.adapt(player.getWorld(), region.getMinimumPoint()), BukkitAdapter.adapt(player.getWorld(), region.getMaximumPoint()), RegionGUI.getInstance().getDataFolder() + "/schematics", regName.toLowerCase(), true);
            ApiOstrov.getWorldEditor().save(player, BukkitAdapter.adapt(player.getWorld(), region.getMinimumPoint()), BukkitAdapter.adapt(player.getWorld(), region.getMaximumPoint()), regName.toLowerCase(), "");
        }
        
        
        
        //сохранение в файл
        /*final com.sk89q.worldedit.world.World faweWorld = FaweAPI.getWorld(player.getWorld().getName());
        CuboidRegion  toSave = new CuboidRegion(faweWorld, region.getMinimumPoint(), region.getMaximumPoint());
        
        
        EditSession editSession = new EditSessionBuilder(toSave.getWorld()).autoQueue(false).fastmode(true).build();
        
        try {
            Clipboard clipboard = new BlockArrayClipboard(toSave);
            ForwardExtentCopy copy = new ForwardExtentCopy(editSession, toSave, clipboard, region.getMinimumPoint());
            copy.setCopyingEntities(false);
            copy.setCopyingBiomes(false);
            Operations.completeLegacy(copy);
            //editSession.flushQueue(); тут нельзя, не успевает сохранить!

            File file = new File("plugins/RegionGUI/schematics/land/"+regName.toLowerCase()+".schem");
            final ClipboardFormat cf = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

            
            clipboard.save(file, cf); //java.lang.ArrayIndexOutOfBoundsException: -71 ??
            
        } catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException ex) {
            
            RegionGUI.log_err("Сохранение копии региона "+region.getId()+" : "+ex.getMessage());
            
        } finally {
            
            editSession.flushQueue();
            
        }*/
        
           // new BukkitRunnable() {
            //    @Override
           //     public void run() {
                   // editSession.flushQueue();
           //     }
          //  }.runTaskLater(RegionGUI.getInstance(), 5);
        
      
        
        
        
        
        
        if (this.claimTemplate.isGenerateBorder()) {
            final Material mat = claimTemplate.getBorderMaterial();
            final World world = player.getWorld();
            new BukkitRunnable() {
                @Override
                public void run() {
                    //editSession.flushQueue();
                    //Walls walls = new Walls(world, mat, region);
                    Walls walls = new Walls(world, mat, down.toLocation(world), claimTemplate.getSize() );
                }
            }.runTaskLater(RegionGUI.getInstance(), 30);
            
        }
        
        
        
        
        
        final Iterator<String> iterator = this.claimTemplate.getRunCommands().iterator();
        while (iterator.hasNext()) {
            final String replace = iterator.next().replace("%player%", player.getName()).replace("%region%", region.getId()).replace("%world%", player.getWorld().getName());
            if (replace.startsWith("<server>")) {
                final String trim = replace.replace("<server>", "").trim();
                RegionGUI.log_ok("Performing Command:" + trim);
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), trim);
            }
            else {
                player.performCommand(replace);
            }
        } 
        
        
        
        
        
        
    }
}











/*
Работает, но блочит!!
        final World faweWorld = FaweAPI.getWorld(player.getWorld().getName());
        CuboidRegion  toSave = new CuboidRegion(faweWorld, region.getMinimumPoint(), region.getMaximumPoint());
        
        EditSession editSession = new EditSessionBuilder(toSave.getWorld()).autoQueue(true).fastmode(true).build();
        
        Clipboard clipboard = new BlockArrayClipboard(toSave);
        
        
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, toSave, clipboard, region.getMinimumPoint());
        copy.setCopyingEntities(false);
        copy.setCopyingBiomes(false);
        Operations.completeLegacy(copy);
        
        //copy.cancel();
        
        editSession.cancel();
        editSession.close();
        
        File file = new File("plugins/RegionGUI/Schematics/"+regName.toLowerCase()+".schem");

        final ClipboardFormat cf = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

        try {
            
            clipboard.save(file, cf);
            


            
        } catch (IOException ex) {
            
            RegionGUI.log_err("Сохранение копии региона "+region.getId()+" : "+ex.getMessage());
            
        } finally {
            //clipboard.close();
        }
*/