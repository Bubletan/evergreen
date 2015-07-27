package eg.net.game.out;

import java.util.Objects;

import eg.game.model.PrivacyLevel;
import eg.net.game.AbstractGamePacket;

public final class PrivacyConfigPacket implements AbstractGamePacket {
    
    private final PrivacyLevel publicMode;
    private final PrivacyLevel privateMode;
    private final PrivacyLevel tradeMode;
    
    public PrivacyConfigPacket(PrivacyLevel publicMode, PrivacyLevel privateMode, PrivacyLevel tradeMode) {
        this.publicMode = Objects.requireNonNull(publicMode);
        this.privateMode = Objects.requireNonNull(privateMode);
        this.tradeMode = Objects.requireNonNull(tradeMode);
    }
    
    public PrivacyLevel getPublicMode() {
        return publicMode;
    }
    
    public PrivacyLevel getPrivateMode() {
        return privateMode;
    }
    
    public PrivacyLevel getTradeMode() {
        return tradeMode;
    }
}
