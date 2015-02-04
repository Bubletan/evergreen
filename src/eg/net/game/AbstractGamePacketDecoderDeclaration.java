package eg.net.game;

import eg.net.game.in.ButtonPacket;
import eg.net.game.in.ChatPacket;
import eg.net.game.in.CommandPacket;
import eg.net.game.in.MovementPacket;
import eg.net.game.in.codec.ButtonPacketDecoder;
import eg.net.game.in.codec.ChatPacketDecoder;
import eg.net.game.in.codec.CommandPacketDecoder;
import eg.net.game.in.codec.MovementPacketDecoder;

public enum AbstractGamePacketDecoderDeclaration {
	
	CHAT_PACKET_DECODER(4, ChatPacket.class, ChatPacketDecoder.class),
	AUTO_MOVEMENT_PACKET_DECODER(98, MovementPacket.class, MovementPacketDecoder.class),
	COMMAND_PACKET_DECODER(103, CommandPacket.class, CommandPacketDecoder.class),
	DEFAULT_MOVEMENT_PACKET_DECODER(164, MovementPacket.class, MovementPacketDecoder.class),
	BUTTON_PACKET_DECODER(185, ButtonPacket.class, ButtonPacketDecoder.class),
	MAP_MOVEMENT_PACKET_DECODER(248, MovementPacket.class, MovementPacketDecoder.class);
	
	private final int packetType;
	private final Class<? extends AbstractGamePacket> packetClass;
	private final Class<? extends AbstractGamePacketDecoder<?>> decoderClass;
	
	private <T extends AbstractGamePacket> AbstractGamePacketDecoderDeclaration(int packetType,
			Class<T> packetClass, Class<? extends AbstractGamePacketDecoder<T>> decoderClass) {
		this.packetType = packetType;
		this.packetClass = packetClass;
		this.decoderClass = decoderClass;
	}
	
	public int getPacketType() {
		return packetType;
	}
	
	public Class<? extends AbstractGamePacket> getPacketClass() {
		return packetClass;
	}
	
	public Class<? extends AbstractGamePacketDecoder<?>> getDecoderClass() {
		return decoderClass;
	}
}
