//THIS IS NOW OBSOLETE
//IT STILL FUNCTIONS
//BUT IT IS NO LONGER USED IN FINAL PRODUCTION
//THIS IS NOW OBSOLETE
//IT STILL FUNCTIONS
//BUT IT IS NO LONGER USED IN FINAL PRODUCTION
//THIS IS NOW OBSOLETE
//IT STILL FUNCTIONS
//BUT IT IS NO LONGER USED IN FINAL PRODUCTION
//THIS IS NOW OBSOLETE
//IT STILL FUNCTIONS
//BUT IT IS NO LONGER USED IN FINAL PRODUCTION
package dev.theredsrt4.ttvattendance.stats

import dev.theredsrt4.ttvattendance.AttendanceTable
import dev.theredsrt4.ttvattendance.StreamTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


// logs into sql server and generates stats for ttvattendance
// setup for postgres, but is easily changed. see jetbrains exposed documentation
fun main(args: Array<String>) {
    // set logger to only warnings
    System.setProperty("org.slf4j.simplelogger.defaultLogLevel", "WARNING")

    if(args.size < 4 || args.size > 5) {
        println("Missing the correct arguments!\nUsage: java -jar stats.jar [streamer] [url] [user] [password] [range (optional)]")
        return
    }

    // data we are collecting
    var streamsLength = 0 // number of streams
    var streams: ArrayList<Int>? = null // ids of streams in range, null if no range
    var total = 0 // total number of viewers
    var active = 0 // total number of viewers who have shown up to half of the streams
    var dedicated = 0 // total number of viewers who have came to each of the streams
    var time = ""

    // connect to database
    val database = Database.connect(args[1], driver = "org.postgresql.Driver", user = args[2], password = args[3])

    // get tables
    val streamTable = StreamTable(args[0])
    val chartTable = AttendanceTable(args[0])

    // get total streams in range
    val ranged = args.size == 5 && args[4].contains("-")
    if(ranged) streams = arrayListOf()
    val min: Int = if(ranged) args[4].split("-")[0].toInt() else 0
    val max: Int = if(ranged) args[4].split("-")[1].toInt() else Int.MAX_VALUE
    transaction(database) {
        streamTable.selectAll().forEach {
            val id = it[streamTable.date]
            if(id in min..max) {
                if(ranged) streams?.add(id)
                streamsLength++
            }
        }
    }

    // get total number of viewers
    transaction(database) {
        chartTable.selectAllBatched().forEach { batch -> batch.forEach {
            if(!ranged) {
                // check all
                val num = it[chartTable.streams].size
                if(num == streamsLength) dedicated++
                else if(num > streamsLength / 2) active++
                total++
            } else {
                // check ranged
                val attended = it[chartTable.streams]
                var num = 0
                attended.forEach { stream -> if(streams!!.contains(stream)) num++ }
                if(num == streamsLength) dedicated++
                else if(num > streamsLength / 2) active++
                if(num > 0) total++
            }
        }}
    }

    transaction(database) {
        val timeSum = chartTable
            .slice(chartTable.time.sum())
            .selectAll()
            .first()[chartTable.time.sum()]
        time = timeSum.toString()
        }
    // remove streamer from data, a bit hacky
    total--
    dedicated--

    // print data
    if(ranged) println("Showing statistics from ${args[0]} streams in range ${args[4]}...")
    else println("Showing statistics from all ${args[0]} streams...")
    println("Number of streams: $streamsLength")
    println("Number of unique viewers: $total")
    println("Number of active viewers(>50%): $active")
    println("Number of dedicated viewers(100%): $dedicated")
    println("Number of minutes watched: $time")
}
