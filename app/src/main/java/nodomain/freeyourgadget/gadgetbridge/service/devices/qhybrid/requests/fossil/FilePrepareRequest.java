package nodomain.freeyourgadget.gadgetbridge.service.devices.qhybrid.requests.fossil;

import android.bluetooth.BluetoothGattCharacteristic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import nodomain.freeyourgadget.gadgetbridge.service.devices.qhybrid.requests.Request;

public class FilePrepareRequest extends Request {
    private boolean isFinished = false;
    private short handle;

    public FilePrepareRequest(short fileHandle) {
        this.handle = fileHandle;
        ByteBuffer buffer = this.createBuffer();
        buffer.putShort(fileHandle);

        this.data = buffer.array();
    }

    @Override
    public void handleResponse(BluetoothGattCharacteristic characteristic) {
        super.handleResponse(characteristic);

        if(!characteristic.getUuid().toString().equals(this.getRequestUUID().toString())){
            throw new RuntimeException("wrong response UUID");
        }

        byte[] value = characteristic.getValue();

        byte type = (byte)(value[0] & 0x0F);

        if(type != 9) throw new RuntimeException("wrong response type");

        if(value.length != 4) throw new RuntimeException("wrong response length");

        ByteBuffer buffer = ByteBuffer.wrap(value);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if(this.handle != buffer.getShort(1)) throw new RuntimeException("wrong response handle");

        byte status = buffer.get(3);

        if(status != 0) throw new RuntimeException("wrong response status");

        this.isFinished = true;

        this.onPrepare();
    }

    public void onPrepare(){}

    @Override
    public byte[] getStartSequence() {
        return new byte[]{9};
    }

    @Override
    public int getPayloadLength() {
        return 3;
    }

    public boolean isFinished(){
        return this.isFinished;
    }

    @Override
    public UUID getRequestUUID() {
        return UUID.fromString("3dda0003-957f-7d4a-34a6-74696673696d");
    }
}
