package eg.net.game;

import eg.net.game.in.ButtonPacket;
import eg.net.game.in.CameraAlteredPacket;
import eg.net.game.in.ChatMessagePacket;
import eg.net.game.in.CommandPacket;
import eg.net.game.in.KeepalivePacket;
import eg.net.game.in.MapLoadedPacket;
import eg.net.game.in.MouseClickPacket;
import eg.net.game.in.MovementPacket;
import eg.net.game.in.WindowFocusAlteredPacket;
import eg.net.game.in.codec.ButtonPacketDecoder;
import eg.net.game.in.codec.CameraAlteredPacketDecoder;
import eg.net.game.in.codec.ChatMessagePacketDecoder;
import eg.net.game.in.codec.CommandPacketDecoder;
import eg.net.game.in.codec.KeepalivePacketDecoder;
import eg.net.game.in.codec.MapLoadedPacketDecoder;
import eg.net.game.in.codec.MouseClickPacketDecoder;
import eg.net.game.in.codec.MovementPacketDecoder;
import eg.net.game.in.codec.WindowFocusAlteredPacketDecoder;

public enum InboundAbstractGamePacketDeclaration {
    
    KEEPALIVE_PACKET(0, KeepalivePacket.class, KeepalivePacketDecoder.class),
    WINDOW_FOCUS_ALTERED(3, WindowFocusAlteredPacket.class, WindowFocusAlteredPacketDecoder.class),
    CHAT_MESSAGE_PACKET(4, ChatMessagePacket.class, ChatMessagePacketDecoder.class),
    CAMERA_ALTERED_PACKET(86, CameraAlteredPacket.class, CameraAlteredPacketDecoder.class),
    AUTO_MOVEMENT_PACKET(98, MovementPacket.class, MovementPacketDecoder.class),
    COMMAND_PACKET(103, CommandPacket.class, CommandPacketDecoder.class),
    MAP_LOADED_PACKET(121, MapLoadedPacket.class, MapLoadedPacketDecoder.class),
    DEFAULT_MOVEMENT_PACKET(164, MovementPacket.class, MovementPacketDecoder.class),
    BUTTON_PACKET(185, ButtonPacket.class, ButtonPacketDecoder.class),
    MOUSE_CLICK_PACKET(241, MouseClickPacket.class, MouseClickPacketDecoder.class),
    MAP_MOVEMENT_PACKET(248, MovementPacket.class, MovementPacketDecoder.class);
    
    private final int packetType;
    private final Class<? extends AbstractGamePacket> packetClass;
    private final Class<? extends AbstractGamePacketDecoder<?>> decoderClass;
    
    private <T extends AbstractGamePacket> InboundAbstractGamePacketDeclaration(
            int packetType, Class<T> packetClass,
            Class<? extends AbstractGamePacketDecoder<T>> decoderClass) {
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
