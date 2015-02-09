package eg.model.player.div;

import eg.model.Charactor;
import eg.model.item.Item;
import eg.model.player.Player;
import eg.util.Misc;
import eg.util.io.Buffer;

public final class Actions {
	
	private static final int FOCUS_CHARACTOR_UPDATE_MASK = 0b1;
	private static final int FOCUS_COORDINATE_UPDATE_MASK = 0b10;
	private static final int FORCED_CHAT_UPDATE_MASK = 0b100;
	private static final int ANIMATION_UPDATE_MASK = 0b1000;
	private static final int APPEARANCE_UPDATE_MASK = 0b1_0000;
	private static final int HIT_UPDATE_MASK = 0b10_0000;
	private static final int SHORT_UPDATE_CONFIG_MASK = 0b100_0000;
	private static final int CHAT_UPDATE_MASK = 0b1000_0000;
	private static final int EFFECT_UPDATE_MASK = 0b1_0000_0000 | SHORT_UPDATE_CONFIG_MASK;
	private static final int HIT_UPDATE_2_MASK = 0b10_0000_0000 | SHORT_UPDATE_CONFIG_MASK;
	
	private Player player;
	
	private int config = APPEARANCE_UPDATE_MASK;
	
	private int effectReqId, effectReqHeight, effectReqDelay;
	private int animReqId, animReqDelay;
	private boolean forcedChatReqShowInChatbox;
	private String forcedChatReqMessage;
	private int chatReqAnim, chatReqColor;
	private String chatReqMessage;
	private Charactor focusCharReqTarget;
	
	private int focusCoordReqX, focusCoordReqY;
	private int hitReqDamage, hitReqType, hitReq2Damage, hitReq2Type;
	
	private final Buffer preAppearanceBuffer = new Buffer(176);
	private final Buffer postAppearanceBuffer = new Buffer(12);
	private boolean nonAppearanceBufferBuilt;
	
	private final Buffer appearanceBuffer = new Buffer(58);
	private boolean appearanceBufferBuilt;
	
	public Actions(Player player) {
		this.player = player;
	}
	
	public void preSyncProcess() {
		preAppearanceBuffer.setPosition(0);
		postAppearanceBuffer.setPosition(0);
		appearanceBuffer.setPosition(0);
		nonAppearanceBufferBuilt = false;
		appearanceBufferBuilt = false;
	}
	
	public void postSyncProcess() {
		config = 0;
	}
	
	public boolean isSyncRequired() {
		return config != 0;
	}
	
	public void forceChat(String msg) {
		if (msg.length() > 80) {
			throw new IllegalArgumentException("Message length must be <= 80.");
		}
		if (msg.charAt(0) == '~') {
			throw new IllegalArgumentException("Message may not start with '~'.");
		}
		forcedChatReqMessage = msg;
		forcedChatReqShowInChatbox = false;
		config |= FORCED_CHAT_UPDATE_MASK;
	}
	
	public synchronized void putToBuffer(Buffer buf, boolean forceAppearance, boolean noChat) {
		int c = config;
		if (forceAppearance) {
			c |= APPEARANCE_UPDATE_MASK;
		}
		if (noChat) {
			c &= ~CHAT_UPDATE_MASK;
		}
		if ((c & SHORT_UPDATE_CONFIG_MASK) != 0) {
			buf.putLEShort(c);
		} else {
			buf.putByte(c);
		}
		if (!nonAppearanceBufferBuilt) {
			buildPreAppearanceBuffer(preAppearanceBuffer, config);
			buildPostAppearanceBuffer(postAppearanceBuffer);
			nonAppearanceBufferBuilt = true;
		}
		if (!noChat || (c & CHAT_UPDATE_MASK) == 0) {
			buf.putBytes(preAppearanceBuffer.getData(), 0, preAppearanceBuffer.getPosition());
		} else {
			buildPreAppearanceBuffer(buf, c);
		}
		if ((c & APPEARANCE_UPDATE_MASK) != 0) {
			if (!appearanceBufferBuilt) {
				buildAppearanceBuffer(appearanceBuffer);
				appearanceBufferBuilt = true;
			}
			buf.putBytes(appearanceBuffer.getData(), 0, appearanceBuffer.getPosition());
		}
		buf.putBytes(postAppearanceBuffer.getData(), 0, postAppearanceBuffer.getPosition());
	}
	
