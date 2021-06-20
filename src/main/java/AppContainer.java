import javax.swing.*;
import java.math.BigInteger;
import java.util.Random;

public class AppContainer {
    public static void main(String[] args) {
        App app = new App();
        app.display();
    }
    protected static boolean isPrime(final BigInteger n, final int noOfTests) {
        if (BigInteger.valueOf(2).equals(n) || BigInteger.valueOf(3).equals(n)) {
            return true;
        }
        if (BigInteger.valueOf(1).compareTo(n) > 0 || BigInteger.valueOf(1).and(n).compareTo(BigInteger.valueOf(0)) == 0) {
            return false;
        }
        BigInteger k = BigInteger.valueOf(1);
        BigInteger m = n.subtract(BigInteger.valueOf(1));
        while (m.and(BigInteger.valueOf(1)).compareTo(BigInteger.valueOf(0)) == 0) {
            k = k.add(BigInteger.valueOf(1));
            m = m.divide(BigInteger.valueOf(2));
        }
        Random rand = new Random();
        BigInteger lower = BigInteger.valueOf(2);
        BigInteger upper = n.subtract(BigInteger.valueOf(1));
        for (int test = 0; test < noOfTests; test++) {
            BigInteger a = randomBigIntegerInRange(rand, lower, upper);
            BigInteger b = a.modPow(m, n);
            if (b.compareTo(BigInteger.valueOf(1)) != 0 && b.compareTo(n.subtract(BigInteger.valueOf(1))) != 0) {
                BigInteger i = BigInteger.valueOf(1);
                while (i.compareTo(k) == -1 && b.compareTo(n.subtract(BigInteger.valueOf(1))) != 0) {
                    b = b.modPow(BigInteger.valueOf(2), n);
                    if (b.compareTo(BigInteger.valueOf(1)) == 0) {
                        return false;
                    }
                    i = i.add(BigInteger.valueOf(1));
                }
                if (b.compareTo(n.subtract(BigInteger.valueOf(1))) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    protected static BigInteger randomBigIntegerInRange(Random rand, BigInteger lower, BigInteger upper) {
        BigInteger a = new BigInteger(upper.bitLength(), rand);
        if (a.compareTo(lower) < 0) {
            a = a.add(lower);
        }
        if (a.compareTo(upper) > 0) {
            a = a.mod(upper.subtract(lower)).add(lower);
        }
        return a;
    }
    protected static BigInteger generatePrime(int length) {
        if(length%32==1) length++;
        BigInteger p = generatePrimeCandidate(length);
        while (!isPrime(p, 128)) {
            p = generatePrimeCandidate(length);
        }
        return p;
    }
    protected static BigInteger generatePrimeCandidate(final int length) {
        Random rand = new Random();
        BigInteger b = new BigInteger(length, rand);
        b = b.or(BigInteger.valueOf(1));
        b = b.or(BigInteger.valueOf(1 << length - 1 - 1));
        b = b.and(BigInteger.valueOf(1).shiftLeft(length - 1).not());
        return b;
    }
    protected static BigInteger findD(BigInteger e, BigInteger Fi){
        //choosing latter for security purposes <->
        BigInteger i = new BigInteger("1");
        while(true){
            BigInteger top = Fi.multiply(i).add(BigInteger.valueOf(1));
            BigInteger wholes = top.divide(e);
            if(top.subtract(wholes.multiply(e)).compareTo(BigInteger.valueOf(0))==0) return top.divide(e);
            i=i.add(BigInteger.valueOf(1));
        }
    }
    protected static String decrypt(BigInteger d, BigInteger N, String msg) {
        StringBuilder decryptedString = new StringBuilder();
        msg = msg.replaceAll(" ","");
        //remember about cmd
        String parsed[] = msg.split(",");
        for(int i = 0; i<parsed.length;i++){
            try {
                BigInteger base = new BigInteger(parsed[i]);
                BigInteger decryptedNum = base.modPow(d, N);
                decryptedString.append((char) (decryptedNum.intValue()));
            }catch(Exception anyBigIntegerException){
                return "Error";
            }
        }
        return decryptedString.toString();
    }
    protected static boolean isDivisible(BigInteger number,BigInteger divisor){
        BigInteger quotient = number.divide(divisor);
        if(number.subtract(divisor.multiply(quotient)).equals(BigInteger.valueOf(0))){
            return true;
        }
        else return false;
    }

}
