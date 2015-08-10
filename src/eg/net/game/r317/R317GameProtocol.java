package eg.net.game.r317;

import java.util.HashMap;
import java.util.Map;

import eg.net.game.AbstractGamePacket;
import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GameProtocol;
import eg.net.game.out.*;
import eg.net.game.r317.in.*;
import eg.net.game.r317.out.*;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
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
        
        putEncoder(AnimationResetPacket.class, new AnimationResetPacketEncoder()); // 1
        putEncoder(TabFlashPacket.class, new TabFlashPacketEncoder()); // 24
        putEncoder(AmountInputInterfacePacket.class, new AmountInputInterfacePacketEncoder()); // 27
        putEncoder(CameraWavePacket.class, new CameraWavePacketEncoder()); // 35
        putEncoder(ByteConfigPacket.class, new ByteConfigPacketEncoder()); // 36
        putEncoder(MulticombatOverlayPacket.class, new MulticombatOverlayPacketEncoder()); // 61
        putEncoder(NpcSyncPacket.class, new NpcSyncPacketEncoder()); // 65
        putEncoder(InterfaceOffsetPacket.class, new InterfaceOffsetPacketEncoder()); // 70
        putEncoder(TabInterfacePacket.class, new TabInterfacePacketEncoder()); // 71
        putEncoder(MapLoadingPacket.class, new MapLoadingPacketEncoder()); // 73
        putEncoder(PathResetPacket.class, new PathResetPacketEncoder()); // 78
        putEncoder(InterfaceScrollPositionPacket.class, new InterfaceScrollPositionPacketEncoder()); // 79
        putEncoder(PlayerSyncPacket.class, new PlayerSyncPacketEncoder()); // 81
        putEncoder(IntConfigPacket.class, new IntConfigPacketEncoder()); // 87
        putEncoder(GameInterfacePacket.class, new GameInterfacePacketEncoder()); // 97
        putEncoder(MinimapStatePacket.class, new MinimapStatePacketEncoder()); // 99
        putEncoder(TabSelectPacket.class, new TabSelectPacketEncoder()); // 106
        putEncoder(CameraResetPacket.class, new CameraResetPacketEncoder()); // 107
        putEncoder(LogoutPacket.class, new LogoutPacketEncoder()); // 109
        putEncoder(EnergyAlteredPacket.class, new EnergyAlteredPacketEncoder()); // 110
        putEncoder(SystemUpdatePacket.class, new SystemUpdatePacketEncoder()); // 114
        putEncoder(InterfaceColorPacket.class, new InterfaceColorPacketEncoder()); // 122
        putEncoder(InterfaceTextPacket.class, new InterfaceTextPacketEncoder()); // 126
        putEncoder(SkillAlteredPacket.class, new SkillAlteredPacketEncoder()); // 134
        putEncoder(DialogueInterfacePacket.class, new DialogueInterfacePacketEncoder()); // 164
        putEncoder(CameraPositionPacket.class, new CameraPositionPacketEncoder()); // 166
        putEncoder(CameraFocusPacket.class, new CameraFocusPacketEncoder()); // 177
        putEncoder(NameInputInterfacePacket.class, new NameInputInterfacePacketEncoder()); // 187
        putEncoder(PrivacyConfigPacket.class, new PrivacyConfigPacketEncoder()); // 206
        putEncoder(WalkableGameInterfacePacket.class, new WalkableGameInterfacePacketEncoder()); // 208
        putEncoder(InterfaceClosingPacket.class, new InterfaceClosingPacketEncoder()); // 219
        putEncoder(WeightAlteredPacket.class, new WeightAlteredPacketEncoder()); // 240
        putEncoder(PlayerInitPacket.class, new PlayerInitPacketEncoder()); // 249
        putEncoder(GameMessagePacket.class, new GameMessagePacketEncoder()); // 253
        
        putDecoder(0, new KeepalivePacketDecoder());
        putDecoder(3, new WindowFocusAlteredPacketDecoder());
        putDecoder(4, new PublicChatMessagePacketDecoder());
        putDecoder(86, new CameraAlteredPacketDecoder());
        MovementPacketDecoder mpd = new MovementPacketDecoder();
        putDecoder(98, mpd);
        putDecoder(103, new CommandPacketDecoder());
        putDecoder(121, new MapLoadedPacketDecoder());
        putDecoder(126, new PrivateChatMessagePacketDecoder());
        putDecoder(164, mpd);
        putDecoder(185, new ButtonPacketDecoder());
        putDecoder(241, new MouseClickPacketDecoder());
        putDecoder(248, mpd);
        
        putDecoder(208, new AmountInputEnteredPacketDecoder());
        putDecoder(60, new NameInputEnteredPacketDecoder());
        
        putDecoder(188, new FriendAdditionPacketDecoder());
        putDecoder(215, new FriendRemovalPacketDecoder());
        putDecoder(133, new IgnoreAdditionPacketDecoder());
        putDecoder(74, new IgnoreRemovalPacketDecoder());
        
        putDecoder(132, new ObjectOptionOnePacketDecoder());
        putDecoder(252, new ObjectOptionTwoPacketDecoder());
        putDecoder(70, new ObjectOptionThreePacketDecoder());
        
        putDecoder(155, new NpcOptionOnePacketDecoder());
        putDecoder(17, new NpcOptionTwoPacketDecoder());
        putDecoder(21, new NpcOptionThreePacketDecoder());
        
        putDecoder(128, new PlayerOptionOnePacketDecoder());
        putDecoder(153, new PlayerOptionTwoPacketDecoder());
        putDecoder(73, new PlayerOptionThreePacketDecoder());
        putDecoder(139, new PlayerOptionFourPacketDecoder());
        putDecoder(39, new PlayerOptionFivePacketDecoder());
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
