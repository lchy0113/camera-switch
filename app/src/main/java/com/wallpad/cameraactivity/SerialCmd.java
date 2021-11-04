package com.wallpad.cameraactivity;

public class SerialCmd {
    public static final int CMD_CALL_ON = 0x01;
    public static final int CMD_MONITORING = 0x02;
    public static final int CMD_TALK_ON = 0x04;
    public static final int CMD_CALL_OFF = 0x05;

    public static final int CMD_DOOR_OPEN = 0x11;
    public static final int CMD_FIRE_OPEN = 0x12;
    public static final int CMD_FIRE_CLOSE = 0x13;

    public static final int CMD_START_MAIN = 0x31;
    public static final int CMD_ALIVE = 0x32;
    public static final int CMD_RESET = 0x33;
    public static final int CMD_DEVICE_POLLING = 0x38;
    public static final int CMD_OUTING_STATE = 0x3A;
    public static final int CMD_DEVICE_ID_INIT = 0x3B;

    public static final int CMD_AUDIO_PATH = 0x41;
    public static final int CMD_SUB_VIDEO_PATH = 0x42;
    public static final int CMD_CAM_POWER = 0x44;
    public static final int CMD_LED = 0x47;
    public static final int CMD_DEVICE_CONTROL = 0x4C;

    public static final int CMD_VERSION = 0x57;

    public static final int CMD_LIGHT_STATUS = 0x6C;
}
