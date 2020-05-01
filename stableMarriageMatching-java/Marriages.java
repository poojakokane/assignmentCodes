import java.util.Random;
import java.util.*;

public class Marriages {

    static boolean DEBUG = false;

    public static void main(String[] args) {
        for (int n = 0; n < 100; n += 1) {
            Population village = Population.newRandom(10);
            Population.Marriage marriage = village.galeShapelyMarriage();
            if (marriage.isStable()) {
                System.out.println("Marriage " + n + " is stable.");
            } else {
                throw new IllegalStateException("Marriage "+n+" is not stable!");
            }
        }
    }



    static class Population {
        int[][] boysPreferences; // boysPreferences[b][i] is the ith choice of boy b
        int[][] girlsPreferences; // girlsPreferences[g][i] is the ith choice of girl g

        static Population newRandom(int n) {
            Population pop = new Population();
            pop.boysPreferences = new int[n][n];
            pop.girlsPreferences = new int[n][n];
            for (int b = 0; b < n; b += 1) {
                for (int i = 0; i < n; i += 1) {
                    pop.boysPreferences[b][i] = i;
                    pop.girlsPreferences[b][i] = i;
                }
                randomize(pop.boysPreferences[b]);
                randomize(pop.girlsPreferences[b]);
            }

            return pop;
        }


        class Marriage {
            int[] wives;
            Population pop;
            int[] nextToPropose;
            
            public Marriage(Population p){
                wives = new int[p.boysPreferences.length];
                nextToPropose = new int[p.boysPreferences.length];
                for(int i=0; i<wives.length; i++){
                    wives[i] = -1;
                    nextToPropose[i] = 0;
                }

               this.pop = p;
            }

            int husbandOfGirl(int g) {
                for (int b = 0; b < wives.length; b += 1) {
                    if (wives[b]==g) return b;
                }

                return -1;
                //throw new IllegalStateException();
            }

            int wifeOfBoy(int b) {
                return wives[b];
            }

            int findNextFreeMan(){
                for(int i=0; i<wives.length; i++){
                    if(DEBUG)
                        System.out.println("\t wifeOfBoy("+i+")="+wifeOfBoy(i));
                    int w = wifeOfBoy(i);
                    if(w == -1){
                        // This boy i is free
                        return i;
                    }
                }

                // No men are free
                return -1;
            }

            int proposeToGirl(int boyId){
                if(nextToPropose[boyId] < wives.length){
                    //propose this girl
                    int preferenceOfBoy = nextToPropose[boyId]++;
                    if(DEBUG){
                        System.out.println("Boy " + boyId + " proposing to " 
                            + pop.boysPreferences[boyId][preferenceOfBoy]);
                    }
                    return pop.boysPreferences[boyId][preferenceOfBoy];
                }

                // Exhausted proposing all girls
                //System.out.println("Boy " + boyId + " exhausted proposals");
                //assert(false);
                throw new IllegalStateException("Boy " + boyId + " exhausted proposals");
                //return -1;
            }

            boolean isStable() {
                //TODO: implement this method
                //throw new UnsupportedOperationException("Not implemented yet!");
                //System.out.println(Population.boysPreferences[0][0]);

                // Check if no boy / girl has more than one spouse
                Set<Integer> uniqueBrides = new HashSet<Integer>();
                for(int i=0; i<wives.length; i++){
                    if(wives[i] == -1){
                        // This guy remained unmarried : UNSTABLE
                        //System.out.println("Guy " + i + " remained unmarried.");
                        return false;
                    }

                    uniqueBrides.add(wives[i]);
                }
                if(uniqueBrides.size() < wives.length){
                    // A girl ended up marrying more than one guy
                    //System.out.println("Some girl eneded up marrying more than one guys.");
                    return false;
                }

                //For each pair or couples: if they would be "happier" in a cross configuraion
                //"happier" -> one member from each couple has more preference of staying with 
                // one another than with their current partner
                for(int c1 = 0; c1 < wives.length; c1++){
                    for(int c2 = c1+1; c2 < wives.length; c2++){
                        if(couplesWouldRatherSwapPartners(c1, c2)){

                            return false;
                        }
                    }
                }

                // No failure conditions satisfied
                return true;
            }

