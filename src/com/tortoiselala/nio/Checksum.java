package com.tortoiselala.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Checksum {
    public static int sum(File fp) throws IOException {

        // Open file and get channel from the stream
        FileInputStream fis = new FileInputStream(fp);
        FileChannel fc = fis.getChannel();

        // Get the file's size and then map it into memory
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

        // Compute checksum
        int sum = 0;
        while(bb.hasRemaining()){
            if((sum & 1) != 0){
                sum = (sum >> 1) + 0x8000;
            }
            else{
                sum >>= 1;
            }
            sum += bb.get() & 0xff;
            sum &= 0xffff;
        }

        fc.close();
        return sum;
    }
}