	private void buildPreAppearanceBuffer(Buffer buf, int config) {
		if ((config & EFFECT_UPDATE_MASK) != 0) {
			buf.putLEShort(effectReqId);
			buf.putShort(effectReqHeight);
			buf.putShort(effectReqDelay);
		}
		if ((config & ANIMATION_UPDATE_MASK) != 0) {
			buf.putLEShort(animReqId);
			buf.putNegatedByte(animReqDelay);
		}
		if ((config & FORCED_CHAT_UPDATE_MASK) != 0) {
			if (forcedChatReqShowInChatbox) {
				buf.putByte('~');
			}
			buf.putLine(forcedChatReqMessage);
		}
		if ((config & CHAT_UPDATE_MASK) != 0) {
			buf.putByte(chatReqAnim);
			buf.putByte(chatReqColor);
			buf.putByte(player.getPrivilege());
			byte[] bytes = chatReqMessage.getBytes();
			buf.putNegatedByte(bytes.length);
			buf.putBytesReversely(bytes, 0, bytes.length);
		}
		if ((config & FOCUS_CHARACTOR_UPDATE_MASK) != 0) {
			int target = 0xffff;
			if (focusCharReqTarget != null) {
				target = focusCharReqTarget.getIndex();
				if (focusCharReqTarget instanceof Player) {
					target += 0x8000;
				}
			}
			buf.putLEShort(target);
		}
	}
	
	private void buildAppearanceBuffer(Buffer buf) {
		int begin = buf.shiftAndGetPosition(1);
		buf.putByte(player.getIdentikit().getGender());
		buf.putByte(player.headIcon);
		buf.putByte(player.headIconPk);
		if (player.getEquipment().getHeadwear() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getHeadwear().getType().getId());
		} else {
			buf.putByte(0);
		}
		if (player.getEquipment().getBackwear() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getBackwear().getType().getId());
		} else {
			buf.putByte(0);
		}
		if (player.getEquipment().getNeckwear() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getNeckwear().getType().getId());
		} else {
			buf.putByte(0);
		}
		if (player.getEquipment().getRightHand() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getRightHand().getType().getId());
		} else {
			buf.putByte(0);
		}
		if (player.getEquipment().getTop() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getTop().getType().getId());
		} else {
			buf.putShort(0x100 + player.getIdentikit().getTorso());
		}
		if (player.getEquipment().getLeftHand() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getLeftHand().getType().getId());
		} else {
			buf.putByte(0);
		}
		if (!player.getEquipment().hasFullBody()) {
			buf.putShort(0x100 + player.getIdentikit().getArms());
		} else {
			buf.putByte(0);
		}
		if (player.getEquipment().getBottom() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getBottom().getType().getId());
		} else {
			buf.putShort(0x100 + player.getIdentikit().getLegs());
		}
		if (!player.getEquipment().hasFullHelm() && !player.getEquipment().hasFullMask()) {
			buf.putShort(0x100 + player.getIdentikit().getHead());		
		} else {
			buf.putByte(0);
		}
		if (player.getEquipment().getHandwear() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getHandwear().getType().getId());
		} else {
			buf.putShort(0x100 + player.getIdentikit().getHands());
		}
		if (player.getEquipment().getFootwear() != Item.NOTHING) {
			buf.putShort(0x200 + player.getEquipment().getFootwear().getType().getId());
		} else {
			buf.putShort(0x100 + player.getIdentikit().getFeet());
		}
		if (player.getIdentikit().getGender() != 1 && !player.getEquipment().hasFullMask()) {
			buf.putShort(0x100 + player.getIdentikit().getBeard());
		} else {
			buf.putByte(0);
		}
		buf.putByte(player.getIdentikit().getHairColor());	
		buf.putByte(player.getIdentikit().getTorsoColor());	
		buf.putByte(player.getIdentikit().getLegsColor());	
		buf.putByte(player.getIdentikit().getFeetColor());	
		buf.putByte(player.getIdentikit().getSkinColor());	
		buf.putShort(player.getIdleAnimation().getStand()); 
		buf.putShort(player.getIdleAnimation().getTurn());
		buf.putShort(player.getIdleAnimation().getWalk());
		buf.putShort(player.getIdleAnimation().getTurn180());
		buf.putShort(player.getIdleAnimation().getTurn90Cw());
		buf.putShort(player.getIdleAnimation().getTurn90Ccw());
		buf.putShort(player.getIdleAnimation().getRun());
		buf.putLong(Misc.encryptUsername(player.getUsername()));
		buf.putByte(player.getStatistics().getCombatLevel());
		buf.putShort(0); //XXX: Skill
		buf.putOppositeByteLength(buf.getPosition() - begin);
	}
	
	private void buildPostAppearanceBuffer(Buffer buf) {
		if ((config & FOCUS_COORDINATE_UPDATE_MASK) != 0) {
			buf.put128PlusLEShort(focusCoordReqX << 1 | 1);
			buf.putLEShort(focusCoordReqY << 1 | 1);
		}
		if ((config & HIT_UPDATE_MASK) != 0) {
			buf.putByte(hitReqDamage);
			buf.put128PlusByte(hitReqType);
			Skill hitpoints = player.getStatistics().getHitpoints();
			buf.putNegatedByte(hitpoints.getPseudoLevel());
			buf.putByte(hitpoints.getLevel());
		}
		if ((config & HIT_UPDATE_2_MASK) != 0) {
			buf.putByte(hitReq2Damage);
			buf.put128PlusNegatedByte(hitReq2Type);
			Skill hitpoints = player.getStatistics().getHitpoints();
			buf.putByte(hitpoints.getPseudoLevel());
			buf.putNegatedByte(hitpoints.getLevel());
		}
	}
}
