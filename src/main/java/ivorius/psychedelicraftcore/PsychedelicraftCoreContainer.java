/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Created by lukas on 25.02.14.
 */
public class PsychedelicraftCoreContainer extends DummyModContainer
{
    public static Logger logger = LogManager.getLogger("psychedelicraftcore");

    public PsychedelicraftCoreContainer()
    {
        super(new ModMetadata());

        ModMetadata myMeta = super.getMetadata();
        myMeta.authorList = Arrays.asList("Angelic Industries", "Ivorius");
        myMeta.description = "CoreMod for Psychedelicraft. Required API Psychedelicraft is built on top of. ";
        myMeta.modId = PsychedelicraftLoadingPlugin.MODID;
        myMeta.version = ivorius.psychedelicraft.Tags.VERSION;
        myMeta.name = PsychedelicraftLoadingPlugin.NAME;
        myMeta.url = "https://github.com/AngelicIndustries/Psychedelicraft-Legacy";
        myMeta.requiredMods.add(new DefaultArtifactVersion("ivtoolkit", true));
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
}
