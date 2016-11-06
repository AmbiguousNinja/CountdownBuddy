package ambiguousninja.countdownsolver;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

class NumbersSolver {
    private static int target;
    private static int closestDelta;
    private static boolean foundSolution;
    private static List<String> solution;

    public NumbersSolver() {
        this.target = 0;
        this.solution = new ArrayList<>();
    }

    public List<String> solve(int[] numSet, int target) {
        this.foundSolution = false;
        this.target = target;
        this.closestDelta = 999;

        powerSet(numSet);

        if(!foundSolution) {
            solution.add("\nImpossible! (Off by " + closestDelta + ")");
        }

        return solution;
    }

    private void powerSet(int[] numSet) {
        int len = numSet.length;

        BitSet state = new BitSet(len);

        while(increment(state, len) && !foundSolution) {
            permute(getSubset(numSet, state), 0);
        }
    }

    // Increment to next state
    private boolean increment(BitSet state, int length) {
        int index = length - 1;

        while(index >= 0 && state.get(index)) {
            state.clear(index);
            index--;
        }

        if(index < 0)   return false;

        state.set(index);

        return true;
    }

    // Returns subset represented by state
    private static List<Integer> getSubset(int[] numbers, BitSet state) {
        List<Integer> subset = new ArrayList<>();

        int len = numbers.length;

        for(int i = 0; i < len; i++) {
            if(state.get(i)) {
                subset.add(numbers[i]);
            }
        }

        return subset;
    }

    // Finds permutations of an array. Operate on each permutation recursively
    private static void permute(List<Integer> numbers, int k) {
        if(k == numbers.size() && !foundSolution) {
            addOperator(numbers, new ArrayList<Character>(), 0);
        } else {
            for(int i = k; i < numbers.size(); i++) {
                Collections.swap(numbers, i, k);
                permute(numbers, k + 1);
            }
        }
    }

    // Try every combination of operators
    // TODO - Optimize calculations:
    //  - Dividing/Multiplying by 1
    //  - Undoing an operation (e.g. 5*10/10). Similar to first point
    //  - a / b = b
    //  - a - b = b
    //  - Divide by zero
    private static void addOperator(List<Integer> numbers, List<Character> operators, int position) {
        if(position == numbers.size()) {
            int total = numbers.get(0);

            for(int i = 1; i < numbers.size(); i++) {
                switch(operators.get(i - 1)) {
                    case '+':
                        total += numbers.get(i);
                        break;
                    case '-':
                        if(total < numbers.get(i)) return;	// No negatives allowed
                        total -= numbers.get(i);
                        break;
                    case '*':
                        total *= numbers.get(i);
                        break;
                    case '/':
                        if(total % numbers.get(i) != 0) return;	// Only ints are allowed
                        total /= numbers.get(i);
                        break;
                }
            }

            if(total == target) {
                solution.clear();
                createSolution(numbers, operators);
                foundSolution = true;
            } else if(Math.abs(total - target) < closestDelta) {   // Store if closest solution found so far
                solution.clear();
                closestDelta = Math.abs(total - target);
                createSolution(numbers, operators);
            }
        } else {
            if(position == 0) {
                addOperator(numbers, operators, ++position);
            } else {
                int nextPos = position + 1;
                int prevPos = position - 1;

                operators.add(prevPos, '+');
                addOperator(numbers, operators, nextPos);

                operators.add(prevPos, '-');
                addOperator(numbers, operators, nextPos);

                operators.add(prevPos, '*');
                addOperator(numbers, operators, nextPos);

                operators.add(prevPos, '/');
                addOperator(numbers, operators, nextPos);
            }
        }
    }

    // Converts to more readable (step-by-step in-fix) notation
    private static void createSolution(List<Integer> numbers, List<Character> operators) {
        StringBuilder step;
        int total = numbers.get(0);

        for(int i = 1; i < numbers.size(); i++) {
            step = new StringBuilder();

            switch(operators.get(i - 1)) {
                case '+':
                    step.append(total);
                    step.append(" + ");
                    step.append(numbers.get(i));
                    total += numbers.get(i);
                    break;
                case '-':
                    step.append(total);
                    step.append(" - ");
                    step.append(numbers.get(i));
                    total -= numbers.get(i);
                    break;
                case '*':
                    step.append(total);
                    step.append(" * ");
                    step.append(numbers.get(i));
                    total *= numbers.get(i);
                    break;
                case '/':
                    step.append(total);
                    step.append(" / ");
                    step.append(numbers.get(i));
                    total /= numbers.get(i);
                    break;
            }

            step.append(" = " + total);
            solution.add(step.toString());
        }
    }
}