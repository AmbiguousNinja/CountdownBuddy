package ambiguousninja.countdownsolver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private NumbersSolver ns;

    private EditText choice1;
    private EditText choice2;
    private EditText choice3;
    private EditText choice4;
    private EditText choice5;
    private EditText choice6;

    private EditText target;
    private TextView solution;

    private Random r;
    private final int[] validSet = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 25, 75, 50, 100};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InputFilter[] numberFilter = new InputFilter[]{ new NumberFilter(1, 999)};

        ns = new NumbersSolver();

        choice1 = (EditText) findViewById(R.id.number1_editText);
        choice2 = (EditText) findViewById(R.id.number2_editText);
        choice3 = (EditText) findViewById(R.id.number3_editText);
        choice4 = (EditText) findViewById(R.id.number4_editText);
        choice5 = (EditText) findViewById(R.id.number5_editText);
        choice6 = (EditText) findViewById(R.id.number6_editText);

        target = (EditText) findViewById(R.id.target_editText);
        solution = (TextView) findViewById(R.id.solution_textView);

        choice1.setFilters(numberFilter);
        choice2.setFilters(numberFilter);
        choice3.setFilters(numberFilter);
        choice4.setFilters(numberFilter);
        choice5.setFilters(numberFilter);
        choice6.setFilters(numberFilter);
        target.setFilters(numberFilter);

        r = new Random();
        randomize();
    }

    public void solveQuestion(View view) {
        solution.setText("");

        int[] numSet = new int[6];

        numSet[0] = validate(choice1, 1);
        numSet[1] = validate(choice2, 1);
        numSet[2] = validate(choice3, 1);
        numSet[3] = validate(choice4, 1);
        numSet[4] = validate(choice5, 1);
        numSet[5] = validate(choice6, 1);

        int targetValue = validate(target, 999);

        new SolveNumbers(numSet, targetValue).execute();
    }

    private int validate(EditText choice, int defaultVal) {
        String value = choice.getText().toString();

        if(value.equals("")) {
            choice.setText(Integer.toString(defaultVal));
            return defaultVal;
        } else {
            return Integer.parseInt(value);
        }
    }

    // Randomizes the puzzle using only valid numbers from Countdown.
    // TODO - Currently its possible to have invalid set:
    //  - more than 2 of a small number
    //  - more than 1 of a large number
    //  - more than 4 large numbers
    public void randomize(View view) {
        randomize();
    }

    private void randomize() {
        solution.setText("");

        choice1.setText(Integer.toString(validSet[r.nextInt(14)]));
        choice2.setText(Integer.toString(validSet[r.nextInt(14)]));
        choice3.setText(Integer.toString(validSet[r.nextInt(14)]));
        choice4.setText(Integer.toString(validSet[r.nextInt(14)]));
        choice5.setText(Integer.toString(validSet[r.nextInt(14)]));
        choice6.setText(Integer.toString(validSet[r.nextInt(14)]));

        target.setText(Integer.toString(r.nextInt(899) + 101));
    }

    private class SolveNumbers extends AsyncTask<Integer, Void, List<String>> {
        private int[] numSet;
        private int target;

        private SolveNumbers(int[] numSet, int target) {
            this.numSet = numSet;
            this.target = target;
        }
        @Override
        protected List<String> doInBackground(Integer... params) {
            return ns.solve(this.numSet, this.target);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            StringBuilder builder = new StringBuilder();

            for(String step : result) {
                builder.append(step + "\n");
            }
            solution.setText(builder.toString());
        }
    }
}