            boolean couplesWouldRatherSwapPartners(int c1, int c2){
                int couple1Boy = c1;
                int couple1Girl = wifeOfBoy(couple1Boy);

                int couple2Boy = c2;
                int couple2Girl = wifeOfBoy(couple2Boy);

                // If couple1Boy wants to be with couple2Girl more than couple1Girl AND
                // couple2Girl wants to be with couple1Boy more than couple2Boy
                // OR vice versa
                if(   (whereDoesThisGirlLieInThisBoysPreferenceList(couple1Boy, couple2Girl) 
                       < whereDoesThisGirlLieInThisBoysPreferenceList(couple1Boy, couple1Girl))
                    &&(whereDoesThisBoyLieInThisGirlsPreferenceList(couple1Boy, couple2Girl)
                       < whereDoesThisBoyLieInThisGirlsPreferenceList(couple2Boy, couple2Girl))){
                   // throw new IllegalStateException("Marriage is not stable!");
                    return true;
                }

                if(   (whereDoesThisGirlLieInThisBoysPreferenceList(couple2Boy, couple1Girl) 
                       < whereDoesThisGirlLieInThisBoysPreferenceList(couple2Boy, couple2Girl))
                    &&(whereDoesThisBoyLieInThisGirlsPreferenceList(couple2Boy, couple1Girl)
                       < whereDoesThisBoyLieInThisGirlsPreferenceList(couple1Boy, couple1Girl))){
                    //throw new IllegalStateException("Marriage is not stable!");
                    return true;
                }

                // They dont have better cross affinity
                return false;
            }
        }


        int whereDoesThisGirlLieInThisBoysPreferenceList(int boyId, int girlId){
            for(int i=0; i<boysPreferences.length; i++){
                if(boysPreferences[boyId][i] == girlId){
                    return i;
                }
            }

            return -1;
        }

        int whereDoesThisBoyLieInThisGirlsPreferenceList(int boyId, int girlId){
            for(int i=0; i<girlsPreferences.length; i++){
                if(girlsPreferences[girlId][i] == boyId){
                    return i;
                }
            }

            return -1;
        }

        Marriage galeShapelyMarriage() {
            //TODO: implement the Gale-Shapely algorithm for computing a stable marriage
            //throw new UnsupportedOperationException("Not implemented yet!");
            Marriage m = new Marriage(this);
            
            /*
            for(int i = 0; i < this.boysPreferences.length; i++){
                m.wives[i] = this.boysPreferences[i][0];
            }
            */

            int nextFreeMan = m.findNextFreeMan();
            while(nextFreeMan != -1){
                //int womanProposed = m.proposeToGirl(nextFreeMan);
                for(int i=0; i<m.wives.length; i++) {
                    int womanProposed = boysPreferences[nextFreeMan][i];
                    if (DEBUG)
                        System.out.println("2. Boy " + nextFreeMan + " proposed to " + womanProposed + " whose fiancee is " + m.husbandOfGirl(womanProposed));
                    if (womanProposed == -1) {
                        if (DEBUG)
                            System.out.println("This should never happen as there must always be a girl available");
                        assert (false);
                    } else if (m.husbandOfGirl(womanProposed) == -1) {
                        // Girl is free, engage this couple
                        m.wives[nextFreeMan] = womanProposed;
                        break;
                    } else {
                        //Girl is already engaged
                        // See if girl would like to break current engagement?
                        int currentBoy = m.husbandOfGirl(womanProposed);
                        if (whereDoesThisBoyLieInThisGirlsPreferenceList(nextFreeMan, womanProposed)
                                < whereDoesThisBoyLieInThisGirlsPreferenceList(currentBoy, womanProposed)) {
                            // Yes she would break current engagement and re-engage
                            m.wives[currentBoy] = -1;
                            m.wives[nextFreeMan] = womanProposed;
                            break;
                        }
                    }
                }

                if(DEBUG)
                    System.out.println("3. Boy " + nextFreeMan + "\'s current wife is " + m.wifeOfBoy(nextFreeMan));
                nextFreeMan = m.findNextFreeMan();
            }

            return m;
        }

    }

    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    static void randomize(int[] a) {
        Random random = new Random();
        for (int i = 1; i < a.length; i += 1) {
            swap(a, i, random.nextInt(i));
        }
    }

}
