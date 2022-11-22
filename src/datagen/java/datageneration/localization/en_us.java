package datageneration.localization;

import me.mangorage.nethermelt.NetherMeltCore;
import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static me.mangorage.nethermelt.common.core.Constants.MODID;
import static me.mangorage.nethermelt.common.core.Constants.Translatable.ROOT_TOOLTIP_WRONG_DIMENSION;

// TODO: FINISH en_us LanguageProvider
public class en_us extends LanguageProvider {
    public en_us(DataGenerator gen) {
        super(gen, MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        add(Registration.NETHER.BLOCK_ROOT.get(), "Nether Foam Source");
        add(Registration.NETHER.BLOCK_DEAD_ROOT.get(), "Dead Nether Foam Source");
        add(Registration.NETHER.BLOCK_FOAM.get(), "Nether Foam");
        add(Registration.NETHER.BLOCK_DEAD_FOAM.get(), "Dead Nether Foam");

        add(NetherMelt.CreativeTab.getDisplayName().getString(), "Nether Melt Mod");

        add(ROOT_TOOLTIP_WRONG_DIMENSION.getKey(), "%s Can only be activated in the %s!");
    }
}
