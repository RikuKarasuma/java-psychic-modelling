package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    /**
     * My attempt at the psychic modelling problem in the 'Algorithm Design Manual 3rd Edition.'
     * I use a binary counting subset generation method and a typical set cover algorithm with
     * length qualifier to determine which combinations of lottery tickets will pay off with
     * a win. Assuming the psychic guarantee is valid... of course.
     */
    public static void main(String[] args) {

        final var fixed_lotto_numbers = new int[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        final var fixed_ticket_size = 6;
        final var psychic_guarantee = new HashSet<>(Arrays.asList(10, 6, 4, 7));

        final var subsets = generateSubsetsFromSet(fixed_lotto_numbers);

        System.out.println("Length of sets: " + subsets.length);
        final var estimated_minimum_winners = findWinningSetsOfTickets(subsets, psychic_guarantee, fixed_ticket_size);

        System.out.printf("Length of winning sets(%d): %d\n", fixed_ticket_size, estimated_minimum_winners.size());
        estimated_minimum_winners
                .forEach(matching -> {
                    for (Integer integer : matching)
                        System.out.print(integer + ", ");

                    System.out.println();
                });
    }

    /**
     * Set cover algorithm.
     */
    private static Set<Set<Integer>> findWinningSetsOfTickets(final Integer[][] total_combos,
                                                              final Set<Integer> psychic_guarantee,
                                                              final int fixed_ticket_size) {
        return Arrays.stream(total_combos)
                .map(Arrays::asList)
                .map(HashSet::new)
                .filter(subset ->
                        fixed_ticket_size == subset.size() &&
                        subset.containsAll(psychic_guarantee))
                .collect(Collectors.toSet());
    }

    /**
     * Binary counting subset generator.
     */
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

                // If we have a bit set. Confirm presence of number.
                if (subsets[i] % 10 == 1)
                    unique_combo_to_store_list.add(subset_to_generate_combos_from[x]);

                subsets[i] = subsets[i] / subset_to_generate_combos_from.length;
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


    private static int compareSortedCombosStringLength(Integer[] c1, Integer[] c2) {
        final var c1Sum = Arrays.stream(c1).map(i -> "" + i).collect(Collectors.joining()).length();
        final var c2Sum = Arrays.stream(c2).map(i -> "" + i).collect(Collectors.joining()).length();

        return Integer.compare(c1Sum, c2Sum);
    }
}