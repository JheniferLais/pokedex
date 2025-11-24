package br.com.pokedex.data.local.model

enum class Generation(val title: String, val range: IntRange) {
    GEN_I("Gen I", 1..151),
    GEN_II("Gen II", 152..251),
    GEN_III("Gen III", 252..386),
    GEN_IV("Gen IV", 387..493),
    GEN_V("Gen V", 494..649),
    GEN_VI("Gen VI", 650..721),
    GEN_VII("Gen VII", 722..809),
    GEN_VIII("Gen VIII", 810..905),
    GEN_IX("Gen IX", 906..1025)
}