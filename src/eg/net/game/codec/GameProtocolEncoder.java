package eg.net.game.codec;

import eg.net.game.GamePacket;
import eg.net.game.GamePacketConstants;
import eg.util.io.IsaacCipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class GameProtocolEncoder extends MessageToByteEncoder<GamePacket> {
	
	private final IsaacCipher cipher;
	
	public GameProtocolEncoder(IsaacCipher cipher) {
		this.cipher = cipher;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, GamePacket in, ByteBuf out) throws Exception {
		int size = GamePacketConstants.OUTGOING_PACKET_SIZE[in.getType()];
		if (size >= 0 && size != in.getSize()) {
			System.err.println("Missmatch in outbound packet size: " + in.getType());
			return;
		}
		out.writeByte(in.getType() + cipher.nextInt());
		if (size == -1) {
			out.writeByte(in.getSize());
		} else if (size == -2) {
			out.writeByte(in.getSize() >> 8);
			out.writeByte(in.getSize());
		}
		if (in.getSize() != 0) {
			out.writeBytes(in.getData(), 0, in.getSize());
		}
	}
}
