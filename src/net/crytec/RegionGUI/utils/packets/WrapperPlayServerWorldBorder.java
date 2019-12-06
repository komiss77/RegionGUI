package net.crytec.RegionGUI.utils.packets;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.PacketType;

public class WrapperPlayServerWorldBorder extends AbstractPacket
{
    public static final PacketType TYPE;
    
    static {
        TYPE = PacketType.Play.Server.WORLD_BORDER;
    }
    
    public WrapperPlayServerWorldBorder() {
        super(new PacketContainer(WrapperPlayServerWorldBorder.TYPE), WrapperPlayServerWorldBorder.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerWorldBorder(final PacketContainer packet) {
        super(packet, WrapperPlayServerWorldBorder.TYPE);
    }
    
    public EnumWrappers.WorldBorderAction getAction() {
        return (EnumWrappers.WorldBorderAction)this.handle.getWorldBorderActions().read(0);
    }
    
    public void setAction(final EnumWrappers.WorldBorderAction value) {
        this.handle.getWorldBorderActions().write(0, value);
    }
    
    public int getPortalTeleportBoundary() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setPortalTeleportBoundary(final int value) {
        this.handle.getIntegers().write(0, value);
    }
    
    public double getCenterX() {
        return (double)this.handle.getDoubles().read(0);
    }
    
    public void setCenterX(final double value) {
        this.handle.getDoubles().write(0, value);
    }
    
    public double getCenterZ() {
        return (double)this.handle.getDoubles().read(1);
    }
    
    public void setCenterZ(final double value) {
        this.handle.getDoubles().write(1, value);
    }
    
    public double getOldRadius() {
        return (double)this.handle.getDoubles().read(2);
    }
    
    public void setOldRadius(final double value) {
        this.handle.getDoubles().write(2, value);
    }
    
    public double getRadius() {
        return (double)this.handle.getDoubles().read(3);
    }
    
    public void setRadius(final double value) {
        this.handle.getDoubles().write(3, value);
    }
    
    public long getSpeed() {
        return (long)this.handle.getLongs().read(0);
    }
    
    public void setSpeed(final long value) {
        this.handle.getLongs().write(0, value);
    }
    
    public int getWarningTime() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setWarningTime(final int value) {
        this.handle.getIntegers().write(1, value);
    }
    
    public int getWarningDistance() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setWarningDistance(final int value) {
        this.handle.getIntegers().write(2, value);
    }
}
