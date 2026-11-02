/* Copyright (c) 2014, Lukas Tenbrink. */
package ivorius.psychedelicraft.client;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.client.audio.MovingSoundDrug;
import ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import ivorius.psychedelicraft.client.rendering.SmoothCameraHelper;
import ivorius.psychedelicraft.client.rendering.shaders.PSRenderStates;
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.fluids.FluidWithIconSymbolRegistering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/** Registered only through the physical client proxy. */
public final class ClientEventHandler {
    public void register() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderHand(net.minecraftforge.client.event.RenderHandEvent event) {
        // Forge fires this before clearing depth for the hand. Keep the world depth intact.
        if ("Depth".equals(PSRenderStates.currentRenderPass) || "Shadows".equals(PSRenderStates.currentRenderPass))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            PSBlocks.psycheLeaves.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(event.entity); // Initialize drug helper

        if (event.world.isRemote && drugProperties != null)
            initializeMovingSoundDrug(event.entity, drugProperties);
    }

    public void initializeMovingSoundDrug(Entity entity, DrugProperties drugProperties)
    {
        SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
        for (String drugName : drugProperties.getAllDrugNames())
        {
            if (PSConfig.hasBGM(drugName))
                soundHandler.playSound(new MovingSoundDrug(new ResourceLocation(Psychedelicraft.MODID, "drug." + drugName.toLowerCase()), entity, drugProperties, drugName));
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.PORTAL)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityLivingBase renderEntity = mc.renderViewEntity;
            DrugProperties drugProperties = DrugProperties.getDrugProperties(renderEntity);

            if (drugProperties != null && drugProperties.renderer != null)
            {
                drugProperties.renderer.renderOverlaysAfterShaders(event.partialTicks, renderEntity, renderEntity.ticksExisted, event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), drugProperties);
            }
        }
    }

    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event)
    {
        IIconRegister iconRegister = event.map;
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
        {
            if (fluid instanceof FluidWithIconSymbolRegistering)
                ((FluidWithIconSymbolRegistering) fluid).registerIcons(iconRegister, event.map.getTextureType());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            PSRenderStates.update();
            Minecraft mc = Minecraft.getMinecraft();

            if (mc != null && !mc.isGamePaused())
            {
                DrugProperties drugProperties = DrugProperties.getDrugProperties(mc.renderViewEntity);

                if (drugProperties != null)
                {
                    SmoothCameraHelper.instance.update(mc.gameSettings.mouseSensitivity, DrugEffectInterpreter.getSmoothVision(drugProperties));
                }
            }
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event instanceof ConfigChangedEvent.OnConfigChangedEvent && event.modID.equals(Psychedelicraft.MODID))
        {
            PSConfig.loadConfig(event.configID);

            if (Psychedelicraft.config.hasChanged())
                Psychedelicraft.config.save();
        }
    }
}
