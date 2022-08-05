package me.mangorage.nethermelt.datageneration.localization;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.core.Registration;
import me.mangorage.nethermelt.core.RootType;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static me.mangorage.nethermelt.core.Constants.MODID;
import static me.mangorage.nethermelt.core.Constants.Translatable.ROOT_TOOLTIP_WRONG_DIMENSION;

public class en_us extends LanguageProvider {
    public en_us(DataGenerator gen) {
        super(gen, MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(RootType.NETHER.getLiveVariantBlock(), "Nether Foam Source");
        add(Registration.BLOCK_DEAD_ROOT.get(), "Dead Nether Foam Source");
        add(Registration.BLOCK_FOAM.get(), "Nether Foam");
        add(Registration.BLOCK_DEAD_FOAM.get(), "Dead Nether Foam");

        add(NetherMelt.CreativeTab.getDisplayName().getString(), "Nether Melt Mod");

        add(ROOT_TOOLTIP_WRONG_DIMENSION.getKey(), "%s Can only be activated in the %s!");
    }
}
