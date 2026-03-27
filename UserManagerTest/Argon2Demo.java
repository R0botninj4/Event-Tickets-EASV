import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2Demo {

    public static void main(String[] args) {

        String plainPassword = "admin123";

        Argon2 argon2 = Argon2Factory.create();

        try {
            String hash = argon2.hash(3, 65536, 1, plainPassword.toCharArray());

            System.out.println("Plain password: " + plainPassword);
            System.out.println("Argon2 hash: " + hash);

        } finally {
            argon2.wipeArray(plainPassword.toCharArray());
        }
    }
}