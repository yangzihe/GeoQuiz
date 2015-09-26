package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
    private static final String TAG = QuizActivity.class.getName();
    private static final String KEY_INDEX = "index";
    private static final String IS_CHEATER = "com.example.geoquiz.is_cheater";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true)
    };

    private int mCurrIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrIndex = (mCurrIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrIndex = (mCurrIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrIndex == 0) {
                    mCurrIndex = mQuestionBank.length;
                }
                mCurrIndex = (mCurrIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrIndex].isTrueQuestion();
                Intent cheat = new Intent(QuizActivity.this, CheatActivity.class);
                cheat.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(cheat, 0);
            }
        });

        if (savedInstanceState != null) {
            mCurrIndex = savedInstanceState.getInt(KEY_INDEX);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATER, false);
        }

        updateQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves the current index of the question that we are on, as well as whether the user is a
     * cheater or not, so that if the configurations are changed and the activity is destroyed and
     * created again, the information is persisted.
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrIndex);
        savedInstanceState.putBoolean(IS_CHEATER, mIsCheater);
    }

    /**
     * Gets information from CheatActivity on whether or not the user cheated.
     *
     * @param requestCode
     * @param resultCode
     * @param data Contains boolean that is set to true if the user decided to cheat
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    /**
     * Updates the mQuestionTextView with the next question from mQuestionBank.
     */
    private void updateQuestion() {
        int question = mQuestionBank[mCurrIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    /**
     * Checks whether or not the user gave the correct answer for a question.
     *
     * @param userPressedTrue If True button was pressed
     */
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrIndex].isTrueQuestion();

        int msgResId = 0;
        if (mIsCheater) {
            msgResId = R.string.judgement_toast;
        } else {
            if (answerIsTrue == userPressedTrue) {
                msgResId = R.string.correct_text;
            } else {
                msgResId = R.string.incorrect_text;
            }
        }
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }
}
