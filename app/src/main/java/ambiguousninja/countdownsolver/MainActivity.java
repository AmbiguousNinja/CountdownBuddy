package ambiguousninja.countdownsolver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * TODO-List
 *
 * UI:
 * - Finalize UI
 *
 * UX:
 * - Safety checks
 * - Initial Values
 *
 * Logic:
 * - Step-by-step
 * - Closest solution
 * - Optimize further
 *
 * Extra:
 * - "Help" Activity/Dialogue
 */

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ns = new NumbersSolver();

        choice1 = (EditText) findViewById(R.id.choice1);
        choice2 = (EditText) findViewById(R.id.choice2);
        choice3 = (EditText) findViewById(R.id.choice3);
        choice4 = (EditText) findViewById(R.id.choice4);
        choice5 = (EditText) findViewById(R.id.choice5);
        choice6 = (EditText) findViewById(R.id.choice6);

        target = (EditText) findViewById(R.id.targetText);
        solution = (TextView) findViewById(R.id.textView);
    }

    public void solveQuestion(View view) {
        solution.setText("");

        int[] numSet = new int[6];

        numSet[0] = Integer.parseInt(choice1.getText().toString());
        numSet[1] = Integer.parseInt(choice2.getText().toString());
        numSet[2] = Integer.parseInt(choice3.getText().toString());
        numSet[3] = Integer.parseInt(choice4.getText().toString());
        numSet[4] = Integer.parseInt(choice5.getText().toString());
        numSet[5] = Integer.parseInt(choice6.getText().toString());

        int targetValue = Integer.parseInt(target.getText().toString());

        new SolveNumbers(numSet, targetValue).execute();
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
