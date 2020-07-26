package net.crytec.RegionGUI.data;
/*


public class PreviewBorder implements Runnable {
    
    private final WrapperPlayServerWorldBorder resetcenterPacket;
    private final WrapperPlayServerWorldBorder resetsizePacket;
    private final WrapperPlayServerWorldBorder centerPacket;
    private final WrapperPlayServerWorldBorder sizePacket;
    private final Player player;
    private final int size;
    private final BukkitTask task;
    
    
    
    public PreviewBorder(final Player player, final int size) {
        this.player = player;
        final World world = player.getWorld();
        this.size = size;
        
        this.resetcenterPacket = new WrapperPlayServerWorldBorder();
        (this.resetsizePacket = new WrapperPlayServerWorldBorder()).setRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setOldRadius(world.getWorldBorder().getSize());
        this.resetsizePacket.setSpeed(0L);
        this.resetsizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        this.resetcenterPacket.setCenterX(world.getWorldBorder().getCenter().getBlockX());
        this.resetcenterPacket.setCenterZ(world.getWorldBorder().getCenter().getBlockZ());
        this.resetcenterPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
        
        this.centerPacket = new WrapperPlayServerWorldBorder();
        (this.sizePacket = new WrapperPlayServerWorldBorder()).setRadius(this.size);
        this.sizePacket.setOldRadius(this.size);
        this.sizePacket.setSpeed(0L);
        this.sizePacket.setAction(EnumWrappers.WorldBorderAction.LERP_SIZE);
        this.centerPacket.setCenterX(player.getLocation().getBlockX());
        this.centerPacket.setCenterZ(player.getLocation().getBlockZ());
        this.centerPacket.setAction(EnumWrappers.WorldBorderAction.SET_CENTER);
        this.centerPacket.sendPacket(this.player);
        
        Bukkit.getScheduler().runTaskLater((Plugin)RegionGUI.getInstance(), () -> this.sizePacket.sendPacket(this.player), 1L);
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)RegionGUI.getInstance(), (Runnable)this, 5L, 15L);
    }
    
    
    
    public void reset() {
        this.resetcenterPacket.sendPacket(this.player);
        Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> this.resetsizePacket.sendPacket(this.player), 1L);
    }
    
    
    
    private void updateCenter() {
        final Location add = this.player.getLocation().getBlock().getLocation().clone().add(0.5, 0.0, 0.5);
        this.centerPacket.setCenterX(add.getX());
        this.centerPacket.setCenterZ(add.getZ());
        this.centerPacket.sendPacket(this.player);
    }
    
    
    
    @Override
    public void run() {
        if (player ==  null || !player.isOnline() || player.isSneaking()) {
            this.task.cancel();
            this.reset();
            player.resetTitle();
        } else {
            player.sendTitle("", "§2Shift - остановить показ", 0, 30, 0);
            this.updateCenter();
        }
    }
    
    
    
    @Override
    protected void finalize() {
        System.out.println("Destroyed RegionPreview instance!");
    }
    
    
}*/
