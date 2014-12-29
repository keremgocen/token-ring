import com.kerem.distributed.TokenRingGenerator;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting up token-ring simulator...");

        // Create token-ring simulator with predefined configuration
        TokenRingGenerator tokenRingGenerator = new TokenRingGenerator(2, 4444);
    }
}
