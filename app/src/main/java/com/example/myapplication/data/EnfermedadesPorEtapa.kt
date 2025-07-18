package com.example.myapplication.data

val enfermedadesPorEtapa = mapOf(
    "Estreptococosis" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "Pleuroneumonia" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "PRRS" to listOf(EtapaProductiva.GESTACION, EtapaProductiva.MATERNIDAD, EtapaProductiva.DESTETE),
    "Circovirus" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "Salmonelosis" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "Erisipela" to listOf(EtapaProductiva.GESTACION, EtapaProductiva.MATERNIDAD),
    "Brucelosis" to listOf(EtapaProductiva.GESTACION, EtapaProductiva.MATERNIDAD),
    "Clostridiosis" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "AFLATOXINA" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "OCRATOXINA" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "TRICOTECENOS" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "FUMONISINA" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "CAMPILOBACTERIOSIS" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "COCCIDIOSIS" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "ASCARIS SUUM" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "ESTRONGILOIDES" to listOf(EtapaProductiva.DESTETE, EtapaProductiva.ENGORDA),
    "ZEARALENONA" to listOf(EtapaProductiva.GESTACION, EtapaProductiva.MATERNIDAD)
) 