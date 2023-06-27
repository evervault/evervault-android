package com.evervault.sdk.input.model

enum class CreditCardType {
    AMEX,
    DINERS_CLUB,
    DISCOVER,
    JCB,
    ELO,
    HIPER,
    HIPERCARD,
    MAESTRO,
    MASTERCARD,
    MIR,
    UNION_PAY,
    VISA,
    ;

    val brand: String get() = when(this) {
        AMEX -> "American Express"
        DINERS_CLUB -> "Diners Club"
        DISCOVER -> "Discover"
        JCB -> "JCB"
        ELO -> "Elo"
        HIPER -> "Hiper"
        HIPERCARD -> "Hipercard"
        MAESTRO -> "Maestro"
        MASTERCARD -> "Mastercard"
        MIR -> "Mir"
        UNION_PAY -> "UnionPay"
        VISA -> "Visa"
    }
}
