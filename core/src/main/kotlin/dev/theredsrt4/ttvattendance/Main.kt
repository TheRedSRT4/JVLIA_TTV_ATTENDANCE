package dev.theredsrt4.ttvattendance

import org.jetbrains.exposed.sql.Database


fun main() {
    TtvAttendance(
        "jvlia",
        HttpApiHandler("0o2yfr1f5av3716vnnlyahb405acgd", "ip2n8hli2rmv6aar2sspkdgtt6xdnx", "ttva.secret"),
        Database.connect(
            "jdbc:postgresql://162.33.23.226/",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "Cupcake12!"
        ),
        "ttva.ftl",
        120,
        1,
        "X9TV457sqHFYHVHD9rW3HXPW"
    )
}