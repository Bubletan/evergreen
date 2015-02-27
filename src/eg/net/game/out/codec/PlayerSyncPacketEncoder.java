package eg.net.game.out.codec;

import eg.model.Charactor;
import eg.model.Coordinate;
import eg.model.item.Item;
import eg.model.player.Player;
import eg.model.req.Animation;
import eg.model.req.Effect;
import eg.model.req.ForceChatMessage;
import eg.model.req.ForceMovement;
import eg.model.req.Hit;
import eg.model.sync.SyncBlock;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSection;
import eg.model.sync.SyncStatus;
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
		
		putSection(packet.getLocalSection(), buf, payloadBuf, packet.getOrigin());
		
		buf.putBits(8, packet.getLocalPlayersCount());
		
		for (SyncSection sec : packet.getNonLocalSections()) {
			putSection(sec, buf, payloadBuf, packet.getOrigin());
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
	
	private void putSection(SyncSection sec, Buffer buf, Buffer payloadBuf, Coordinate origin) {
		
		SyncStatus status = sec.getStatus();
		SyncBlockSet set = sec.getBlockSet();
		
		switch (status.getType()) {
		
		case TRANSITION:
			buf.putBit(true).putBits(2, 3);
			buf.putBits(2, ((SyncStatus.Transition) status).getCoordinate().getHeight());
			buf.putBit(((SyncStatus.Transition) status).isTeleporting());
			buf.putBit(set.size() != 0);
			Coordinate coordinate = ((SyncStatus.Transition) status).getCoordinate();
			Coordinate sectorOrigin = ((SyncStatus.Transition) status).getSectorOrigin();
			buf.putBits(7, coordinate.getY() - sectorOrigin.getY());
			buf.putBits(7, coordinate.getX() - sectorOrigin.getX());
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
			
			int config = (set.contains(SyncBlock.Type.FORCE_MOVEMENT) ? 0b100_0000_0000 : 0)
					| (set.contains(SyncBlock.Type.EFFECT) ? 0b1_0000_0000 : 0)
					| (set.contains(SyncBlock.Type.ANIMATION) ? 0b1000 : 0)
					| (set.contains(SyncBlock.Type.FORCE_CHAT_MESSAGE) ? 0b100 : 0)
					| (set.contains(SyncBlock.Type.CHAT_MESSAGE) ? 0b1000_0000 : 0)
					| (set.contains(SyncBlock.Type.INTERACT) ? 0b1 : 0)
					| (set.contains(SyncBlock.Type.APPEARANCE) ? 0b1_0000 : 0)
					| (set.contains(SyncBlock.Type.TURN) ? 0b10 : 0)
					| (set.contains(SyncBlock.Type.PRIMARY_HIT) ? 0b10_0000 : 0)
					| (set.contains(SyncBlock.Type.SECONDARY_HIT) ? 0b10_0000_0000 : 0);
			
			if (config >= 0x100) {
				payloadBuf.putLEShort(config | 0b100_0000);
			} else {
				payloadBuf.putByte(config);
			}
			
			if (set.contains(SyncBlock.Type.FORCE_MOVEMENT)) {
				SyncBlock.ForceMovement block = set.get(SyncBlock.Type.FORCE_MOVEMENT);
				ForceMovement forceMovement = block.getForceMovement();
				// TODO something else if forceMovement is null
				payloadBuf.put128PlusNegatedByte(forceMovement.getOriginX());
				payloadBuf.put128PlusNegatedByte(forceMovement.getOriginY());
				payloadBuf.put128PlusNegatedByte(forceMovement.getDestinationX());
				payloadBuf.put128PlusNegatedByte(forceMovement.getDestinationY());
				payloadBuf.put128PlusLEShort(forceMovement.getDurationX());
				payloadBuf.put128PlusShort(forceMovement.getDurationY());
				payloadBuf.put128PlusNegatedByte(forceMovement.getDirection().toInt());
			}
			
			if (set.contains(SyncBlock.Type.EFFECT)) {
				SyncBlock.Effect block = set.get(SyncBlock.Type.EFFECT);
				Effect effect = block.getEffect();
				payloadBuf.putLEShort(effect != null ? effect.getId() : 0xffff);
				payloadBuf.putShort(effect != null ? effect.getHeight() : 0);
				payloadBuf.putShort(effect != null ? effect.getDelay() : 0);
			}
			
			if (set.contains(SyncBlock.Type.ANIMATION)) {
				SyncBlock.Animation block = set.get(SyncBlock.Type.ANIMATION);
				Animation animation = block.getAnimation();
				payloadBuf.putLEShort(animation != null ? animation.getId() : 0xffff);
				payloadBuf.putNegatedByte(animation != null ? animation.getDelay() : 0);
			}
			
			if (set.contains(SyncBlock.Type.FORCE_CHAT_MESSAGE)) {
				SyncBlock.ForceChatMessage block = set.get(SyncBlock.Type.FORCE_CHAT_MESSAGE);
				ForceChatMessage forceChatMessage = block.getForceChatMessage();
				if (forceChatMessage != null) {
					if (forceChatMessage.showInChatbox()) {
						payloadBuf.putByte('~');
					}
					payloadBuf.putLine(forceChatMessage.getMessage());
				} else {
					payloadBuf.putByte('\n');
				}
			}
			
			if (set.contains(SyncBlock.Type.CHAT_MESSAGE)) {
				SyncBlock.ChatMessage block = set.get(SyncBlock.Type.CHAT_MESSAGE);
				payloadBuf.putByte(block.getAnimationEffect());
				payloadBuf.putByte(block.getColorEffect());
				payloadBuf.putByte(block.getPrivilege());
				byte[] bytes = block.getCompressedMessage();
				payloadBuf.putNegatedByte(bytes.length);
				payloadBuf.putBytesReversely(bytes, 0, bytes.length);
			}
			
			if (set.contains(SyncBlock.Type.INTERACT)) {
				SyncBlock.Interact block = set.get(SyncBlock.Type.INTERACT);
				Charactor target = block.getTarget();
				int index = target != null ? target.getIndex() : 0xffff;
				if (target instanceof Player) {
					index += 0x8000;
				}
				payloadBuf.putLEShort(index);
			}
			
			if (set.contains(SyncBlock.Type.APPEARANCE)) {
				SyncBlock.Appearance block = set.get(SyncBlock.Type.APPEARANCE);
				Player player = block.getPlayer();
				int begin = payloadBuf.shiftAndGetPosition(1);
				payloadBuf.putByte(player.getIdentikit().getGender());
				payloadBuf.putByte(player.headIcon);
				payloadBuf.putByte(player.headIconPk);
				if (player.getEquipment().getHeadwear() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getHeadwear().getType().getId());
				} else {
					payloadBuf.putByte(0);
				}
				if (player.getEquipment().getBackwear() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getBackwear().getType().getId());
				} else {
					payloadBuf.putByte(0);
				}
				if (player.getEquipment().getNeckwear() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getNeckwear().getType().getId());
				} else {
					payloadBuf.putByte(0);
				}
				if (player.getEquipment().getRightHand() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getRightHand().getType().getId());
				} else {
					payloadBuf.putByte(0);
				}
				if (player.getEquipment().getTop() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getTop().getType().getId());
				} else {
					payloadBuf.putShort(0x100 + player.getIdentikit().getTorso());
				}
				if (player.getEquipment().getLeftHand() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getLeftHand().getType().getId());
				} else {
					payloadBuf.putByte(0);
				}
				if (!player.getEquipment().hasFullBody()) {
					payloadBuf.putShort(0x100 + player.getIdentikit().getArms());
				} else {
					payloadBuf.putByte(0);
				}
				if (player.getEquipment().getBottom() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getBottom().getType().getId());
				} else {
					payloadBuf.putShort(0x100 + player.getIdentikit().getLegs());
				}
				if (!player.getEquipment().hasFullHelm() && !player.getEquipment().hasFullMask()) {
					payloadBuf.putShort(0x100 + player.getIdentikit().getHead());		
				} else {
					payloadBuf.putByte(0);
				}
				if (player.getEquipment().getHandwear() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getHandwear().getType().getId());
				} else {
					payloadBuf.putShort(0x100 + player.getIdentikit().getHands());
				}
				if (player.getEquipment().getFootwear() != Item.NOTHING) {
					payloadBuf.putShort(0x200 + player.getEquipment().getFootwear().getType().getId());
				} else {
					payloadBuf.putShort(0x100 + player.getIdentikit().getFeet());
				}
				if (player.getIdentikit().getGender() != 1 && !player.getEquipment().hasFullMask()) {
					payloadBuf.putShort(0x100 + player.getIdentikit().getBeard());
				} else {
					payloadBuf.putByte(0);
				}
				payloadBuf.putByte(player.getIdentikit().getHairColor());	
				payloadBuf.putByte(player.getIdentikit().getTorsoColor());	
				payloadBuf.putByte(player.getIdentikit().getLegsColor());	
				payloadBuf.putByte(player.getIdentikit().getFeetColor());	
				payloadBuf.putByte(player.getIdentikit().getSkinColor());	
				payloadBuf.putShort(player.getIdleAnimation().getStand()); 
				payloadBuf.putShort(player.getIdleAnimation().getTurn());
				payloadBuf.putShort(player.getIdleAnimation().getWalk());
				payloadBuf.putShort(player.getIdleAnimation().getTurn180());
				payloadBuf.putShort(player.getIdleAnimation().getTurn90Cw());
				payloadBuf.putShort(player.getIdleAnimation().getTurn90Ccw());
				payloadBuf.putShort(player.getIdleAnimation().getRun());
				payloadBuf.putLong(Misc.encryptUsername(player.getUsername()));
				payloadBuf.putByte(player.getStatistics().getCombatLevel());
				payloadBuf.putShort(0); // skill
				payloadBuf.putOppositeByteLength(payloadBuf.getPosition() - begin);
			
			}
			
			if (set.contains(SyncBlock.Type.TURN)) {
				SyncBlock.Turn block = set.get(SyncBlock.Type.TURN);
				Coordinate target = block.getTarget();
				int x = 0;
				int y = 0;
				if (target != null) {
					x = target.getX() << 1 | 1;
					y = target.getY() << 1 | 1;
				}
				payloadBuf.put128PlusLEShort(x);
				payloadBuf.putLEShort(y);
			}
			
			if (set.contains(SyncBlock.Type.PRIMARY_HIT)) {
				SyncBlock.PrimaryHit block = set.get(SyncBlock.Type.PRIMARY_HIT);
				Hit hit = block.getHit();
				payloadBuf.putByte(hit.getDamage());
				payloadBuf.put128PlusByte(hit.getType());
				payloadBuf.putNegatedByte(block.getHealthLeft());
				payloadBuf.putByte(block.getHealthTotal());
			}
			
			if (set.contains(SyncBlock.Type.SECONDARY_HIT)) {
				SyncBlock.SecondaryHit block = set.get(SyncBlock.Type.SECONDARY_HIT);
				Hit hit = block.getHit();
				payloadBuf.putByte(hit.getDamage());
				payloadBuf.put128PlusNegatedByte(hit.getType());
				payloadBuf.putByte(block.getHealthLeft());
				payloadBuf.putNegatedByte(block.getHealthTotal());
			}
		}
	}
}
