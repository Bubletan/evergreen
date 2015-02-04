package eg.net.game;

import eg.net.game.out.InterfaceTextPacket;
import eg.net.game.out.LoadMapPacket;
import eg.net.game.out.MainInterfacePacket;
import eg.net.game.out.MessagePacket;
import eg.net.game.out.PlayerSyncPacket;
import eg.net.game.out.SidebarInterfacePacket;
import eg.net.game.out.SystemUpdatePacket;
import eg.net.game.out.codec.InterfaceTextPacketEncoder;
import eg.net.game.out.codec.LoadMapPacketEncoder;
import eg.net.game.out.codec.MainInterfacePacketEncoder;
import eg.net.game.out.codec.MessagePacketEncoder;
import eg.net.game.out.codec.PlayerSyncPacketEncoder;
import eg.net.game.out.codec.SidebarInterfacePacketEncoder;
import eg.net.game.out.codec.SystemUpdatePacketEncoder;

public enum AbstractGamePacketEncoderDeclaration {
	
	SIDEBAR_INTERFACE_PACKET_ENCODER(71, SidebarInterfacePacket.class, SidebarInterfacePacketEncoder.class),
	LOAD_MAP_PACKET_ENCODER(73, LoadMapPacket.class, LoadMapPacketEncoder.class),
	PLAYER_SYNC_PACKET_ENCODER(81, PlayerSyncPacket.class, PlayerSyncPacketEncoder.class),
	MAIN_INTERFACE_PACKET_ENCODER(97, MainInterfacePacket.class, MainInterfacePacketEncoder.class),
	SYSTEM_UPDATE_PACKET_ENCODER(114, SystemUpdatePacket.class, SystemUpdatePacketEncoder.class),
	INTERFACE_TEXT_PACKET_ENCODER(126, InterfaceTextPacket.class, InterfaceTextPacketEncoder.class),
	MESSAGE_PACKET_ENCODER(253, MessagePacket.class, MessagePacketEncoder.class);
	
	private final int packetType;
	private final Class<? extends AbstractGamePacket> packetClass;
	private final Class<? extends AbstractGamePacketEncoder<?>> encoderClass;
	
	private <T extends AbstractGamePacket> AbstractGamePacketEncoderDeclaration(int packetType,
			Class<T> packetClass, Class<? extends AbstractGamePacketEncoder<T>> encoderClass) {
		this.packetType = packetType;
		this.packetClass = packetClass;
		this.encoderClass = encoderClass;
	}
	
	public int getPacketType() {
		return packetType;
	}
	
	public Class<? extends AbstractGamePacket> getPacketClass() {
		return packetClass;
	}
	
	public Class<? extends AbstractGamePacketEncoder<?>> getEncoderClass() {
		return encoderClass;
	}
}
