package ivorius.psychedelicraft.entities.drugs;

import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class DrugAttributeModifierTest {
    @Test
    public void updatesOnlyRequestedAttributeAndPreservesOtherModifiers() {
        ServersideAttributeMap attributes = new ServersideAttributeMap();
        IAttribute speed = new RangedAttribute("test.speed", 10.0, 0.0, 100.0);
        IAttribute health = new RangedAttribute("test.health", 20.0, 0.0, 100.0);
        IAttributeInstance speedInstance = attributes.registerAttribute(speed);
        IAttributeInstance healthInstance = attributes.registerAttribute(health);
        AttributeModifier otherMod = new AttributeModifier(UUID.randomUUID(), "Other mod", 4.0, 0);
        healthInstance.applyModifier(otherMod);

        DrugProperties.changeDrugModifier(attributes, health, 0.5, 2);
        assertEquals(36.0, healthInstance.getAttributeValue(), 0.00001);
        assertEquals(10.0, speedInstance.getAttributeValue(), 0.00001);

        DrugProperties.changeDrugModifier(attributes, health, 0.0, 2);
        assertEquals(24.0, healthInstance.getAttributeValue(), 0.00001);
        assertSame(otherMod, healthInstance.getModifier(otherMod.getID()));
        assertNull(healthInstance.getModifier(DrugProperties.drugUUID));
    }

    @Test
    public void unchangedTicksReuseModifierButOperationChangesTakeEffect() {
        ServersideAttributeMap attributes = new ServersideAttributeMap();
        IAttribute attribute = new RangedAttribute("test.value", 10.0, 0.0, 100.0);
        IAttributeInstance instance = attributes.registerAttribute(attribute);

        DrugProperties.changeDrugModifier(attributes, attribute, 0.5, 2);
        AttributeModifier first = instance.getModifier(DrugProperties.drugUUID);
        DrugProperties.changeDrugModifier(attributes, attribute, 0.5, 2);
        assertSame(first, instance.getModifier(DrugProperties.drugUUID));

        DrugProperties.changeDrugModifier(attributes, attribute, 0.5, 0);
        assertEquals(10.5, instance.getAttributeValue(), 0.00001);
        assertNotSame(first, instance.getModifier(DrugProperties.drugUUID));
    }

    @Test
    public void missingAttributesAreIgnored() {
        ServersideAttributeMap attributes = new ServersideAttributeMap();
        IAttribute absent = new RangedAttribute("test.absent", 10.0, 0.0, 100.0);
        DrugProperties.changeDrugModifier(attributes, absent, 0.5, 2);
        assertNull(attributes.getAttributeInstance(absent));
    }
}
