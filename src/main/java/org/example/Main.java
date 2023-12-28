package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        final var subsets = generateSubsetsFromSet(new int[]{1, 2, 3, 4, 5});

        for (int i = 0; i < subsets.length; i ++) {
            for (int x = 0; x < subsets[i].length; x ++) {
                System.out.print(subsets[i][x] + ", ");
            }
            System.out.println();
        }
    }

    private static Integer[][] generateSubsetsFromSet(final int[] subset_to_generate_combos_from) {
        // Binary counting method of generating subsets.

        final int subset_length = subset_to_generate_combos_from.length;
        // Total combination length.
        final int generation_length = (int) Math.pow(2, subset_length);
        final var subsets = new int[generation_length];
        final var returnedTreeSet = new TreeSet<>(Main::compareSortedCombosLexico);

        for ( int i = 0; i < generation_length; i ++) {

            int b = 1;
            // Init this index.
            subsets[i] = 0;
            // grab index for inner generation
            int number = i;
            while (number > 0) {
                // Create our unique binary for this index.
                subsets[i] += (number % 2) * b;
                number = number / 2;
                b = b * 10;
            }

            // Reverse the binary and store the combos in a usable
            // integer array.
            final var unique_combo_to_store_list = new TreeSet<Integer>();
            for ( int x = 0; x < subset_length; x ++) {

                // If we have a base 10 number confirm presence of unique number,
                // otherwise Zero.
                if (subsets[i] % 10 == 1)
                    unique_combo_to_store_list.add(subset_to_generate_combos_from[x]);

                subsets[i] = subsets[i] / 10;
            }

            // use distinct size to create new resized array.
            returnedTreeSet.add(unique_combo_to_store_list.toArray(new Integer[0]));
        }

        return returnedTreeSet.toArray(new Integer[0][]);
    }

    // Different methods of sorting our generated subsets within a TreeSet.
    private static int compareSortedCombosSummed(Integer[] c1, Integer[] c2) {
        final var c1Sum = Arrays.stream(c1).mapToInt(i -> i).sum();
        final var c2Sum = Arrays.stream(c2).mapToInt(i -> i).sum();

        return Integer.compare(c1Sum, c2Sum);
    }

    private static int compareSortedCombosLexico(Integer[] c1, Integer[] c2) {
        final var c1Sum = Arrays.stream(c1).map(i -> "" + i).collect(Collectors.joining());
        final var c2Sum = Arrays.stream(c2).map(i -> "" + i).collect(Collectors.joining());

        return c1Sum.compareTo(c2Sum);
    }
}