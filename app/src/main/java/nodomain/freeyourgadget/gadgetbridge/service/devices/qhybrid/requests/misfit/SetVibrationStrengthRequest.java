package nodomain.freeyourgadget.gadgetbridge.service.devices.qhybrid.requests.misfit;


import java.nio.ByteBuffer;

import nodomain.freeyourgadget.gadgetbridge.service.devices.qhybrid.requests.Request;

public class SetVibrationStrengthRequest extends Request {
    public SetVibrationStrengthRequest(short strength){
        super();
        init(strength);
    }

    private void init(int strength){
        ByteBuffer buffer = createBuffer();
        buffer.put((byte)strength);
        this.data = buffer.array();
    }

    @Override
    public int getPayloadLength() {
        return 4;
    }

    @Override
    public byte[] getStartSequence() {
        return new byte[]{2, 15, 8};
    }
}
