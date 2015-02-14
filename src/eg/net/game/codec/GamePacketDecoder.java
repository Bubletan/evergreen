package eg.net.game.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.AbstractGamePacketDecoderDeclaration;
import eg.net.game.GamePacket;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

@Sharable
public final class GamePacketDecoder extends MessageToMessageDecoder<GamePacket> {
	
	private static final AbstractGamePacketDecoder<?>[] DECODERS =
			new AbstractGamePacketDecoder<?>[256];
	
	static {
		int successCount = 0, totalCount = 0;
		Map<Class<? extends AbstractGamePacketDecoder<?>>, AbstractGamePacketDecoder<?>> pool =
				new HashMap<>();
		for (AbstractGamePacketDecoderDeclaration dec :
			AbstractGamePacketDecoderDeclaration.values()) {
			try {
				AbstractGamePacketDecoder<?> decoder = pool.get(dec.getDecoderClass());
				if (decoder == null) {
					decoder = dec.getDecoderClass().newInstance();
					pool.put(dec.getDecoderClass(), decoder);
				}
				DECODERS[dec.getPacketType()] = decoder;
				successCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			totalCount++;
		}
		(successCount == totalCount ? System.out : System.err)
				.println("Registered succesfully " + successCount +
				" out of " + totalCount + " packet decoders.");
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, GamePacket in,
			List<Object> out) throws Exception {
		AbstractGamePacketDecoder<?> decoder = DECODERS[in.getType()];
		if (decoder != null) {
			try {
				out.add(decoder.decode(in));
			} catch (Exception e) {
				System.err.println("Error decoding packet: " + in.getType());
			}
		} else {
			System.err.println("Unsupported inbound packet type: " + in.getType());
		}
	}
}
