package me.mangorage.nethermelt.datageneration.localization;

public enum LanguageType {
    EN_US("en_us");

    private String value;

    LanguageType(String language) {
        this.value = language;
    }

    public String get() {
        return value;
    }

}
