package server.utils;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
public class SecureHashing {
    private static final DigestSHA3 digester = new SHA3.Digest512();

    public static byte[] hash(String input){
        return hash(input.getBytes());
    }

    public static byte[] hash(byte[] input){
        return digester.digest(input);
    }
}
