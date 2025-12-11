private val crcTable: IntArray = run {
    val table = IntArray(256)
    for (i in 0 until 256) {
        var c: Int = i
        for (x in 0 until 8) {
            if (c and 1 != 0) {
                c = (0xedb88320 xor ((c ushr 1).toLong())).toInt()
            } else {
                c = c ushr 1
            }
        }
        table[i] = c
    }
    table
}

internal fun crc32(buffer: ByteArray): Int {
    var crc: Int = 0xffffffff.toInt()
    val len = buffer.size

    for (i in 0 until len) {
        crc = crcTable[(crc xor buffer[i].toInt()) and 0xff] xor (crc ushr 8)
    }

    return crc xor 0xffffffff.toInt()
}
