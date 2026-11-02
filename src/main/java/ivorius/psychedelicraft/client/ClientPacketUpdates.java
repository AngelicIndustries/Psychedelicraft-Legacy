package ivorius.psychedelicraft.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ivorius.ivtoolkit.network.PacketExtendedEntityPropertiesData;
import ivorius.ivtoolkit.network.PacketTileEntityData;
import ivorius.ivtoolkit.network.PartialUpdateHandler;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.network.ClientUpdateHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

final class ClientPacketUpdates {
    private ClientPacketUpdates() {}

    static void schedule(IMessage message, MessageContext context) {
        Minecraft mc = Minecraft.getMinecraft();
        World receivedWorld = mc.theWorld;
        ByteBuf payload = ClientUpdateHandlers.payload(message);
        byte[] data = new byte[payload.readableBytes()];
        try {
            payload.readBytes(data);
        } finally {
            // IvToolkit decodes into a separate buffer, owned by the receiving handler.
            payload.release();
        }
        mc.func_152344_a(() -> {
            if (receivedWorld == null || mc.theWorld != receivedWorld || mc.getNetHandler() != context.netHandler)
                return;

            ByteBuf buffer = Unpooled.wrappedBuffer(data);
            try {
                if (message instanceof PacketExtendedEntityPropertiesData) {
                    PacketExtendedEntityPropertiesData update = (PacketExtendedEntityPropertiesData) message;
                    if (!DrugProperties.EEP_KEY.equals(update.getEepKey()))
                        return;
                    Entity entity = receivedWorld.getEntityByID(update.getEntityID());
                    if (entity == null)
                        return;
                    Object properties = entity.getExtendedProperties(update.getEepKey());
                    if (properties instanceof PartialUpdateHandler)
                        ((PartialUpdateHandler) properties).readUpdateData(buffer, update.getContext());
                } else {
                    PacketTileEntityData update = (PacketTileEntityData) message;
                    if (!receivedWorld.blockExists(update.getX(), update.getY(), update.getZ()))
                        return;
                    TileEntity tile = receivedWorld.getTileEntity(update.getX(), update.getY(), update.getZ());
                    if (tile instanceof PartialUpdateHandler)
                        ((PartialUpdateHandler) tile).readUpdateData(buffer, update.getContext());
                }
            } catch (RuntimeException e) {
                Psychedelicraft.logger.warn("Could not apply Psychedelicraft client update", e);
            } finally {
                buffer.release();
            }
        });
    }
}
