package ivorius.psychedelicraft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import ivorius.ivtoolkit.network.PacketExtendedEntityPropertiesData;
import ivorius.ivtoolkit.network.PacketTileEntityData;
import ivorius.psychedelicraft.Psychedelicraft;

/** Keep the existing wire format without loading client classes on a dedicated server. */
public final class ClientUpdateHandlers {
    private ClientUpdateHandlers() {}

    public static ByteBuf payload(IMessage message) {
        if (message instanceof PacketExtendedEntityPropertiesData)
            return ((PacketExtendedEntityPropertiesData) message).getPayload();
        return ((PacketTileEntityData) message).getPayload();
    }

    public static final class EntityData implements IMessageHandler<PacketExtendedEntityPropertiesData, IMessage> {
        @Override
        public IMessage onMessage(PacketExtendedEntityPropertiesData message, MessageContext context) {
            Psychedelicraft.proxy.handleClientUpdate(message, context);
            return null;
        }
    }

    public static final class TileData implements IMessageHandler<PacketTileEntityData, IMessage> {
        @Override
        public IMessage onMessage(PacketTileEntityData message, MessageContext context) {
            Psychedelicraft.proxy.handleClientUpdate(message, context);
            return null;
        }
    }
}
