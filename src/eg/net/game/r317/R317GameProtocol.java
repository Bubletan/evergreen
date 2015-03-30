package eg.net.game.r317;

import java.util.HashMap;
import java.util.Map;

import eg.net.game.AbstractGamePacket;
import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GameProtocol;
import eg.net.game.out.CameraResetPacket;
import eg.net.game.out.ConfigPacket;
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
import eg.net.game.out.WeightAlteredPacket;
import eg.net.game.r317.in.ButtonPacketDecoder;
import eg.net.game.r317.in.CameraAlteredPacketDecoder;
import eg.net.game.r317.in.ChatMessagePacketDecoder;
import eg.net.game.r317.in.CommandPacketDecoder;
import eg.net.game.r317.in.KeepalivePacketDecoder;
import eg.net.game.r317.in.MapLoadedPacketDecoder;
import eg.net.game.r317.in.MouseClickPacketDecoder;
import eg.net.game.r317.in.MovementPacketDecoder;
import eg.net.game.r317.in.WindowFocusAlteredPacketDecoder;
import eg.net.game.r317.out.CameraResetPacketEncoder;
import eg.net.game.r317.out.ConfigPacketEncoder;
import eg.net.game.r317.out.DialogueInterfacePacketEncoder;
import eg.net.game.r317.out.GameMessagePacketEncoder;
import eg.net.game.r317.out.InterfaceTextPacketEncoder;
import eg.net.game.r317.out.LogoutPacketEncoder;
import eg.net.game.r317.out.MainInterfacePacketEncoder;
import eg.net.game.r317.out.MapLoadingPacketEncoder;
import eg.net.game.r317.out.MulticombatOverlayPacketEncoder;
import eg.net.game.r317.out.NpcSyncPacketEncoder;
import eg.net.game.r317.out.PlayerInitPacketEncoder;
import eg.net.game.r317.out.PlayerSyncPacketEncoder;
import eg.net.game.r317.out.SidebarInterfacePacketEncoder;
import eg.net.game.r317.out.SystemUpdatePacketEncoder;
import eg.net.game.r317.out.WeightAlteredPacketEncoder;

public final class R317GameProtocol implements GameProtocol {
    
    private static final int[] INCOMING_PACKET_SIZE = {
        0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0-9
        0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10-19
        0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20-29
        0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30-39
        2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40-49
        0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50-59
        8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60-69
        6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70-79
        0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80-89
        0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90-99
        0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100-109
        0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110-119
        1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120-129
        0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130-139
        0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140-149
        0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150-159
        0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160-169
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170-179
        0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180-189
        0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190-199
        2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200-209
        4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210-219
        0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220-229
        1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230-239
        0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240-249
        0, 0, 6, 6, 0, 0, 0 // 250-255
    };
    
    private static final int[] OUTGOING_PACKET_SIZE = {
        0, 0, 0, 0, 6, 0, 0, 0, 4, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, -2, 4, 3, 0, 0, 0, 
        0, 0, 0, 0, 5, 0, 0, 6, 0, 0, 
        9, 0, 0, -2, 0, 0, 0, 0, 0, 0, 
        -2, 1, 0, 0, 2, -2, 0, 0, 0, 0, 
        6, 3, 2, 4, 2, 4, 0, 0, 0, 4, 
        0, -2, 0, 0, 7, 2, 0, 6, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 
        0, 2, 0, 0, -1, 4, 1, 0, 0, 0, 
        1, 0, 0, 0, 2, 0, 0, 15, 0, 0, 
        0, 4, 4, 0, 0, 0, -2, 0, 0, 0, 
        0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 
        0, 0, 2, 0, 0, 0, 0, 14, 0, 0, 
        0, 4, 0, 0, 0, 0, 3, 0, 0, 0, 
        4, 0, 0, 0, 2, 0, 6, 0, 0, 0, 
        0, 3, 0, 0, 5, 0, 10, 6, 0, 0, 
        0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 
        4, 0, 0, 0, 0, 0, 3, 0, 2, 0, 
        0, 0, 0, 0, -2, 7, 0, -2, 2, 0, 
        0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 
        8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        2, -2, 0, 0, 0, 0, 6, 0, 4, 3, 
        0, 0, 0, -1, 6, 0, 0
    };
    
    private final Map<Class<? extends AbstractGamePacket>, AbstractGamePacketEncoder<?>> encoders = new HashMap<>(256);
    private final AbstractGamePacketDecoder<?>[] decoders = new AbstractGamePacketDecoder<?>[256];
    
    public R317GameProtocol() {
        
        putEncoder(ConfigPacket.class, new ConfigPacketEncoder()); // 36, 87
        putEncoder(MulticombatOverlayPacket.class, new MulticombatOverlayPacketEncoder()); // 61
        putEncoder(NpcSyncPacket.class, new NpcSyncPacketEncoder()); // 65
        putEncoder(SidebarInterfacePacket.class, new SidebarInterfacePacketEncoder()); // 71
        putEncoder(MapLoadingPacket.class, new MapLoadingPacketEncoder()); // 73
        putEncoder(PlayerSyncPacket.class, new PlayerSyncPacketEncoder()); // 81
        putEncoder(MainInterfacePacket.class, new MainInterfacePacketEncoder()); // 97
        putEncoder(CameraResetPacket.class, new CameraResetPacketEncoder()); // 107
        putEncoder(LogoutPacket.class, new LogoutPacketEncoder()); // 109
        putEncoder(SystemUpdatePacket.class, new SystemUpdatePacketEncoder()); // 114
        putEncoder(InterfaceTextPacket.class, new InterfaceTextPacketEncoder()); // 126
        putEncoder(DialogueInterfacePacket.class, new DialogueInterfacePacketEncoder()); // 164
        putEncoder(WeightAlteredPacket.class, new WeightAlteredPacketEncoder()); // 240
        putEncoder(PlayerInitPacket.class, new PlayerInitPacketEncoder()); // 249
        putEncoder(GameMessagePacket.class, new GameMessagePacketEncoder()); // 253
        
        putDecoder(0, new KeepalivePacketDecoder());
        putDecoder(3, new WindowFocusAlteredPacketDecoder());
        putDecoder(4, new ChatMessagePacketDecoder());
        putDecoder(86, new CameraAlteredPacketDecoder());
        MovementPacketDecoder mpd = new MovementPacketDecoder();
        putDecoder(98, mpd);
        putDecoder(103, new CommandPacketDecoder());
        putDecoder(121, new MapLoadedPacketDecoder());
        putDecoder(164, mpd);
        putDecoder(185, new ButtonPacketDecoder());
        putDecoder(241, new MouseClickPacketDecoder());
        putDecoder(248, mpd);
    }
    
    private <T extends AbstractGamePacket> void putEncoder(Class<T> type, AbstractGamePacketEncoder<T> encoder) {
        encoders.put(type, encoder);
    }
    
    private void putDecoder(int type, AbstractGamePacketDecoder<?> decoder) {
        decoders[type] = decoder;
    }
    
    @Override
    public int getInboundPacketSize(int type) {
        return INCOMING_PACKET_SIZE[type];
    }
    
    @Override
    public int getOutboundPacketSize(int type) {
        return OUTGOING_PACKET_SIZE[type];
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractGamePacket> AbstractGamePacketDecoder<T> getInboundPacketDecoder(int type) {
        return (AbstractGamePacketDecoder<T>) decoders[type];
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractGamePacket> AbstractGamePacketEncoder<T> getOutboundPacketEncoder(Class<T> type) {
        return (AbstractGamePacketEncoder<T>) encoders.get(type);
    }
}
