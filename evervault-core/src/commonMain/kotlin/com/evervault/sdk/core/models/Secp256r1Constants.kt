package com.evervault.sdk.core.models

data class Secp256r1Constants(
    val p: String = "FF FF FF FF 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF",
    val a: String = "FF FF FF FF 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FC",
    val b: String = "5A C6 35 D8 AA 3A 93 E7 B3 EB BD 55 76 98 86 BC 65 1D 06 B0 CC 53 B0 F6 3B CE 3C 3E 27 D2 60 4B",
    val seed: String = "C4 9D 36 08 86 E7 04 93 6A 66 78 E1 13 9D 26 B7 81 9F 7E 90",
    val generator: String =
        "04 6B 17 D1 F2 E1 2C 42 47 F8 BC E6 E5 63 A4 40 F2 77 03 7D 81 2D EB 33 A0 F4 A1 39 45 D8 98 C2 96 4F E3 42 E2 FE 1A 7F 9B 8E E7 EB 4A 7C 0F 9E 16 2B CE 33 57 6B 31 5E CE CB B6 40 68 37 BF 51 F5",
    val n: String = "FF FF FF FF 00 00 00 00 FF FF FF FF FF FF FF FF BC E6 FA AD A7 17 9E 84 F3 B9 CA C2 FC 63 25 51",
    val h: String = "01"
)
