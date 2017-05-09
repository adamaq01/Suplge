package fr.adamaq01.suplgetest;

/**
 * Created by Adamaq01 on 08/05/2017.
 */
public class FramePacket {

    private byte[] frame;
    private int frameSize;
    private int framePerSeconds;

    public FramePacket() {
        this.frame = new byte[0];
        this.frameSize = 0;
        this.framePerSeconds = 0;
    }

    public FramePacket(byte[] frame, int frameSize, int framePerSeconds) {
        this.frame = frame;
        this.frameSize = frameSize;
        this.framePerSeconds = framePerSeconds;
    }

    public byte[] getFrame() {
        return frame;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public int getFramePerSeconds() {
        return framePerSeconds;
    }
}
