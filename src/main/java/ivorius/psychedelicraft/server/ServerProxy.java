/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.server;

import ivorius.psychedelicraft.PSProxy;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.events.PSCoreHandlerServer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class ServerProxy implements PSProxy
{
    @Override
    public void handleClientUpdate(cpw.mods.fml.common.network.simpleimpl.IMessage message,
                                   cpw.mods.fml.common.network.simpleimpl.MessageContext context)
    {
        ivorius.psychedelicraft.network.ClientUpdateHandlers.payload(message).release();
    }

    @Override
    public void preInit()
    {
        Psychedelicraft.coreHandlerServer = new PSCoreHandlerServer();
        Psychedelicraft.coreHandlerServer.register();
    }

    @Override
    public void registerRenderers()
    {

    }

    @Override
    public void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size)
    {

    }

    @Override
    public void createDrugRenderer(DrugProperties drugProperties)
    {

    }

    @Override
    public void loadConfig(String configID)
    {

    }
    @Override
    public Object getClientGuiElement(int id, net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, int x, int y, int z) {
        return null;
    }
}
