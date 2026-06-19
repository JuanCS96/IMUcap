package com.biomechApp.imuCap.utils

class Names {

    fun joints(): List<String> {
        return listOf(
            "Neck",
            "Lumbar",
            "Right Hip",
            "Left Hip",
            "Right Knee",
            "Left Knee",
            "Right Ankle",
            "Left Ankle",
            "Right Shoulder",
            "Left Shoulder",
            "Right Elbow",
            "Left Elbow",
            "Right Wrist",
            "Left Wrist"
        )
    }

    fun jointsDoF(): List<List<String>> {
        return listOf(
            listOf("Neck Lat. Bend. (R)", "Neck Rot. (R)", "Neck Flex. (R)"),
            listOf("Lumbar Lat. Bend. (R)", "Lumbar Rot. (R)", "Lumbar Flex. (R)"),
            listOf("Hip Abd. (R)", "Hip Rot. (R)", "Hip Flex. (R)"),
            listOf("Hip Abd. (L)", "Hip Rot. (L)", "Hip Flex. (L)"),
            listOf("Knee Abd. (R)", "Knee Rot. (R)", "Knee Flex. (R)"),
            listOf("Knee Abd. (L)", "Knee Rot. (L)", "Knee Flex. (L)"),
            listOf("Ankle Abd. (R)", "Ankle Rot. (R)", "Ankle Flex. (R)"),
            listOf("Ankle Abd. (L)", "Ankle Rot. (L)", "Ankle Flex. (L)"),
            listOf("Shoulder Abd. (R)", "Shoulder Rot. (R)", "Shoulder Flex. (R)"),
            listOf("Shoulder Abd. (L)", "Shoulder Rot. (L)", "Shoulder Flex. (L)"),
            listOf("Elbow Dev. (R)", "Elbow Pron. (R)", "Elbow Flex. (R)"),
            listOf("Elbow Dev. (L)", "Elbow Pron. (L)", "Elbow Flex. (L)"),
            listOf("Wrist Dev. (R)", "Wrist Pron. (R)", "Wrist Flex. (R)"),
            listOf("Wrist Dev. (L)", "Wrist Pron. (L)", "Wrist Flex. (L)")
        )
    }

    fun segments(): List<String> {
        return listOf(
            "Pelvis",
            "Torso",
            "Head",
            "Right Thigh",
            "Left Thigh",
            "Right Shank",
            "Left Shank",
            "Right Foot",
            "Left Foot",
            "Right Arm",
            "Left Arm",
            "Right Forearm",
            "Left Forearm",
            "Right Hand",
            "Left Hand"
        )
    }

    fun segmentsDoF(): List<List<String>> {
        return listOf(
            listOf("Pelvis X", "Pelvis Y", "Pelvis Z"),
            listOf("Torso X", "Torso Y", "Torso Z"),
            listOf("Head X", "Head Y", "Head Z"),
            listOf("Thigh X (R)", "Thigh Y (R)", "Thigh Z (R)"),
            listOf("Thigh X (L)", "Thigh Y (L)", "Thigh Z (L)"),
            listOf("Shank X (R)", "Shank Y (R)", "Shank Z (R)"),
            listOf("Shank X (L)", "Shank Y (L)", "Shank Z (L)"),
            listOf("Foot X (R)", "Foot Y (R)", "Foot Z (R)"),
            listOf("Foot X (L)", "Foot Y (L)", "Foot Z (L)"),
            listOf("Arm X (R)", "Arm Y (R)", "Arm Z (R)"),
            listOf("Arm X (L)", "Arm Y (L)", "Arm Z (L)"),
            listOf("Forearm X (R)", "Forearm Y (R)", "Forearm Z (R)"),
            listOf("Forearm X (L)", "Forearm Y (L)", "Forearm Z (L)"),
            listOf("Hand X (R)", "Hand Y (R)", "Hand Z (R)"),
            listOf("Hand X (L)", "Hand Y (L)", "Hand Z (L)")
        )
    }
}