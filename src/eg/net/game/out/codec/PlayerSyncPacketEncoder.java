package eg.net.game.out.codec;

import eg.model.Charactor;
import eg.model.Coordinate;
import eg.model.item.Item;
import eg.model.player.Player;
import eg.model.req.Animation;
import eg.model.req.Chat;
import eg.model.req.Effect;
import eg.model.req.ForceChat;
import eg.model.req.Hit;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSegment;
import eg.model.sync.SyncStatus;
import eg.model.sync.block.AnimationBlock;
import eg.model.sync.block.AppearanceBlock;
import eg.model.sync.block.ChatBlock;
import eg.model.sync.block.EffectBlock;
import eg.model.sync.block.ForceChatBlock;
import eg.model.sync.block.ForceMovementBlock;
import eg.model.sync.block.InteractBlock;
import eg.model.sync.block.PrimaryHitBlock;
import eg.model.sync.block.SecondaryHitBlock;
import eg.model.sync.block.TurnBlock;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.PlayerSyncPacket;
import eg.util.Misc;
import eg.util.io.Buffer;

public final class PlayerSyncPacketEncoder implements
		AbstractGamePacketEncoder<PlayerSyncPacket> {
	
	@Override
	public GamePacket encode(PlayerSyncPacket packet) throws Exception {
		
		Buffer buf = new Buffer();
		buf.beginBitAccess();
		
		Buffer payloadBuf = new Buffer();
		
		putSegment(packet.getLocalSegment(), buf, payloadBuf, packet.getOrigin());
		
		buf.putBits(8, packet.getLocalPlayersCount());
		
		for (SyncSegment seg : packet.getNonLocalSegments()) {
			putSegment(seg, buf, payloadBuf, packet.getOrigin());
		}
		
		if (payloadBuf.getPosition() != 0) {
			buf.putBits(11, 2047);
			buf.endBitAccess();
			buf.putBytes(payloadBuf.getData(), 0, payloadBuf.getPosition());
		} else {
			buf.endBitAccess();
		}
		return new GamePacket(81, buf.getData(), buf.getPosition());
	}
	
	private void putSegment(SyncSegment segment, Buffer buf, Buffer payloadBuf, Coordinate origin) {
		
		SyncStatus status = segment.getStatus();
		SyncBlockSet set = segment.getBlockSet();
		
		switch (status.getType()) {
		
		case TRANSITION:
			buf.putBit(true).putBits(2, 3);
			buf.putBits(2, ((SyncStatus.Transition) status).getCoordinate().getHeight());
			buf.putBit(((SyncStatus.Transition) status).isTeleporting());
			buf.putBit(set.size() != 0);
			Coordinate coordinate = ((SyncStatus.Transition) status).getCoordinate();
			Coordinate lastKnownSector = ((SyncStatus.Transition) status).getLastKnownSector();
			buf.putBits(7, coordinate.getRelativeY(lastKnownSector));
			buf.putBits(7, coordinate.getRelativeX(lastKnownSector));
			break;
			
		case RUN:
			buf.putBit(true).putBits(2, 2);
			buf.putBits(3, ((SyncStatus.Run) status).getPrimaryDirection().toInt());
	    	buf.putBits(3, ((SyncStatus.Run) status).getSecondaryDirection().toInt());
	    	buf.putBit(set.size() != 0);
			break;
			
		case WALK:
			buf.putBit(true).putBits(2, 1);
			buf.putBits(3, ((SyncStatus.Walk) status).getDirection().toInt());
			buf.putBit(set.size() != 0);
			break;
			
		case ADDITION:
			buf.putBits(11, ((SyncStatus.Addition) status).getIndex());
			buf.putBit(true).putBit(true);
			int dx = ((SyncStatus.Addition) status).getCoordinate().getX() - origin.getX();
			int dy = ((SyncStatus.Addition) status).getCoordinate().getY() - origin.getY();
			buf.putBits(5, dy).putBits(5, dx);
			break;
			
		case REMOVAL:
			buf.putBit(true).putBits(2, 3);
			break;
			
		case STAND:
			if (set.size() != 0) {
				buf.putBit(true).putBits(2, 0);
			} else {
				buf.putBit(false);
			}
			break;
		}
		
		if (set.size() != 0) {
			
			int config = 0;
			if (set.contains(ForceMovementBlock.class)) config |= 0b100_0000_0000;
			if (set.contains(EffectBlock.class)) config |= 0b1_0000_0000;
			if (set.contains(AnimationBlock.class)) config |= 0b1000;
			if (set.contains(ForceChatBlock.class)) config |= 0b100;
			if (set.contains(ChatBlock.class)) config |= 0b1000_0000;
			if (set.contains(InteractBlock.class)) config |= 0b1;
			if (set.contains(AppearanceBlock.class)) config |= 0b1_0000;
			if (set.contains(TurnBlock.class)) config |= 0b10;
			if (set.contains(PrimaryHitBlock.class)) config |= 0b10_0000;
			if (set.contains(SecondaryHitBlock.class)) config |= 0b10_0000_0000;
			if (config >= 0x100) {
				payloadBuf.putLEShort(config | 0b100_0000);
			} else {
				payloadBuf.putByte(config);
			}
			
			if (set.contains(ForceMovementBlock.class)) {
				putForceMovementBlock(set.get(ForceMovementBlock.class), payloadBuf);
			}
			if (set.contains(EffectBlock.class)) {
				putEffectBlock(set.get(EffectBlock.class), payloadBuf);
			}
			if (set.contains(AnimationBlock.class)) {
				putAnimationBlock(set.get(AnimationBlock.class), payloadBuf);
			}
			if (set.contains(ForceChatBlock.class)) {
				putForceChatBlock(set.get(ForceChatBlock.class), payloadBuf);
			}
			if (set.contains(ChatBlock.class)) {
				putChatBlock(set.get(ChatBlock.class), payloadBuf);
			}
			if (set.contains(InteractBlock.class)) {
				putInteractBlock(set.get(InteractBlock.class), payloadBuf);
			}
			if (set.contains(AppearanceBlock.class)) {
				putAppearanceBlock(set.get(AppearanceBlock.class), payloadBuf);
			}
			if (set.contains(TurnBlock.class)) {
				putTurnBlock(set.get(TurnBlock.class), payloadBuf);
			}
			if (set.contains(PrimaryHitBlock.class)) {
				putPrimaryHitBlock(set.get(PrimaryHitBlock.class), payloadBuf);
			}
			if (set.contains(SecondaryHitBlock.class)) {
				putSecondaryHitBlock(set.get(SecondaryHitBlock.class), payloadBuf);
			}
			
		}
	}
	
	private void putForceMovementBlock(ForceMovementBlock block, Buffer buf) {
		buf.put128PlusNegatedByte(block.getOriginX());
		buf.put128PlusNegatedByte(block.getOriginY());
		buf.put128PlusNegatedByte(block.getDestinationX());
		buf.put128PlusNegatedByte(block.getDestinationY());
		buf.put128PlusLEShort(block.getDurationX());
		buf.put128PlusShort(block.getDurationY());
		buf.put128PlusNegatedByte(block.getDirection().toInt());
	}
	
	private void putEffectBlock(EffectBlock block, Buffer buf) {
		Effect effect = block.getEffect();
		buf.putLEShort(effect != null ? effect.getId() : 0xffff);
		buf.putShort(effect != null ? effect.getHeight() : 0);
		buf.putShort(effect != null ? effect.getDelay() : 0);
	}
	
	private void putAnimationBlock(AnimationBlock block, Buffer buf) {
		Animation animation = block.getAnimation();
		buf.putLEShort(animation != null ? animation.getId() : 0xffff);
		buf.putNegatedByte(animation != null ? animation.getDelay() : 0);
	}
	
	private void putForceChatBlock(ForceChatBlock block, Buffer buf) {
		ForceChat chat = block.getChat();
		if (chat != null) {
			if (chat.showInChatbox()) {
				buf.putByte('~');
			}
			buf.putLine(chat.getMessage());
		} else {
			buf.putByte(10);
		}
	}
	
	private void putInteractBlock(InteractBlock block, Buffer buf) {
		Charactor target = block.getTarget();
		int index = 0xffff;
		if (target != null) {
			index = target.getIndex();
			if (target instanceof Player) {
				index += 0x8000;
			}
		}
		buf.putLEShort(index);
	}
	
	private void putTurnBlock(TurnBlock block, Buffer buf) {
		Coordinate target = block.getTarget();
		int x = 0;
		int y = 0;
		if (target != null) {
			x = target.getX() << 1 | 1;
			y = target.getY() << 1 | 1;
		}
		buf.put128PlusLEShort(x);
		buf.putLEShort(y);
	}
	
	private void putPrimaryHitBlock(PrimaryHitBlock block, Buffer buf) {
		Hit hit = block.getHit();
		buf.putByte(hit.getDamage());
		buf.put128PlusByte(hit.getType());
		buf.putNegatedByte(block.getLeftHealth());
		buf.putByte(block.getTotalHealth());
	}
	
	private void putSecondaryHitBlock(SecondaryHitBlock block, Buffer buf) {
		Hit hit = block.getHit();
		buf.putByte(hit.getDamage());
		buf.put128PlusNegatedByte(hit.getType());
		buf.putByte(block.getLeftHealth());
		buf.putNegatedByte(block.getTotalHealth());
	}
	
	private void putAppearanceBlock(AppearanceBlock block, Buffer buf) {
		Player player = block.getPlayer();
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
		buf.putShort(0); // skill
		buf.putOppositeByteLength(buf.getPosition() - begin);
	}
	
	private void putChatBlock(ChatBlock block, Buffer buf) {
		Chat chat = block.getChat();
		buf.putByte(chat.getAnimation());
		buf.putByte(chat.getColor());
		buf.putByte(chat.getPrivilege());
		byte[] bytes = chat.getMessage().getBytes();
		buf.putNegatedByte(bytes.length);
		buf.putBytesReversely(bytes, 0, bytes.length);
	}
}
