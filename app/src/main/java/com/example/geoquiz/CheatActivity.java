package com.example.geoquiz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends ActionBarActivity {
    public static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN =
            "com.example.geoquiz.answer_shown";
    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;
    private Button mShowAnswer;
    private TextView mAnswerText;

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerText = (TextView) findViewById(R.id.answerTextView);
        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerText.setText("True");
                } else {
                    mAnswerText.setText("False");
                }
            mIsAnswerShown = true;
            setAnswerShownResult(mIsAnswerShown);
            }
        });

        if (savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves whether or not the user decided to cheat.
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
    }
}
