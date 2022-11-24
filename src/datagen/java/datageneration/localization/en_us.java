package datageneration.localization;

import me.mangorage.nethermelt.NetherMeltCore;
import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static me.mangorage.nethermelt.common.core.Constants.MOD_ID;
import static me.mangorage.nethermelt.common.core.Constants.Translatable.ROOT_TOOLTIP_WRONG_DIMENSION;

// TODO: FINISH en_us LanguageProvider
public class en_us extends LanguageProvider {
    public en_us(DataGenerator gen) {
        super(gen, MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        add(Registration.NETHER.BLOCK_ROOT.get(), "Nether Foam Source");
        add(Registration.NETHER.BLOCK_DEAD_ROOT.get(), "Dead Nether Foam Source");
        add(Registration.NETHER.BLOCK_FOAM.get(), "Nether Foam");
        add(Registration.NETHER.BLOCK_DEAD_FOAM.get(), "Dead Nether Foam");

        add(NetherMeltCore.CreativeTab.getDisplayName().getString(), "Nether Melt Mod");

        add(ROOT_TOOLTIP_WRONG_DIMENSION.getKey(), "%s Can only be activated in the %s!");

        add(Registration.SLUDGE_BUCKET_ITEM.get(), "Bucket of Sludge");
        add(Registration.SLUDGE_BLOCK.get(), "sludge Source");
        add(Registration.SLUDGE_TYPE.get().getDescriptionId(), "Sludge");
    }
}
