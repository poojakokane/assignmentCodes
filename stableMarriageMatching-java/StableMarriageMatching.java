import java.util.Random;

public class Marriages {

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

            int husbandOfGirl(int g) {
                for (int b = 0; b <= wives.length; b += 1) {
                    if (wives[b]==g) return b;
                }
                throw new IllegalStateException();
            }

            int wifeOfBoy(int b) {
                return wives[b];
            }

            boolean isStable() {
                //TODO: implement this method
                throw new UnsupportedOperationException("Not implemented yet!");
            }
        }




        Marriage galeShapelyMarriage() {
            //TODO: implement the Gale-Shapely algorithm for computing a stable marriage
            throw new UnsupportedOperationException("Not implemented yet!");
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
