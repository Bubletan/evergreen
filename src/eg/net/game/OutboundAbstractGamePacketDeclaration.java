package eg.net.game;

import eg.net.game.out.*;
import eg.net.game.out.codec.*;

public enum OutboundAbstractGamePacketDeclaration {
	
	MULTICOMBAT_OVERLAY_PACKET	(61,	MulticombatOverlayPacket.class,	MulticombatOverlayPacketEncoder.class),
	SIDEBAR_INTERFACE_PACKET	(71,	SidebarInterfacePacket.class,	SidebarInterfacePacketEncoder.class),
	MAP_LOADING_PACKET			(73,	MapLoadingPacket.class,			MapLoadingPacketEncoder.class),
	PLAYER_SYNC_PACKET			(81,	PlayerSyncPacket.class,			PlayerSyncPacketEncoder.class),
	MAIN_INTERFACE_PACKET		(97,	MainInterfacePacket.class,		MainInterfacePacketEncoder.class),
	CAMERA_RESET_PACKET			(107,	CameraResetPacket.class,		CameraResetPacketEncoder.class),
	LOGOUT_PACKET				(109,	LogoutPacket.class,				LogoutPacketEncoder.class),
	SYSTEM_UPDATE_PACKET		(114,	SystemUpdatePacket.class,		SystemUpdatePacketEncoder.class),
	INTERFACE_TEXT_PACKET		(126,	InterfaceTextPacket.class,		InterfaceTextPacketEncoder.class),
	DIALOGUE_INTERFACE_PACKET	(164,	DialogueInterfacePacket.class,	DialogueInterfacePacketEncoder.class),
	WEIGHT_UPDATE_PACKET		(240,	WeightUpdatePacket.class,		WeightUpdatePacketEncoder.class),
	PLAYER_INIT_PACKET			(249,	PlayerInitPacket.class,			PlayerInitPacketEncoder.class),
	GAME_MESSAGE_PACKET			(253,	GameMessagePacket.class,		GameMessagePacketEncoder.class);
	
	private final int packetType;
	private final Class<? extends AbstractGamePacket> packetClass;
	private final Class<? extends AbstractGamePacketEncoder<?>> encoderClass;
	
	private <T extends AbstractGamePacket> OutboundAbstractGamePacketDeclaration(int packetType,
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
