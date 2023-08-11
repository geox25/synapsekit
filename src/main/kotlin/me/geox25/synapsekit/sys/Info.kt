package me.geox25.synapsekit.sys

import oshi.SystemInfo
import oshi.hardware.CentralProcessor
import oshi.hardware.ComputerSystem
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem
import java.security.MessageDigest

val sysInfo = SystemInfo()
val os: OperatingSystem = sysInfo.operatingSystem
val hwAbsLayer: HardwareAbstractionLayer = sysInfo.hardware
val cenProc: CentralProcessor = hwAbsLayer.processor
val compSys: ComputerSystem = hwAbsLayer.computerSystem

fun String.hashSHA256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

fun generateRawHWID() : String {
    // System Values (as String)
    val vendor: String = os.manufacturer
    val procSerialNum: String = compSys.serialNumber
    val procID: String = cenProc.processorIdentifier.identifier.replace(" ", "")
    val procs: String = cenProc.logicalProcessorCount.toString()

    val componentIdentifier = "##HWID:"
    val delimeter = "#"

    return componentIdentifier + arrayOf(vendor, procSerialNum, procID, procs).joinToString("") { "${delimeter}${it}" }
}

fun generateSecureHWID() : String {
    return generateRawHWID().hashSHA256()
}