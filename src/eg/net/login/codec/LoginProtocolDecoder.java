package eg.net.login.codec;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import eg.net.login.LoginRequest;
import eg.util.io.IsaacCipher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class LoginProtocolDecoder extends ByteToMessageDecoder {
	
	private static final BigInteger RSA_EXPONENT = new BigInteger("124425314960550024206991065332877157931472210939505789558012215720454903710618146200843877022273818555405810618059191162604008259757866640421952188957253368398733319663236323097864278319463888334484786055755767881706264786840339899269810859874287402892848784247637729987603089254067178011764721326471352835473");
	private static final BigInteger RSA_MODULUS = new BigInteger("143690958001225849100503496893758066948984921380482659564113596152800934352119496873386875214251264258425208995167316497331786595942754290983849878549630226741961610780416197036711585670124061149988186026407785250364328460839202438651793652051153157765358767514800252431284681765433239888090564804146588087023");
	
	public static final int STATUS_EXCHANGE_DATA = 0;
	
	public static final int TYPE_STANDARD = 16;
	public static final int TYPE_RECONNECTION = 18;
	
	private int state;
	
	private static final SecureRandom random = new SecureRandom();

	private int loginLength;
	private boolean reconnecting;
	private long serverSeed;
	private int usernameHash;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (state == 0 && decodeHandshake(ctx, in, out)
				|| state == 1 && decodeHeader(ctx, in, out)
				|| state == 2 && decodePayload(ctx, in, out)) {
			state++;
		}
	}
	
	private boolean decodeHandshake(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		if (in.readableBytes() >= 2) {
			
			in.readUnsignedByte(); // 14, login request
			
			usernameHash = in.readUnsignedByte();
			serverSeed = random.nextLong();
			
			ByteBuf response = ctx.alloc().buffer(17);
			response.writeByte(STATUS_EXCHANGE_DATA);
			response.writeLong(0);
			response.writeLong(serverSeed);
			ctx.channel().writeAndFlush(response);
			
			return true;
		}
		return false;
	}
	
	private boolean decodeHeader(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws IOException {
		if (in.readableBytes() >= 2) {
			int loginType = in.readUnsignedByte();

			if (loginType != TYPE_STANDARD && loginType != TYPE_RECONNECTION) {
				throw new IOException("Invalid login type.");
			}

			reconnecting = loginType == TYPE_RECONNECTION;
			loginLength = in.readUnsignedByte();

			return true;
		}
		return false;
	}
	
	private boolean decodePayload(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= loginLength) {
			ByteBuf payload = in.readBytes(loginLength);
			int clientVersion = 255 - payload.readUnsignedByte();

			int releaseNumber = payload.readUnsignedShort();

			int lowMemoryFlag = payload.readUnsignedByte();
			if (lowMemoryFlag != 0 && lowMemoryFlag != 1) {
				throw new Exception("Invalid value for low memory flag.");
			}

			boolean lowMemory = lowMemoryFlag == 1;

			int[] archiveCrcs = new int[9];
			for (int i = 0; i < 9; i++) {
				archiveCrcs[i] = payload.readInt();
			}

			int securePayloadLength = payload.readUnsignedByte();
			if (securePayloadLength != loginLength - 41) {
				throw new Exception("Secure payload length mismatch.");
			}

			ByteBuf securePayload = payload.readBytes(securePayloadLength);

			if (false) {
				BigInteger bigInteger = new BigInteger(securePayload.array());
				bigInteger = bigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);

				securePayload = Unpooled.wrappedBuffer(bigInteger.toByteArray());
			}

			int secureId = securePayload.readUnsignedByte();
			if (secureId != 10) {
				throw new Exception("Invalid secure payload id.");
			}

			long clientSeed = securePayload.readLong();
			long reportedServerSeed = securePayload.readLong();
			if (reportedServerSeed != serverSeed) {
				throw new Exception("Server seed mismatch.");
			}

			int uid = securePayload.readInt();

			String username = readString(securePayload);
			String password = readString(securePayload);

			if (password.length() < 6 || password.length() > 20) {
				throw new Exception("Invalid password.");
			} else if (username.isEmpty() || username.length() > 12) {
				throw new Exception("Invalid username.");
			}

			int[] seed = new int[4];
			seed[0] = (int) (clientSeed >> 32);
			seed[1] = (int) clientSeed;
			seed[2] = (int) (serverSeed >> 32);
			seed[3] = (int) serverSeed;

			IsaacCipher decodingRandom = new IsaacCipher(seed);
			for (int i = 0; i < seed.length; i++) {
				seed[i] += 50;
			}

			IsaacCipher encodingRandom = new IsaacCipher(seed);

			LoginRequest request = new LoginRequest(username, password, usernameHash, uid, decodingRandom, encodingRandom, reconnecting, lowMemory, releaseNumber, archiveCrcs,
					clientVersion);

			out.add(request);
			if (in.isReadable()) {
				out.add(in.readBytes(in.readableBytes()));
			}
			return true;
		}
		return false;
	}
	
	public static String readString(ByteBuf buffer) {
		StringBuilder builder = new StringBuilder();
		int character;
		while (buffer.isReadable() && (character = buffer.readUnsignedByte()) != 10) {
			builder.append((char) character);
		}
		return builder.toString();
	}
}
