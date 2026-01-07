package com.evervault.sdk.core.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class HexHandlerTest {
    @Test
    fun testHexDecode() {
        val string = "3082014b3082010306072a8648ce3d02013081f7020101302c06072a8648ce3d0101022100ffffffff00000001000000000000000000000000ffffffffffffffffffffffff305b0420ffffffff00000001000000000000000000000000fffffffffffffffffffffffc04205ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b031500c49d360886e704936a6678e1139d26b7819f7e900441046b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c2964fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5022100ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc63255102010103420004ecca800d6b488b64b03bf304b1216aa5064010ab47361c3774ed911c4c24fc9cd41e6c0a6f2b57cb6334f500d19fa5bbe1f35feb27785510e398055939bc7fe1"
        val result = HexHandler.decode(string)
        assertEquals(335, result.size)
        assertEquals(48, result[0])
    }
}
