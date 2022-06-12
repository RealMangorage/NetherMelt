package me.mangorage.nethermelt.datageneration.localization;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class en_us extends LanguageProvider {
    public en_us(DataGenerator gen) {
        super(gen, NetherMelt.MOD_ID, LanguageType.EN_US.get());
    }

    @Override
    protected void addTranslations() {
        add(Registry.BLOCK_ROOT.get(), "Nether Foam Source");
        add(Registry.BLOCK_DEAD_ROOT.get(), "Dead Nether Foam Source");
        add(Registry.BLOCK_FOAM.get(), "Nether Foam");
        add(Registry.BLOCK_DEAD_FOAM.get(), "Dead Nether Foam");

        add(NetherMelt.CreativeTab.getDisplayName().getString(), "Nether Melt Mod");
    }
}
