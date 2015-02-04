package eg.net.game.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eg.net.game.AbstractGamePacket;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.AbstractGamePacketEncoderDeclaration;
import eg.net.game.GamePacket;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

@Sharable
public final class GamePacketEncoder extends MessageToMessageEncoder<AbstractGamePacket> {
	
	private static final Map<Class<? extends AbstractGamePacket>,
			AbstractGamePacketEncoder<?>> ENCODERS = new HashMap<>(256);
	
	static {
		int successCount = 0, totalCount = 0;
		for (AbstractGamePacketEncoderDeclaration dec :
				AbstractGamePacketEncoderDeclaration.values()) {
			try {
				ENCODERS.put(dec.getPacketClass(), dec.getEncoderClass().newInstance());
				successCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			totalCount++;
		}
		(successCount == totalCount ? System.out : System.err)
				.println("Registered succesfully " + successCount +
				" out of " + totalCount + " packet encoders.");
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, AbstractGamePacket in,
			List<Object> out) throws Exception {
		if (in instanceof GamePacket) {
			out.add(in);
		} else {
			encode(in.getClass(), in, out);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends AbstractGamePacket> void encode(Class<T> type,
			AbstractGamePacket in, List<Object> out) {
		AbstractGamePacketEncoder<T> encoder = (AbstractGamePacketEncoder<T>) ENCODERS.get(type);
		if (encoder != null) {
			try {
				out.add(encoder.encode((T) in));
			} catch (Exception e) {
				// Packet could not be encoded.
			}
		}
	}
}
