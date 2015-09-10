package com.nemory.bundledfun;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.helpers.Current;
import com.nemory.bundledfun.objects.Question;
import com.nemory.bundledfun.objects.User;
import com.nemory.bundledfun.tools.CountdownTimer;
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapController;
//import com.google.android.maps.MapView;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapController;
//import org.osmdroid.views.MapView;
	
public class QuizActivity extends Activity implements OnClickListener {
	
	private Button btnA, btnB, btnC, btnSkip;
	private TextView tvTimer, tvScore, tvCorrectAnswers, tvQuestion;
	private CountdownTimer timer2;
	private Chronometer chTimeElapsed;
	private LinearLayout lQuestionContainer;
	private LinearLayout.LayoutParams lParams;
//	private MapView mvMap;
//	private MapController mapController;
	private MediaPlayer mp;
	private Question currentQuestion;
	private int currentScore = 0;
	private HashMap<String, Integer> sounds;
	private SoundPool mSoundPool;
	private int totalQuestions;
	private long lastPausedTime = 0;
	private int correctedAnswers = 0;
	private int totalTimeElapsed = 0;
	private ArrayList<Question> currentQuestions = new ArrayList<Question>();
	private ArrayList<Question> skippedQuestions = new ArrayList<Question>();
	private boolean isSkipMode = false;
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_quiz);
		this.initialize();
		this.prompt();
	}
	
	@Override
	protected void onStop() {
		this.pauseGame();
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		this.resumeGame();
		super.onRestart();
	}
	
	private void initializeQuestions(){
		currentQuestions.clear();
		for(Question q : Question.questions){
			if(q.getDifficulty().equals(getIntent().getStringExtra("DIFFICULTY"))){
				currentQuestions.add(q);
			}
		}
		if(currentQuestions.size() <= 2)
		{
			btnSkip.setEnabled(false);
		}
	}
	
	private void startGame(){
		Collections.shuffle(currentQuestions); // randomize questions
		currentQuestion = currentQuestions.get(0); // get the first question in the list
		this.replaceView();
		btnA.setText(currentQuestion.getChoice_a());
		btnB.setText(currentQuestion.getChoice_b());
		btnC.setText(currentQuestion.getChoice_c());
		if(timer2 != null){ timer2.cancel(); }
		this.setCountDownTimer(currentQuestion.getTimer());
		timer2.create();
		chTimeElapsed.start();
	}
	
	private void pauseGame(){
		View v = lQuestionContainer.getChildAt(1);
		if(v instanceof VideoView){
			VideoView view = (VideoView) v;
			if(view.isPlaying()){
				view.pause();
			}
		}
		
		if(mp != null){
			mp.pause();
		}
		
		if(timer2 != null){
			timer2.pause();
		}

		lastPausedTime = chTimeElapsed.getBase() - SystemClock.elapsedRealtime();
		chTimeElapsed.stop();
	}
	
	private void resumeGame(){
		View v = lQuestionContainer.getChildAt(1);
		if(v instanceof VideoView){
			VideoView view = (VideoView) v;
			if(!view.isPlaying()){
				view.start();
			}
		}
		
		if(mp != null){
			mp.start();
		}
		
		timer2.resume();
		chTimeElapsed.setBase(SystemClock.elapsedRealtime() + lastPausedTime);
		chTimeElapsed.start();
	}
	
	private void restartGame(){
		btnSkip.setEnabled(true);
		isSkipMode = false;
		skippedQuestions.clear();
		lastPausedTime = 0;
		totalTimeElapsed = 0;
		currentScore = 0;
		correctedAnswers = 0;
		tvScore.setText(""+currentScore);
		tvCorrectAnswers.setText(correctedAnswers + "/" + totalQuestions);
		chTimeElapsed.setBase(SystemClock.elapsedRealtime() + lastPausedTime);
		this.initializeQuestions();
		this.startGame();
	}
	
	private void finishGame()
	{
		this.pauseGame();
		String scoreCritics = "";
		
		if(currentScore == 100){
			scoreCritics = "Perfect";
		}else if(currentScore >= 80){
			scoreCritics = "Great";
		}else if(currentScore >= 40 && currentScore < 80){
			scoreCritics = "Good";
		}else if(currentScore < 40){
			scoreCritics = "Better luck next time dude. :)";
		}
		
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setCancelable(false);
		d.setTitle(scoreCritics);
		d.setMessage("Thanks for having fun! \n You got a score of " + currentScore + " \n What would you like to do next?");
		
		d.setButton(d.BUTTON_POSITIVE, "Save Score", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				saveScore();
			}
		});
		
		d.setButton(d.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		
		d.setButton(d.BUTTON_NEUTRAL, "More Options", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				AlertDialog d = new AlertDialog.Builder(QuizActivity.this).create();
				d.setCancelable(false);
				d.setTitle("More Options");
				d.setButton(d.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog d = new AlertDialog.Builder(QuizActivity.this).create();
						d.setCancelable(false);
						d.setTitle("Warning");
						d.setMessage("Are you sure you want to restart the game?\n This will reset all current score data.");

						d.setButton(d.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								restartGame();
							}
						});
						
						d.setButton(d.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								resumeGame();
							}
						});
						
						d.show();
					}
				});
				
				d.setButton(d.BUTTON_NEUTRAL, "Share Score", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My BundledFun Score");
						sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just got a score of " + currentScore + "/" + Question.getTotalScore() + " and Correct Answers: "+ correctedAnswers +"/" + totalQuestions + " from playing BundledFun");
						startActivity(Intent.createChooser(sharingIntent, "Share Through"));
					}
				});
				
				d.show();
			}
		});

		d.show();
	}
	
	@SuppressWarnings("static-access")
	private void prompt(){
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setCancelable(false);
		d.setTitle("Start the game?");
		d.setMessage("Be ready for the \"Bundled Fun Questions.\"");

		d.setButton(d.BUTTON_POSITIVE, "Start", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startGame();
			}
		});
		
		d.setButton(d.BUTTON_NEGATIVE, "Back to Main Menu", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		
		d.show();
	}
	
	private static class ImageLoader extends AsyncTask<String, Integer, Drawable>{
		
		@Override
		protected Drawable doInBackground(String... params) {
			Drawable imageLoaded;
			imageLoaded = LoadImageFromWebOperations(params[0]);
			return imageLoaded;
		}
	}
	
	private static Drawable LoadImageFromWebOperations(String url)
    {
      try{
        InputStream is = (InputStream) new URL(url).getContent();
        Drawable d = Drawable.createFromStream(is, "src name");
        return d;
      }catch (Exception e) {
        System.out.println("Exc="+e);
        return null;
      }
   }
	
	@SuppressLint("SdCardPath")
	private void replaceView(){
		
		View view = lQuestionContainer.getChildAt(1);
		tvQuestion.setText(currentQuestion.getText());
		String file_url = Constants.HOST_NAME + "/BundledFun/public/groups/" + Current.group.getName() + "/files/questions/" + currentQuestion.getFileName();
		
		if(currentQuestion.getType().equals("text")){
			tvQuestion.setVisibility(View.GONE);
			//mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
			TextView textView = new TextView(this);
			textView.setLayoutParams(lParams);
			textView.setTextSize(30);
			textView.setGravity(Gravity.CENTER);
			textView.setText(currentQuestion.getText());
			lQuestionContainer.addView(textView, 1);
			
		}else if(currentQuestion.getType().equals("image")){
			tvQuestion.setVisibility(View.VISIBLE);
//			mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
		    ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(lParams);
		
			if(Current.streamOnline){
				Drawable drawable = null;
				
				try {
					drawable = new ImageLoader().execute(file_url).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
	            imageView.setImageDrawable(drawable);
			}else{
				Uri imageUri = Uri.fromFile(new File(Constants.DATAPATH + "/files/questions/" + currentQuestion.getFileName()));
				imageView.setImageURI(imageUri);
			}
			
			lQuestionContainer.addView(imageView, 1);
			
		}else if(currentQuestion.getType().equals("video")){
			
			pauseGame();
			
			tvQuestion.setVisibility(View.VISIBLE);
			tvQuestion.setText(currentQuestion.getText());
//			mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
			final VideoView videoView = new VideoView(this);
			
			if(Current.streamOnline){
				videoView.setVideoPath(file_url);
			}else{
				videoView.setVideoPath(Constants.DATAPATH + "/files/questions/" + currentQuestion.getFileName());
			}

			videoView.setLayoutParams(lParams);
	        videoView.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mpx) {
					mpx.setLooping(true);
					videoView.start();
					resumeGame();
				}
			});
	        
	        lQuestionContainer.addView(videoView, 1);

		}else if(currentQuestion.getType().equals("audio")){
			tvQuestion.setVisibility(View.GONE);
//			mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
			TextView textView = new TextView(this);
			textView.setLayoutParams(lParams);
			textView.setTextSize(30);
			textView.setGravity(Gravity.CENTER);
			textView.setText(currentQuestion.getText());
			lQuestionContainer.addView(textView, 1);
			
			try {
				mp = new MediaPlayer();
				mp.setLooping(true);
				
				if(Current.streamOnline){
					Uri uri = Uri.parse(file_url);
					mp.setDataSource(this, uri);

				}else{
					mp.setDataSource(Constants.DATAPATH + "/files/questions/" + currentQuestion.getFileName());	
				}
				
				mp.prepare();
				mp.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void nextQuestion()
	{
		Log.d("QUESTIONS: ", "size: " + currentQuestions.size());
		
		if(isSkipMode)
		{
			Collections.shuffle(skippedQuestions); // randomize questions
			currentQuestion = skippedQuestions.get(0); // get the first question in the list\
		}
		else
		{
			Collections.shuffle(currentQuestions); // randomize questions
			currentQuestion = currentQuestions.get(0); // get the first question in the list
		}
		
		this.replaceView();
		btnA.setText(currentQuestion.getChoice_a());
		btnB.setText(currentQuestion.getChoice_b());
		btnC.setText(currentQuestion.getChoice_c());
		if(timer2 != null){ timer2.cancel(); }
		this.setCountDownTimer(currentQuestion.getTimer());
		timer2.create();
	}
	
	private void saveScore(){
		
		final ProgressDialog saveScoreDialog = new ProgressDialog(this);
		saveScoreDialog.setMessage("Saving...");
		saveScoreDialog.setIndeterminate(true);
		saveScoreDialog.setCancelable(false);
		
		saveScoreDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				Toast.makeText(QuizActivity.this, "Done", Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		saveScoreDialog.show();
		
		Thread t = new Thread(new Runnable() {
			
			boolean running = true;
			
			public void run() {
				while(running){
					try {
						User.saveScoreOnline(Current.user.getId(), currentScore, totalTimeElapsed, correctedAnswers, Current.user.getAccessToken());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					running = false;
					saveScoreDialog.dismiss();
				}
			}
		});

		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nav_bar, menu);
		return true;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_quit){
			this.pauseGame();
			AlertDialog d = new AlertDialog.Builder(this).create();
			d.setTitle("Warning");
			d.setCancelable(false);
			d.setMessage("Nothing will be saved in this current quiz if you quit without finishing.");

			d.setButton(d.BUTTON_POSITIVE, "Quit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					resumeGame();
				}
			});
			
			d.show();
		}else if(item.getItemId() == R.id.menu_pause_resume){
			this.pauseGame();
			AlertDialog d = new AlertDialog.Builder(this).create();
			d.setCancelable(false);
			d.setTitle("Paused");
			d.setButton(d.BUTTON_POSITIVE, "Resume", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					resumeGame();
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "Restart", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					AlertDialog d = new AlertDialog.Builder(QuizActivity.this).create();
					d.setCancelable(false);
					d.setTitle("Warning");
					d.setMessage("Are you sure you want to restart the game?\n This will reset all current score data.");

					d.setButton(d.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							restartGame();
						}
					});
					
					d.setButton(d.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							resumeGame();
						}
					});
					
					d.show();
				}
			});
			
			d.show();
		}
		else if(item.getItemId() == R.id.menu_restart){
			AlertDialog d = new AlertDialog.Builder(QuizActivity.this).create();
			d.setCancelable(false);
			d.setTitle("Warning");
			d.setMessage("Are you sure you want to restart the game?\n This will reset all current data.");

			d.setButton(d.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					restartGame();
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					resumeGame();
				}
			});
			
			d.show();
		}
		else if(item.getItemId() == R.id.menu_report){
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"nemoryoliver@gmail.com"});
			i.putExtra(Intent.EXTRA_TEXT, "Question: " + currentQuestion.getText() + "\n\nAnswer: " + currentQuestion.getAnswer() + "\n\n\nComments:");
			i.putExtra(Intent.EXTRA_SUBJECT, "Question Mistake Report");
			
			try {
			    startActivity(Intent.createChooser(i, "Report Question"));
			} catch (android.content.ActivityNotFoundException ex) {
			    Toast.makeText(QuizActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void compareAnswer(String answer){
		
		if(mp != null){
			
			if(mp.isPlaying())
			{
				mp.stop();
			}
			
			mp.release();
			mp = null;
		}
		
		if(answer.trim().equals(currentQuestion.getAnswer().trim()))
		{
			correctedAnswers++;
			currentScore += currentQuestion.getPoints();
			tvScore.setText("" + currentScore);
			tvCorrectAnswers.setText(correctedAnswers + "/" + totalQuestions);
			this.playSound("correct");
		}
		else
		{
			this.pauseGame();
			this.playSound("wrong");
			AlertDialog d = new AlertDialog.Builder(this).create();
			d.setCancelable(false);
			d.setTitle("Sorry");
			d.setMessage("You've got the wrong answer!\n The correct answer is: " + currentQuestion.getAnswer());
			d.setButton(d.BUTTON_POSITIVE, "Next", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
					return;
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "Restart", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					AlertDialog d = new AlertDialog.Builder(QuizActivity.this).create();
					d.setCancelable(false);
					d.setTitle("Warning");
					d.setMessage("Are you sure you want to restart the game?\n This will reset all current score data.");

					d.setButton(d.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							restartGame();
							return;
						}
					});
					
					d.setButton(d.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							resumeGame();
							return;
						}
					});
					
					d.show();
				}
			});
			
			d.show();
		}
		
		if(isSkipMode)
		{
			skippedQuestions.remove(0);
		}
		else
		{
			currentQuestions.remove(0);
		}
		
		if(currentQuestions.size() <= 2)
		{
			btnSkip.setEnabled(false);
		}

		if(currentQuestions.size() == 0 && skippedQuestions.size() == 0)
		{
			this.finishGame();
			return;
		}
		
		if(currentQuestions.size() == 0 && skippedQuestions.size() > 0)
		{
			isSkipMode = true;
		}
		
		resumeGame();
		nextQuestion();
	}
	
	private void playSound(String sound){
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(sounds.get(sound), volume, volume, 1, 0, 1f);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnA:
			compareAnswer(currentQuestion.getChoice_a());
			break;
			
		case R.id.btnB:
			compareAnswer(currentQuestion.getChoice_b());
			break;
			
		case R.id.btnC:
			compareAnswer(currentQuestion.getChoice_c());
			break;
			
		case R.id.btnSkip:
			
			if(mp != null){
				
				if(mp.isPlaying())
				{
					mp.stop();
				}
				
				mp.release();
				mp = null;
			}
			
			skippedQuestions.add(currentQuestion);
			currentQuestions.remove(0);
			
			if(currentQuestions.size() <= 2)
			{
				btnSkip.setEnabled(false);
			}
			
			nextQuestion();
			break;
		}
	}

	private void setCountDownTimer(long ms){
		timer2 = new CountdownTimer(ms, 1000, true) {
		     public void onTick(long ms) {
		         tvTimer.setText(ms / 1000 + "");
		         totalTimeElapsed++;
		     }

		     public void onFinish() {
		    	 
		    	 if(mp != null)
		    	 {
					if(mp.isPlaying())
					{
						mp.stop();
					}
					
					mp.release();
					mp = null;
				}
				
		    	 if(isSkipMode)
		 		{
		 			skippedQuestions.remove(0);
		 		}
		 		else
		 		{
		 			currentQuestions.remove(0);
		 		}
		 		
		 		if(currentQuestions.size() <= 2)
		 		{
		 			btnSkip.setEnabled(false);
		 		}

		 		if(currentQuestions.size() == 0 && skippedQuestions.size() == 0)
		 		{
		 			finishGame();
		 			return;
		 		}
		 		
		 		if(currentQuestions.size() == 0 && skippedQuestions.size() > 0)
		 		{
		 			isSkipMode = true;
		 		}
				
		    	 nextQuestion();
		     }
		  };
	}
	
	private void initialize(){
		
		// TEXTVIEWS
		tvTimer 			= (TextView) findViewById(R.id.tvTimer);
		tvScore 			= (TextView) findViewById(R.id.tvScore);
		tvCorrectAnswers 	= (TextView) findViewById(R.id.tvCorrectAnswers);
		tvQuestion 			= (TextView) findViewById(R.id.tvQuestion);
		
		// BUTTON VIEWS
		btnA = (Button) findViewById(R.id.btnA);
		btnB = (Button) findViewById(R.id.btnB);
		btnC = (Button) findViewById(R.id.btnC);
		btnSkip = (Button) findViewById(R.id.btnSkip);

		// MAP
//		mvMap = (MapView) findViewById(R.id.mvMap);
//		mvMap.setTileSource(TileSourceFactory.MAPNIK);
		//mvMap.setMultiTouchControls(true);
//		mvMap.setBuiltInZoomControls(true);
//		
//		mapController = mvMap.getController();
//		mapController.setZoom(8);
		
		//CHRONOMETER ELAPSED TIME TIMER
		chTimeElapsed = (Chronometer) findViewById(R.id.chTimeElapsed);
		
		btnA.setOnClickListener(this);
		btnB.setOnClickListener(this);
		btnC.setOnClickListener(this);
		btnSkip.setOnClickListener(this);
		
		lQuestionContainer = (LinearLayout) findViewById(R.id.lQuestionContainer);
		lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lParams.gravity = Gravity.CENTER;
		timer2 = null;

		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		sounds = new HashMap<String, Integer>();
		sounds.put("wrong", mSoundPool.load(this, R.raw.wrong, 1));
		sounds.put("correct", mSoundPool.load(this, R.raw.correct, 1));
		
		initializeQuestions();
		
		totalQuestions = currentQuestions.size();
		tvCorrectAnswers.setText("0/" + totalQuestions);
	}

//	@Override
//	protected boolean isRouteDisplayed() {
//		
//		return false;
//	}
}