package net.crytec.RegionGUI.utils.packets;

import java.lang.reflect.InvocationTargetException;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;
import com.google.common.base.Objects;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public abstract class AbstractPacket
{
    protected PacketContainer handle;
    
    protected AbstractPacket(final PacketContainer handle, final PacketType type) {
        if (handle == null) {
            throw new IllegalArgumentException("Packet handle cannot be NULL.");
        }
        if (!Objects.equal((Object)handle.getType(), (Object)type)) {
            throw new IllegalArgumentException(handle.getHandle() + " is not a packet of type " + type);
        }
        this.handle = handle;
    }
    
    public PacketContainer getHandle() {
        return this.handle;
    }
    
    public void sendPacket(final Player receiver) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, this.getHandle());
        }
        catch (InvocationTargetException cause) {
            throw new RuntimeException("Cannot send packet.", cause);
        }
    }
    
    public void broadcastPacket() {
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(this.getHandle());
    }
    
    @Deprecated
    public void recievePacket(final Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, this.getHandle());
        }
        catch (IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException("Cannot recieve packet.", cause);
        }
    }
    
    public void receivePacket(final Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, this.getHandle());
        }
        catch (IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException("Cannot receive packet.", cause);
        }
    }
}
