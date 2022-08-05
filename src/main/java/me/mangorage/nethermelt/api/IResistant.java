package me.mangorage.nethermelt.api;


// Implement this into your block to make it Resistant to Root's
// Meant for my mod, but can be used by any mod so my mod doesnt interfere with it!

public interface IResistant {
    default boolean isResistant() {return true;}
}
