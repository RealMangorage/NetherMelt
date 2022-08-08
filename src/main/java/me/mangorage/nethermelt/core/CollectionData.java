package me.mangorage.nethermelt.core;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CollectionData {

    private final String MODID;
    private final String ID;
    private final String Name;
    private final List<ResourceKey<Level>> dimensions;


    private CollectionData(String MODID, String ID, String Name, List<ResourceKey<Level>> dimensions) {
        this.MODID = MODID;
        this.ID = ID;
        this.Name = Name;
        this.dimensions = dimensions;
    }

    public static Builder create() {
        return new Builder();
    }

    public String getModID() {
        return MODID;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public List<ResourceKey<Level>> getDimensions() {
        return dimensions;
    }

    public boolean isModLoaded() {
        return ModList.get().isLoaded(getModID());
    }


    public static class Builder {
        private String MODID;
        private String Name;
        private List<ResourceKey<Level>> dimensions = new ArrayList<>();
        private String ID;

        public Builder setModID(String MODID) {
            this.MODID = MODID;
            return this;
        }

        public Builder setName(String Name) {
            this.Name = Name;
            return this;
        }

        public Builder setID(String ID) {
            this.ID = ID;
            return this;
        }

        public Builder setDimensions(List<ResourceKey<Level>> dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder addDimensions(ResourceKey<Level>... dimensions) {
            for (ResourceKey<Level> dimension : dimensions) {
                this.dimensions.add(dimension);
            }
            return this;
        }

        public CollectionData build() {
            Objects.requireNonNull(MODID);
            Objects.requireNonNull(ID);
            Objects.requireNonNull(Name);
            Objects.requireNonNull(dimensions);

            return new CollectionData(MODID, ID, Name, dimensions);
        }
    }



}
