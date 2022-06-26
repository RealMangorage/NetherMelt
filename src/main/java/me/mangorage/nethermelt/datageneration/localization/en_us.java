package me.mangorage.nethermelt.datageneration.localization;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.setup.Registry;
import me.mangorage.nethermelt.util.Translatable;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class en_us extends LanguageProvider {
    public en_us(DataGenerator gen) {
        super(gen, NetherMelt.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Registry.BLOCK_ROOT.get(), "Nether Foam Source");
        add(Registry.BLOCK_DEAD_ROOT.get(), "Dead Nether Foam Source");
        add(Registry.BLOCK_FOAM.get(), "Nether Foam");
        add(Registry.BLOCK_DEAD_FOAM.get(), "Dead Nether Foam");

        add(NetherMelt.CreativeTab.getDisplayName().getString(), "Nether Melt Mod");

        add(Translatable.ROOT_TOOLTIP_WRONG_DIMENSION.getKey(), "Nether Foam Source Can only be activated in the Nether!");
    }
}
