package eg.net.game;

import eg.net.game.out.CameraResetPacket;
import eg.net.game.out.DialogueInterfacePacket;
import eg.net.game.out.GameMessagePacket;
import eg.net.game.out.InterfaceTextPacket;
import eg.net.game.out.LogoutPacket;
import eg.net.game.out.MainInterfacePacket;
import eg.net.game.out.MapLoadingPacket;
import eg.net.game.out.MulticombatOverlayPacket;
import eg.net.game.out.NpcSyncPacket;
import eg.net.game.out.PlayerInitPacket;
import eg.net.game.out.PlayerSyncPacket;
import eg.net.game.out.SidebarInterfacePacket;
import eg.net.game.out.SystemUpdatePacket;
import eg.net.game.out.WeightUpdatePacket;
import eg.net.game.out.codec.CameraResetPacketEncoder;
import eg.net.game.out.codec.DialogueInterfacePacketEncoder;
import eg.net.game.out.codec.GameMessagePacketEncoder;
import eg.net.game.out.codec.InterfaceTextPacketEncoder;
import eg.net.game.out.codec.LogoutPacketEncoder;
import eg.net.game.out.codec.MainInterfacePacketEncoder;
import eg.net.game.out.codec.MapLoadingPacketEncoder;
import eg.net.game.out.codec.MulticombatOverlayPacketEncoder;
import eg.net.game.out.codec.NpcSyncPacketEncoder;
import eg.net.game.out.codec.PlayerInitPacketEncoder;
import eg.net.game.out.codec.PlayerSyncPacketEncoder;
import eg.net.game.out.codec.SidebarInterfacePacketEncoder;
import eg.net.game.out.codec.SystemUpdatePacketEncoder;
import eg.net.game.out.codec.WeightUpdatePacketEncoder;

public enum OutboundAbstractGamePacketDeclaration {
	
	MULTICOMBAT_OVERLAY_PACKET	(61,	MulticombatOverlayPacket.class,	MulticombatOverlayPacketEncoder.class),
	NPC_SYNC_PACKET				(65,	NpcSyncPacket.class,			NpcSyncPacketEncoder.class),
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
