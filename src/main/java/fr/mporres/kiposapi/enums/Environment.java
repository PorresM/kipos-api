package fr.mporres.kiposapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Environment {
    DEVELOPMENT("dev"),
    INTEGRATION("int"),
    RECETTE("rec"),
    PREPRODUCTION("preprod"),
    PRODUCTION("prod");

    @Getter
    private String shortName;
}
