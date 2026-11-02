/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

/**
 * Created by lukas on 24.05.14.
 */
public interface PSProxy
{
    void handleClientUpdate(cpw.mods.fml.common.network.simpleimpl.IMessage message,
                            cpw.mods.fml.common.network.simpleimpl.MessageContext context);

    void preInit();

    void registerRenderers();

    void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size);

    void createDrugRenderer(DrugProperties drugProperties);

    void loadConfig(String configID);
    Object getClientGuiElement(int id, net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, int x, int y, int z);
}
