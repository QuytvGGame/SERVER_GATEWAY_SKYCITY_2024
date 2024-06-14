package com.ggame.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
/* 
 * PACKET DATA SEND AND RECEIVE FROM CLIENT TO SERVER
 * */
public class MessageOld {
    public int command;
    private ByteArrayOutputStream os = null;
    public DataOutputStream dos = null;
    private ByteArrayInputStream is = null;
    public DataInputStream dis = null;

    public MessageOld(int command) {
        this.command = command;
        os = new ByteArrayOutputStream();
        dos = new DataOutputStream(os);
    }
    public MessageOld(int command, byte[] data) {
        this.command = command;
        is = new ByteArrayInputStream(data);
        dis = new DataInputStream(is);
    }

    public String ByteArrayToString(byte[] val)
	{
		String b = "";
		int len = val.length;
		for(int i = 0; i < len; i++) {
			if(i != 0) {
				b += ",";
			}            
			b += val[i]+"";
		}
		return b;
	}
	public String toStringMsg(){
		byte[] b = toByteArray ();

		String result = ByteArrayToString(b);
		return result;
	}
	
	public byte[] toByteArray() {
    	int datalen = 0;
    	byte[] data = null;
    	byte[] bytes = null;
    	
		try {
			if (dos!=null)
			{
				dos.flush();
				data = os.toByteArray();
				datalen = data.length;
				dos.close();
			}
			ByteArrayOutputStream bos1 = new ByteArrayOutputStream(6+datalen);
			DataOutputStream dos1 = new DataOutputStream(bos1);
			dos1.writeInt(command);
			dos1.writeShort(datalen);
			if (datalen>0){
				dos1.write(data);
			}
			bytes = bos1.toByteArray();
			dos1.close();
		} catch (IOException e) {}
    	
    	return bytes;
    }

}
