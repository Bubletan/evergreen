package eg.net.login;

import eg.util.net.IsaacCipher;

/**
 * @author Bubletan <https://github.com/Bubletan>
 * @author Graham
 */
public final class LoginRequest {
    
    private final String username;
    private final String password;
    private final int usernameHash;
    private final int uid;
    private final IsaacCipher decodingCipher;
    private final IsaacCipher encodingCipher;
    private final boolean reconnecting;
    private final boolean lowMemory;
    private final int releaseNumber;
    private final int[] archiveCrcs;
    private final int clientVersion;
    
    public LoginRequest(String username, String password, int usernameHash,
            int uid, IsaacCipher decodingCipher, IsaacCipher encodingCipher,
            boolean reconnecting, boolean lowMemory, int releaseNumber,
            int[] archiveCrcs, int clientVersion) {
        this.username = username;
        this.password = password;
        this.usernameHash = usernameHash;
        this.uid = uid;
        this.decodingCipher = decodingCipher;
        this.encodingCipher = encodingCipher;
        this.reconnecting = reconnecting;
        this.lowMemory = lowMemory;
        this.releaseNumber = releaseNumber;
        this.archiveCrcs = archiveCrcs;
        this.clientVersion = clientVersion;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public int getUsernameHash() {
        return usernameHash;
    }
    
    public int getUid() {
        return uid;
    }
    
    public IsaacCipher getDecodingCipher() {
        return decodingCipher;
    }
    
    public IsaacCipher getEncodingCipher() {
        return encodingCipher;
    }
    
    public boolean isReconnecting() {
        return reconnecting;
    }
    
    public boolean isLowMemory() {
        return lowMemory;
    }
    
    public int getReleaseNumber() {
        return releaseNumber;
    }
    
    public int[] getArchiveCrcs() {
        return archiveCrcs;
    }
    
    public int getClientVersion() {
        return clientVersion;
    }
}
