import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class GeneratorDecryptor {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean loopMsg;
        while(true) {
            System.out.println("Prime number length: ");
            String strLength = input.nextLine();
            int length = Integer.parseInt(strLength);
            System.out.println("Insert displacement of e: ");
            String eDisplacement = input.nextLine();
            BigInteger p = generatePrime(length);
            BigInteger q = generatePrime(length);
            BigInteger N = p.multiply(q);
            BigInteger Fi = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));
            BigInteger e = new BigInteger(eDisplacement);

            String val = "1";
            if(e.compareTo(Fi) >= 0){
                e=Fi;
                val = "-1";
            }

            for (long start = System.currentTimeMillis();; e = e.add(new BigInteger(val))) {
                //choosing latter for security purposes <->
                if(isPrime(e,128) && !isDivisible(Fi,e)){
                    break;
                }else if((System.currentTimeMillis()-start)>10000){
                    System.out.println("Choose a smaller e");
                }
                //if (getBigGCD(e, Fi).equals(BigInteger.valueOf(1))) break;
            }
            BigInteger d = findD(e, Fi);
            System.out.println("p : " + p);
            System.out.println("q : " + q);
            System.out.println("Fi: " + Fi);
            System.out.println("e: " + e);
            System.out.println("N: " + N);
            System.out.println("d: " + d);
            System.out.println("N: " + N);
            loopMsg=true;
            while(loopMsg) {
                System.out.println("Given message: ");
                String msg = input.nextLine();
                String decryptedMsg = decrypt(d, N, msg);
                System.out.println(decryptedMsg);
                System.out.println("To change locks type: locks"); System.out.print("              Alternatively press enter");
                if(input.nextLine().equals("locks")){
                    loopMsg=false;
                }
            }
        }
    }
    public static boolean isDivisible(BigInteger number,BigInteger divisor){
        BigInteger quotient = number.divide(divisor);
        if(number.subtract(divisor.multiply(quotient)).equals(BigInteger.valueOf(0))){
            return true;
        }
        else return false;
    }
    public static String decrypt(BigInteger d, BigInteger N, String msg) {
        StringBuilder decryptedString = new StringBuilder();
        msg = msg.replaceAll(" ","");
        //remember about cmd
        String parsed[] = msg.split(",");
        for(int i = 0; i<parsed.length;i++){
            BigInteger base = new BigInteger(parsed[i]);
            BigInteger decryptedNum = base.modPow(d,N);
            decryptedString.append((char)(decryptedNum.intValue()));
        }
        return decryptedString.toString();
    }
    public static BigInteger findD(BigInteger e, BigInteger Fi){
        //choosing latter for security purposes <->
        BigInteger i = new BigInteger("1");
        while(true){
            BigInteger top = Fi.multiply(i).add(BigInteger.valueOf(1));
            BigInteger wholes = top.divide(e);
            if(top.subtract(wholes.multiply(e)).compareTo(BigInteger.valueOf(0))==0) return top.divide(e);
            i=i.add(BigInteger.valueOf(1));
        }
    }
    public static BigInteger getBigGCD(BigInteger x, BigInteger y) {
        if (x.compareTo(y) < 0) {
            BigInteger temp = x;
            x = y;
            y = temp;
        }
        BigInteger remainder = new BigInteger("-1");
        while (!remainder.equals(BigInteger.valueOf(0))) {
            remainder = x.remainder(y);
            x = y;
            y = remainder;
        }
        return x;
    }
    public static BigInteger generatePrime(final int length) {
        BigInteger p = generatePrimeCandidate(length);
        while (!isPrime(p, 128)) {
            p = generatePrimeCandidate(length);
        }
        return p;
    }
    /**
     * @param n         tested for being a prime
     * @param noOfTests number of tries
     * @return true if `n` is *probably* prime
     * @Miller-Rabin n-1 = 2^k * m, where k,m are whole numbers
     */
    private static boolean isPrime(final BigInteger n, final int noOfTests) {
        // Jesli jest 2 lub 3 to jest prime
        if (BigInteger.valueOf(2).equals(n) || BigInteger.valueOf(3).equals(n)) {
            return true;
        }
        // jesli jest mniejsze niz 1 lub jest parzysta to nie jest prime
        if (BigInteger.valueOf(1).compareTo(n) > 0 || BigInteger.valueOf(1).and(n).compareTo(BigInteger.valueOf(0)) == 0) {
            return false;
        }
        BigInteger k = BigInteger.valueOf(1); // s
        BigInteger m = n.subtract(BigInteger.valueOf(1)); // r
        // 2^k * m == 2^(k+1) * m/2
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

    private static BigInteger randomBigIntegerInRange(Random rand, BigInteger lower, BigInteger upper) {
        BigInteger a = new BigInteger(upper.bitLength(), rand);
        if (a.compareTo(lower) < 0) {
            a = a.add(lower);
        }
        if (a.compareTo(upper) > 0) {
            a = a.mod(upper.subtract(lower)).add(lower);
        }
        return a;
    }

    /**
     * Generate random number with binary representation of length `length`.
     */
    private static BigInteger generatePrimeCandidate(final int length) {
        Random rand = new Random();
        // randomowa liczba o binarnej reprezentacji dlugosci `length`
        BigInteger b = new BigInteger(length, rand);
        // make it odd
        b = b.or(BigInteger.valueOf(1));
        // zapewniamy ze MSB bedzie 1, zeby liczba na pewno byla dlugosci `length` bitow
        // odejmujemy 1 bo to jest int, a nie uint
        b = b.or(BigInteger.valueOf(1 << length - 1 - 1));
        // robimy zeby byla zawsze dodatnia
        b = b.and(BigInteger.valueOf(1).shiftLeft(length - 1).not());
        return b;
    }
}
